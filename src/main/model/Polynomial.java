package model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/*
 * Represents the polynomial object and ways to operate on it
 */
public class Polynomial implements IDerivable {
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
        while (!Objects.equals(polynomialStr, "")) {
            if (polynomialStr.contains(" + ") || polynomialStr.contains(" - ")) {
                String nextTermString = polynomialStr.split(" [+-] ")[0];
                Term term = new Term(nextTermString);
                addTermToList(term);
                polynomialStr = polynomialStr.replace(nextTermString, "");

                String operator = polynomialStr.substring(0,3);
                if (Objects.equals(operator, " + ")) {
                    polynomialStr = polynomialStr.substring(3);
                } else { // if (Objects.equals(operator, " - "))
                    polynomialStr = "-" + polynomialStr.substring(3);
                }
            } else {
                Term term = new Term(polynomialStr);
                addTermToList(term);
                break;
            }
        }

        EventLog.getInstance().logEvent(new Event("Created a new polynomial " + this));
    }

    // Adds a term
    // MODIFIES: this
    // REQUIRES: term is non-zero (soft requirement)
    // EFFECTS: adds a new term to polynomial
    public void addTerm(Term term) {
        if (term.isZero()) {
            return;
        }

        addTermToList(term);
        EventLog.getInstance().logEvent(new Event("Added " + term + " to polynomial (now " + this + ")"));
    }

    // Adds a term to ordered terms list
    // MODIFIES: this
    // EFFECTS: Adds a term to ordered terms list
    private void addTermToList(Term term) {
        // insert term at the correct position
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
    @Override
    public double evaluateAtPoint(double point) {
        return evaluateAtPoint(point, this.orderedTerms);
    }

    // Evaluates the polynomial at point x
    // EFFECTS: Returns the function result
    public static double evaluateAtPoint(double point, List<Term> orderedTerms) {
        double sum = 0;

        for (Term term : orderedTerms) {
            sum += term.evaluateAtPoint(point);
        }

        return sum;
    }

    // Gets the deep-copy of the terms
    // EFFECTS: gets the terms
    public List<Term> getTerms() {
        List<Term> terms = new LinkedList<>();

        // add the derivative of each term as long as they're not zero
        for (Term term : orderedTerms) {
            terms.add(term.createCopy());
        }

        return terms;
    }

    // Sets the polynomial to the zero polynomial
    // MODIFIES: this
    // EFFECTS: Sets the polynomial to the zero polynomial
    @Override
    public void reset() {
        orderedTerms.clear();

        Event log = new Event("Reset polynomial to zero polynomial");
        EventLog.getInstance().logEvent(log);
    }

    // Sets the polynomial to its derivative
    // MODIFIES: this
    // EFFECTS: Sets the polynomial to its derivative
    @Override
    public void derive() {
        List<Term> orderedDerivativeTerms = getDerivativeForTerms(this);
        orderedTerms.clear();
        orderedTerms.addAll(orderedDerivativeTerms);

        Event log = new Event("Derived polynomial (now " + this + ")");
        EventLog.getInstance().logEvent(log);
    }

    // Gets the derivative of the polynomial w/ logging
    // EFFECTS: gets the derivative
    @Override
    public Polynomial getDerivative() {
        List<Term> orderedDerivativeTerms = getDerivativeForTerms(this);
        Polynomial derivative = new Polynomial();
        derivative.orderedTerms.addAll(orderedDerivativeTerms);

        return derivative;
    }

    protected static List<Term> getDerivativeForTerms(Polynomial polynomial) {
        List<Term> orderedTerms = new LinkedList<>();

        // add the derivative of each term as long as they're not zero
        for (Term term : polynomial.orderedTerms) {
            Term termDerivative = term.getDerivative();
            if (!termDerivative.isZero()) {
                orderedTerms.add(termDerivative);
            }
        }

        return orderedTerms;
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

    // Gets the x intercepts of the function as a rational if possible
    // EFFECTS: gets the x intercepts
    public List<Root> getXIntercepts() {
        return Root.solveForPolynomial(this);
    }

    // Gets the critical points of the function as a rational if possible
    // EFFECTS: gets the critical points
    public List<Root> getCriticalPoints() {
        return Root.solveForPolynomial(getDerivative());
    }

    // Gets the inflection points of the function as a rational if possible
    // EFFECTS: gets the inflection points
    public List<Root> getInflectionPoints() {
        return Root.solveForPolynomial(getDerivative().getDerivative());
    }

    // Overriding toString() method of String class
    // EFFECTS: Returns the string of the coefficient
    @Override
    public String toString() {
        if (this.orderedTerms.size() > 0) {
            StringBuilder string = new StringBuilder(this.orderedTerms.get(this.orderedTerms.size() - 1).toString());
            for (int i = this.orderedTerms.size() - 2; i >= 0; i--) {
                Term term = this.orderedTerms.get(i);

                if (!term.isNegative()) {
                    string.append(" + ").append(term);
                } else {
                    string.append(" - ").append(term.getAbs());
                }

            }
            return string.toString();
        } else {
            return "0";
        }
    }
}