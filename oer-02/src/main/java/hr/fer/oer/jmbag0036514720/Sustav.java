package hr.fer.oer.jmbag0036514720;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Sustav {
    public static void main(String[] args) throws IOException {
        var maxIter = Integer.parseInt(args[0]);
        var equations = new BufferedReader(new InputStreamReader(Sustav.class.getResourceAsStream("/"+args[1]))).lines()
            .filter(l -> l.startsWith("["))
            .map(l -> l.replaceFirst("\\[", "").replaceAll("]", ""))
            .map(l -> Arrays.stream(l.split(","))
                .map(String::strip)
                .map(Double::parseDouble)
                .collect(Collectors.toList())
            ).collect(Collectors.toList());
        System.out.println(equations);

        // funkcija je

        var fun = new IFunction() {

            @Override
            public int getNumberOfVariables() {
                return 10;
            }

            @Override
            public double getValue(double[] inputs) {
                var out = 0.0;
                for (var equation : equations) {
                    var y = equation.get(equation.size() - 1);
                    var tmp = y;
                    for (int i = 0; i < inputs.length; i++) {
                        tmp -= equation.get(i) * inputs[i];
                    }
                    out += tmp * tmp;
                }
                return out;
            }

            @Override
            public double[] getGradient(double[] inputs) {
                return new double[0];
            }
        };


        var result = NumOptAlgorithms.gradientDescent(fun, maxIter);
        System.out.println(Arrays.toString(result));
    }
}
