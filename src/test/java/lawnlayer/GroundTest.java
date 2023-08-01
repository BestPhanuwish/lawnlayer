package lawnlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;

public class GroundTest{
    
    private Ground empty;
    private Ground dirt;
    private Ground greenPath;
    private Ground redPath;
    private Ground grass;
    private Ground concrete;

    @BeforeEach
    public void setUp() {
        // set up app so it's not crash while testing
        PApplet app = new PApplet();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);

        empty = new Ground();
        dirt = new Ground(20, 20, GroundType.Dirt);
        greenPath = new Ground(40, 40, GroundType.GreenPath, app.loadImage("src/main/resources/lawnlayer/greenPath.png"));
        redPath = new Ground(60, 60, GroundType.RedPath, app.loadImage("src/main/resources/lawnlayer/redPath.png"));
        grass = new Ground(80, 80, GroundType.Grass, app.loadImage("src/main/resources/lawnlayer/grass.png"));
        concrete = new Ground(100, 100, GroundType.Concrete, app.loadImage("src/main/resources/lawnlayer/concrete_tile.png"));
    }

    @Test
    @DisplayName("Existence check")
    public void simpleGround() {
        assertNotNull(empty);
        assertNotNull(dirt);
        assertNotNull(greenPath);
        assertNotNull(redPath);
        assertNotNull(grass);
        assertNotNull(concrete);
        
        // check default x and y value when it's empty
        assertTrue(empty.isSamePoint(new Point(0, 0)));
    }

    @Test
    @DisplayName("getter method for GroundType")
    public void getterGroundType() {
        assertEquals(GroundType.Dirt, dirt.getGroundType());
        assertEquals(GroundType.GreenPath, greenPath.getGroundType());
        assertEquals(GroundType.RedPath, redPath.getGroundType());
        assertEquals(GroundType.Grass, grass.getGroundType());
        assertEquals(GroundType.Concrete, concrete.getGroundType());
    }

    @Test
    @DisplayName("setter method for GroundType")
    public void setterGroundType() {
        dirt.setGroundType(GroundType.Grass);
        assertEquals(GroundType.Grass, dirt.getGroundType());
    }

    @Test
    @DisplayName("isHittableGround method simple check")
    public void isHittable() {
        assertFalse(dirt.isHittableGround());
        assertTrue(greenPath.isHittableGround());
        assertTrue(redPath.isHittableGround());
        assertTrue(grass.isHittableGround());
        assertTrue(concrete.isHittableGround());
    }

}
