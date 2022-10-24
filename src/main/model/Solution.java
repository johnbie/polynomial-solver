package model;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    private static final DecimalFormat ROUNDING_FORMAT = new DecimalFormat("0.000000");
    private double value;
    private String displayText;
    private static final double EPSILON = 0.0000000001;

    public Solution() {
        this.value = 0;
        displayText = "0";
    }

    public Solution(double value) {
        this.value = value;

        ROUNDING_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
        displayText = ROUNDING_FORMAT.format(value);
    }

    public Solution(int numerator, int denominator) {
        this.value = (double)(numerator) / denominator;
        displayText = numerator + (denominator > 1 ? "/" + denominator : "");
    }

    // REQUIREMENT: rootedPart is positive
    public Solution(int numerator, int denominator, int rootedPart, boolean isPositive) {
        // get value
        this.value = isPositive ? Math.sqrt(rootedPart) : -Math.sqrt(rootedPart);
        this.value += numerator;
        this.value /= 2 * denominator;

        // extract the square from rooted part (if exists)
        int squaredRootedPart = NMathUtil.getLargestFactorableSquare(rootedPart);
        rootedPart /= squaredRootedPart * squaredRootedPart;

        // simplify if needed
        int gcd = NMathUtil.getGCD(Math.abs(numerator), NMathUtil.getGCD(denominator, squaredRootedPart));
        numerator /= gcd;
        denominator /= gcd;
        squaredRootedPart /= gcd;

        // get the display value
        displayText = numerator + (isPositive ? "+" : "-");
        displayText += squaredRootedPart > 1 ? squaredRootedPart : "";
        displayText += "sqrt(" + rootedPart + ")";
        displayText +=  denominator > 1 ? "/" + denominator : "";
    }

    // Gets the x intercepts of the function in parameter as a rational if possible
    // EFFECTS: gets the x intercepts
    public static List<Solution> solveForPolynomial(Polynomial polynomial) {
        List<Solution> solutions = new ArrayList<>();
        List<Term> terms = polynomial.getTerms();

        int size = terms.size();
        if (size == 0) { // no more solutions
            return solutions;
        }

        // purpose: factor out x=0 from polynomial until constant exists
        // method: get the degree of last term; this is the number of x=0
        int numberOfZeroIntercepts = terms.get(0).getDegree();
        if (numberOfZeroIntercepts > 0) {
            solutions.add(new Solution());
        }

        // no more solutions because the remaining "factor" is a constant
        if (size == 1) {
            return solutions;
        }

        normalizeTerms(terms, getLcmForDenominators(terms), numberOfZeroIntercepts);
        runRationalRootTheorem(solutions, terms);

        // check and solve for quadratic
        checkSolveQuadratic(solutions, terms);

        //

        // return all the coefficients as a list
        return solutions;
    }

    private static List<Solution> sortAscending() {
        return null;
    }

    @Override
    public String toString() {
        return displayText;
    }

    // Gets the lowest common multiple of the denominator
    // EFFECTS: gets the lcm
    private static int getLcmForDenominators(List<Term> terms) {
        // get LCM of the denominators
        int lcm = 1;
        for (Term term : terms) {
            int denominator = term.getDenominator();

            if (denominator > 1) {
                lcm = NMathUtil.getLCM(lcm,denominator);
            }
        }

        return lcm;
    }

    // Normalizes the polynomial such that the x's are factored out
    // and the coefficients are integers (multiply all by lcm)
    // MODIFIES: list of terms
    // EFFECTS: Normalizes the polynomial
    private static void normalizeTerms(List<Term> terms, int lcm, int numberOfZeroIntercepts) {
        // subtract the degree, increase numerator to integer-normalized value, and set denominator to 1
        for (Term term : terms) {
            term.setDegree(term.getDegree() - numberOfZeroIntercepts);
            term.setNumerator(term.getNumerator() * lcm);
            term.setDenominator(1); // for coverage; redundant
        }
    }

    // check for and add rational coefficients based on the Rational Root Theorem
    // also, factors out the rational components found in the polynomial
    // REQUIRES: normalized polynomial (denominators all equal 1)
    // MODIFIES: coefficients, polynomial
    // EFFECTS: check for and add rational coefficients
    private static void runRationalRootTheorem(List<Solution> solutions, List<Term> normalizedTerms) {
        // get factors for leading coefficient and constant (both now an integer)
        int lastPos = normalizedTerms.size() - 1;
        int leadingCoefficient = normalizedTerms.get(lastPos).getNumerator();
        List<Integer> leadingCoefficientFactors = NMathUtil.getFactors(Math.abs(leadingCoefficient));
        int constant = normalizedTerms.get(0).getNumerator();
        List<Integer> constantFactors = NMathUtil.getFactors(Math.abs(constant));

        // use epsilon and absolute value to account for rounding error
        for (Integer a : constantFactors) {
            for (Integer b : leadingCoefficientFactors) {
                double pointValPositive = Polynomial.evaluateAtPoint((double)(a) / b, normalizedTerms);
                if (Math.abs(pointValPositive) < EPSILON) {
                    solutions.add(new Solution(a, b));
                    factorOut(a, b, normalizedTerms);
                }
                double pointValNegative = Polynomial.evaluateAtPoint((double)(-a) / b, normalizedTerms);
                if (Math.abs(pointValNegative) < EPSILON) {
                    solutions.add(new Solution(-a, b));
                    factorOut(-a, b, normalizedTerms);
                }
            }
        }
    }

    // factors out rational solution from polynomial
    // REQUIRES: polynomial that can be factored by the input
    // MODIFIES: polynomial
    // EFFECTS: factors out rational solution from polynomial
    private static void factorOut(int n, int d, List<Term> normalizedTerms) {
        int size = normalizedTerms.size();
        Term nextTerm = normalizedTerms.remove(size - 1);
        int newDegree = nextTerm.getDegree() - 1;
        int remainder = nextTerm.getNumerator() / d;

        // look at terms with in reverse order (highest degree first)
        for (int i = size - 2; i >= 0; i--) {
            nextTerm = normalizedTerms.remove(i);
            int nextDegree = nextTerm.getDegree();

            while (newDegree >= nextDegree) {
                normalizedTerms.add(i, new Term(remainder, 1, newDegree));
                if (newDegree == nextDegree) {
                    remainder = (nextTerm.getNumerator() + (remainder * n)) / d;
                } else {
                    remainder = (remainder * n) / d;
                }
                newDegree--;
            }
        }
    }

    // check for and solves quadratic function
    // at this point, assume no rationals exists (and has such no linears)
    // MODIFIES: coefficients
    // EFFECTS: check for and solves quadratic/linear function
    private static void checkSolveQuadratic(List<Solution> solutions, List<Term> normalizedTerms) {
        int a = 0;
        int b = 0;
        int c = 0;

        for (Term term : normalizedTerms) {
            if (term.getDegree() == 0) {
                c = term.getNumerator();
            } else if (term.getDegree() == 1) {
                b = term.getNumerator();
            } else if (term.getDegree() == 2) {
                a = term.getNumerator();
            } else {
                return; // NOT a quadratic
            }
        }
        checkSolveQuadratic(solutions, a, b, c);
    }

    // check for and solves quadratic function
    // at this point, assume no rationals exists (and has such no linears)
    // MODIFIES: coefficients
    // EFFECTS: check for and solves quadratic/linear functions
    private static void checkSolveQuadratic(List<Solution> solutions, int a, int b, int c) {
        if (a != 0) {
            int denominator = 2 * a;
            int numerator = -b;

            if (denominator < 0) {
                numerator *= -1;
                denominator *= -1;
            }

            // get and factor out squares
            int rootedPart = (b * b) - (4 * a * c);

            // rational solutions were already factored out
            if (rootedPart > 0) {
                solutions.add(new Solution(numerator, denominator, rootedPart, false));
                solutions.add(new Solution(numerator, denominator, rootedPart, true));
            }
        }
    }
}
