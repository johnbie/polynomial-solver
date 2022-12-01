package model;

/*
 * The interface for defining a class that has a default value (i.e. 0, sin(x), e^x)
 * and can be evaluated at point f(a)
 */
public interface IFunction {
    // MODIFIES: this
    // EFFECTS: Sets IFunction to its default (i.e. zero)
    void reset();

    // EFFECTS: Evaluates IFunction at point x and return result
    double evaluateAtPoint(double point);
}
