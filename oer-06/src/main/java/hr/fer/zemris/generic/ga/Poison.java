package hr.fer.zemris.generic.ga;

public class Poison extends RectSolution {
    public Poison() {
        super(null);
        poison = true;
    }

    @Override
    public GASolution<int[]> duplicate() {
        return this;
    }

}
