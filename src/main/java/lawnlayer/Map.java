package lawnlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

import processing.core.PApplet;

public class Map {
    
    /**
     * grid map contains all the Ground tile
     */
    private Ground[][] grid;

    /**
     * initialise the size of the grid accorded to screen size and sprite size
     * @param topBarSize
     * @param screenWidth
     * @param screenHeight
     * @param spriteSize
     */
    public Map(int topBarSize, int screenWidth, int screenHeight, int spriteSize) {
        this.grid = new Ground[(screenHeight-topBarSize)/spriteSize][screenWidth/spriteSize];
    }

    /**
     * Update map data accorded to level name
     * @param app the main application to load the concrete sprite
     * @param mapName the map name can found in config file
     * @param SPRITESIZE the sprite size value from static variable on main app
     * @param TOPBAR the topbar size value from static variable on main app
     * @throws FileNotFoundException if the level text file does not exist, will pass this exception to the main app
     * @throws Exception throw exception when map not read correctly (eg. the concrete is not cover all frame of the map)
    */
    public void loadMap(PApplet app, String mapName, int SPRITESIZE, int TOPBAR) throws Exception {
        File mapFile = new File(mapName);
        Scanner scan = new Scanner(mapFile);
        for (int i = 0; i < grid.length; i++) { // loop on each row in text file within map boundary
            String line = scan.nextLine();
            for (int j = 0; j < grid[i].length; j++) { // loop on each column in text file within map boundary
                // if X is on text file then place cement, otherwise empty dirt
                if (line.charAt(j) == 'X') {
                    grid[i][j] = new Ground(j*SPRITESIZE, TOPBAR+i*SPRITESIZE, GroundType.Concrete, app.loadImage("src/main/resources/lawnlayer/concrete_tile.png"));
                } else {
                    // If no concrete on the border, then throw exception and close the game
                    if (i == 0 || j == 0 || i == grid.length-1 || j == grid[i].length-1) {
                        scan.close();
                        throw new Exception("Map format uncorrectly read. Please ensure that X place on row: " + i+1 + " column: " + j+1);
                    } else {
                        grid[i][j] = new Ground(j*SPRITESIZE, TOPBAR+i*SPRITESIZE, GroundType.Dirt);
                    }
                }
            }
        }
        scan.close();
    }
    
    /**
     * Graphic handle method, to draw all the Ground in map every frame
     * @param app main app to draw ground
     */
    public void drawMap(PApplet app) {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                this.grid[i][j].draw(app);
            }
        }
    }

    /**
     * getter method to get the map grid
     * @return grid map contains Ground
     */
    public Ground[][] getMapGrid() {
        return this.grid;
    }

    /**
     * Clear all the Pathway on map grid technically and visually
     * by loop through the map and change all path tile to dirt
     */
    public void clearPath() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (this.grid[i][j].getGroundType() == GroundType.GreenPath || this.grid[i][j].getGroundType() == GroundType.RedPath) {
                    this.grid[i][j].setGroundType(GroundType.Dirt);
                    this.grid[i][j].setSprite(null);
                }
            }
        }
    }

    /**
     * Loop and check if point is on the map grid
     * @param p another point
     * @return true of this point is in grid
     */
    public boolean pointIsInGrid(Point p) {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (this.grid[i][j].isSamePoint(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * get the current ground that player is stood on
     * @param p player
     * @return Ground that player stood on
     * @throws Exception if weird case happen where player not stand on the grid map
     * ONLY USE THIS FUNCTION WHEN PLAYER IS ON THE GRID MAP
     */
    public Ground getGroundFromPlayerPoint(Player p) throws Exception {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (this.grid[i][j].getX() == p.getX() && this.grid[i][j].getY() == p.getY()) {
                    return this.grid[i][j];
                }
            }
        }
        throw new Exception("Point X:" + p.x + " Y: " + p.y + " is not on the grid map and unable to get the ground");
    }

    /**
     * get ground from specific point without need to be on the grid
     * @param p point
     * @return Ground that point is on
     * @throws Exception if point is outside the map area
     */
    public Ground getGroundFromPoint(Point p) throws Exception {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (this.grid[i][j].getX() == p.getX() && this.grid[i][j].getY() == p.getY()) {
                    return this.grid[i][j];
                }

                // get the grid bound
                int leftLimit = this.grid[i][j].getX();
                int upLimit = this.grid[i][j].getY();
                int rightLimit;
                try {
                    rightLimit = this.grid[i][j+1].getX();
                } catch (IndexOutOfBoundsException e) {
                    rightLimit = this.grid[i][j].getX() + 20;
                }
                int downLimit;
                try {
                    downLimit = this.grid[i+1][j].getY();
                } catch (IndexOutOfBoundsException e) {
                    downLimit = this.grid[i][j].getY() + 20;
                }

                // check if the point is on the grid boundary
                if (p.getX() >= leftLimit && p.getX() < rightLimit && p.getY() >= upLimit && p.getY() <= downLimit) {
                    return this.grid[i][j];
                }
            }
        }
        throw new Exception("Point X:" + p.x + " Y: " + p.y + " is not on the grid map and unable to get the ground");
    }

    /**
     * get the coordinate grid map row and column
     * can be any point within the map area
     * @param x coordinate
     * @param y coordinate
     * @return String contains grid row and column
     */
    public String getCoordinateInGridMap(int x, int y){
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                // if it the same position of grid then it's that grid
                if (this.grid[i][j].getX() == x && this.grid[i][j].getY() == y) {
                    return i + "," + j;
                }

                // get the grid bound
                int leftLimit = this.grid[i][j].getX();
                int upLimit = this.grid[i][j].getY();
                int rightLimit;
                try {
                    rightLimit = this.grid[i][j+1].getX();
                } catch (IndexOutOfBoundsException e) {
                    rightLimit = this.grid[i][j].getX() + 20;
                }
                int downLimit;
                try {
                    downLimit = this.grid[i+1][j].getY();
                } catch (IndexOutOfBoundsException e) {
                    downLimit = this.grid[i][j].getY() + 20;
                }

                // check if the point is on the grid boundary
                if (x >= leftLimit && x < rightLimit && y >= upLimit && y <= downLimit) {
                    return i + "," + j;
                }
            }
        }
        return "-1";
    }

    /**
     * Random loop until the Point select is match the GroundType
     * @param type of the ground
     * @return Point of map grid with that type of ground
     */
    public Point getRandomPointInMapGridWhichIs(GroundType type) {
        while (true) {
            Ground[] spaceRow = this.grid[new Random().nextInt(this.grid.length)];
            Ground place = spaceRow[new Random().nextInt(spaceRow.length)];
            if (place.getGroundType() == type) 
                return new Point(place.getX(), place.getY());
        }
    }

    /**
     * This method can only be called within the class
     * fill the array recursively
     * @param floodfill array contains 0,1,2
     * @param i curent row index
     * @param j current column index
     */
    private void floodfillRecursive(int[][] floodfill, int i, int j) {
        /*
        i will never be less than 0 or greater than row size
        j will never be less than 0 or greater than column size
        the current index should be 0 only to be able to fill (which is normal dirt)
        */
        if (i < 0 || j < 0 || i >= floodfill.length || j >= floodfill[0].length || floodfill[i][j] != 0) {
            return;
        }

        // by making the floodfill 1 mean that specific grid will trun to dirt because it's where the enemy existed
        floodfill[i][j] = 1;

        // fill the neighborhood, by doing this the neighborhood will fill the next the neighborhood if condition above is satisfied
        floodfillRecursive(floodfill, i+1, j);
        floodfillRecursive(floodfill, i-1, j);
        floodfillRecursive(floodfill, i, j+1);
        floodfillRecursive(floodfill, i, j-1);
    }

    /**
     * fill the map with grass regarded to enemies position
     * @param app to filled the grass
     * @param enemies will not filled within the enemy teritory
     */
    public void fillGrass(PApplet app, ArrayList<Enemy> enemies) {
        /*
        fill the new array with number accorded to groundtype of grid map where
        2 = concrete or grass
        0 = dirt
        */
        int[][] floodfill = new int[this.grid.length][this.grid[0].length];
        for (int i = 0; i < floodfill.length; i++) {
            for (int j = 0; j < floodfill[i].length; j++) {
                if (this.grid[i][j].getGroundType() == GroundType.Concrete || this.grid[i][j].getGroundType() == GroundType.Grass) {
                    floodfill[i][j] = 2;
                } else {
                    floodfill[i][j] = 0;
                }
            }
        }

        /*
        loop through every enemy to check its neightborhood
        and decided whether or not the space will turn into grass
        if empty dirt have no enemy nearby then it will change to grass
        */
        for (Enemy e : enemies) {
            // initialise the start point of floodfill process
            int i = 0;
            int j = 0;

            // check each 4 corner of the enemy
            Point[] enemyCoordinate = {
                e,
                new Point(e.x+e.getSpriteSize(), e.y),
                new Point(e.x, e.y+e.getSpriteSize()),
                new Point(e.x+e.getSpriteSize(), e.y+e.getSpriteSize())
            };

            // loop through every corner if the type of that corner landed is dirt, then get that coordinate
            for (Point p : enemyCoordinate) {
                String coordinate = this.getCoordinateInGridMap(p.x, p.y);
                if (!coordinate.equals("-1")) {
                    String[] stringArray = coordinate.split(",");
                    int yCoor = Integer.parseInt(stringArray[0]);
                    int xCoor = Integer.parseInt(stringArray[1]);
                    if (this.grid[yCoor][xCoor].getGroundType() == GroundType.Dirt) {
                        i = yCoor;
                        j = xCoor;
                        break;
                    }
                }
            }

            /*
            Change the current floodfill grid if the current coordinate is normal dirt
            0 = normal dirt that will change to grass
            1 = this will be dirt because it's the neightborhood with enemy coordinate
            2 = grass or concrete that will stop the filling process
            */
            if (floodfill[i][j] == 0)
                this.floodfillRecursive(floodfill, i, j);

        }

        // loop throught the floodfill then fill the grass if floodfill element is 0
        for (int i = 0; i < floodfill.length; i++) {
            for (int j = 0; j < floodfill[i].length; j++) {
                if (this.grid[i][j].getGroundType() == GroundType.Dirt && floodfill[i][j] == 0) {
                    this.grid[i][j].setGroundType(GroundType.Grass);
                    this.grid[i][j].setSprite(app.loadImage("src/main/resources/lawnlayer/grass.png"));
                }
            }
        }
    }

    public void fillAllGrass(PApplet app) {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (this.grid[i][j].getGroundType() == GroundType.Dirt) {
                    this.grid[i][j].setGroundType(GroundType.Grass);
                    this.grid[i][j].setSprite(app.loadImage("src/main/resources/lawnlayer/grass.png"));
                }
            }
        }
    }

}
