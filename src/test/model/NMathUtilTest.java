package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

final class NMathUtilTest {
    @Test
    public void testGetFactors() {
        assertEquals("[1]", NMathUtil.getFactors(1).toString());
        assertEquals("[1, 2]", NMathUtil.getFactors(2).toString());
        assertEquals("[1, 3]", NMathUtil.getFactors(3).toString());
        assertEquals("[1, 2, 4]", NMathUtil.getFactors(4).toString());
    }

    @Test
    public void testGetGreatestCommonDivisorForRelativePrime() {
        int gcd = NMathUtil.getGreatestCommonDivisor(7,13);
        assertEquals(1, gcd);
    }

    @Test
    public void testGetGreatestCommonDivisorForFactors() {
        int gcd = NMathUtil.getGreatestCommonDivisor(12,24);
        assertEquals(12, gcd);
    }

    @Test
    public void testGetGetLowestCommonMultipleForRelativePrime() {
        int gcd = NMathUtil.getLowestCommonMultiple(7,13);
        assertEquals(7 * 13, gcd);
    }

    @Test
    public void testGetGetLowestCommonMultipleForForFactors() {
        int gcd = NMathUtil.getLowestCommonMultiple(12,24);
        assertEquals(24, gcd);
    }
}