package model;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Root implements Comparable<Root>  {
    private static final double DELTA = 0.0001;
    private static final DecimalFormat ROUNDING_FORMAT = new DecimalFormat("0.000000");
    private double value;
    private String displayText;

    public Root() {
        this.value = 0;
        displayText = "0";
    }

    public Root(double value) {
        this.value = value;

        ROUNDING_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
        displayText = ROUNDING_FORMAT.format(value);
    }

    public Root(int numerator, int denominator) {
        this.value = (double)(numerator) / denominator;
        displayText = numerator + (denominator > 1 ? "/" + denominator : "");
    }

    // REQUIREMENT: rootedPart is positive
    public Root(int numerator, int denominator, int rootedPart, boolean isPositive) {
        // get value
        this.value = isPositive ? Math.sqrt(rootedPart) : -Math.sqrt(rootedPart);
        this.value += numerator;
        this.value /= denominator;

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
    public static List<Root> solveForPolynomial(Polynomial polynomial) {
        List<Root> roots = new ArrayList<>();
        List<Term> terms = polynomial.getTerms();

        int size = terms.size();
        if (size == 0) { // no more solutions
            return roots;
        }

        // purpose: factor out x=0 from polynomial until constant exists
        // method: get the degree of last term; this is the number of x=0
        int numberOfZeroIntercepts = terms.get(0).getDegree();
        if (numberOfZeroIntercepts > 0) {
            roots.add(new Root());
        }

        // no more solutions because the remaining "factor" is a constant
        if (size == 1) {
            return roots;
        }

        normalizeTerms(terms, getLcmForDenominators(terms), numberOfZeroIntercepts);
        runRationalRootTheorem(roots, terms);

        // check and solve for quadratic
        if (terms.size() > 1 && !checkSolveQuadratic(roots, terms)) {
            // add real number solutions
            checkFindRealRoots(roots, terms);
        }

        // return all the coefficients as a list
        Collections.sort(roots);
        return roots;
    }

    @Override
    public String toString() {
        return displayText;
    }

    @Override
    public int compareTo(Root root) {
        return Double.compare(this.value, root.value);
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
    private static void runRationalRootTheorem(List<Root> roots, List<Term> normalizedTerms) {
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
                if (NMathUtil.approximatelyEqualToZero(pointValPositive)) {
                    roots.add(new Root(a, b));
                    factorOut(a, b, normalizedTerms);
                }
                double pointValNegative = Polynomial.evaluateAtPoint((double)(-a) / b, normalizedTerms);
                if (NMathUtil.approximatelyEqualToZero(pointValNegative)) {
                    roots.add(new Root(-a, b));
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
    private static boolean checkSolveQuadratic(List<Root> roots, List<Term> normalizedTerms) {
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
                return false; // NOT a quadratic
            }
        }
        checkSolveQuadratic(roots, a, b, c);
        return true;
    }

    // check for and solves quadratic function
    // at this point, assume no rationals exists (and has such no linears)
    // MODIFIES: coefficients
    // REQUIRES: a != 0 (already guaranteed by caller function)
    // EFFECTS: check for and solves quadratic/linear functions
    private static void checkSolveQuadratic(List<Root> roots, int a, int b, int c) {
        int denominator = 2 * a;
        int numerator = -b;

        if (a < 0) {
            numerator *= -1;
            denominator *= -1;
        }

        // get and factor out squares
        int rootedPart = (b * b) - (4 * a * c);

        // rational solutions were already factored out
        if (rootedPart > 0) {
            roots.add(new Root(numerator, denominator, rootedPart, false));
            roots.add(new Root(numerator, denominator, rootedPart, true));
        }
    }

    private static void checkFindRealRoots(List<Root> roots, List<Term> normalizedTerms) {
        checkBetweenZeroAndOne(roots, normalizedTerms);
        checkBetweenOneAndInfinity(roots, normalizedTerms, true);
        checkBetweenOneAndInfinity(roots, normalizedTerms, false);
    }

    private static void checkBetweenZeroAndOne(List<Root> roots, List<Term> normalizedTerms) {
        double point = -1;
        double lastSolution = Polynomial.evaluateAtPoint(point, normalizedTerms);

        // check for roots between -1 and 1
        while (point < 1) {
            point += DELTA;

            double currentSolution = Polynomial.evaluateAtPoint(point, normalizedTerms);
            if (NMathUtil.signsAreOpposites(lastSolution, currentSolution)) {
                addSolutionFromRange(roots, normalizedTerms, point - DELTA, point);
            }
            lastSolution = currentSolution;
        }
    }

    private static void checkBetweenOneAndInfinity(List<Root> roots,
                                                   List<Term> normalizedTerms,
                                                   boolean isPositive) {
        double point = isPositive ? 1 : -1;
        Term greatestTerm = normalizedTerms.get(normalizedTerms.size() - 1);
        double lastSolution = Polynomial.evaluateAtPoint(point, normalizedTerms) / greatestTerm.evaluateAtPoint(point);
        double currentSolution;

        // check for roots towards +infinity
        // uses a cool trick where p(x) / leading term approaches 1 towards both infinities
        // stop when value
        int deltasSinceLastSignificantEvent = 0;
        double lastDifference = 1; // placeholder
        while (lastSolution < 0 || deltasSinceLastSignificantEvent < 10000) {
            point += (DELTA * (isPositive ? 1 : -1));
            currentSolution = Polynomial.evaluateAtPoint(point, normalizedTerms) / greatestTerm.evaluateAtPoint(point);

            if (NMathUtil.signsAreOpposites(lastSolution, currentSolution)) {
                addSolutionFromRange(roots, normalizedTerms, point - (DELTA * (isPositive ? 1 : -1)), point);
                deltasSinceLastSignificantEvent = 0;
            }

            if (lastSolution > 0 && lastSolution < 2
                    && Math.abs(lastDifference) < DELTA / 10) { // slope: m < 1/10
                deltasSinceLastSignificantEvent++;
            } else {
                deltasSinceLastSignificantEvent = 0;
            }
            lastDifference = lastSolution - currentSolution;
            lastSolution = currentSolution;
        }
    }

    // confirmed that there's one and exactly one solution within range
    private static void addSolutionFromRange(List<Root> roots,
                                             List<Term> normalizedTerms,
                                             double left, double right) {
        double leftValue = Polynomial.evaluateAtPoint(left, normalizedTerms);
        double midPoint = (left + right) / 2;
        double value = Polynomial.evaluateAtPoint(midPoint, normalizedTerms);

        while (!NMathUtil.approximatelyEqualToZero(value)) {
            if ((0 < value && value < leftValue) || (0 > value && value > leftValue)) {
                left = midPoint;
                leftValue = value;
            } else {
                right = midPoint;
            }
            midPoint = (left + right) / 2;
            value = Polynomial.evaluateAtPoint(midPoint, normalizedTerms);
        }

        roots.add(new Root(midPoint));
    }

}
