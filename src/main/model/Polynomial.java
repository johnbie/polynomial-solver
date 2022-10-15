package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/*
 * Represents the polynomial object and ways to operate on it
 */
public class Polynomial {
    private List<Term> orderedTerms;
    private static final double EPSILON = 0.0000000001;

    // Constructs a polynomial
    // EFFECTS: Constructs a polynomial with an empty linked list named `orderedTerms`
    public Polynomial() {
        orderedTerms = new LinkedList<>();
    }

    // Constructs a term from string input
    // REQUIRES: string input that's consistent with toString method result
    // EFFECTS: Constructs a term equal to the constant 0
    public Polynomial(String polynomialStr) {
        orderedTerms = new LinkedList<>();
        if (!Objects.equals(polynomialStr, "")) {
            if (polynomialStr.contains(" + ")) {
                String[] termStrings = polynomialStr.split(" \\+ ");
                for (String termStr : termStrings) {
                    Term term = new Term(termStr);
                    addTerm(term);
                }
            } else {
                Term term = new Term(polynomialStr);
                addTerm(term);
            }
        }
    }

    // Adds a term
    // MODIFIES: this
    // REQUIRES: term is non-zero (soft requirement)
    // EFFECTS: adds a new term to polynomial
    public void addTerm(Term term) {
        if (term.isZero()) {
            return;
        }

        // insert term at the correct position (i.e. constants are first, then
        for (int i = 0; i < orderedTerms.size(); i++) {
            int degree = orderedTerms.get(i).getDegree();
            if (term.getDegree() < degree) {
                orderedTerms.add(i, term);
                return;
            } else if (term.getDegree() == degree) {
                orderedTerms.get(i).combineTerm(term);

                // check and remove if sum is zero
                if (orderedTerms.get(i).isZero()) {
                    orderedTerms.remove(i);
                }
                return;
            }
        }

        // if still didn't add, add to the "end"
        orderedTerms.add(term);
    }

    // Evaluates the polynomial at point x
    // EFFECTS: Returns the function result
    public double evaluateAtPoint(double point) {
        double sum = 0;

        for (Term term : orderedTerms) {
            sum += term.evaluateAtPoint(point);
        }

        return sum;
    }

    // Gets the derivative of the polynomial
    // EFFECTS: gets the derivative
    public Polynomial getDerivative() {
        Polynomial polynomial = new Polynomial();

        // add the derivative of each term as long as they're not zero
        for (Term term : orderedTerms) {
            Term termDerivative = term.getDerivative();
            if (!termDerivative.isZero()) {
                polynomial.orderedTerms.add(termDerivative);
            }
        }

        return polynomial;
    }

    // Gets the y intercept of the function as a rational
    // EFFECTS: gets the y intercept
    public String getYIntercept() {
        if (orderedTerms.size() == 0 || orderedTerms.get(0).getDegree() > 0) {
            return "0";
        } else {
            return orderedTerms.get(0).toString(); // hacky but works
        }
    }

    // Gets the y intercepts of the function as a rational if possible
    // EFFECTS: gets the x intercepts
    public String getXIntercepts() {
        return getXIntercepts(createCopy());
    }

    // Gets the y intercepts of the function in parameter as a rational if possible
    // EFFECTS: gets the x intercepts
    protected String getXIntercepts(Polynomial clone) {
        int size = clone.orderedTerms.size();
        if (size == 0) {
            return "All real numbers";
        }

        List<String> coefficients = new ArrayList<>();

        // purpose: factor out x=0 from polynomial until constant exists
        // method: get the degree of last term; this is the number of x=0
        int numberOfZeroIntercepts = clone.orderedTerms.get(0).getDegree();
        if (numberOfZeroIntercepts > 0) {
            coefficients.add("0");
        }

        // no more coefficients because the remaining "factor" is a constant
        if (size == 1) {
            return coefficients.toString();
        }

        normalize(clone, getLCM(clone), numberOfZeroIntercepts);
        runRationalRootTheorem(coefficients, clone);

        // check and solve for quadratic
        checkSolveQuadratic(coefficients, clone);
        // couldn't solve for arbitrary values

        // return all the coefficients as a list
        return coefficients.toString();
    }

    // Gets the critical points of the function as a rational if possible
    // EFFECTS: gets the critical points
    public String getCriticalPoints() {
        return getXIntercepts(getDerivative());
    }

    // Gets the inflection points of the function as a rational if possible
    // EFFECTS: gets the inflection points
    public String getInflectionPoints() {
        return getXIntercepts(getDerivative().getDerivative());
    }

    // creates a copy of the polynomial
    // EFFECTS: Returns the polynomial copy
    public Polynomial createCopy() {
        Polynomial polynomial = new Polynomial();

        for (Term term : orderedTerms) {
            polynomial.orderedTerms.add(term.createCopy());
        }

        return polynomial;
    }

    // Overriding toString() method of String class
    // EFFECTS: Returns the string of the coefficient
    @Override
    public String toString() {
        if (this.orderedTerms.size() > 0) {
            StringBuilder string = new StringBuilder(this.orderedTerms.get(this.orderedTerms.size() - 1).toString());
            for (int i = this.orderedTerms.size() - 2; i >= 0; i--) {
                string.append(" + ").append(this.orderedTerms.get(i).toString());
            }
            return string.toString();
        } else {
            return "0";
        }
    }

    // Gets the lowest common multiple of the denominator
    // EFFECTS: gets the lcm
    private static int getLCM(Polynomial polynomial) {
        // get LCM of the denominators
        int lcm = 1;
        for (Term term : polynomial.orderedTerms) {
            int denominator = term.getDenominator();

            if (denominator > 1) {
                lcm = NMathUtil.getLowestCommonMultiple(lcm,denominator);
            }
        }

        return lcm;
    }

    // Normalizes the polynomial such that the x's are factored out
    // and the coefficients are integers (multiply all by lcm)
    // MODIFIES: polynomial
    // EFFECTS: Normalizes the polynomial
    private static void normalize(Polynomial polynomial, int lcm, int numberOfZeroIntercepts) {
        // subtract the degree, increase numerator to integer-normalized value, and set denominator to 1
        for (Term term : polynomial.orderedTerms) {
            term.setDegree(term.getDegree() - numberOfZeroIntercepts);
            term.setNumerator(term.getNumerator() * lcm);
            term.setDenominator(1); // for coverage; redundant
        }
    }

    // check for and add rational coefficients based on the Rational Root Theorem
    // also, factors out the rational components found in the polynomial
    // MODIFIES: coefficients, polynomial
    // EFFECTS: check for and add rational coefficients
    private static void runRationalRootTheorem(List<String> coefficients, Polynomial normalizedPoly) {
        // get factors for leading coefficient and constant (both now an integer)
        int lastPos = normalizedPoly.orderedTerms.size() - 1;
        int leadingCoefficient = normalizedPoly.orderedTerms.get(lastPos).getNumerator();
        List<Integer> leadingCoefficientFactors = NMathUtil.getFactors(Math.abs(leadingCoefficient));
        int constant = normalizedPoly.orderedTerms.get(0).getNumerator();
        List<Integer> constantFactors = NMathUtil.getFactors(Math.abs(constant));

        // use epsilon and absolute value to account for rounding error
        for (Integer constantFactor : constantFactors) {
            for (Integer coefficientFactor : leadingCoefficientFactors) {
                double pointValPositive = normalizedPoly.evaluateAtPoint((double)(constantFactor) / coefficientFactor);
                if (Math.abs(pointValPositive) < EPSILON) {
                    coefficients.add(constantFactor + (coefficientFactor == 1 ? "" : "/" + coefficientFactor));
                    factorOut(constantFactor, coefficientFactor, normalizedPoly);
                }
                double pointValNegative = normalizedPoly.evaluateAtPoint((double)(-constantFactor) / coefficientFactor);
                if (Math.abs(pointValNegative) < EPSILON) {
                    coefficients.add("-" + constantFactor + (coefficientFactor == 1 ? "" : "/" + coefficientFactor));
                    factorOut(-constantFactor, coefficientFactor, normalizedPoly);
                }
            }
        }
    }

    // factors out rational solution from polynomial
    // MODIFIES: polynomial
    // EFFECTS: factors out rational solution from polynomial
    private static void factorOut(int n, int d, Polynomial normalizedPoly) {
        int size = normalizedPoly.orderedTerms.size();
        if (size <= 1) {
            return;
        }

        List<Term> newTerms = new LinkedList<>();
        Term nextTerm = normalizedPoly.orderedTerms.get(size - 1);
        int newDegree = nextTerm.getDegree() - 1;
        int remainder = nextTerm.getNumerator() / d;

        // look at terms with in reverse order (highest degree first)
        for (int i = size - 2; i >= 0; i--) {
            nextTerm = normalizedPoly.orderedTerms.get(i);
            int nextDegree = nextTerm.getDegree();

            while (newDegree >= nextDegree && remainder != 0) {
                newTerms.add(0, new Term(remainder, 1, newDegree));
                if (newDegree == nextDegree) {
                    remainder = (nextTerm.getNumerator() + (remainder * n)) / d;
                } else {
                    remainder = (remainder * n) / d;
                }
                newDegree--;
            }
        }
        normalizedPoly.orderedTerms = newTerms;
    }

    // check for and solves quadratic function
    // at this point, assume no rationals exists (and has such no linears)
    // MODIFIES: coefficients
    // EFFECTS: check for and solves quadratic/linear function
    private static void checkSolveQuadratic(List<String> coefficients, Polynomial polynomial) {
        int a = 0;
        int b = 0;
        int c = 0;

        for (Term term : polynomial.orderedTerms) {
            if (term.getDegree() == 0) {
                c = term.getNumerator();
            } else if (term.getDegree() == 1) {
                b = term.getNumerator();
            } else if (term.getDegree() == 2) {
                a = term.getNumerator();
            }
        }
        checkSolveQuadratic(coefficients, a, b, c);
    }

    // check for and solves quadratic function
    // at this point, assume no rationals exists (and has such no linears)
    // MODIFIES: coefficients
    // EFFECTS: check for and solves quadratic/linear functions
    private static void checkSolveQuadratic(List<String> coefficients, int a, int b, int c) {
        if (a != 0) {
            if (a < 0) {
                a *= -1;
                // b double-negative = original
            } else {
                b *= -1; // b needs to be negative
            }
            int rootedPart = (b * b) - (4 * a * c);
            if (rootedPart > 0) {
                coefficients.add(b + "+sqrt(" + rootedPart + ")/" + (2 * a));
                coefficients.add(b + "-sqrt(" + rootedPart + ")/" + (2 * a));
            }
        }
    }
}
