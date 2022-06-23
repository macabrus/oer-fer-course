package hr.fer.oer.jmbag0036514720;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class Prijenosna {
    public static void main(String[] args) {
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
        //a⋅x1+b⋅x31 x2+ced⋅x3(1+cos(e⋅x4))+f⋅x4 x25
        var fun = new IFunction() {
            @Override
            public int getNumberOfVariables() {
                return 6;
            }

            @Override
            public double getValue(double[] inputs) {
                // a b c d e f
                var a = inputs[0];
                var b = inputs[1];
                var c = inputs[2];
                var d = inputs[3];
                var e = inputs[4];
                var f = inputs[5];
                var out = 0.0;
                for (var eq : equations) {
                    var y = eq.get(eq.size() - 1);
                    var expression = a * eq.get(0) + b * pow(eq.get(0), 3) * eq.get(1)  + c * exp(d * eq.get(2)) * (1 + cos(e * eq.get(3))) + f * eq.get(3) * pow(eq.get(4), 2);
                    out += pow(y - expression, 2);
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
