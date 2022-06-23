package hr.fer.oer.jmbag0036514720;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.min;

public class SASEGASA {

    private final int popSize = 30;
    private final int initialVillages = 5;
    private final double minSuccessfulChildrenPercent = 0.4;
    private final double maxSelPressure = 30;
    private final double comparisonFactor = 1;
    private final IFunction fun;
    private final int numOfVariables;
    private final Random rand = new Random();
    public double[][] population;
    private static final int MAX_ITER = 40_000;

    public SASEGASA(IFunction fun) {
        this.fun = fun;
        numOfVariables = fun.getNumberOfVariables();
        population = new double[popSize][numOfVariables];
        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < numOfVariables; j++) {
                // uniformno iz podrucja [-5, 5]
                population[i][j] = rand.nextDouble() * 10 - 5;
            }
        }
    }


    public static void main(String[] args) {
        var fun = Prijenosna.getFunction(new String[]{"10000", "prijenosna.txt"});
        var sase = new SASEGASA(fun);
        sase.run();
        System.out.println(Arrays.toString(Arrays.stream(sase.population).max(Comparator.comparing(sase::getFitness)).get()));
    }

    private String fmt(double[] arr) {
        return "[" + Arrays.stream(arr)
            .mapToObj(num -> String.format("%10.4f", num))
            .collect(Collectors.joining(", ")) + "]";
    }

    public double getFitness(double[] solution) {
        // posto minimiziramo, zelimo sto manji mean square error
        // i zato je dobrota obratna funkciji pogreske
        // maksimiziramo negativnu srednju kvadratnu pogresku
        return -fun.getValue(solution);
    }

    public void run() {
        var sentinel = new double[0][0];
        // iteracije za sela
        for (int villages = initialVillages; villages > 0; villages--) {
            System.out.println("--- Running with %s villages ---".formatted(villages));
            // odredivanje ograde sela u listi cijele populacije
            int k = population.length / villages; // rounded to lower
            int m = population.length % villages; // remainder
            for (int i = 0; i < villages; i++) {
                System.out.println("--- %s-th village ---".formatted(i));
                int lowerIndex = i * k + min(i, m);
                int upperIndex = (i + 1) * k + min(i + 1, m);
                var village = Arrays.asList(population).subList(lowerIndex, upperIndex);
                var startingBest = village.stream().max(Comparator.comparing(this::getFitness)).get();
                System.out.println("Starting best: " + fmt(startingBest));
                System.out.println("Fitness:       " + getFitness(startingBest));
                for (int j = 0; j < MAX_ITER; j++) {
                    var pool = new ArrayList<double[]>();
                    var nextPop = new ArrayList<double[]>();
                    var numGenerations = 0;
                    while (
                        nextPop.size() < minSuccessfulChildrenPercent * village.size() &&
                        nextPop.size() + pool.size() < maxSelPressure * village.size()
                    ) {
                        numGenerations++;
                        // dvoturnirska selekcija roditelja
                        double[] mom, dad, child;
                        mom = tournamentSelect(village, 2);
                        do {
                            dad = tournamentSelect(village, 2);
                        } while (mom == dad);
                        // izracunavanje djeteta
                        if (rand.nextDouble() < 0.5) {
                            child = crossover(mom, dad);
                        } else {
                            child = crossoverStochastic(mom, dad);
                        }
                        // mutiranje dijeteta
                        if (rand.nextDouble() < 0.5) {
                            mutate(child);
                        } else {
                            mutateAlt(child);
                        }
                        // dobrota dijeteta
                        var childFitness = getFitness(child);
                        var momFitness = getFitness(mom);
                        var dadFitness = getFitness(dad);
                        // ovisno o tome kakvo je dijete, stavimo ga u sljedecu generaciju sela
                        if (childFitness > momFitness && childFitness > dadFitness) {
                            nextPop.add(child);
                        }
                        // ili ako je losije, u bazen
                        else {
                            pool.add(child);
                        }
                    }
                    // je li konvergiralo?
                    if (
                        nextPop.size() < minSuccessfulChildrenPercent * village.size() ||
                        nextPop.size() + pool.size() > maxSelPressure * village.size()
                    ) {
                        break;
                    }
                    // popunimo ostalih 60% sa random jedinkama iz poola
                    while (pool.size() > 0) {
                        if (nextPop.size() == village.size()) {
                            break;
                        }
                        nextPop.add(pool.get(rand.nextInt(pool.size())));
                    }
                    // ako i dalje nije popunjena
                    while (nextPop.size() < village.size()) {
                        // stvaramo nove da popunimo
                        double[] mom, dad, child;
                        mom = tournamentSelect(village, 2);
                        do {
                            dad = tournamentSelect(village, 2);
                        } while (mom == dad);
                        // izracunavanje djeteta
                        if (rand.nextDouble() < 0.5) {
                            child = crossover(mom, dad);
                        } else {
                            child = crossoverStochastic(mom, dad);
                        }
                        // mutiranje dijeteta
                        if (rand.nextDouble() < 0.5) {
                            mutate(child);
                        } else {
                            mutateAlt(child);
                        }
                        nextPop.add(child);
                    }
                    // replacing with new population after selection
                    for (int l = 0; l < nextPop.size(); l++) {
                        population[lowerIndex + l] = nextPop.get(l);
                    }
                }
                // Since village is just immutable view of array
                // it is accessing elements of backing array s
                // we can just reuse same command as above
                var endingBest = village.stream().max(Comparator.comparing(this::getFitness)).get();
                System.out.println("Ending best: " + fmt(endingBest));
                System.out.println("Fitness:       " + getFitness(endingBest));
            }
        }
    }

    private List<double[]> selectOffspring(List<double[]> village) {
        List<double[]> pool, nextPop = null;
        for (int i = 0; i < MAX_ITER; i++) {

        }
        return nextPop;
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private double[] tournamentSelect(List<double[]> village, int k /*k participants per tournament*/) {
        var copy = new ArrayList<>(village);
        Collections.shuffle(copy);
        return copy.subList(0, min(k, copy.size())).stream().max(Comparator.comparing(this::getFitness)).get();
    }

    private double[] crossover(double[] mom, double[] dad) {
        if (mom.length != dad.length) throw new InputMismatchException("Mom and dad should be of same species");
        var child = new double[mom.length];
        for (int i = 0; i < mom.length; i++) {
            // linear interpolation
            child[i] = (dad[i] + mom[i]) / 2;
        }
        return child;
    }

    private double[] crossoverStochastic(double[] mom, double[] dad) {
        if (mom.length != dad.length) throw new InputMismatchException("Mom and dad should be of same species");
        var child = new double[mom.length];
        for (int i = 0; i < mom.length; i++) {
            // 20% chance for
            if (rand.nextDouble() < 0.2) {
                // linear interpolation on every gene
                child[i] = (dad[i] + mom[i]) / 2;
                continue;
            }
            child[i] = mom[i];
        }
        return child;
    }

    private double[] mutate(double[] solution) {
        for (int i = 0; i < solution.length; i++) {
            if (rand.nextDouble() < 0.3) {
                solution[i] += rand.nextGaussian() * 0.9;
            }
        }
        return solution;
    }

    private double[] mutateAlt(double[] solution) {
        for (int i = 0; i < solution.length; i++) {
            if (rand.nextDouble() < 0.3) {
                solution[i] += rand.nextGaussian() * 0.1;
            }
        }
        return solution;
    }

}
