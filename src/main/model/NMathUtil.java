package model;

import java.util.ArrayList;
import java.util.List;

/*
 * The math utility class for natural numbers N
 */
public final class NMathUtil {
    // Returns the factors for a positive integer (i.e. 6 has factors of [1,2,3,6]
    // REQUIRES: positive integer (a >= 0)
    // EFFECTS: Returns the factors
    public static List<Integer> getFactors(int a) {
        List<Integer> factors = new ArrayList<>();

        for (int i = 1; i <= a; i++) {
            if (a % i == 0) {
                factors.add(i);
            }
        }

        return factors;
    }

    // Returns the largest factorable square number from integer
    // (i.e. 10^2 can be factored out of 200, leaving 2)
    // REQUIRES: positive integer (a >= 0); negative numbers will just return 1
    // EFFECTS: Returns the largest factorable square number from integer
    public static int getLargestFactorableSquare(int a) {
        int result = 1;
        int n = 2;

        while (n * n <= a) {
            while (a % (n * n) == 0) {
                result *= n;
                a /= n * n;
            }
            n++;
        }
        return result;
    }

    // Returns the lowest common multiple of two numbers.
    // Code is based on the observation that lcm(a,b) = |ab|/gcd(a,b) = |a| * |b|/gcd(a,b)
    // (https://en.wikipedia.org/wiki/Least_common_multiple)
    // REQUIRES: two positive integers (a >= 0; b >= 0)
    // EFFECTS: Returns the lowest common multiple
    public static int getLCM(int a, int b) {
        return a / NMathUtil.getGCD(a,b) * b;
    }

    // Returns the greatest common divisor of two numbers.
    // Code is based on a subtraction-based implementation of the Euclid's Algorithm (https://en.wikipedia.org/wiki/Euclidean_algorithm)
    // REQUIRES: two positive integers (a >= 0; b >= 0)
    // EFFECTS: Returns the greatest common divisor
    public static int getGCD(int a, int b) {
        if (a == 0 || b == 0) {
            return 1;
        }

        while (a != b) {
            if (a > b) {
                a -= b;
            } else {
                b -= a;
            }
        }
        return a;
    }
}
