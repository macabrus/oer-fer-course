package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.IOptAlgorithm;
import hr.fer.zemris.trisat.SATFormula;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GSAT implements IOptAlgorithm {

    private SATFormula formula;

    public GSAT(SATFormula formula) {
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
