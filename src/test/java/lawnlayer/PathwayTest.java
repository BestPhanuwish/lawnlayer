package lawnlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;

public class PathwayTest {
    
    private PApplet app;
    private Pathway path;

    @BeforeEach
    public void setUp() {
        // set up app so it's not crash while testing
        app = new PApplet();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);

        path = new Pathway();
    }

    @Test
    @DisplayName("Existence Check")
    public void simplePropagate() {
        assertNotNull(path);
    }

    @Test
    @DisplayName("Getter method for path size")
    public void simpleGetterSize() {
        assertEquals(0, path.getSize());
    }

    @Test
    @DisplayName("Simeple add point to path method")
    public void simpleAddPoint() {
        path.addPoint(new Ground());
        assertEquals(1, path.getSize());
    }

    @Test
    @DisplayName("Simeple get point to path method")
    public void simpleGetPoint() {
        path.addPoint(new Ground(10, 20, GroundType.Concrete));
        assertNotNull(path.getPoint(0));
        assertEquals(10, path.getPoint(0).getX());
        assertEquals(20, path.getPoint(0).getY());
        assertTrue(path.getPoint(1).isSamePoint(new Point(0, 0)));
    }

    @Test
    @DisplayName("Simeple get the lastest point check")
    public void simpleGetLastPoint() {
        path.addPoint(new Ground());
        path.addPoint(new Ground(10, 10, GroundType.Concrete));
        path.addPoint(new Ground(20, 20, GroundType.Concrete));
        path.addPoint(new Ground(30, 30, GroundType.Concrete));
        path.addPoint(new Ground(40, 40, GroundType.Concrete));
        assertEquals(5, path.getSize());
        assertTrue(path.getLastestPoint().isSamePoint(new Point(40, 40)));
    }

    @Test
    @DisplayName("Simeple pointIsInPath method check")
    public void simplePointIsInPath() {
        path.addPoint(new Ground());
        path.addPoint(new Ground(10, 10, GroundType.Concrete));
        path.addPoint(new Ground(20, 20, GroundType.Concrete));
        path.addPoint(new Ground(30, 30, GroundType.Concrete));
        path.addPoint(new Ground(40, 40, GroundType.Concrete));
        assertTrue(path.pointIsInPath(new Point(20, 20)));
        assertFalse(path.pointIsInPath(new Point(40, 20)));
    }

    @Test
    @DisplayName("Simeple clear path method")
    public void simpleClear() {
        path.addPoint(new Ground());
        path.addPoint(new Ground(10, 10, GroundType.Concrete));
        path.addPoint(new Ground(20, 20, GroundType.Concrete));
        path.addPoint(new Ground(30, 30, GroundType.Concrete));
        path.addPoint(new Ground(40, 40, GroundType.Concrete));
        path.clear();
        assertEquals(0, path.getSize());
    }

    @Test
    @DisplayName("Simeple create and get propagate method")
    public void simpleCreatePropagate() {
        path.addPoint(new Ground());
        path.addPoint(new Ground(10, 10, GroundType.Concrete));
        path.addPoint(new Ground(20, 20, GroundType.Concrete));
        path.addPoint(new Ground(30, 30, GroundType.Concrete));
        path.addPoint(new Ground(40, 40, GroundType.Concrete));
        
        path.createPropagate(new Point(20, 20));
        assertEquals(2, path.getPropagate(0).getIndex());
    }

    @Test
    @DisplayName("Edge create and get propagate method but propagate point is not on pathway")
    public void edgeCreatePropagate() {
        path.addPoint(new Ground());
        path.addPoint(new Ground(10, 10, GroundType.Concrete));
        path.addPoint(new Ground(20, 20, GroundType.Concrete));
        path.addPoint(new Ground(30, 30, GroundType.Concrete));
        path.addPoint(new Ground(40, 40, GroundType.Concrete));
        
        path.createPropagate(new Point(20, 40));
        assertEquals(0, path.getPropagate(0).getIndex());
    }

    @Test
    @DisplayName("Edge create and get propagate method but path array is empty")
    public void emptyPathCreatePropagate() {
        path.createPropagate(new Point(20, 20));
        assertEquals(0, path.getPropagate(0).getIndex());
    }

    @Test
    @DisplayName("Clear propagate method")
    public void clearPropagate() {
        path.addPoint(new Ground());
        path.addPoint(new Ground(10, 10, GroundType.Concrete));
        path.addPoint(new Ground(20, 20, GroundType.Concrete));
        path.addPoint(new Ground(30, 30, GroundType.Concrete));
        path.addPoint(new Ground(40, 40, GroundType.Concrete));
        
        path.createPropagate(new Point(20, 20));
        path.clearPropagate();
        assertEquals(0, path.getPropagate(0).getIndex());
    }

    public void addGreenPath() {
        path.addPoint(new Ground());
        path.addPoint(new Ground(10, 10, GroundType.GreenPath));
        path.addPoint(new Ground(20, 20, GroundType.GreenPath));
        path.addPoint(new Ground(30, 30, GroundType.GreenPath));
        path.addPoint(new Ground(40, 40, GroundType.GreenPath));
    }

    @Test
    @DisplayName("Simple update method")
    public void simpleUpdate() {
        this.addGreenPath();

        PowerUp item = new PowerUp(null, new Point(0, 0), 0);
        item.setActive(false);

        assertFalse(path.update(app, item));
    }

    @Test
    @DisplayName("Update method with propagate")
    public void propagateUpdate() {
        this.addGreenPath();

        PowerUp item = new PowerUp(null, new Point(0, 0), 0);
        item.setActive(false);

        path.createPropagate(new Point(20,20));
        
        // should be false for 6 frame because it not catch up with the last green path yet
        for (int i=0; i<6; i++) {
            assertFalse(path.update(app, item));
        }

        // the last time it catch should be true
        assertTrue(path.update(app, item));
    }

    @Test
    @DisplayName("Update method but player have barrier")
    public void itemUpdate() {
        this.addGreenPath();

        PowerUp item = new PowerUp(null, new Point(0, 0), 0);

        path.createPropagate(new Point(20,20));
        
        // should be false for 6 frame because it not catch up with the last green path yet
        for (int i=0; i<6; i++) {
            path.update(app, item);
        }

        // the last time it catch should be true but if it have power up then it's false
        item.setActive(true);
        item.setPowerUpType(app, PowerType.Barrier);
        assertFalse(path.update(app, item));
    }

    @Test
    @DisplayName("Update method but player have other item")
    public void itemOtherUpdate() {
        this.addGreenPath();

        PowerUp item = new PowerUp(null, new Point(0, 0), 0);

        path.createPropagate(new Point(20,20));
        
        // should be false for 6 frame because it not catch up with the last green path yet
        for (int i=0; i<6; i++) {
            path.update(app, item);
        }

        // the last time it catch should be true but if it have power up then it's false
        item.setActive(true);
        item.setPowerUpType(app, PowerType.Speed);
        assertTrue(path.update(app, item));
    }

    @Test
    @DisplayName("Update method but propagate happen at player")
    public void edgeUpdate() {
        path.addPoint(new Ground());
        path.addPoint(new Ground(10, 10, GroundType.GreenPath));
        path.addPoint(new Ground(20, 20, GroundType.GreenPath));

        PowerUp item = new PowerUp(null, new Point(0, 0), 0);

        path.createPropagate(new Point(20,20));
        
        // player need to died immediately
        assertTrue(path.update(app, item));
    }

    @Test
    @DisplayName("Simple fill grass method test")
    public void simpleFillGrass() {
        this.addGreenPath();
        path.fillGrass(app);
        for (int i=0; i<path.getSize(); i++) {
            assertEquals(GroundType.Grass, path.getPoint(i).getGroundType());
        }
    }

    @Test
    @DisplayName("Edge fill grass method test")
    public void edgeFillGrass() {
        path.fillGrass(app);
        assertEquals(GroundType.GreenPath, path.getPoint(0).getGroundType());
    }

}
