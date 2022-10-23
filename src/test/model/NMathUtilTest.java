package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NMathUtilTest {
    @Test
    public void testGetFactors() {
        assertEquals("[1]", NMathUtil.getFactors(1).toString());
        assertEquals("[1, 2]", NMathUtil.getFactors(2).toString());
        assertEquals("[1, 3]", NMathUtil.getFactors(3).toString());
        assertEquals("[1, 2, 4]", NMathUtil.getFactors(4).toString());
    }

    @Test
    public void testGetLargestFactorableSquare() {
        assertEquals(1, NMathUtil.getLargestFactorableSquare(0));
        assertEquals(1, NMathUtil.getLargestFactorableSquare(1));
        assertEquals(2, NMathUtil.getLargestFactorableSquare(4));
        assertEquals(4, NMathUtil.getLargestFactorableSquare(32));
        assertEquals(10, NMathUtil.getLargestFactorableSquare(200));
    }

    @Test
    public void testGetGreatestCommonDivisorForZero() {
        int gcd1 = NMathUtil.getGCD(1,0);
        assertEquals(1, gcd1);

        int gcd2 = NMathUtil.getGCD(0,1);
        assertEquals(1, gcd2);

        int gcd3 = NMathUtil.getGCD(0,0);
        assertEquals(1, gcd3);
    }

    @Test
    public void testGetGreatestCommonDivisorForRelativePrime() {
        int gcd = NMathUtil.getGCD(7,13);
        assertEquals(1, gcd);
    }

    @Test
    public void testGetGreatestCommonDivisorForFactors() {
        int gcd = NMathUtil.getGCD(12,24);
        assertEquals(12, gcd);
    }

    @Test
    public void testGetGetLowestCommonMultipleForRelativePrime() {
        int gcd = NMathUtil.getLCM(7,13);
        assertEquals(7 * 13, gcd);
    }

    @Test
    public void testGetGetLowestCommonMultipleForForFactors() {
        int gcd = NMathUtil.getLCM(12,24);
        assertEquals(24, gcd);
    }

    // only needed for coverage; approved by TA
    @Test
    public void testStaticConstructor() {
        NMathUtil nMathUtil = new NMathUtil();
        assertNotNull(nMathUtil);
    }
}