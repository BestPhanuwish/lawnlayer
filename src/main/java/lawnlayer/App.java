package lawnlayer;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.data.JSONObject;
import processing.data.JSONArray;

public class App extends PApplet {

    /**
     * These final variable are variable that have been provided on the scaffold
     * You may not need to change these
     */
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int TOPBAR = 80;

    public static final int FPS = 60;

    /**
     * The config path name of the game
     */
    public String configPath;
    
    /**
     * List contains of all Level object detail in the game such as
     * - outlay: file name for map tp load in
     * - goal: the goal for each level
     * - enemies: JSON Array of enemies within the level preparing to load in
     */
    public ArrayList<Level> levelDetails = new ArrayList<Level>();
    
    /**
     * Map data within the game 
     * contains all the Ground in grid map 2D array 
     * help clean up the main App by manage map within its class
     */
    public Map map;

    /**
     * Enemies object contains all the enemies data in the game
     */
    public Enemies enemies;

    /**
     * Pathway that player walked past dirt
     * contains list of Ground inside it
     * Detect propagation of enemy
     */
    public Pathway path;

    /**
     * Player to computer communication
     * All the user input will affect within this class
     */
    public Player player;

    /**
     * Powerup item that will pop-up in the game at certain time
     */
    public PowerUp item;

    /**
     * Game essential variable that will affect the main app straightaway
     * progress can reach goal can cause whole level to change
     * level change can cause the game to end
     * lives change can cause the game to end
     */
    public int progress;
    public int level;
    public int lives;

    /**
     * Initialise the application and config path name.
    */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * the first function to run when openning the app to setup important aspect of the game
     * frameRate determine how fast the draw loop will run per second
     * levelDetails: convert JSON data to array list of Level
     * map: initialise size of map accorded windows size and sprite size, then load the map using outlay from map details
     * enemies: initialise enemies then load it in accorded to level data
     * path: initialise path array and its propagate array
     * player: initialise the start point and set static status and load its sprite
     * item: initialise after get random point in map, then set item type by random and delay for 10 second
    */
    public void setup() {
        frameRate(FPS);

        try {
            JSONObject json = loadJSONObject(configPath);

            // convert json object to Level list object
            JSONArray levelDetailsJSON = json.getJSONArray("levels");
            for (int i=0; i<levelDetailsJSON.size(); i++) {
                levelDetails.add(new Level(
                    levelDetailsJSON.getJSONObject(i).getString("outlay"), // add level outlay to Level object
                    levelDetailsJSON.getJSONObject(i).getDouble("goal"), // add level goal to Level object
                    levelDetailsJSON.getJSONObject(i).getJSONArray("enemies") // add enemies json array to Level object
                ));
            }

            // initialise standalone essential variable (declare here bcause lives need JSON data)
            this.progress = 0;
            this.level = 1;
            this.lives = json.getInt("lives");

        } catch (Exception e) {
            // If unable to find JSON file, then close the game
            System.err.println("Can't load JSON game data file");
            System.exit(0);
        }

        // Initialise important class
        this.map = new Map(TOPBAR, WIDTH, HEIGHT, SPRITESIZE);
        this.enemies = new Enemies();
        this.path = new Pathway();
        this.player = new Player(loadImage("src/main/resources/lawnlayer/ball.png"), TOPBAR, SPRITESIZE);
        
        // Update map data from text file and catch error
        try {
            this.map.loadMap(this, this.levelDetails.get(this.level-1).getOutlay(), SPRITESIZE, TOPBAR);
        } catch (FileNotFoundException e) {
            System.err.println("Map text file level: " + this.level + " does not exist");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        } 

        // Update enemies data from json array
        this.enemies.loadEnemies(this, this.levelDetails.get(this.level-1).getEnemies(), this.map, SPRITESIZE);

        // create an item object
        this.item = new PowerUp(loadImage("src/main/resources/lawnlayer/ball.png"), this.map.getRandomPointInMapGridWhichIs(GroundType.Dirt), 600);
        this.item.setPowerUpType(this, PowerType.getRandomType());
    }

    /**
     * Clear player path and set player back to top-right corner, use when changing map, or player losing live
     */
    public void reset() {
        this.path.clear();
        this.path.clearPropagate();
        this.map.clearPath();
        this.player.reset();
    }

    /**
     * Display topbar of GUI (number of lives, progress towards goal,
    current level number, timer remaining on powerup)
     */
    public void displayData() {
        this.textSize(32);
        this.text("Lives: " + this.lives, 50, 32);
        this.text("Level " + this.level, 1132, 64);
        this.text(this.progress + "%/" + this.levelDetails.get(this.level-1).getGoal() + "%", 760, 32);

        // show item GUI when item is activating
        if (this.item.isActive())
            this.text(this.item.getPowerName() + ": " + this.item.getTimeLeft(), 300, 32);
    }

    /**
     * update the level progress by loop through the map and check porpotion of grass out of all tile except concrete
     */
    public void updateProgress() {
        int total_tile = 0;
        int concrete_tile = 0;
        int grass_tile = 0;
        Ground[][] grid = this.map.getMapGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                total_tile += 1;
                if (grid[i][j].getGroundType() == GroundType.Concrete) {
                    concrete_tile += 1;
                } else if (grid[i][j].getGroundType() == GroundType.Grass) {
                    grass_tile += 1;
                }
            }
        }

        double result = ((double) grass_tile/(total_tile-concrete_tile)) * 100;
        this.progress = (int) Math.ceil(result);
    }
	
    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        // draw brown background
        background(99,60,28);

        // if player's lives more than 0 and the level is existed continue the game normally
        if (this.lives > 0 && this.level <= this.levelDetails.size()) {

            // update the progress, if reach the goal then changing level
            this.updateProgress();
            if (this.progress >= this.levelDetails.get(this.level-1).getGoal()) {
                this.reset();
                this.progress = 0;
                this.level += 1;

                // If no level left then cancel the code below it
                if (this.level > this.levelDetails.size())
                    return;

                // Load map data from text file and catch error
                try {
                    this.map.loadMap(this, this.levelDetails.get(this.level-1).getOutlay(), SPRITESIZE, TOPBAR);
                } catch (FileNotFoundException e) {
                    System.err.println("Map text file level: " + this.level + " does not exist");
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }

                // Load enemies data from json array
                this.enemies.loadEnemies(this, this.levelDetails.get(this.level-1).getEnemies(), this.map, SPRITESIZE);

            }

            // if player hit the green path then reset, otherwise continue the logic
            if (this.path.pointIsInPath(this.player)) {
                this.lives -= 1;
                this.reset();
            }

            // update path propagate and check if player will died
            boolean propagated = this.path.update(this, this.item);
            if (propagated) {
                this.lives -= 1;
                this.reset();
            }

            // Check if player reach the grid map
            if (this.map.pointIsInGrid(this.player)) {

                // if reach grid map then get the current ground that player stood on
                Ground currentPlace = new Ground();
                try {
                    currentPlace = this.map.getGroundFromPlayerPoint(this.player);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }

                // update player direction within update method
                // check if the ground that player stood on is grass or concrete
                // if yes then fill the grass
                boolean touchBorder = this.player.update(this, currentPlace, this.path);
                if (touchBorder) {
                    map.fillGrass(this, this.enemies.getEnemiesArray());
                }

                // check if place that player stood on is the same place as where item stand, if yes then receive power
                this.item.collectItem(this.player);
            }

            // update enemies direction
            try {
                this.enemies.update(this, this.map, this.path);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

            // Update logic 
            this.player.move(this.item);
            this.enemies.move(this.item);
            this.item.tick(this, this.map.getRandomPointInMapGridWhichIs(GroundType.Dirt));

            // Handle graphic
            this.map.drawMap(this);
            this.player.draw(this);
            this.enemies.draw(this);
            this.item.draw(this);

            // Player text data display
            this.displayData();

        } else if (this.lives <= 0) {
            // if player lost the last lives then show the game over screen
            this.textSize(128);
            this.textAlign(CENTER);
            this.text("GAME OVER", 640, 360);
            return;
        } else if (this.level > this.levelDetails.size()) {
            // if player reach last level then shwo victory screen
            this.textSize(128);
            this.textAlign(CENTER);
            this.text("YOU WIN", 640, 360);
            return;
        }
    }

    /**
     * Called every frame if a key is down.
     * You can access the key with the keyCode variable.
     */
    public void keyPressed() {
        // Left: 37
        // Up: 38
        // Right: 39
        // Down: 40
        if (this.keyCode == 37) {
            this.player.pressedLeft(this.item);
        } else if (this.keyCode == 38) {
            this.player.pressedUp(this.item);
        } else if (this.keyCode == 39) {
            this.player.pressedRight(this.item, WIDTH);
        } else if (this.keyCode == 40) {
            this.player.pressedDown(this.item, HEIGHT);
        }
    }

    /**
     * Runs the program.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
