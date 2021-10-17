package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.IOptAlgorithm;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.SATFormulaStats;

import java.util.*;

// flipamo random par bitova u nepunom rjesenju
public class Algorithm6 implements IOptAlgorithm {

    private static final int MAX_ITER = 100000; // force stop even if local optimum is not yet found
    private static final double flipPercentage = 0.3;
    private final Random rand = new Random();

    private final SATFormula formula;

    public Algorithm6(SATFormula formula) {
        this.formula = formula;
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {
        BitVector input = initial.orElseGet(() -> new BitVector(rand, formula.getNumberOfVariables()));
        while (true) {
            // if initial vector is passed, we use it
            // otherwise generate random one
            for (int i = 0; i < formula.getNumberOfVariables(); i++) {
                if (rand.nextDouble() <= flipPercentage) {
                    input = input.copy().flip(i);
                }
            }
//            System.out.println("Starting with vector: " + input);
            var stats = new SATFormulaStats(formula);
            for (int restart = 0; restart < MAX_ITER; restart++) {
                stats.setAssignment(input, false);
                int inputFitness = stats.getNumberOfSatisfied();
//                System.out.println("Input satisfies " + inputFitness + "/" + formula.getNumberOfClauses() + " clauses.");
                // provjerimo jesmo li pogodili
                if (formula.isSatisfied(input)) {
                    return Optional.of(input);
                }
                var neighbors = genNeighbors(input);
//                System.out.println("Generated neighbors: ");
//                Arrays.stream(neighbors).forEach(System.out::println);
                var scores = new int[neighbors.length];
                for (int i = 0; i < neighbors.length; i++) {
                    stats.setAssignment(neighbors[i], false);
                    scores[i] = stats.getNumberOfSatisfied();
                }
                Integer topScore = Arrays.stream(scores).max().getAsInt();
//                System.out.println("Top score in neighbors is: " + topScore);
                // ako nema boljeg od trenutnog inputa, nasli smo optimum
                if (topScore <= inputFitness) {
//                    System.out.println("Returning " + input);
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
