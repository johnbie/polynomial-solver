package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TermTest {
    @Test
    public void testTermFromString() {
        Term zero = new Term("0");
        assertTrue(zero.isZero());

        Term x = new Term("x");
        assertEquals(1, x.getNumerator());
        assertEquals(1, x.getDenominator());
        assertEquals(1, x.getDegree());

        Term twoXSquared = new Term("2x^2");
        assertEquals(2, twoXSquared.getNumerator());
        assertEquals(1, twoXSquared.getDenominator());
        assertEquals(2, twoXSquared.getDegree());

        Term rationalTerm = new Term("1/4x^5");
        assertEquals(1, rationalTerm.getNumerator());
        assertEquals(4, rationalTerm.getDenominator());
        assertEquals(5, rationalTerm.getDegree());

        Term negativeTerm = new Term("-x^10");
        assertEquals(-1, negativeTerm.getNumerator());
        assertEquals(1, negativeTerm.getDenominator());
        assertEquals(10, negativeTerm.getDegree());
    }

    @Test
    public void testIsZeroTrue() {
        Term term = new Term();
        assertTrue(term.isZero());
    }

    @Test
    public void testIsZeroFalse() {
        Term term = new Term(1,1,0);
        assertFalse(term.isZero());
    }

    @Test
    public void testEvaluateAtPoint() {
        for(int i = 0; i < 20; i++) {
            Term term = new Term(1,2, i);

            for (int j = 0; j < 10; j++) {
                double expectedPoint = Math.pow(j, i) / 2;
                double point = term.evaluateAtPoint(j);
                assertEquals(expectedPoint, point);
            }
        }
    }

    @Test
    public void testCombineTermsBase() {
        Term term1 = new Term(1, 1, 1);
        Term term2 = new Term(1, 1, 1);
        term1.combineTerm(term2);
        assertEquals(2, term1.getNumerator());
        assertEquals(1, term1.getDenominator());
        assertEquals(1, term1.getDegree());
    }

    @Test
    public void testCombineTermsFalse() {
        Term term1 = new Term(1, 1, 1);
        Term term2 = new Term(1, 1, 2);

        assertFalse(term1.combineTerm(term2));
        assertEquals(1, term1.getNumerator());
        assertEquals(1, term1.getDenominator());
        assertEquals(1, term1.getDegree());
    }

    @Test
    public void testCombineTermsRational() {
        Term term1 = new Term(1, 2, 1);
        Term term2 = new Term(1, 3, 1);
        term1.combineTerm(term2);
        assertEquals(5, term1.getNumerator());
        assertEquals(6, term1.getDenominator());
        assertEquals(1, term1.getDegree());
    }

    @Test
    public void testCombineTermsCanceling() {
        Term term1 = new Term(1, 1, 1);
        Term term2 = new Term(-1, 1, 1);
        term1.combineTerm(term2);
        assertTrue(term1.isZero());
        assertEquals(0, term1.getNumerator());
        assertEquals(1, term1.getDenominator());
        assertEquals(1, term1.getDegree());
    }

    @Test
    public void testGetDerivativeConstant() {
        Term term = new Term(2, 3, 0);
        Term derivative = term.getDerivative();
        assertTrue(derivative.isZero());
        assertEquals(0, derivative.getNumerator());
        assertEquals(1, derivative.getDenominator());
        assertEquals(0, derivative.getDegree());
    }

    @Test
    public void testGetDerivativeBase() {
        Term term = new Term(1, 1, 1);
        Term derivative = term.getDerivative();
        assertEquals(1, derivative.getNumerator());
        assertEquals(1, derivative.getDenominator());
        assertEquals(0, derivative.getDegree());
    }

    @Test
    public void testGetDerivativeHigherDegree() {
        Term term = new Term(1, 1, 10);
        Term derivative = term.getDerivative();
        assertEquals(10, derivative.getNumerator());
        assertEquals(1, derivative.getDenominator());
        assertEquals(9, derivative.getDegree());
    }

    @Test
    public void testGetDerivativeSimplified() {
        Term term = new Term(2, 3, 3);
        Term derivative = term.getDerivative();
        assertEquals(2, derivative.getNumerator());
        assertEquals(1, derivative.getDenominator());
        assertEquals(2, derivative.getDegree());
    }

    @Test
    public void testToString() {
        Term zero = new Term("0");
        assertEquals("0", zero.toString());

        Term x = new Term("x");
        assertEquals("x", x.toString());

        Term twoXSquared = new Term("2x^2");
        assertEquals("2x^2", twoXSquared.toString());

        Term rationalTerm = new Term("1/4x^5");
        assertEquals("1/4x^5", rationalTerm.toString());

        Term negativeTerm = new Term("-x^10");
        assertEquals("-x^10", negativeTerm.toString());
    }
}