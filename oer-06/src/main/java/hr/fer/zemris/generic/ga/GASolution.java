package hr.fer.zemris.generic.ga;

// a single solution consisting of rectangle list
public abstract class GASolution<T> implements Comparable<GASolution<T>> {
    protected T data;
    public double fitness;
    public boolean poison = false;

    public GASolution() { }

    public T getData() {
        return data;
    }

    public abstract GASolution<T> duplicate();

    @Override
    public int compareTo(GASolution<T> o) {
        return -Double.compare(this.fitness, o.fitness);
    }
}
