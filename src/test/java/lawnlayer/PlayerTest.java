package lawnlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;

public class PlayerTest {
    
    private PApplet app;
    private Player player;

    @BeforeEach
    public void setUp() {
        // set up app so it's not crash while testing
        app = new PApplet();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);

        player = new Player(null, 0, 20);
    }

    @Test
    @DisplayName("Simple Player")
    public void simplePlayer() {
        assertNotNull(player);
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
    }

    @Test
    @DisplayName("Move right and then move for 9 frame")
    public void simpleMove() {
        PowerUp item = new PowerUp(null, new Point(100, 0), 10);
        player.pressedRight(item, 200);
        for (int i=0; i<9; i++) {
            player.move(item);
        }
        assertEquals(20, player.getX());
    }

    @Test
    @DisplayName("Move right and then move for 9 frame with Speed item")
    public void simpleSpeedMove() {
        PowerUp item = new PowerUp(null, new Point(100, 0), 10);
        item.setActive(true);
        item.setPowerUpType(app, PowerType.Speed);
        player.pressedRight(item, 200);
        for (int i=0; i<9; i++) {
            player.move(item);
        }
        assertEquals(40, player.getX());
    }

    @Test
    @DisplayName("Move Right and press other key while moving rihgt")
    public void edgeMove() {
        PowerUp item = new PowerUp(null, new Point(100, 0), 10);
        player.pressedRight(item, 200);
        assertEquals(2, player.getX());
        player.pressedLeft(item);
        player.pressedDown(item, 10);
        player.pressedUp(item);
    }

    @Test
    @DisplayName("Move right 29 frame but it move out of map")
    public void outOfMap() {
        PowerUp item = new PowerUp(null, new Point(100, 0), 10);
        for (int i=0; i<29; i++) {
            player.pressedRight(item, 60);
            player.update(app, new Ground(i, i, GroundType.Concrete), new Pathway());
            player.move(item);
        }
        assertEquals(40, player.getX());
    }

    @Test
    @DisplayName("Wild test for moving")
    public void complexMove() {
        Pathway path = new Pathway();
        PowerUp item = new PowerUp(null, new Point(100, 0), 10);

        // move right and hold for 3 block
        for (int i=0; i<30; i++) {
            player.pressedRight(item, 200);
            player.update(app, new Ground(i, i, GroundType.Concrete), path);
            player.move(item);
        }
        assertEquals(60, player.getX(), "move right and hold for 3 blocks");

        // move down and release for 1 block
        player.pressedDown(item, 200);
        for (int i=0; i<9; i++) {
            player.move(item);
        }
        player.update(app, new Ground(60, 20, GroundType.Dirt), path);
        assertEquals(60, player.getX(), "move down and release for 1 block");
        assertEquals(20, player.getY(), "move down and release for 1 block");

        // continue moving down without pressing key for 2 block
        for (int i=1; i<=20; i++) {
            player.move(item);
            if (i%10 == 0)
                player.update(app, new Ground(60, 20+(i*2), GroundType.Dirt), path);
        }
        assertEquals(60, player.getX(), "move down 2 block");
        assertEquals(60, player.getY(), "move down 2 block");

        // continue moving down but pressing up for 2 blocks
        for (int i=1; i<=20; i++) {
            player.pressedUp(item);
            player.move(item);
            if (i%10 == 0)
                player.update(app, new Ground(60, 60+(i*2), GroundType.Dirt), path);
        }
        assertEquals(60, player.getX(), "move down 2 block but pressing up");
        assertEquals(100, player.getY(), "move down 2 block but pressing up");
    }

    /**
     * Sorry at this point I might need another late day to get all the testing done
     * such as the rest of Player action testing, Map, and Enemies class
     * :(
     */

}
