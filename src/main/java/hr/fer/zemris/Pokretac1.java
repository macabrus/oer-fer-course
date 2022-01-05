package hr.fer.zemris;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.Evaluator;
import hr.fer.zemris.generic.ga.Poison;
import hr.fer.zemris.generic.ga.RectSolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Pokretac1 {
    Thread[] pool;
    LinkedBlockingQueue<RectSolution> solutions = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<RectSolution> evaluatedSolutions = new LinkedBlockingQueue<>();
    // broj iteracija evaluiranja, biranja, krizanja, mutiranja
    private int maxIter = 10000;
    // broj generiranih rjesenja
    private int popSize = 32;
    // broj pravokutnika po jedinki
    private int numRectangles = 30;
    // K-turnirska selekcija
    private int k = 3;
    // number of pixels mutation can move rectangle for
    private int mutationIntensity = 10;
    // probability that mutation will happen on each rectangle
    private double mutationProbability = 0.5;
    //global best solution
    private RectSolution globalBest;

    public void run() throws URISyntaxException, IOException, InterruptedException {
        JFrame frame = new JFrame();
        JLabel picLabel = new JLabel();
        frame.add(picLabel);
        frame.setPreferredSize(new Dimension(200, 133));
        frame.pack();
        frame.setVisible(true);
        GrayScaleImage mainImage = GrayScaleImage.load(new File(getClass().getResource("/kuca.png").toURI()));
        pool = new Thread[Runtime.getRuntime().availableProcessors()];
        // make evaluator worker threads, each with copy of an image
        for (int i = 0; i < pool.length; i ++) {
            pool[i] = new Thread(new Runnable() {
                private GrayScaleImage image = new GrayScaleImage(mainImage.getData().clone(), mainImage.getWidth(), mainImage.getHeight());
                @Override
                public void run() {
                    while (true) {
                        RectSolution solution = null;
                        try {
                            solution = solutions.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (solution.poison) {
                            return;
                        }
                        // System.out.println("Evaluating image");
                        var evaluator = new Evaluator(image);
                        evaluator.evaluate(solution);
                        try {
                            evaluatedSolutions.put(solution);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            pool[i].start();
        }
        System.out.println("Generating initial %s solutions on queue".formatted(popSize));
        var rng = RNG.getRNG();
        for (int i = 0; i < popSize; i++) {
            var data = new int[numRectangles * 5 + 1];
            data[0] = rng.nextInt(0, 255); // pozadina
            // svi ostali pravokutnici
            for(int j = 1; j < numRectangles * 5; j += 5) {
                data[j + 0] = rng.nextInt(0, mainImage.getWidth()); // x
                data[j + 1] = rng.nextInt(0, mainImage.getHeight()); // y
                data[j + 2] = rng.nextInt(data[j + 0], mainImage.getWidth()) - data[j + 0]; // w
                data[j + 3] = rng.nextInt(data[j + 1], mainImage.getHeight()) - data[j + 1]; // h
                data[j + 4] = rng.nextInt(0, 255);
            }
            solutions.put(new RectSolution(data));
        }
        for (int i = 0; i < maxIter; i++) {
            System.out.println("Performing iteration %s".formatted(i));
            var currentPop = new ArrayList<RectSolution>();
            for (int j = 0; j < popSize; j++) {
                var evaluatedSol = evaluatedSolutions.take();
                currentPop.add(evaluatedSol);
            }
            System.out.println("Gathered all evaluated solutions");
            // remember best solution
            var iterBest = bestOf(currentPop, Comparator.comparing(e -> e.fitness));
            if (globalBest == null || globalBest.fitness <= iterBest.fitness) {
                globalBest = iterBest;
                SwingUtilities.invokeLater(() -> {
                    Evaluator e = new Evaluator(mainImage);
                    var im = e.draw(globalBest, new GrayScaleImage(mainImage.getWidth(), mainImage.getHeight()));
                    picLabel.setIcon(new ImageIcon(im.toBufferedImage()));
                });
            }
            System.out.println("Global best fitness: %s".formatted(globalBest.fitness));
            for (int j = 0; j < popSize; j++) {
                // System.out.println("Performing selection, crossover & mutation");
                // selection
                var mom = bestOf(pickNRandomElements(currentPop, k, rng), Comparator.comparing(e -> e.fitness));
                var dad = bestOf(pickNRandomElements(currentPop, k, rng), Comparator.comparing(e -> e.fitness));
                // crossover
                var childData = new int[numRectangles * 5 + 1];
                var crossoverPoint = rng.nextInt(1, numRectangles * 5);
                System.arraycopy(mom.getData(), 0, childData, 0, crossoverPoint);
                System.arraycopy(dad.getData(), crossoverPoint, childData, crossoverPoint, childData.length - crossoverPoint);
                var child = new RectSolution(childData);
                // TODO: mutate child
                mutate(child, mainImage.getWidth(), mainImage.getHeight());
                // add to next pop
                solutions.put(child);
            }
        }
        // poison all threads
        for (int i = 0; i < pool.length; i++) {
            solutions.put(new Poison());
        }
        // join all threads
        for (var thread : pool) {
            thread.join();
        }
        // write best image...
        // Showing on JFrame
        var eval = new Evaluator(mainImage);
        var result = new GrayScaleImage(mainImage.getWidth(), mainImage.getHeight());
        eval.draw(globalBest, result);
        var f = new File("/Users/bernard/IdeaProjects/oer-06/src/main/resources/out.png");
        f.createNewFile();
        result.save(f);
    }

    public <E> List<E> pickNRandomElements(List<E> list, int n, IRNG r) {
        int length = list.size();
        if (length < n) return null;
        //We don't need to shuffle the whole list
        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , r.nextInt(0, i + 1));
        }
        return list.subList(length - n, length);
    }

    public void mutate(RectSolution solution, int w, int h) {
        var rects = solution.getData();
        var rng = RNG.getRNG();
        for (int i = 1; i < rects.length; i += 5) {
            if (rng.nextDouble() < mutationProbability) {
                var tlX = min(w, max(0, rects[i] + rng.nextInt(0, mutationIntensity * 2) - mutationIntensity));
                var tlY = min(h, max(0, rects[i + 1] + rng.nextInt(0, mutationIntensity * 2) - mutationIntensity));
                var brX = min(w, max(0, rects[i] + rects[i + 2] + rng.nextInt(0, mutationIntensity * 2) - mutationIntensity));
                var brY = min(h, max(0, rects[i + 1] + rects[i + 3] + rng.nextInt(0, mutationIntensity * 2) - mutationIntensity));
                if (tlX > brX) {
                    var tmp = tlX;
                    tlX = brX;
                    brX = tmp;
                }
                if (tlY > brY) {
                    var tmp = tlY;
                    tlY = brY;
                    brY = tmp;
                }
                rects[i] = tlX;
                rects[i + 1] = tlY;
                rects[i + 2] = brX - tlX;
                rects[i + 3] = brY - tlY;
                // color mutation
                rects[i + 4] = min(255, max(0, rects[i + 4] + rng.nextInt(0, mutationIntensity * 2) - mutationIntensity));
            }
        }
    }

    public <E> E bestOf(Collection<? extends E> collection, Comparator<E> cmp) {
        return collection.stream().max(cmp).get();
    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        new Pokretac1().run();
    }
}
