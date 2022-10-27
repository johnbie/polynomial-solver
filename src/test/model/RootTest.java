package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RootTest {
    @Test
    public void testRootAtZeroes() {
        Root zero1 = new Root();
        assertEquals(0, zero1.getValue());
        assertEquals("0", zero1.getDisplayText());

        Root zero2 = new Root(0, 1);
        assertEquals(0, zero2.getValue());
        assertEquals("0", zero2.getDisplayText());

        Root zero3 = new Root(0, 2);
        assertEquals(0, zero3.getValue());
        assertEquals("0", zero3.getDisplayText());

        Root zero4 = new Root(0, 2, 0, true);
        assertEquals(0, zero4.getValue());
        assertEquals("0", zero4.getDisplayText());
    }

    @Test
    public void testRootAtPositives() {
        Root negative1 = new Root(2,4);
        assertEquals(0.5, negative1.getValue());
        assertEquals("1/2", negative1.getDisplayText());

        Root negative2 = new Root(2, 4, 10, true);
        assertEquals(1.290569415042095, negative2.getValue());
        assertEquals("2+sqrt(10)/4", negative2.getDisplayText());
    }

    @Test
    public void testRootAtNegatives() {
        Root negative1 = new Root(-2,4);
        assertEquals(-0.5, negative1.getValue());
        assertEquals("-1/2", negative1.getDisplayText());

        Root negative2 = new Root(2,-4);
        assertEquals(negative1.getValue(), negative2.getValue());
        assertEquals(negative1.getDisplayText(), negative2.getDisplayText());

        Root negative3 = new Root(2, -4, 10, true);
        assertEquals(0.2905694150420949, negative3.getValue());
        assertEquals("-2+sqrt(10)/4", negative3.getDisplayText());
    }

    @Test
    public void testQuadraticRoots() {
        Root root1 = new Root(1, 1, 0, true);
        assertEquals(1, root1.getValue());
        assertEquals("1", root1.getDisplayText());

        Root root2 = new Root(0, 1, 2, true);
        assertEquals(1.4142135623730951, root2.getValue());
        assertEquals("sqrt(2)", root2.getDisplayText());
    }
}