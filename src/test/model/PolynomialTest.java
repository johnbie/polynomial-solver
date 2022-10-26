package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PolynomialTest {
    @Test
    public void testPolynomialFromString() {
        Polynomial zero1 = new Polynomial("0");
        assertEquals("0", zero1.toString());

        Polynomial zero2 = new Polynomial("");
        assertEquals("0", zero2.toString());

        Polynomial polynomial = new Polynomial("x^2 + 7/15x - 4/15");
        assertEquals("x^2 + 7/15x - 4/15", polynomial.toString());
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
    public void testGetXInterceptsZero() {
        Polynomial zero = new Polynomial();
        assertEquals("[]", zero.getXIntercepts().toString());
    }

    @Test
    public void testGetXInterceptsInteger() {
        Polynomial polynomial1 = new Polynomial("x^3 + 6x^2 + 11x + 6");
        assertEquals("[-3, -2, -1]", polynomial1.getXIntercepts().toString());

        Polynomial polynomial2 = new Polynomial("x^4 + -x^3 + x + -1");
        assertEquals("[-1, 1]", polynomial2.getXIntercepts().toString());
    }

    @Test
    public void testGetXInterceptsRational() {
        Polynomial polynomial1 = new Polynomial("x^2 + 7/15x + -4/15");
        assertEquals("[-4/5, 1/3]", polynomial1.getXIntercepts().toString());

        Polynomial polynomial2 = new Polynomial("x^3 + -11/4x^2 + -27/2x + 45/4");
        assertEquals("[-3, 3/4, 5]", polynomial2.getXIntercepts().toString());
    }

    @Test
    public void testGetXInterceptsSquareRooted() {
        Polynomial polynomial1 = new Polynomial("x^3 + 1/2x^2 + -x");
        assertEquals("[-1-sqrt(17)/4, 0, -1+sqrt(17)/4]", polynomial1.getXIntercepts().toString());

        Polynomial polynomial2 = new Polynomial("-x^2 + 2x + 1");
        assertEquals("[1-sqrt(2), 1+sqrt(2)]", polynomial2.getXIntercepts().toString());

        Polynomial polynomial3 = new Polynomial("x^3 + 1/2x^2 + -x");
        assertEquals("[-1-sqrt(17)/4, 0, -1+sqrt(17)/4]", polynomial3.getXIntercepts().toString());

        Polynomial polynomial4 = new Polynomial("x^2 + 5x - 1/2");
        assertEquals("[-5-3sqrt(3)/2, -5+3sqrt(3)/2]", polynomial4.getXIntercepts().toString());
    }

    @Test
    public void testGetXInterceptsSquareUnfoundRoots() {
        Polynomial polynomial1 = new Polynomial("-x^3 + x^2 + x + 1");
        assertEquals("[1.839287]", polynomial1.getXIntercepts().toString());

        Polynomial polynomial2 = new Polynomial("1/14x^3 + 2x^2 + 10x - 10");
        assertEquals("[-21.024318, -7.826503, 0.850821]", polynomial2.getXIntercepts().toString());

        Polynomial polynomial3 = new Polynomial("x^3 - 100000");
        assertEquals("[46.415888]", polynomial3.getXIntercepts().toString());

        Polynomial polynomial4 = new Polynomial("-x^4 + 3x + 100000");
        assertEquals("[-17.780422, 17.785166]", polynomial4.getXIntercepts().toString());

        Polynomial polynomial5 = new Polynomial("-1/10000x^4 - x^2 + 12x - 20");
        assertEquals("[2.000200, 9.879109]", polynomial5.getXIntercepts().toString());
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
        assertEquals("[0]", polynomial.getCriticalPoints().toString());
    }

    @Test
    public void testGetInflectionPoints() {
        Polynomial polynomial = new Polynomial("x^3 + x + 2");
        assertEquals("[0]", polynomial.getInflectionPoints().toString());
    }
}