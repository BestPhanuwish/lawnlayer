package lawnlayer;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * There are 3 different item in the game and it have constructor (make it easy to manage than GroundType enum)
 */
enum PowerType {
    Barrier("Barrier", "src/main/resources/lawnlayer/barrier.png"),
    Speed("Speed", "src/main/resources/lawnlayer/speed.png"),
    Freeze("Freeze", "src/main/resources/lawnlayer/freeze.png");

    /**
     * name of power up (will use to display on GUI)
     */
    private String name;

    /**
     * sprite file soruce string will use to load image on PApplet
     */
    private String sprite;

    /**
     * PowerType constructor each need name and its file source name
     * @param name
     * @param sprite
     */
    private PowerType(String name, String sprite) {
        this.name = name;
        this.sprite = sprite;
    }

    /**
     * get name of power up
     * @return name
     */
    public String gerName() {
        return this.name;
    }

    /**
     * get sprite sorce of the power up
     * @return sprite file name
     */
    public String gerSprite() {
        return this.sprite;
    }

    /**
    * Pick a random value of the PowerType enum.
    * @return a random PowerType.
    */
    public static PowerType getRandomType() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}

public class PowerUp extends Sprite {
    
    /**
     * Power type of the current power up
     */
    private PowerType type;

    /**
     * the item will active for only 10 second accorded to the scaffold
     */
    private boolean active;
    private static final int active_last = 600; // 600 frame mean 10 second
    private int active_timer;

    /**
     * the item will wait for random time before spawn again
     */
    private boolean available;
    private int available_last;
    private int available_timer;

    /**
     * Create Power up once the game is load in
     * @param sprite of the power up
     * @param p position point of the power up (will be random)
     * @param wait_until_available wait time until available to pick and see, accorded to the scaffold the item will need to wait for 10 second before appear when player just join in the game
     */
    public PowerUp(PImage sprite, Point p, int wait_until_available) {
        super(sprite, p.x, p.y);
        this.active = false;
        this.active_timer = 0;
        this.available = false;
        this.available_last = wait_until_available;
        this.available_timer = 0;
    }

    /**
     * if item is available to collect the display it visually
     * @param app to draw power up on screen
     */
    public void draw(PApplet app) {
        if (available)
            app.image(this.sprite, this.x, this.y);
    }

    /**
     * check whether the player will collecting an item
     * if collect then the item will be active and unavailable
     * @param player
     */
    public void collectItem(Player player) {
        if (this.isSamePoint(player)) {
            active = true;
            available = false;
        }
    }

    /**
     * set the power up to new type and load image using app accorded to new type
     * @param app
     * @param type
     */
    public void setPowerUpType(PApplet app, PowerType type) {
        this.type = type;
        this.setSprite(app.loadImage(this.type.gerSprite()));
    }

    /**
     * set active
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * check whether the item is still active
     * @return true if active
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * check whether the item is available to collect
     * @return true if available
     */
    public boolean isAvailable() {
        return this.available;
    }

    /**
     * get the how many times left for item to be active in second to show on GUI
     * @return time left in second
     */
    public int getTimeLeft() {
        return (int) Math.ceil((double) (active_last/60) - (double) (active_timer/60));
    }

    /**
     * get the type of current power up
     * @return PowerType
     */
    public PowerType getPowerType() {
        return this.type;
    }

    /**
     * get the name of current power up
     * @return name
     */
    public String getPowerName() {
        return this.type.gerName();
    }

    /**
     * this method will run every frame
     * to update status of power up on the game
     * @param app to load power up sprite
     * @param p to place the power up to specific point (should be in the map grid and only on dirt Ground)
     */
    public void tick(PApplet app, Point p) {
        if (active) {
            // if still active then run until it reach the active time left
            active_timer += 1;
            if (active_timer >= active_last) {
                // item unactivate
                active = false;
                active_timer = 0;

                // reset the item
                this.setPowerUpType(app, PowerType.getRandomType());
                this.setPoint(p);
            }
        } else {
            // if item is not active then wait until it's available
            if (!available) {
                available_timer += 1;
                if (available_timer >= available_last) {
                    available = true;
                    available_timer = 0;
                    available_last = (new Random().nextInt(10)) * 60; // set random wait time within 10 second
                }
            }
        }
    }

}
