package hr.fer.zemris.trisat.algorithm;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class Algorithm3Test extends BaseTest {

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
        var alg3 = new Algorithm3(formula);
        var res = alg3.solve(Optional.empty()).orElse(null);
        if (res == null) {
            System.out.println("Solution was not found which is acceptable.");
        }
        else {
            System.out.println("Solution was found!");
            assertTrue(expect.contains(res.toString()));
        }
    }

    @Test
    void test0100() {
        // ponekad ne nade rjesenje...
        var formula = prepareFormula("/01-3sat/uf50-0100.cnf");
        var alg3 = new Algorithm3(formula);
        alg3.solve(Optional.empty()).ifPresent(res -> System.out.println("Solution was found!" + res));
    }

    @Test
    void test01000() {
        // ponekad ne nade rjesenje...
        var formula = prepareFormula("/01-3sat/uf50-01000.cnf");
        var alg3 = new Algorithm3(formula);
        alg3.solve(Optional.empty()).ifPresent(res -> System.out.println("Solution was found!" + res));
    }

}