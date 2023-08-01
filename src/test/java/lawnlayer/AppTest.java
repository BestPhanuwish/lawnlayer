package lawnlayer;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

public class AppTest extends App {

    @BeforeEach
    public void setUp() {
        this.noLoop();
        PApplet.runSketch(new String[] {"App"}, this);
        this.setup();
        this.settings();
        this.delay(2000);
    }

    @Test
    @DisplayName("Check existence")
    public void simpleApp() {
        assertNotNull(this);
    }

    @Test
    @DisplayName("PlayerMovement")
    public void playerInput() {
        // move right one block
        this.keyCode = 39;
        for (int i=0; i<10; i++) {
            this.keyPressed();
            this.draw();
        }

        // move down one block
        this.keyCode = 40;
        for (int i=0; i<10; i++) {
            this.keyPressed();
            this.draw();
        }

        // move left one block
        this.keyCode = 37;
        for (int i=0; i<10; i++) {
            this.keyPressed();
            this.draw();
        }

        // move up one block
        this.keyCode = 38;
        for (int i=0; i<10; i++) {
            this.keyPressed();
            this.draw();
        }

        // move down one block
        this.keyCode = 40;
        for (int i=0; i<10; i++) {
            this.keyPressed();
            this.draw();
        }

        // move right one block
        this.keyCode = 39;
        for (int i=0; i<10; i++) {
            this.keyPressed();
            this.draw();
        }

        // move right 3 block (without presssing right)
        this.keyCode = 1;
        for (int i=0; i<30; i++) {
            this.keyPressed();
            this.draw();
        }
    }
    
    @Test
    @DisplayName("Check progress reset when go next level")
    public void nextLevel() {
        this.draw();
        this.map.fillAllGrass(this);
        this.draw();
        assertEquals(0, this.progress);
        assertEquals(2, this.level);
    }

    @Test
    @DisplayName("Player died :(")
    public void playerDead() {
        this.draw();
        this.lives = 0;
        this.draw();
        assertEquals(0, this.lives);
    }

    @Test
    @DisplayName("Player win :)")
    public void playerWin() {
        this.draw();
        this.map.fillAllGrass(this);
        this.draw();
        assertEquals(2, this.level);
        this.draw();
        this.map.fillAllGrass(this);
        this.draw();
        assertEquals(3, this.level);
    }

}
