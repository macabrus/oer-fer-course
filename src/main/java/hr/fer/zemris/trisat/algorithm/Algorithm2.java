package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.IOptAlgorithm;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.SATFormulaStats;

import java.util.*;

// Multistart local search (moze biti i single start)
// 1. starts with n random inputs
// 2. evaluate their fitness
// 3. compute neighbors of fittest one
// 4. neighbors are new inputs
public class Algorithm2 implements IOptAlgorithm {
    private static final int MAX_ITER = 100000; // force stop even if local optimum is not yet found
    private static final int MAX_TRIES = 1; // start at 10 random locations
    private final Random rand = new Random();

    private final SATFormula formula;

    public Algorithm2(SATFormula formula) {
        this.formula = formula;
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {
        for (int retry = 0; retry < MAX_TRIES; retry++) {
            // if initial vector is passed, we use it
            // otherwise generate random one
            BitVector input = initial.orElseGet(() -> new BitVector(rand, formula.getNumberOfVariables()));
            System.out.println("Starting with vector: " + input);
            var stats = new SATFormulaStats(formula);
            for (int restart = 0; restart < MAX_ITER; restart++) {
                stats.setAssignment(input, false);
                int inputFitness = stats.getNumberOfSatisfied();
                System.out.println("Input satisfies " + inputFitness + "/" + formula.getNumberOfClauses() + " clauses.");
                // provjerimo jesmo li pogodili
                if (formula.isSatisfied(input)) {
                    return Optional.of(input);
                }
                var neighbors = genNeighbors(input);
                System.out.println("Generated neighbors: ");
                Arrays.stream(neighbors).forEach(System.out::println);
                var scores = new int[neighbors.length];
                for (int i = 0; i < neighbors.length; i++) {
                    stats.setAssignment(neighbors[i], false);
                    scores[i] = stats.getNumberOfSatisfied();
                }
                Integer topScore = Arrays.stream(scores).max().getAsInt();
                System.out.println("Top score in neighbors is: " + topScore);
                // ako nema boljeg od trenutnog inputa, nasli smo optimum
                if (topScore <= inputFitness) {
                    System.out.println("Returning " + input);
//                    return Optional.of(input);
                    break;
                }
                var fittest = new ArrayList<BitVector>();
                for (int i = 0; i < neighbors.length; i++) {
                    if (topScore.equals(scores[i])) {
                        fittest.add(neighbors[i]);
                    }
                }
//                System.out.println("There are " + fittest.size() + " neighbors with same score.");
                input = fittest.get(rand.nextInt(fittest.size()));
//                System.out.println("Chose " + input + " as next input");
//                System.out.println("-".repeat(50));
            }
        }
        // not found
        return Optional.empty();
    }

    private BitVector[] genNeighbors(BitVector input) {
        var neighbors = new BitVector[input.getSize()];
        var bits = input.copy().copyBackingBitSet();
        for (int i = 0; i < input.getSize(); i++) {
            var copy = (BitSet) bits.clone();
            copy.flip(i);
            neighbors[i] = new BitVector(copy, formula.getNumberOfVariables());
        }
        return neighbors;
    }
}
