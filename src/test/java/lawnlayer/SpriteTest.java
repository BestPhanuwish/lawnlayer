package lawnlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;
import processing.core.PImage;

public class SpriteTest {
    
    PApplet app;

    @BeforeEach
    public void setUp() {
        // set up app so it's not crash while testing
        app = new PApplet();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
    }

    @Test
    @DisplayName("Simple sprite set up")
    public void simpleSprite() {
        PImage playerSprite = app.loadImage("src/main/resources/lawnlayer/ball.png");
        Sprite sprite = new Player(playerSprite, 0, 0); // Can't instantiate sprite that why it need player class to test
        assertNotNull(sprite);
    }

    @Test
    @DisplayName("Getter method")
    public void getterSprite() {
        PImage playerSprite = app.loadImage("src/main/resources/lawnlayer/ball.png");
        Sprite sprite = new Player(playerSprite, 0, 0);
        assertEquals(playerSprite, sprite.getSprite());
    }

    @Test
    @DisplayName("Setter method")
    public void setterSprite() {
        PImage playerSprite = app.loadImage("src/main/resources/lawnlayer/ball.png");
        PImage wormSprite = app.loadImage("src/main/resources/lawnlayer/worm.png");

        Sprite sprite = new Player(playerSprite, 0, 0);
        sprite.setSprite(wormSprite);

        // it should not be player sprite anymore
        assertNotEquals(playerSprite, sprite.getSprite());
        assertEquals(wormSprite, sprite.getSprite());
    }

}
