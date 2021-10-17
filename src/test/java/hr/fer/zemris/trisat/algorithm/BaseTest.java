package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.TriSATSolver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BaseTest {
    public SATFormula prepareFormula(String inputFile) {
        var is = TriSATSolver.class.getResourceAsStream( inputFile);
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
        return new SATFormula(numVars, bitVectors);
    }

}
