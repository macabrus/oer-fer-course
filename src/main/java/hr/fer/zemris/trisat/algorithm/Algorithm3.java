package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.IOptAlgorithm;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.SATFormulaStats;

import java.util.*;

public class Algorithm3 implements IOptAlgorithm {
    private final SATFormula formula;

    private final Random rand = new Random();
    private final int numberOfBest = 2;
    private final double percentageConstantUp = 0.01;
    private final double percentageConstantDown = 0.1;
    private final double percentageUnitAmount = 50.;
    private final double[] post; // accumulated priorities for clauses

    public Algorithm3(SATFormula formula) {
        this.formula = formula;
        this.post = new double[formula.getNumberOfClauses()];
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {
        BitVector input = initial.orElseGet(() -> new BitVector(rand, formula.getNumberOfVariables()));
        var stats = new SATFormulaStats(formula);
        while (true) {
            System.out.println("Starting with vector: " + input);
            stats.setAssignment(input, false);
            System.out.println("Input fitness: " + stats.getNumberOfSatisfied() + "/" + formula.getNumberOfClauses());
            if (formula.isSatisfied(input)) {
                System.out.println("Returning " + input);
                return Optional.of(input);
            }
            System.out.println("Updating statistics");
            for (int i = 0; i < formula.getNumberOfClauses(); i++) {
                if (formula.satisfiesClause(input, i)) {
                    post[i] += (1 - post[i]) * percentageConstantUp;
                }
                else {
                    post[i] -= (0 - post[i]) * percentageConstantDown;
                }
            }
//            System.out.println(Arrays.toString(post));
            var topNeighbors = Arrays.stream(genNeighbors(input))
                .map(n -> new AbstractMap.SimpleEntry<>(computeFitness(n), n))
                .sorted(Comparator.comparing(e -> -e.getKey()))
                .limit(numberOfBest)
                .map(AbstractMap.SimpleEntry::getValue)
                .toArray(BitVector[]::new);
            input = topNeighbors[rand.nextInt(topNeighbors.length)];
//            sleep(1000);
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // racuna Z
    private int computeFitness(BitVector input) {
        var Z = 0;
        for (int i = 0; i < formula.getNumberOfClauses(); i++) {
            if (formula.satisfiesClause(input, i)) {
                Z += percentageUnitAmount * (1-post[i]);
            }
            else {
                Z += -percentageUnitAmount * (1-post[i]);
            }
        }
        return Z;
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
