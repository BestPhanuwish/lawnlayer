package lawnlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import processing.data.JSONObject;
import processing.core.PApplet;
import processing.data.JSONArray;

public class LevelTest {

    JSONArray gameLevelDetails;
    Level level;
    
    @BeforeEach
    public void setUp() {
        // set up app so it's not crash while testing
        PApplet app = new PApplet();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);

        // get real level details from config file (so this file it need to be existed in the game otherwise all the test will fail)
        JSONObject json = app.loadJSONObject("config.json");
        this.gameLevelDetails = json.getJSONArray("levels");

        /**
         * To get the object in JSON array
         * 
         * gameLevelDetails.getJSONObject(index)
         * 
         * after that there're different method to get value inside an object such as
         * 
         * - .getString("outlay")
         * - .getDouble("goal")
         * - .getJSONArray("enemies")
         */

         // By create Level object here it'll be more easier to test the further method
         this.level = new Level(
            gameLevelDetails.getJSONObject(0).getString("outlay"),
            gameLevelDetails.getJSONObject(0).getDouble("goal"),
            gameLevelDetails.getJSONObject(0).getJSONArray("enemies")
        );
    }

    @Test
    @DisplayName("Simple Level setup test")
    public void simpleLevel() {
        assertNotNull(this.level);
    }

    @Test
    @DisplayName("Getter method for level outlay name")
    public void getterOutlay() {
        assertEquals("level1.txt", this.level.getOutlay());
    }

    @Test
    @DisplayName("Setter method for level outlay name")
    public void setterOutlay() {
        this.level.setOutlay(gameLevelDetails.getJSONObject(1).getString("outlay"));
        assertNotEquals("level1.txt", this.level.getOutlay());
        assertEquals("level2.txt", this.level.getOutlay());
    }

    @Test
    @DisplayName("Getter method for level progress goal")
    public void getterGoal() {
        assertEquals(80, this.level.getGoal());
    }

    @Test
    @DisplayName("Setter method for level progress goal")
    public void setterGoal() {
        this.level.setGoal(50);
        assertNotEquals(80, this.level.getGoal());
        assertEquals(50, this.level.getGoal());
    }

    @Test
    @DisplayName("Getter method for level enemies data")
    public void getterEnemies() {
        assertEquals(gameLevelDetails.getJSONObject(0).getJSONArray("enemies"), this.level.getEnemies());
    }

    @Test
    @DisplayName("Setter method for level enemies data")
    public void setterEnemies() {
        this.level.setEnemies(gameLevelDetails.getJSONObject(1).getJSONArray("enemies"));;
        assertNotEquals(gameLevelDetails.getJSONObject(0).getJSONArray("enemies"), this.level.getEnemies());
        assertEquals(gameLevelDetails.getJSONObject(1).getJSONArray("enemies"), this.level.getEnemies());
    }

}
