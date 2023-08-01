package lawnlayer;

import processing.data.JSONArray;

public class Level {
    
    /**
     * The String file of map outlay for the level
     */
    private String outlay;

    /**
     * The progress goal of level
     */
    private int goal;

    /**
     * The JSON array contains enemies data for the level
     */
    private JSONArray enemies;

    /**
     * Create Level class that contains important information of Level instead of load JSON multiple times
     * @param outlay
     * @param goal
     * @param enemies
     */
    public Level(String outlay, Double goal, JSONArray enemies) {
        this.outlay = outlay;
        this.goal = (int) (goal*100);
        this.enemies = enemies;
    }

    /**
     * setter method for map outlay String
     * @param outlay
     */
    public void setOutlay(String outlay) {
        this.outlay = outlay;
    }

    /**
     * getter method for map outlay String
     * @return outlay String
     */
    public String getOutlay() {
        return this.outlay;
    }

    /**
     * setter method for level's progress goal
     * @param goal
     */
    public void setGoal(int goal) {
        this.goal = goal;
    }

    /**
     * getter method for level goal
     * @return level's progress goal
     */
    public int getGoal() {
        return this.goal;
    }

    /**
     * setter method for enemies JSON data
     * @param enemies
     */
    public void setEnemies(JSONArray enemies) {
        this.enemies = enemies;
    }

    /**
     * getter method for enemies JSON data
     * @return enemies JSON data
     */
    public JSONArray getEnemies() {
        return this.enemies;
    }

}
