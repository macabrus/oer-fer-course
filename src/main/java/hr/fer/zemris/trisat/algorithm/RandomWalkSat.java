package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.IOptAlgorithm;
import hr.fer.zemris.trisat.SATFormula;

import java.util.Optional;

public class RandomWalkSat implements IOptAlgorithm {
    private SATFormula formula;

    private static final int MAX_TRIES = 3;
    private static final int MAX_FLIPS = 1;

    public RandomWalkSat(SATFormula formula) {
        this.formula = formula;
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {
        for (int retry = 0; retry < MAX_TRIES; retry ++) {

        }
        return Optional.empty();
    }
}
