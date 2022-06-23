package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.IOptAlgorithm;
import hr.fer.zemris.trisat.SATFormula;

import java.util.*;

public class RandomWalkSAT implements IOptAlgorithm {
    private SATFormula formula;
    private static final int MAX_ITER = 100000; // force stop even if local optimum is not yet found
    private static final int MAX_FLIPS = 5;
    private static final double p = 0.1;
    private final Random rand = new Random();
    public RandomWalkSAT(SATFormula formula) {
        this.formula = formula;
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {
        //for restart = 1 do MAX-TRIES
        BitVector input = initial.orElseGet(() -> new BitVector(rand, formula.getNumberOfVariables()));
        for (int i = 0; i < MAX_ITER; i++) {
            System.out.println("Trying:  " + input);
            System.out.println("Fitness: " + formula.getNumberOfSatisfiedClauses(input) + "/" + formula.getNumberOfClauses());
            if (formula.isSatisfied(input)) {
                return Optional.of(input);
            }
            var unsatisfiedClauses = new ArrayList<Integer>();
            for (int j = 0; j < formula.getNumberOfClauses(); j++) {
                if (formula.satisfiesClause(input, j)) {
                    unsatisfiedClauses.add(j);
                }
            }
            for(int flip = 0; flip < MAX_FLIPS; flip++) {
                int unsatisfiedClauseIndex= unsatisfiedClauses.get(rand.nextInt(unsatisfiedClauses.size()));
                // s vjerojatnošću p random okreni jednu varijablu u toj klauzuli
                if (rand.nextDouble() < p) {
                    var unsatisfiedClause = formula.getClause(unsatisfiedClauseIndex);
                    var clauseVars = unsatisfiedClause.getValue();
                    var varIndex = randomTruthyBitIndex(clauseVars);
                    input = input.copy().flip(varIndex);
                }
                // s vjerojatnošću (1-p) okreni onu varijablu koja rezultira
                // novom dodjelom s minimalnim brojem nezadovoljenih klauzula
                if (rand.nextDouble() < (1 - p)) {
                    var neighbors = genNeighbors(input);
                    input = Arrays.stream(neighbors)
                        .max(Comparator.comparing(n -> formula.getNumberOfSatisfiedClauses(n)))
                        .orElseThrow();
                }
            }
        }
        //T = random dodjela varijabli
        //  ako T zadovoljava klauzule, vrati T
        //for promjena = 1 do MAX-FLIPS
        //random odaberi jednu nezadovoljenu klauzulu
        //kraj kraj
        //vrati "dodjela nije pronađena"
        return Optional.empty();
    }

    private int randomTruthyBitIndex(BitVector bv) {
//        System.out.println("VARS " + bv);
        var v = bv.copyBackingBitSet();
        int card = v.cardinality();
//        System.out.println("CARDINALITY: " + card);
        int winner = rand.nextInt(card) + 1;
//        System.out.println("WINNER: " + winner);
        for(int i = 0, j = 0, len = bv.getSize(); i < len; i++) {
            if (v.get(i)) {
                j++;
            }
            if (j == winner) {
//                System.out.println("INDEX: " + i);
                return i;
            }
        }
        throw new RuntimeException("This should not happen");
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
