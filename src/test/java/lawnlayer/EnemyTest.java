package lawnlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;

public class EnemyTest {

    Enemy enemy1;
    boolean enemy1IsGoingLeft;
    boolean enemy1IsGoingUp;
    Enemy enemy2;
    boolean enemy2IsGoingLeft;
    boolean enemy2IsGoingUp;

    Enemy enemy3;
    
    @BeforeEach
    public void setUp() {
        // set up app so it's not crash while testing
        PApplet app = new PApplet();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);

        this.enemy1 = new Enemy(app.loadImage("src/main/resources/lawnlayer/worm.png"), 20, new Point(50, 50), EnemyType.Worm, true, true);
        this.enemy2 = new Enemy(app.loadImage("src/main/resources/lawnlayer/beetle.png"), 20, new Point(50, 50), EnemyType.Beetle, false, false);
        this.enemy3 = new Enemy(app.loadImage("src/main/resources/lawnlayer/worm.png"), 20, new Point(50, 50), EnemyType.Worm);;
    }

    @Test
    @DisplayName("Existence check")
    public void simpleSetup() {
        assertNotNull(enemy1);
        assertNotNull(enemy2);
        assertNotNull(enemy3);
        assertTrue(enemy1.isSamePoint(enemy2));
    }

    @Test
    @DisplayName("Getter method for enemy type")
    public void getterType() {
        assertEquals(EnemyType.Worm, enemy1.getType());
        assertEquals(EnemyType.Beetle, enemy2.getType());
    }

    @Test
    @DisplayName("Getter method for sprite size")
    public void getterSpriteSize() {
        assertEquals(20, enemy1.getSpriteSize());
        assertEquals(20, enemy2.getSpriteSize());
    }

    @Test
    @DisplayName("Getter method for x direction")
    public void getterX() {
        assertTrue(enemy1.isGoLeft());
        assertFalse(enemy2.isGoLeft());
    }

    @Test
    @DisplayName("Getter method for y direction")
    public void getterY() {
        assertTrue(enemy1.isGoUp());
        assertFalse(enemy2.isGoUp());
    }

    @Test
    @DisplayName("Setter method for x direction")
    public void setterX() {
        enemy1.changeXdirection();
        assertFalse(enemy1.isGoLeft());
        enemy2.changeXdirection();
        assertTrue(enemy2.isGoLeft());
    }

    @Test
    @DisplayName("Setter method for y direction")
    public void setterY() {
        enemy1.changeYdirection();
        assertFalse(enemy1.isGoUp());
        enemy2.changeYdirection();
        assertTrue(enemy2.isGoUp());
    }

    @RepeatedTest(5)
    @DisplayName("Simple move method")
    public void simpleMove() {
        enemy1.move();
        assertEquals(48, enemy1.getX());
        assertEquals(48, enemy1.getY());
        enemy2.move();
        assertEquals(52, enemy2.getX());
        assertEquals(52, enemy2.getY());
    }

    @Test
    @DisplayName("Complex move method")
    public void complexMove() {
        // move multiple times check
        for (int i=0; i<5; i++) {
            enemy1.move();
        }
        assertEquals(40, enemy1.getX(), "enemy1 move multiple times");
        assertEquals(40, enemy1.getY(), "enemy1 move multiple times");

        //change direction then move multiple times again
        enemy1.changeXdirection();
        for (int i=0; i<5; i++) {
            enemy1.move();
        }
        assertEquals(50, enemy1.getX(), "enemy1 move multiple times after change X direction");
        assertEquals(30, enemy1.getY(), "enemy1 move multiple times after change X direction");

        // move multiple times check
        for (int i=0; i<5; i++) {
            enemy2.move();
        }
        assertEquals(60, enemy2.getX(), "enemy2 move multiple times");
        assertEquals(60, enemy2.getY(), "enemy2 move multiple times");

        //change direction then move multiple times again
        enemy2.changeYdirection();
        for (int i=0; i<5; i++) {
            enemy2.move();
        }
        assertEquals(70, enemy2.getX(), "enemy2 move multiple times after change Y direction");
        assertEquals(50, enemy2.getY(), "enemy2 move multiple times after change Y direction");
    }

}
