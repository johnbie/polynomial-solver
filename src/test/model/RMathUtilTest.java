package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RMathUtilTest {
    @Test
    public void testApproximatelyEqualZero() {
        assertTrue(RMathUtil.approximatelyEqualToZero(0.0000000001));
        assertTrue(RMathUtil.approximatelyEqualToZero(-0.0000000001));
    }

    @Test
    public void testSignsAreOpposite() {
        assertFalse(RMathUtil.signsAreOpposites(1, 1));
        assertTrue(RMathUtil.signsAreOpposites(1, -1));
        assertTrue(RMathUtil.signsAreOpposites(-1, 1));
        assertFalse(RMathUtil.signsAreOpposites(-1, -1));
    }

    // only needed for coverage; approved by TA
    @Test
    public void testStaticConstructor() {
        RMathUtil rMathUtil = new RMathUtil();
        assertNotNull(rMathUtil);
    }
}