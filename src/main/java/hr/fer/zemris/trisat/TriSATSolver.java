package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.algorithm.GSAT;
import hr.fer.zemris.trisat.algorithm.ILS;
import hr.fer.zemris.trisat.algorithm.RandomWalkSat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class TriSATSolver {
    public static void main(String... args) {
        var algo = Integer.parseInt(args[0]);
        var example = args[1];
        var is = TriSATSolver.class.getResourceAsStream("/01-3sat/" + example);
        var isr = new InputStreamReader(is);
        var br = new BufferedReader(isr);
        var lines = br.lines()
            .takeWhile(l -> !l.startsWith("%"))
            .filter(l -> !l.startsWith("c"))
            .collect(Collectors.toList());
        var format = lines.get(0).strip().replaceAll("\\s+", " ").split(" ");
        var numVars = Integer.parseInt(format[2]);
        var numClauses = Integer.parseInt(format[3]);
        var bitVectors = new BitVector[numClauses][2];
        lines = lines.subList(1,lines.size());
        for (int i = 0; i < lines.size(); i ++) {
            var parts = lines.get(i).strip().replaceAll("\\s+", " ").split(" ");
            var masks = new boolean[numVars];
            var inverts = new boolean[numVars];
            Arrays.fill(inverts, true);
            for (int j = 0; j < parts.length - 1; j++) {
                var v = Integer.parseInt(parts[j]);
                if (v < 0) {
                    v = - v;
                    inverts[v - 1] = false;
                }
                masks[v - 1] = true;
            }
            bitVectors[i][0] = new BitVector(masks);
            bitVectors[i][1] = new BitVector(inverts);
        }
        var formula = new SATFormula(numVars, bitVectors);
        IOptAlgorithm alg = switch (algo) {
            case 1 -> new GSAT(formula);
            case 2 -> new RandomWalkSat(formula);
            case 3 -> new ILS(formula);
            default -> throw new UnsupportedOperationException("Algorithm not implemented");
        };
        Optional<BitVector> solution = alg.solve(Optional.of(new BitVector(numVars)));
//        Optional<BitVector> solution = Optional.empty();//alg.solve(Optional.of(new BitVector(numVars)));
        if (solution.isPresent()) {
            BitVector sol = solution.get();
            System.out.println("Imamo rješenje: " + sol);
        } else {
            System.out.println("Rješenje nije pronađeno.");
        }
    }

}
