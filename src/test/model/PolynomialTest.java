package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PolynomialTest {
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
}