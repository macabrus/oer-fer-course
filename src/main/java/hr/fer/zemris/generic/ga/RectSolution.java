package hr.fer.zemris.generic.ga;

public class RectSolution extends GASolution<int[]>{
    public RectSolution(int[] dataCopy) {
        data = dataCopy;
    }

    @Override
    public GASolution<int[]> duplicate() {
        var dataCopy = new int[data.length];
        System.arraycopy(data, 0, dataCopy, 0, data.length);
        var tmp = new RectSolution(dataCopy);
        tmp.fitness = fitness;
        return tmp;
    }
}
