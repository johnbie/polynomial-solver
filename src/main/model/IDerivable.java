package model;

/*
 * The interface for defining a class that can be derived.
 */
public interface IDerivable extends IFunction {
    // MODIFIES: this
    // EFFECTS: Sets IDerivable to its derivative
    void derive();

    // EFFECTS: returns derivative of IDerivable
    IDerivable getDerivative();
}
