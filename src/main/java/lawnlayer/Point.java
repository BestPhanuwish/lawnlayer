package lawnlayer;

public class Point {
    
    /**
     * The position x and y coordinate of object on the screen
     */
    protected int x;
    protected int y;

    /**
     * Constructor for a point using x and y position
     * @param x coordinate
     * @param y coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the point to specific x and y position
     * @param x require to set x position of point
     * @param y require to set y position of point
     */
    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the point to the same as location of the point provided
     * @param p another point
     */
    public void setPoint(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * set x position of the point instantly
     * @param x require to set x position of point
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * get x position of the current point
     * @return current x position
     */
    public int getX() {
        return this.x;
    }

    /**
     * set y position of the point instantly
     * @param y require to set y position of point
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * get y position of the current point
     * @return current y position
     */
    public int getY() {
        return this.y;
    }

    /**
     * compare current point with another point to see if it's at the same coordinate
     * @param p a other point
     * @return true or false
     */
    public boolean isSamePoint(Point p) {
        if (this.x == p.getX() && this.y == p.getY()) {
            return true;
        }
        return false;
    }

}
