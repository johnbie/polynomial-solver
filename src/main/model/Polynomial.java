package model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/*
 * Represents the polynomial object and ways to operate on it
 */
public class Polynomial {
    private final List<Term> orderedTerms;

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
                String[] termStrs = polynomialStr.split(" \\+ ");
                for (String termStr : termStrs) {
                    Term term = new Term(termStr);
                    addTerm(term);
                }
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

    public double getYIntercept() {
        return evaluateAtPoint(0);
    }

    public double[] getXIntercepts() {
        return null;
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
}
