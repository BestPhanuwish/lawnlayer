package lawnlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class PointTest {
    
    @Test
    @DisplayName("Simple point set up")
    public void simplePoint() {
        Point p = new Point(0, 0);
        assertNotNull(p);
        assertEquals(0, p.x);
        assertEquals(0, p.y);
    }

    @Test
    @DisplayName("Simple getter method")
    public void simpleGetter() {
        Point p = new Point(1000, 280);
        assertEquals(1000, p.getX());
        assertEquals(280, p.getY());
    }

    @RepeatedTest(5)
    @DisplayName("Simple getter method repeat test to make it's always working fine")
    public void repeatGetter() {
        Point p = new Point(-54, 668);
        assertEquals(-54, p.getX());
        assertEquals(668, p.getY());
    }

    @Test
    @DisplayName("Simple setter method")
    public void simpleSetter() {
        Point p = new Point(500, 500);
        assertEquals(500, p.getX());
        assertEquals(500, p.getY());

        // set x and y seperate
        p.setX(550);
        p.setY(450);
        assertEquals(550, p.getX());
        assertEquals(450, p.getY());
    }

    @Test
    @DisplayName("Setter method on both X and Y together")
    public void bothSetter() {
        Point p = new Point(300, -300);
        assertEquals(300, p.getX());
        assertEquals(-300, p.getY());

        // set x and y together
        p.setPoint(100, -100);
        assertEquals(100, p.getX());
        assertEquals(-100, p.getY());
    }

    @Test
    @DisplayName("isSamePoint method check if the point is on the same coordinate as another point")
    public void equalPoint() {
        Point p1 = new Point(15, 30);
        Point p2 = new Point(45, 60);
        Point p3 = new Point(15, 30);

        assertFalse(p1.isSamePoint(p2));
        assertFalse(p2.isSamePoint(p3));
        assertTrue(p3.isSamePoint(p1));
        assertTrue(p1.isSamePoint(p3));
    }

    @Test
    @DisplayName("isSamePoint edge case with same X or Y but not both")
    public void equalEdgePoint() {
        Point p1 = new Point(15, 30);
        Point p2 = new Point(45, 30);
        Point p3 = new Point(15, 60);

        assertFalse(p1.isSamePoint(p2));
        assertFalse(p1.isSamePoint(p3));
    }

    @Test
    @DisplayName("Set point to another point")
    public void pointSetter() {
        Point p1 = new Point(15, 30);
        Point p2 = new Point(45, 60);

        // check the first point
        assertEquals(15, p1.getX());
        assertEquals(30, p1.getY());

        // check the second point
        assertEquals(45, p2.getX());
        assertEquals(60, p2.getY());

        // set p1 to be the same position as p2 and check if p1 will equal p2
        p1.setPoint(p2);
        assertEquals(45, p1.getX());
        assertEquals(60, p1.getY());

        assertEquals(p2.x, p1.x, "Edge case check on both point position if it's equal on X coordinate");
        assertEquals(p2.y, p1.y, "Edge case check on both point position if it's equal on Y coordinate");
    }

    @Test
    @DisplayName("Check if it's same point by, Set point to another point")
    public void equalPointSetter() {
        Point p1 = new Point(15, 30);
        Point p2 = new Point(45, 60);

        // check the first point
        assertEquals(15, p1.getX());
        assertEquals(30, p1.getY());

        // check the second point
        assertEquals(45, p2.getX());
        assertEquals(60, p2.getY());

        // set p1 to be the same position as p2 and check if p1 will equal p2
        p1.setPoint(p2);
        assertTrue(p1.isSamePoint(p2));
        assertTrue(p2.isSamePoint(p1));
    }

}
