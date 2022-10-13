package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PolynomialTest {
    @Test
    public void testPolynomialFromString() {
        Polynomial zero1 = new Polynomial("0");
        assertEquals("0", zero1.toString());

        Polynomial zero2 = new Polynomial("");
        assertEquals("0", zero2.toString());

        Polynomial polynomial = new Polynomial("x^2 + 7/15x + -4/15");
        assertEquals("x^2 + 7/15x + -4/15", polynomial.toString());
    }
    @Test
    public void testAddTerm() {
        Polynomial polynomial = new Polynomial();
        polynomial.addTerm(new Term(1,1,1));
        assertEquals("x", polynomial.toString());
    }

    @Test
    public void testAddTermToExistingAtGreaterDegree() {
        Polynomial polynomial = new Polynomial();
        polynomial.addTerm(new Term(2,1,2));
        polynomial.addTerm(new Term(1,1,1));
        assertEquals("2x^2 + x", polynomial.toString());
    }

    @Test
    public void testAddTermToExistingAtLesserDegree() {
        Polynomial polynomial = new Polynomial();
        polynomial.addTerm(new Term(1,1,1));
        polynomial.addTerm(new Term(2,1,2));
        assertEquals("2x^2 + x", polynomial.toString());
    }

    @Test
    public void testAddTermToExistingAtSameDegree() {
        Polynomial polynomial = new Polynomial();
        polynomial.addTerm(new Term(1,1,1));
        polynomial.addTerm(new Term(2,1,1));
        assertEquals("3x", polynomial.toString());
    }

    @Test
    public void testAddTermToExistingCanceling() {
        Polynomial polynomial = new Polynomial();
        polynomial.addTerm(new Term(1,1,1));
        polynomial.addTerm(new Term(-1,1,1));
        assertEquals("0", polynomial.toString());
    }

    @Test
    public void testEvaluateAtPoint() {
        Polynomial polynomial = new Polynomial();
        polynomial.addTerm(new Term(1,1,0));
        polynomial.addTerm(new Term(1,2,3));

        for (int i = 0; i < 10; i++) {
            double expectedPoint = 1 + (Math.pow(i, 3) / 2);
            double point = polynomial.evaluateAtPoint(i);
            assertEquals(expectedPoint, point);
        }
    }

    @Test
    public void testGetDerivative() {
        Polynomial polynomial = new Polynomial();
        polynomial.addTerm(new Term(1, 1, 1));
        polynomial.addTerm(new Term(1, 1, 10));
        Polynomial derivative = polynomial.getDerivative();
        assertEquals("10x^9 + 1", derivative.toString());
    }

    @Test
    public void testGetXIntercepts() {
        Polynomial zero = new Polynomial();
        assertEquals("All real numbers", zero.getXIntercepts());

        Polynomial polynomial1 = new Polynomial("x^3 + 6x^2 + 11x + 6");
        assertEquals("[-1, -2, -3]", polynomial1.getXIntercepts());

        Polynomial polynomial2 = new Polynomial("x^2 + 7/15x + -4/15");
        assertEquals("[1/3, -4/5]", polynomial2.getXIntercepts());
    }

    @Test
    public void testGetYIntercept() {
        Polynomial zero = new Polynomial();
        assertEquals("0", zero.getYIntercept());

        Polynomial polynomial1 = new Polynomial("10");
        assertEquals("10", polynomial1.getYIntercept());

        Polynomial polynomial2 = new Polynomial("x");
        assertEquals("0", polynomial2.getYIntercept());
    }

    @Test
    public void testGetCriticalPoints() {
        Polynomial polynomial = new Polynomial("x^3 + 2");
        assertEquals("[0]", polynomial.getCriticalPoints());
    }

    @Test
    public void testGetInflectionPoints() {
        Polynomial polynomial = new Polynomial("x^3 + x + 2");
        assertEquals("[0]", polynomial.getInflectionPoints());
    }
}