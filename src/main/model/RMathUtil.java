package model;

/*
 * The math utility class for real numbers R
 */
public final class RMathUtil {
    private static final double EPSILON = 0.0000000001;

    // EFFECT: returns whether double a is approximately equal to 0
    public static boolean approximatelyEqualToZero(double a) {
        return EPSILON >= a && -EPSILON <= a;
    }

    // EFFECT: returns whether the two doubles have different signs (+, -)
    public static boolean signsAreOpposites(double a, double b) {
        return (a > 0 && b < 0) || (a < 0 && b > 0);
    }
}
