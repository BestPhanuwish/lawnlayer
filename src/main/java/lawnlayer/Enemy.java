package lawnlayer;

import java.util.Random;

import processing.core.PImage;
import processing.core.PApplet;

enum EnemyType {
    Worm,
    Beetle
}

public class Enemy extends Sprite {
    
    /**
     * Enemy speed set as final value accored to the scaffold
     */
    private static final int SPEED = 2;

    /**
     * Enemy have different type in the game
     * @see EnemyType above
     */
    private EnemyType type;

    /**
     * the sprite size of an enemy
     */
    private int spriteSize;
    
    /**
     * direction of an enemy
     */
    private boolean goLeft;
    private boolean goUp;

    /**
     * Initialise enemy by
     * @param sprite set enemy image shown on screen
     * @param size collect size of enemy
     * @param p spawn point of an enemy
     * @param type of an enemy
     */
    public Enemy(PImage sprite, int size, Point p, EnemyType type) {
        super(sprite, p.getX(), p.getY());
        this.type = type;
        this.spriteSize = size;
        this.goLeft = new Random().nextBoolean();
        this.goUp = new Random().nextBoolean();
    }

    public Enemy(PImage sprite, int size, Point p, EnemyType type, boolean goLeft, boolean goUp) {
        super(sprite, p.getX(), p.getY());
        this.type = type;
        this.spriteSize = size;
        this.goLeft = goLeft;
        this.goUp = goUp;
    }

    /**
     * draw the enemy to the screen using PApplet and its current x and y position
     * @param app main PApplet required to draw
     */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

    /**
     * getter method for enemy type
     * @return type of enemy
     */
    public EnemyType getType() {
        return this.type;
    }

    /**
     * getter method for enemy sprite size
     * @return size of sprite
     */
    public int getSpriteSize() {
        return this.spriteSize;
    }

    /**
     * Enemy move diagonal direction
     */
    public void move() {
        if (this.goLeft) {
            this.x -= SPEED;
        } else {
            this.x += SPEED;
        }

        if (this.goUp) {
            this.y -= SPEED;
        } else {
            this.y += SPEED;
        }
    }

    /**
     * getter method for X direction of an enemy
     * @return true if enemy is going left, false if going right
     */
    public boolean isGoLeft() {
        return this.goLeft;
    }

    /**
     * change the X direction to the opposite direction
     */
    public void changeXdirection() {
        this.goLeft = !this.goLeft;
    }

    /**
     * getter method for Y direction of an enemy
     * @return true if enemy is going Up, false if going Down
     */
    public boolean isGoUp() {
        return this.goUp;
    }

    /**
     * change the Y direction to the opposite direction
     */
    public void changeYdirection() {
        this.goUp = !this.goUp;
    }

}
