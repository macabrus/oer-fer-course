package hr.fer.oer.jmbag0036514720;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import static java.lang.Math.*;

public class NumOptAlgorithms {

    private static final double epsilon = 0.00001;
    private static final Random rand = new Random();

    /**
     * Algoritam gradijentnog spusta iterativnim postupkom bisekcije.
     * 1. aproksimiramo gradijent u točki ()
     * 2. zabijemo se u prvu vecu ili jednaku vrijednost u smjeru gradijenta
     * 3. binary searchom na straight lineu pretrazujemo minimum
     * @param fun
     * @param maxIter
     */
    public static double[] gradientDescent(IFunction fun, int maxIter) {
        // fixed starting point
//        double x = -5., y = -5.;
        int numVars = fun.getNumberOfVariables();
        double[] pos = new double[numVars];
        for (int i = 0; i < numVars; i++) {
            // u rasponu -5, 5 svaka varijabla
            pos[i] = rand.nextDouble() * 10 - 5;
        }
        for (int i = 0; i < maxIter; i++) {

            var currentValue = fun.getValue(pos);
            System.out.println("Moved to function value %8.10f".formatted(currentValue));
            System.out.println("X1 X2 " + Arrays.toString(pos));

            // explore small shifts in both directions
            var epsilonShiftedValues = new double[numVars];
            for (int j = 0; j < numVars; j++) {
                var temp = new double[numVars];
                System.arraycopy(pos, 0, temp,0, pos.length);
                temp[j] += epsilon;
                epsilonShiftedValues[j] = fun.getValue(temp);
            }

            // now steepness in each direction
            var slopes = new double[numVars];
            for (int j = 0; j < numVars; j++) {
                slopes[j] = (epsilonShiftedValues[j] - currentValue) / epsilon;
            }

            // scale gradient to length 1
            var len = 0.;
            for (var val : slopes) {
                len += val * val;
            }
            System.out.println(Arrays.toString(slopes));

            var normalisationFactor = 1 / sqrt(len);

            var grads = new double[numVars];
            for (int j = 0; j < numVars; j++) {
                grads[j] = normalisationFactor * slopes[j];
            }
//            var gradX = normalisationFactor * slopeX;
//            var gradY = normalisationFactor * slopeY;

            // exponentially increment until we hit
            // some part which is at higher altitude (first worse solution => stopping)
            int step = 1; // 1, 2, 4, 8, 16...
            // negative gradient times step
            var posPlusScaledGrads = new double[numVars];
            for (int j = 0; j < numVars; j++) {
                posPlusScaledGrads[j] = pos[j] - grads[j] * step * epsilon;
            }
            while (fun.getValue(posPlusScaledGrads) < currentValue) {
                step <<= 1;
                for (int j = 0; j < numVars; j++) {
                    posPlusScaledGrads[j] = pos[j] - grads[j] * step * epsilon;
                }
            }

            // bounds for binary search
            var lowers = new double[numVars];
            System.arraycopy(pos, 0, lowers, 0, pos.length);
            var uppers = new double[numVars];
            for (int j = 0; j < numVars; j++) {
                uppers[j] = pos[j] - grads[j] * step * epsilon;
            }

            pos = bisect(lowers, uppers, fun);


//            sleep();
        }
        return pos;
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static final double[] bisect(double[] lowers, double[] uppers, IFunction fun) {
        if (lowers.length != uppers.length) {
            throw new IllegalArgumentException("Lower and upper vector bounds must be of equal dimensions");
        }
        var numVars = lowers.length;
        var len = 0.;
        for (int j = 0; j < numVars; j++) {
            len += ((uppers[j] - lowers[j]) * (uppers[j] - lowers[j]));
        }
        // run until interval length is no longer bigger than epsilon
        while (sqrt(len) > epsilon) {
//            System.out.println("Len is " + len + " sqrt is " + sqrt(len));
            // mid point
            var upperMids = new double[numVars];
            for (int j = 0; j < numVars; j++) {
                upperMids[j] = lowers[j] + (uppers[j] - lowers[j]) * 3 / 4;
            }
            var lowerMids = new double[numVars];
            for (int j = 0; j < numVars; j++) {
                lowerMids[j] = lowers[j] + (uppers[j] - lowers[j]) / 4;
            }
//            System.out.println("%8.2f, %8.2f --- %8.2f, %8.2f".formatted(xUpperMid, yUpperMid, xLowerMid, yLowerMid));

            // if upper half center is larger than lower half center
            // set new upper bound to upper half
            if (fun.getValue(upperMids) > fun.getValue(lowerMids)) {
                uppers = upperMids;
            } else {
                // else set lower bound to lower half
                lowers = lowerMids;
            }
            len = 0.;
            for (int j = 0; j < numVars; j++) {
//                System.out.println("UPPERS AND LOWERS " + uppers[j] + " " + lowers[j]);
                len += ((uppers[j] - lowers[j]) * (uppers[j] - lowers[j]));
            }
        }
        return uppers;
    }
}
