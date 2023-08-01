package lawnlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;

public class PowerUpTest {
    
    private PApplet app;
    private PowerUp item;
    private Player player;

    @BeforeEach
    public void setUp() {
        // set up app so it's not crash while testing
        app = new PApplet();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);

        item = new PowerUp(null, new Point(0, 0), 600);
        player = new Player(null, 0, 0);
    }

    @Test
    @DisplayName("Existence check")
    public void simplePowerUp() {
        assertNotNull(item);
        assertNotNull(player);
    }

    @Test
    @DisplayName("Getter method for active")
    public void getterActive() {
        assertFalse(item.isActive());
    }

    @Test
    @DisplayName("Getter method for available")
    public void getterAvailable() {
        assertFalse(item.isAvailable());
    }

    @Test
    @DisplayName("Player collect item method, with active getter method check again")
    public void collectItem1() {
        assertFalse(item.isActive());
        item.collectItem(player);
        assertTrue(item.isActive());
    }
    @Test
    @DisplayName("Player collect item method, but player is not on same point as item")
    public void collectItem2() {
        assertFalse(item.isActive());
        player.pressedRight(item, 1280);
        item.collectItem(player);
        assertFalse(item.isActive());
    }

    @Test
    @DisplayName("Setter and Getter method for power up type")
    public void simpleType() {
        item.setPowerUpType(app, PowerType.Barrier);
        assertEquals(PowerType.Barrier, item.getPowerType());
        item.setPowerUpType(app, PowerType.Speed);
        assertEquals(PowerType.Speed, item.getPowerType());
        item.setPowerUpType(app, PowerType.Freeze);
        assertEquals(PowerType.Freeze, item.getPowerType());
    }

    @Test
    @DisplayName("Getter method for power up name")
    public void getterTypeName() {
        item.setPowerUpType(app, PowerType.Barrier);
        assertEquals("Barrier", item.getPowerName());
        item.setPowerUpType(app, PowerType.Speed);
        assertEquals("Speed", item.getPowerName());
        item.setPowerUpType(app, PowerType.Freeze);
        assertEquals("Freeze", item.getPowerName());
    }

    @Test
    @DisplayName("Getter method for power up source file name")
    public void getterTypeSprite() {
        item.setPowerUpType(app, PowerType.Barrier);
        assertEquals("src/main/resources/lawnlayer/barrier.png", item.getPowerType().gerSprite());
        item.setPowerUpType(app, PowerType.Speed);
        assertEquals("src/main/resources/lawnlayer/speed.png", item.getPowerType().gerSprite());
        item.setPowerUpType(app, PowerType.Freeze);
        assertEquals("src/main/resources/lawnlayer/freeze.png", item.getPowerType().gerSprite());
    }

    @Test
    @DisplayName("Get random power up type enum method") // I actually have no idea how to test this
    public void randomType() {
        item.setPowerUpType(app, PowerType.getRandomType());
        assertNotNull(item.getPowerType());
    }

    @Test
    @DisplayName("Simple scanerio Tick method that will run every frame")
    public void simpleTick() {
        item.setPowerUpType(app, PowerType.getRandomType());
        
        // run for 1 sec withot any change
        for (int i=0; i<60; i++) {
            item.tick(app, new Point(0,0));
        }

        // it should not available yet, not active, and time left at defalut time which is 10
        assertFalse(item.isActive());
        assertFalse(item.isAvailable());
        assertEquals(10, item.getTimeLeft());
    }

    @Test
    @DisplayName("Tick method for 10-20 second")
    public void longTick() {
        item.setPowerUpType(app, PowerType.getRandomType());
        
        // run for 10 sec withot any change
        for (int i=0; i<600; i++) {
            item.tick(app, new Point(0,0));
        }

        // it should available, not active, and time left at defalut time which is 10
        assertFalse(item.isActive());
        assertTrue(item.isAvailable());
        assertEquals(10, item.getTimeLeft());

        // run for another 10 sec withot any change
        for (int i=0; i<600; i++) {
            item.tick(app, new Point(0,0));
        }

        // it still should be the same as last time
        assertFalse(item.isActive());
        assertTrue(item.isAvailable());
        assertEquals(10, item.getTimeLeft());
    }

    @Test
    @DisplayName("Tick method for 10 second then got collect after that")
    public void collectTick() {
        item.setPowerUpType(app, PowerType.getRandomType());
        
        // run for 10 sec then got collected
        for (int i=0; i<600; i++) {
            item.tick(app, new Point(0,0));
        }
        item.collectItem(player);

        // it should not available, but active, and time left at defalut time which is 10
        assertTrue(item.isActive());
        assertFalse(item.isAvailable());
        assertEquals(10, item.getTimeLeft());
    }

    @Test
    @DisplayName("Tick method for 10 second then got collect after 2 second then check after 5 second and check again after it available")
    public void complexTick() {
        item.setPowerUpType(app, PowerType.getRandomType());
        
        // run for 12 sec then got collected
        for (int i=0; i<720; i++) {
            item.tick(app, new Point(0,0));
        }
        item.collectItem(player);

        // run for another 5 sec
        for (int i=0; i<300; i++) {
            item.tick(app, new Point(0,0));
        }

        // it should not available, but active, and time left for 5 sec
        assertTrue(item.isActive(), "while item is active");
        assertFalse(item.isAvailable(), "while item is active");
        assertEquals(5, item.getTimeLeft(), "while item is active");

        // run for another 15 sec
        for (int i=0; i<900; i++) {
            item.tick(app, new Point(0,0));
        }

        // it should available, not active, and time left at defalut value which is 10 again
        assertFalse(item.isActive(), "while item is respawn");
        assertTrue(item.isAvailable(), "while item is respawn");
        assertEquals(10, item.getTimeLeft(), "while item is respawn");
    }

}
