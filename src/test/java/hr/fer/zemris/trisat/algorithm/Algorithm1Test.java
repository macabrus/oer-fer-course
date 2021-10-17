package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Algorithm1Test extends BaseTest {
    @Test
    public void uf_20_01() {
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
        var gsat = new Algorithm1(formula);
        var solutions = gsat.solveAll(new BitVector(20));
        assertEquals(solutions, expect);
    }

    @Test
    public void uf_20_010() {
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
        var gsat = new Algorithm1(formula);
        var solutions = gsat.solveAll(new BitVector(20));
        assertEquals(solutions, expect);
    }

    @Test
    public void uf_20_0100() {
        var formula = prepareFormula("/01-3sat/uf20-0100.cnf");
        var expect = new HashSet<>(Arrays.asList(
            "01110011100111110010",
            "01110011100111110011",
            "01111011100111110011",
            "01101101110011101011"
        ));
        var gsat = new Algorithm1(formula);
        var solutions = gsat.solveAll(new BitVector(20));
        assertEquals(solutions, expect);
    }

    @Test
    public void uf_20_01000() {
        var formula = prepareFormula("/01-3sat/uf20-01000.cnf");
        var expect = new HashSet<>(Collections.singletonList(
            "01011010100000011010"
        ));
        var gsat = new Algorithm1(formula);
        var solutions = gsat.solveAll(new BitVector(20));
        assertEquals(solutions, expect);
    }

}