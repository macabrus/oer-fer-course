package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.IOptAlgorithm;
import hr.fer.zemris.trisat.SATFormula;

import java.util.Optional;

public class GSAT implements IOptAlgorithm {
    private SATFormula formula;

    public GSAT(SATFormula formula) {

        this.formula = formula;
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {
        return Optional.empty();
    }
}
