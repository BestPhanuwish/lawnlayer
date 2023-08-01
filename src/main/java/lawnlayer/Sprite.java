package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;

public abstract class Sprite extends Point {

    /**
     * The object on the screen
     */
    protected PImage sprite;

    /**
     * Constructor for a sprite, required an image, x, and y position
     * @param sprite of the player
     * @param x coordinate
     * @param y coordinate
     */
    public Sprite(PImage sprite, int x, int y) {
        super(x, y);
        this.sprite = sprite;
    }

    /**
     * every sprite need draw method to display object on the screen
     * @param app require for borrow the draw function from it
     */
    public abstract void draw(PApplet app);

    /**
     * set the current sprite instantly
     * @param sprite PImage from PApplet loadImage() function
     */
    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * get the current sprite
     * @return PImage sprite object
     */
    public PImage getSprite() {
        return this.sprite;
    }

}
