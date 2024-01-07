package dev.vanadium.lightsim.math;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {

    public static List<Double> quadraticSolve(double a, double b, double c) {
        // If a == 0, the denominator is 0, hence no solution is possible.
        if (a == 0.0)
            return new ArrayList<>();
        double discriminant = Math.pow(b, 2) - 4 * a * c;
        if (discriminant < 0)
            return new ArrayList<>();
        ArrayList<Double> solutions = new ArrayList<>();
        solutions.add((-b + Math.sqrt(discriminant)) / 2.0 * a);
        if (discriminant == 0)
            return solutions;
        solutions.add((-b - Math.sqrt(discriminant)) / 2.0 * a);
        return solutions;
    }

}
