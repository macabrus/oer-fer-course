package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.TriSATSolver;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ILSTest {

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

    @Test
    void test01() {
        var formula = prepareFormula("/01-3sat/uf20-01.cnf");
        var expect = new HashSet<>(Arrays.asList(
            "10000100100001101001",
            "10000100000011101001",
            "10010100000011101001",
            "10000100100011101001",
            "10010000010011101001",
            "10010100010011101001",
            "10010001010011101001",
            "01110001111001101111"
        ));
        var ils = new ILS(formula);
        var res = ils.solve(Optional.empty()).orElse(null);
        if (res == null) {
            System.out.println("Solution was not found which is acceptable.");
        }
        else {
            System.out.println("Solution was found!");
            assertTrue(expect.contains(res.toString()));
        }
    }

    @Test
    void test010() {

    }

    @Test
    void test0100() {

    }

}