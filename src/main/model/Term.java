package model;

import java.util.Locale;

/*
 * Represents the term of a polynomial object
 */
public class Term {
    private int numerator;
    private int denominator;
    private int degree;

    // Constructs a default term
    // EFFECTS: Constructs a term equal to the constant 0
    public Term() {
        this.numerator = 0;
        this.denominator = 1;
        this.degree = 0;
    }

    // Constructs a term from string input
    // REQUIRES: string input that's consistent with toString method result
    // EFFECTS: Constructs a term equal to the constant 0
    public Term(String termStr) {
        // default values
        denominator = 1;

        // get degree if exists
        if (termStr.contains("^")) {
            degree = Integer.parseInt(termStr.split("\\^")[1]);
        } else if (termStr.contains("x")) {
            degree = 1;
        } else {
            degree = 0;
        }

        // get coefficients
        if (termStr.indexOf("x") > 0) {
            termStr = termStr.split("x")[0].trim();
        } else if (termStr.indexOf("x") == 0) {
            numerator = 1;
            return;
        }

        if (termStr.contains("/")) {
            this.numerator = Integer.parseInt(termStr.split("/")[0]);
            this.denominator = Integer.parseInt(termStr.split("/")[1]);
        } else if (termStr.split("-").length == 0) {
            this.numerator = -1;
        } else if (!termStr.equals("")) {
            this.numerator = Integer.parseInt(termStr);
        }
    }

    // Constructs a term
    // REQUIRES: denominator != 0; degree >= 0
    // EFFECTS: Constructs a term
    public Term(int numerator, int denominator, int degree) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.degree = degree;
        simplify();
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public int getDegree() {
        return degree;
    }

    // Returns whether the term is a zero (i.e. 0, 0x, 0x^2, etc)
    // EFFECTS: Returns whether the numerator is zero
    public boolean isZero() {
        return numerator == 0;
    }

    // Evaluates the polynomial at point x
    // EFFECTS: Returns the function result
    public double evaluateAtPoint(double point) {
        return Math.pow(point, this.degree) * this.numerator / this.denominator;
    }

    // Combines two term and return true if their degrees are the same. Otherwise return false.
    // MODIFIES: this
    // REQUIRES: term.degree == this.degree (soft requirement)
    // EFFECTS: Combine two like terms
    public boolean combineTerm(Term term) {
        if (this.degree == term.degree) {
            if (this.denominator == term.denominator) {
                this.numerator += term.numerator;
            } else {
                this.numerator *= term.denominator;
                this.numerator += term.numerator * this.denominator;
                this.denominator *= term.denominator;
                simplify();
            }
            return true;
        } else {
            return false;
        }
    }

    // Gets the derivative of the term
    // EFFECTS: returns a derivative of the term
    public Term getDerivative() {
        if (this.degree > 0) {
            return new Term(this.numerator * this.degree, this.denominator, this.degree - 1);
        } else {
            return new Term(); // 0
        }
    }

    // Overriding toString() method of String class
    // EFFECTS: Returns the string of the coefficient
    @Override
    public String toString() {
        String rational = numerator + "";
        if (denominator > 1) {
            rational += "/" + denominator;
        } else if (numerator == -1 && degree > 0) {
            rational = "-";
        } else if (numerator == 1 && degree > 0) {
            rational = "";
        }

        if (degree == 0) {
            return rational;
        } else {
            String variable = "x";
            if (degree > 1) {
                variable += "^" + degree;
            }
            return rational + variable;
        }
    }

    // Simplifies the coefficient such that the denominator is positive
    // and that the numerator and denominator are relatively prime
    // MODIFIES: this
    // EFFECTS: Simplifies the coefficient
    protected void simplify() {
        // normalize so that denominator is positive
        if (this.denominator < 0) {
            this.denominator *= -1;
            this.numerator *= -1;
        }

        int gcd = getLowestCommonDivisor(Math.abs(numerator), denominator);
        if (gcd > 1) {
            this.numerator /= gcd;
            this.denominator /= gcd;
        }
    }

    // Returns the lowest common divisor of two numbers.
    // Code is based on a subtraction-based implementation of the Euclid's Algorithm (https://en.wikipedia.org/wiki/Euclidean_algorithm)
    // REQUIRES: two positive integers (a >= 0; b >= 0)
    // EFFECTS: Simplifies the coefficient
    private int getLowestCommonDivisor(int a, int b) {
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
