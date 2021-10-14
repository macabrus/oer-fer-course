package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.IOptAlgorithm;
import hr.fer.zemris.trisat.SATFormula;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

// simple brute force approach
// which runs in exponential time
// tries every possible combination of variables until it finds right one
// not very efficient...
public class BruteForce implements IOptAlgorithm {

    private SATFormula formula;

    public BruteForce(SATFormula formula) {
        this.formula = formula;
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {
        return Optional.empty();
    }

    public Set<String> solveAll(BitVector initial) {
        var input = initial.copy();
        var solutions = new HashSet<String>();
        for (int i = 0; i < 2 << formula.getNumberOfVariables() - 1; i ++) {
            if (formula.isSatisfied(input)) {
                solutions.add(input.toString());
            }
            input.inc();
        }
        return solutions;
    }
}
