package hr.fer.oer.jmbag0036514720;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class Prijenosna {

    // reads function from file and returns it
    public static IFunction getFunction(String[] args) {
        var equations = new BufferedReader(new InputStreamReader(Prijenosna.class.getResourceAsStream("/"+args[1]))).lines()
            .filter(l -> l.startsWith("["))
            .map(l -> l.replaceFirst("\\[", "").replaceAll("]", ""))
            .map(l -> Arrays.stream(l.split(","))
                .map(String::strip)
                .map(Double::parseDouble)
                .collect(Collectors.toList())
            ).collect(Collectors.toList());
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
                    var x1 = eq.get(0);
                    var x2 = eq.get(1);
                    var x3 = eq.get(2);
                    var x4 = eq.get(3);
                    var x5 = eq.get(4);
                    var expression = a * x1 + b * pow(x1, 3) * x2  + c * exp(d * x3) * (1 + cos(e * x4)) + f * x4 * pow(x5, 2);
                    out += pow(y - expression, 2);
                }
                return out / equations.size();
            }

            @Override
            public double[] getGradient(double[] inputs) {
                return new double[0];
            }
        };
        return fun;
    }
}
