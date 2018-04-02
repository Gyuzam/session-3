package ru.sbt.jschool.session3;

import org.junit.Test;
import ru.sbt.jschool.session3.problem2.Parking;

import static org.junit.Assert.assertEquals;

public class ParkingTest {
    @Test
    public void testTwoCarsParking() throws Exception {
        Parking p = new Parking(5L, 100f);
        boolean p1 = p.moveCarIn(123L, 23L);
        boolean p2 = p.moveCarIn(43L, 25L);
        assertEquals(true, p1);
        assertEquals(true, p2);
        assertEquals("2 cars should be found", 2, p.getCarsIn().size());
    }

    @Test
    public void testCapacityParking() throws Exception {
        Parking p = new Parking(3L, 100f);
        p.moveCarIn(123L, 23L);
        p.moveCarIn(43L, 25L);
        p.moveCarIn(56L, 28L);
        assertEquals("3 cars should be found", 3, p.getCarsIn().size());
        boolean p4 = p.moveCarIn(76L, 29L);
        assertEquals("Should not add another car on parking", false, p4);
    }

    @Test
    public void testInAndOut() throws Exception {
        Parking p = new Parking(3L, 100f);
        p.moveCarIn(123L, 23L);
        p.moveCarIn(43L, 25L);
        p.moveCarIn(56L, 28L);
        assertEquals("3 cars should be found", 3, p.getCarsIn().size());
        p.moveCarOut(123L, 29L);
        assertEquals("2 cars should be found", 2, p.getCarsIn().size());
    }

    @Test
    public void testOutCost() throws Exception {
        Parking p = new Parking(3L, 100f);
        p.moveCarIn(123L, 23L);
        float c = p.moveCarOut(123L, 27L);
        assertEquals("Cost in night hours", 800f, c, .01f);
        p.moveCarIn(56L, 7L);
        c = p.moveCarOut(56L, 12L);
        assertEquals("Cost in day hours", 500f, c, .01f);
        p.moveCarIn(56L, 21L);
        c = p.moveCarOut(56L, 25L);
        assertEquals("Cost in day and night hours", 600f, c, .01f);
    }

    @Test
    public void testTwoTimesIn() throws Exception {
        Parking p = new Parking(3L, 100f);
        p.moveCarIn(123L, 23L);
        boolean p1 = p.moveCarIn(123L, 24L);
        assertEquals("Car shold be already in parking", false, p1);
    }
}
