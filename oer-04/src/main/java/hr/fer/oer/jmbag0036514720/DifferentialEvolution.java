package hr.fer.oer.jmbag0036514720;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DifferentialEvolution {

    // jako dobri parametri koje sam nasao:
    // n = 15 ili vise, F = 0.5, Cr = 0.2, iter = 1000 il vise
    private final int n;
    private final int D;
    private List<double[]> population;
    private static final Random rand = new Random();
    private IFunction fun;
    private boolean kraj = false;
    private double F = 0.5;
    private double Cr = 0.6;
    private final int MAX_ITER;
    private String targetStrategy;
    // najbolje zabiljezeno rjesenje
    private double[] currentBest;
    // najbolje rjesenje trenutne populacije
    private double[] populationBest;

    public DifferentialEvolution(IFunction fun, int n, double F, double Cr, int maxIter, String targetStrategy) {
        this.fun = fun;
        this.D = fun.getNumberOfVariables();
        this.n = n;
        this.F = F;
        this.Cr = Cr;
        this.targetStrategy = targetStrategy;
        MAX_ITER = maxIter;
    }

    // ALGORITAM DIFERENCIJSKE EVOLUCIJE
    public double run() {
        /* generiramo populaciju */
        population = generatePopulation();
        System.out.println("Population size: " + population.size());
        /* evaluiramo populaciju */
        int iter = 0;
        while(iter < MAX_ITER) {
            var trialPopulation = new ArrayList<double[]>();
            // fitnesi
            var fitnesses = population
                .stream()
                .mapToDouble(this::getFitness)
                .toArray();
            // index najboljeg
            int populationBestIndex = IntStream.range(0, fitnesses.length)
                .reduce((a, b) -> fitnesses[a] < fitnesses[b] ? b: a).getAsInt();
            // najbolji trenutne populacije
            populationBest = population.get(populationBestIndex);
            // globalno najbolji?
            if (currentBest == null) {
                currentBest = populationBest;
            }
            else if (getFitness(populationBest) > getFitness(currentBest)) {
                System.arraycopy(populationBest, 0, currentBest, 0, currentBest.length);
            }
            for (int i = 0; i < population.size(); i++) {
                // target vektor strategije
                var target = switch (targetStrategy) {
                    case "rand" -> population.get(rand.nextInt(n));
                    case "target-to-best" -> population.get(populationBestIndex);
                        // population
                        //     .stream()
                        //     .max(Comparator.comparing(this::getFitness))
                        //     .orElse(null);
                    case "best" -> currentBest != null ? currentBest : population.get(i);
                    default -> population.get(i);
                };
                // bazni vektor
                int idx0 = rand.nextInt(n);
                var r0 = population.get(idx0);
                int idx1 = rand.nextInt(n);
                int idx2 = rand.nextInt(n);
                while (idx1 == idx0) {
                    idx1 = rand.nextInt(n);
                }
                while (idx2 == idx0 || idx2 == idx1) {
                    idx2 = rand.nextInt(n);
                }
                var r1 = population.get(idx1);
                var r2 = population.get(idx2);
                //System.out.println("r0" + Arrays.toString(r0));
                //System.out.println("r1" + Arrays.toString(r1));
                //System.out.println("r2" + Arrays.toString(r2));
                // izracun mutanta
                var mutant = add(r0, mul(F, sub(r1,r2)));
                // krizanje ciljnog vektora i vektora mutanta
                var trial = crossover(target, mutant);
                trialPopulation.add(trial);
            }
            // usporedujemo trial vektore sa populacijom
            for (int i = 0; i < population.size(); i++) {
                var trial = trialPopulation.get(i);
                if (getFitness(trial) >= fitnesses[i]) {
                    population.set(i, trial);
                }
            }

            // System.out.println("--- Generation %s ---".formatted(iter));
            //sleep(1000);
            iter++;
        }
        var best = population.stream().max(Comparator.comparing(this::getFitness)).orElse(null);
        var knownBest = new double[] {7, -3, 2, 1, -3, 3}; // ili +3 na predzadnjem
        System.out.println("Known best solution:  " + Arrays.toString(knownBest));
        System.out.println("Best solution fitness:" + getFitness(knownBest));

        System.out.println("Ending best solution: " + Arrays.toString(best));
        System.out.println("Fitness:              " + getFitness(best));
        return getFitness(best);
    }

    private String fmtPop(List<double[]> pop) {
        return pop.stream().map(Arrays::toString).collect(Collectors.joining(" ")).substring(0, 100) + "...";
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private double[] mul(double constant, double[] vec) {
        return Arrays.stream(vec).map(v -> constant * v).toArray();
    }

    private double[] add(double[] vec1, double[] vec2) {
        return IntStream.range(0, vec1.length)
            .mapToDouble((i) -> vec1[i] + vec2[i])
            .toArray();
    }


    private double[] sub(double[] vec1, double[] vec2) {
        return IntStream.range(0, vec1.length)
            .mapToDouble((i) -> vec1[i] - vec2[i])
            .toArray();
    }

    private double[] crossover(double[] target, double[] mutant) {
        if (target.length != mutant.length) throw new InputMismatchException("Mom and dad should be of same species");
        var child = new double[target.length];
        for (int i = 0; i < target.length; i++) {
            // linear interpolation
            child[i] = rand.nextDouble() <= Cr ? mutant[i] : target[i];
        }
        return child;
    }

    public double getFitness(double[] solution) {
        return -fun.getValue(solution);
    }

    public List<double[]> generatePopulation() {
        var pop = new ArrayList<double[]>();
        for (int i = 0; i < n; i++) {
            var unit = new double[D];
            for(int j = 0; j < D; j++) {
                unit[j] = rand.nextDouble() * 20 - 10;
            }
            pop.add(unit);
        }
        return pop;
    }

    public List<double[]> getPopulation() {
        return population;
    }

    public void setPopulation(List<double[]> population) {
        this.population = population;
    }
}
