package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;

enum PlayerStatus {
    Static,
    Left,
    Right,
    Up,
    Down
}

public class Player extends Sprite {

    /**
     * Player speed set as final value accored to the scaffold
     */
    private static final int SPEED = 2;

    /**
     * Start point of player, will be useful when player got reset
     */
    private Point startPoint;

    /**
     * Player's sprite size (it's 20 accored to the scaffold)
     */
    private int spriteSize;

    /**
     * new status detect the instant direction change from user's input
     * status will change when player finished move a grid
     * @see PlayerStatus above on what direction player can go
     */
    private PlayerStatus status;
    private PlayerStatus newStatus;


    /**
     * Constructor for a player, required an image, x, and y position
     * Default status, and new status is static
     * @param sprite of the player
     * @param x coordinate
     * @param y coordinate
     */
    public Player(PImage sprite, int topBarSize, int spriteSize) {
        super(sprite, 0, topBarSize);
        this.startPoint = new Point(0, topBarSize);
        this.spriteSize = spriteSize;
        this.status = PlayerStatus.Static;
        this.newStatus = PlayerStatus.Static;
    }

    /**
     * draw the player to the screen using PApplet and its current x and y position
     * @param app main PApplet required to draw
     */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

    /**
     * Player reset method, put player at the start point then set status to static
     */
    public void reset() {
        this.setPoint(startPoint);
        this.status = PlayerStatus.Static;
        this.newStatus = PlayerStatus.Static;
    }

    /**
     * detect user pressing the left key, update newStatus, and move out if current status is static
     * @param item use to check if player have speed boost
     */
    public void pressedLeft(PowerUp item) {
        // if player will move out of stage, then cancel the action
        if (this.x-SPEED < 0) {
            return;
        }

        // get speed multiplier if have item speed boost
        int multiplier = 1;
        if (item.isActive() && item.getPowerType() == PowerType.Speed)
            multiplier = 2;

        this.newStatus = PlayerStatus.Left;
        if (this.status == PlayerStatus.Static) {
            this.x -= SPEED*multiplier;
            this.status = this.newStatus;
        }
    }

    /**
     * detect user pressing the right key, update newStatus, and move out if current status is static
     * @param item use to check if player have speed boost
     * @param screenWidth to check if player will move out of screen on right size
     */
    public void pressedRight(PowerUp item, int screenWidth) {
        // if player will move out of stage, then cancel the action
        if (this.x+SPEED+this.spriteSize > screenWidth) {
            return;
        }
        
        // get speed multiplier if have item speed boost
        int multiplier = 1;
        if (item.isActive() && item.getPowerType() == PowerType.Speed)
            multiplier = 2;

        this.newStatus = PlayerStatus.Right;
        if (this.status == PlayerStatus.Static) {
            this.x += SPEED*multiplier;
            this.status = this.newStatus;
        }
    }

    /**
     * detect user pressing the up key, update newStatus, and move out if current status is static
     * @param item use to check if player have speed boost
     */
    public void pressedUp(PowerUp item) {
        // if player will move out of stage, then cancel the action
        if (this.y-SPEED < startPoint.getY()) {
            return;
        }

        // get speed multiplier if have item speed boost
        int multiplier = 1;
        if (item.isActive() && item.getPowerType() == PowerType.Speed)
            multiplier = 2;

        this.newStatus = PlayerStatus.Up;
        if (this.status == PlayerStatus.Static) {
            this.y -= SPEED*multiplier;
            this.status = this.newStatus;
        }
    }

    /**
     * detect user pressing the down key, update newStatus, and move out if current status is static
     * @param item use to check if player have speed boost
     * @param screenHeight check if player will move out of screen on down size
     */
    public void pressedDown(PowerUp item, int screenHeight) {
        // if player will move out of stage, then cancel the action
        if (this.y+SPEED+this.spriteSize > screenHeight) {
            return;
        }

        // get speed multiplier if have item speed boost
        int multiplier = 1;
        if (item.isActive() && item.getPowerType() == PowerType.Speed)
            multiplier = 2;

        this.newStatus = PlayerStatus.Down;
        if (this.status == PlayerStatus.Static) {
            this.y += SPEED*multiplier;
            this.status = this.newStatus;
        }
    }

    /**
     * get next point player on the map grid by predict it using current user input
     * don't call this function while moving, if you want to get next move on map grid
     * @return Point of next move
     */
    public Point getNextMove() {
        Point nextMove = new Point(this.x, this.y);
        if (this.newStatus == PlayerStatus.Left) {
            nextMove = new Point(this.x-spriteSize, this.y);
        } else if (this.newStatus == PlayerStatus.Right) {
            nextMove = new Point(this.x+spriteSize, this.y);
        } else if (this.newStatus == PlayerStatus.Up) {
            nextMove = new Point(this.x, this.y-spriteSize);
        } else if (this.newStatus == PlayerStatus.Down) {
            nextMove = new Point(this.x, this.y+spriteSize);
        }
        return nextMove;
    }

    /**
     * update the direction of player if next move if not on the same place
     * fill the path with grass
     * place the green path if the place is dirt
     * @param app
     * @param place
     * @param path
     * @return true if player reach the grass or concrete (use for fill grass on app)
     */
    public boolean update(PApplet app, Ground place, Pathway path) {
        boolean will_fill = false;

        // get next move
        Point nextMove = this.getNextMove();

        // checking if last path that player just walked is existed
        boolean able_to_change_direction = true;
        if (path.getSize() != 0) {
            // if next move will be the same path that player just walked, then stop the change direction process
            if (nextMove.isSamePoint(path.getLastestPoint())) {
                able_to_change_direction = false;
            }
        }

        // add path if player walk on dirt, and end path when player reach concrete or grass
        if (place.getGroundType() == GroundType.Concrete || place.getGroundType() == GroundType.Grass) {
            if (path.getSize() != 0) {
                path.fillGrass(app);
                path.clear(); // get rid of all the element inside path array
                path.clearPropagate();
                will_fill = true;
            }
        } else if (place.getGroundType() == GroundType.Dirt) {
            path.addPoint(place);
        }

        // stop player if tile is concrete. Otherwise, if dirt then set to greenpath
        if (place.getGroundType() == GroundType.Concrete) {
            this.newStatus = PlayerStatus.Static;
            able_to_change_direction = true; // force to be status static if reach tile preventing bug where player move out of the screen when reach the concrete tile while moving back at the same path
        } else if (place.getGroundType() == GroundType.Dirt) {
            place.setGroundType(GroundType.GreenPath);
            place.setSprite(app.loadImage("src/main/resources/lawnlayer/greenPath.png"));
        }

        // change real player status
        if (able_to_change_direction) {
            this.status = this.newStatus;
        }

        return will_fill;
    }

    /**
     * move the player method (done after update direction)
     * @param item use to check if player have speed boost
     */
    public void move(PowerUp item) {
        // get speed multiplier if have item speed boost
        int multiplier = 1;
        if (item.isActive() && item.getPowerType() == PowerType.Speed)
            multiplier = 2;

        // move the player accored to current direction
        if (this.status == PlayerStatus.Left) {
            this.x -= SPEED*multiplier;
        } else if (this.status == PlayerStatus.Right) {
            this.x += SPEED*multiplier;
        } else if (this.status == PlayerStatus.Up) {
            this.y -= SPEED*multiplier;
        } else if (this.status == PlayerStatus.Down) {
            this.y += SPEED*multiplier;
        }
    }

}
