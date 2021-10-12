package hr.fer.zemris.trisat;

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

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isSatisfied(BitVector assignment) {
        var input = assignment.copy();
        var a =
            "10000100100001101001\n" +
            "10000100000011101001\n" +
            "10010100000011101001\n" +
            "10000100100011101001\n" +
            "10010000010011101001\n" +
            "10010100010011101001\n" +
            "10010001010011101001\n" +
            "01110001111001101111";
        for (int i = 0; i < masks.length; i++) {
            // prvo moram ANDat input sa maskom varijabli
            // onda moram xorat rezultat sa invertima
//            var filteredVars = inputs.and(masks[i]);
//            var filteredSigns = inverts[i].copy().and(masks[i]);
            var result = input.xor(inverts[i]).and(masks[i]);

//            if (input.toString().equals("00000100100001101001")) {
//                System.out.println("INPUT    " + input);
//                System.out.println("VARS     " + masks[i]);
//                System.out.println("FLIP     " + inverts[i]);
//                System.out.println("RESULT   " + result);
//                System.out.println("-".repeat(30));
////                sleep(1000);
//            }
            if (result.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return null;
    }
}
