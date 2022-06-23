package hr.fer.oer.jmbag0036514720;

public interface IFunction {
    int getNumberOfVariables();
    double getValue(double[] inputs);
    double[] getGradient(double[] inputs);
}
