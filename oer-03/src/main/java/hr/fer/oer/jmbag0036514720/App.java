package hr.fer.oer.jmbag0036514720;

import java.util.Arrays;

import static java.lang.Math.pow;

public class App {
    public static void main( String[] args ) {
        var fun1 = new IFunction() {

            @Override
            public int getNumberOfVariables() {
                return 2;
            }

            @Override
            public double getValue(double[] inputs) {
                if (inputs.length != getNumberOfVariables()) {
                    throw new IllegalArgumentException("Function accepts 2D inputs");
                }
                var x1 = inputs[0];
                var x2 = inputs[1];
                return pow(x1, 2) + pow(x2 - 1, 2);
            }

            @Override
            public double[] getGradient(double[] inputs) {
                return new double[0];
            }
        };

        var fun2 = new IFunction() {

            @Override
            public int getNumberOfVariables() {
                return 2;
            }

            @Override
            public double getValue(double[] inputs) {
                if (inputs.length != getNumberOfVariables()) {
                    throw new IllegalArgumentException("Function accepts 2D inputs");
                }
                return pow(inputs[0] - 1, 2) + 10 * pow(inputs[1] - 2, 2);
            }

            @Override
            public double[] getGradient(double[] inputs) {
                return new double[0];
            }
        };
        double[] result = NumOptAlgorithms.gradientDescent(fun1, 10);
        System.out.println("Settled at " + Arrays.toString(result));

    }
}
