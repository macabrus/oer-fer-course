package hr.fer.zemris.trisat;

import java.util.AbstractMap;
import java.util.Arrays;

public class SATFormula {
    private final int numberOfVariables;
    private final BitVector[] masks;
    private final BitVector[] inverts;

    public SATFormula(int numberOfVariables, BitVector[][] clauses) {
        this.numberOfVariables = numberOfVariables;
        masks = Arrays.stream(clauses).map(row -> row[0]).toArray(BitVector[]::new);
        inverts = new BitVector[clauses.length];
        for (int i = 0; i < clauses.length; i++) {
            inverts[i] = clauses[i][1].copy().not();
        }
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public int getNumberOfClauses() {
        return masks.length;
    }

    public int getNumberOfSatisfiedClauses(BitVector bv) {
        var input = bv.copy();
        int numOfSatisfied = 0;
        for (int i = 0; i < masks.length; i++) {
            var result = input.xor(inverts[i]).and(masks[i]);
            if (!result.isEmpty()) {
                numOfSatisfied++;
            }
        }
        return numOfSatisfied;
    }

    public boolean satisfiesClause(BitVector input, int clauseIndex) {
        return !input.copy().xor(inverts[clauseIndex]).and(masks[clauseIndex]).isEmpty();
    }

    public boolean isSatisfied(BitVector assignment) {
        var input = assignment.copy();
        for (int i = 0; i < masks.length; i++) {
            var result = input.xor(inverts[i]).and(masks[i]);
            if (result.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public AbstractMap.SimpleEntry<BitVector, BitVector> getClause(int i) {
        return new AbstractMap.SimpleEntry<>(inverts[i].copy(), masks[i].copy());
    }

    @Override
    public String toString() {
        return null;
    }
}
