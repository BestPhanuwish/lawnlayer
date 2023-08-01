package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;

enum GroundType {
    Dirt,
    Grass,
    GreenPath,
    RedPath,
    Concrete;
}

public class Ground extends Sprite {

    /**
     * There are different type of ground in the game
     * @see GroundType enum above
     */
    private GroundType ground;

    /**
     * every ground have the same method as sprite
     * except if ground don't have sprite, then there will be no sprite when draw
     * or if no param given, ground will set to defalut state
     * @param x coordinate
     * @param y coordinate
     * @param ground type of ground
     * @param sprite of the ground (optional)
     */
    public Ground(int x, int y, GroundType ground, PImage sprite) {
        super(sprite, x, y);
        this.ground = ground;
    }

    public Ground(int x, int y, GroundType ground) {
        super(null, x, y);
        this.ground = ground;
    }

    public Ground() {
        super(null, 0, 0);
    }

    /**
     * draw the ground to the screen using PApplet and its current x and y position
     * unless if the GroundType is dirt, then don't need to draw
     * @param app main PApplet required
     */
    public void draw(PApplet app) {
        if (this.ground != GroundType.Dirt)
            app.image(this.sprite, this.getX(), this.getY());
    }

    /**
     * set the current GroundType accord to its enum
     * @param ground the type of ground
     */
    public void setGroundType(GroundType ground) {
        this.ground = ground;
    }

    /**
     * get the GroundType from currect ground
     * @return type of ground
     */
    public GroundType getGroundType() {
        return this.ground;
    }

    /**
     * check if current ground will make enemy change direction when hit
     * @return true or false
     */
    public boolean isHittableGround() {
        if (this.ground == GroundType.Concrete) {
            return true;
        } else if (this.ground == GroundType.RedPath) {
            return true;
        } else if (this.ground == GroundType.GreenPath) {
            return true;
        } else if (this.ground == GroundType.Grass) {
            return true;
        }
        return false;
    }

}
