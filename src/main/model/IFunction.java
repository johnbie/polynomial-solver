package model;

public interface IFunction {
    // MODIFIES: this
    // EFFECTS: Sets IFunction to its default (i.e. zero)
    void reset();

    // EFFECTS: Evaluates IFunction at point x and return result
    double evaluateAtPoint(double point);
}
