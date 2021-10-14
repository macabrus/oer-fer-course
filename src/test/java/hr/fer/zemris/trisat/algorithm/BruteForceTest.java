package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.TriSATSolver;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BruteForceTest {

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
    public void test01() {
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
        var gsat = new BruteForce(formula);
        var solutions = gsat.solveAll(new BitVector(20));
        assertEquals(solutions, expect);
    }

    @Test
    public void test010() {
        var formula = prepareFormula("/01-3sat/uf20-010.cnf");
        var expect = new HashSet<>(Arrays.asList(
            "00111101100001100010",
            "00111101100101100010",
            "00111101100101110010",
            "10111100100111100011",
            "00111101100111100011",
            "10111101100111100011",
            "10111100110111100011",
            "01001111000111110011",
            "00111101100111110011"
        ));
        var gsat = new BruteForce(formula);
        var solutions = gsat.solveAll(new BitVector(20));
        assertEquals(solutions, expect);
    }

    @Test
    public void test0100() {
        var formula = prepareFormula("/01-3sat/uf20-0100.cnf");
        var expect = new HashSet<>(Arrays.asList(
            "01110011100111110010",
            "01110011100111110011",
            "01111011100111110011",
            "01101101110011101011"
        ));
        var gsat = new BruteForce(formula);
        var solutions = gsat.solveAll(new BitVector(20));
        assertEquals(solutions, expect);
    }

    @Test
    public void test01000() {
        var formula = prepareFormula("/01-3sat/uf20-01000.cnf");
        var expect = new HashSet<>(Collections.singletonList(
            "01011010100000011010"
        ));
        var gsat = new BruteForce(formula);
        var solutions = gsat.solveAll(new BitVector(20));
        assertEquals(solutions, expect);
    }

}