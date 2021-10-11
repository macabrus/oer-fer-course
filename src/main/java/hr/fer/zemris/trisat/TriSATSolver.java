package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.algorithm.GSAT;

import java.util.Optional;

public class TriSATSolver {
    public static void main(String... args) {
        // SATFormula formula = ucitajIzDatoteke("neka staza");
        SATFormula formula = null;
        IOptAlgorithm alg = new GSAT(formula);
        Optional<BitVector> solution = alg.solve(Optional.empty());
        if (solution.isPresent()) {
            BitVector sol = solution.get();
            System.out.println("Imamo rješenje: " + sol);
        } else {
            System.out.println("Rješenje nije pronađeno.");
        }
    }

}
