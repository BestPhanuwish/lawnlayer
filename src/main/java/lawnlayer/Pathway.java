package lawnlayer;

import java.util.ArrayList;

import processing.core.PApplet;

/**
 * Propagate class
 * help manage propagate status of pathway
 * able to count their own propagation which is one per 3 frames
 */
class Propagate {

    /**
     * the start index of propagation
     */
    private int index;

    /**
     * count how many times the propagation had occured
     */
    private int count;

    /**
     * the timer to keep track of how many frame had past, and reset once the propagate happened
     */
    private int timer;

    /**
     * Start the propagate class with timer 3 and count 0
     * @param i set the current index on pathway that propagation happened
     */
    public Propagate(int i) {
        this.index = i;
        this.count = 0;
        this.timer = 3;
    }

    /**
     * getter method for index of propagation
     * @return index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * setter method for index of propagation
     * @param index
     */
    public void setIndex(int i) {
        this.index = i;
    }

    /**
     * getter method of counter (will use to propagate the next element of pathway)
     * @return counter
     */
    public int getCount() {
        return this.count;
    }

    /**
     * setter method for counter
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * getter method for timer
     * @return timer
     */
    public int getTimer() {
        return this.timer;
    }

    /**
     * setter method for timer
     * @param timer
     */
    public void setTimer(int timer) {
        this.timer = timer;
    }

}

public class Pathway {
    
    /**
     * Collect the Ground path that player had walked on dirt on array
     */
    private ArrayList<Ground> path;

    /**
     * Collect propagate behaviour that cause by enemy touching player's path
     */
    private ArrayList<Propagate> propagatePath;

    /**
     * Constructor of pathway to initialise the path and propagate path array
     */
    public Pathway() {
        this.path = new ArrayList<Ground>();
        this.propagatePath = new ArrayList<Propagate>();
    }

    /**
     * get the current size of pathway
     * @return size of path's array
     */
    public int getSize() {
        return this.path.size();
    }

    /**
     * add the new ground to the path
     * @param g ground object
     */
    public void addPoint(Ground g) {
        this.path.add(g);
    }

    /**
     * get the point on specific index
     * @return point on path
     */
    public Ground getPoint(int i) {
        try {
            return this.path.get(i);
        } catch (IndexOutOfBoundsException e) {
            return new Ground(0, 0, GroundType.GreenPath);
        }
    }

    /**
     * get the lastest point that player just walked past
     * @return lastest point on path
     */
    public Point getLastestPoint() {
        return this.path.get(this.path.size()-1);
    }

    /**
     * loop through the pathway to check if specific point is in it
     * @param point other point to check
     * @return true if found, otherwise false
     */
    public boolean pointIsInPath(Point point) {
        for (Point p : path) {
            if (p.isSamePoint(point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * empty the pathway
     */
    public void clear() {
        this.path.clear();
    }

    /**
     * create the new propagate point, then add to propagate array
     * @param p point that propagation happened
     */
    public void createPropagate(Point p) {
        for (int i=0; i<path.size(); i++) {
            if (path.get(i).isSamePoint(p)) {
                this.propagatePath.add(new Propagate(i));
            }
        }
    }

    /**
     * get the propagate state on specific index
     * @return propagate state
     */
    public Propagate getPropagate(int i) {
        try {
            return this.propagatePath.get(i);
        } catch (IndexOutOfBoundsException e) {
            return new Propagate(0);
        }
    }

    /**
     * empty the propagate path
     */
    public void clearPropagate() {
        this.propagatePath.clear();
    }

    /**
     * update the path propagation every frame
     * @param app the main app to draw sprite
     * @param map make change on map data while updating path
     * @param item check if path will get propagate if no barrier item
     * @return true mean propagate had reach the player
     */
    public boolean update(PApplet app, PowerUp item) {
        // if have barrier item then it will never propagate and update map path to be only green path
        if (item.isActive() && item.getPowerType() == PowerType.Barrier) {
            for (Ground g : this.path) {
                g.setGroundType(GroundType.GreenPath);
                g.setSprite(app.loadImage("src/main/resources/lawnlayer/greenPath.png"));
            }
            propagatePath.clear();
            return false;
        }

        // loop throught every propagate
        for (Propagate p : this.propagatePath) {
            // if propagate timer reach 0 (which mean it reach 3 frame)
            if (p.getTimer() == 0) {
                // reset the timer and increase the count
                p.setCount(p.getCount()+1);
                p.setTimer(3);

                // get the next propagated and make it red path
                int i1 = p.getIndex()-p.getCount();
                if (i1 >= 0) {
                    this.path.get(i1).setGroundType(GroundType.RedPath);
                    this.path.get(i1).setSprite(app.loadImage("src/main/resources/lawnlayer/redPath.png"));
                }

                // the lastest propagate is important because it's where it will kill the player, if there's no green path left to propagate
                int i2 = p.getIndex()+p.getCount();
                if (i2 < path.size()-1) {
                    this.path.get(i2).setGroundType(GroundType.RedPath);
                    this.path.get(i2).setSprite(app.loadImage("src/main/resources/lawnlayer/redPath.png"));
                } else {
                    return true;
                }
            }

            // if it the first red path touched but player didn't reach another path yet, then return immediately
            if (p.getCount() == 0) {
                if (p.getIndex() >= path.size()-1) {
                    return true;
                }
            }

            // decrease the timer count each frame
            p.setTimer(p.getTimer()-1);
        }
        return false;
    }

    /**
     * fill all the ground inside the path to be grass
     */
    public void fillGrass(PApplet app) {
        for (Ground g : this.path) {
            g.setGroundType(GroundType.Grass);
            g.setSprite(app.loadImage("src/main/resources/lawnlayer/grass.png"));
        }
    }

}
