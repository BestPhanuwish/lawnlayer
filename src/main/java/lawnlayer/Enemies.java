package lawnlayer;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;

public class Enemies {
    
    /**
     * Array contains Enemy object inside
     */
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    /**
     * getter method for enemies array
     * @return enemies array of Enemy
     */
    public ArrayList<Enemy> getEnemiesArray() {
        return this.enemies;
    }


    /**
     * Load the enemies from JSON data to Enemy object array
     * @param app to draw enemy sprite
     * @param enemies enemies JSON array data
     * @param map put enemy on certain place on the map
     * @param spriteSize Enemy object need sprite size
     */
    public void loadEnemies(PApplet app, JSONArray enemies, Map map, int spriteSize) {
        // clear all the Enemy when load new Enemies
        this.enemies.clear();

        // Loop throught the enemies JSON array data
        for (int i=0; i<enemies.size(); i++) {

            // get the spawn point of the enemy by convert String to Point
            Point spawnPoint;
            String spawn = enemies.getJSONObject(i).getString("spawn");
            
            if (spawn.equals("random")) {
                // if the spawn is random then loop until get the dirt place on map
                while (true) {
                    Ground[][] space = map.getMapGrid();
                    Ground[] spaceRow = space[new Random().nextInt(space.length)];
                    Ground place = spaceRow[new Random().nextInt(space.length)];
                    if (place.getGroundType() == GroundType.Concrete) {
                        continue;
                    } else {
                        spawnPoint = new Point(place.getX(), place.getY());
                        break;
                    }
                }
            } else {
                // if spawn specified by coordinate [row,column] then set the point to specific row and column of the map
                String[] coorArray = spawn.split(",");
                int coorY = Integer.parseInt(coorArray[0]);
                int coorX = Integer.parseInt(coorArray[1]);
                Ground[][] space = map.getMapGrid();
                spawnPoint = new Point(space[coorY][coorX].getX(), space[coorY][coorX].getY());
            }

            // set the type of enemy and its sprite image
            int type = enemies.getJSONObject(i).getInt("type");
            EnemyType typeEnemy;
            PImage sprite;
            if (type == 0) {
                sprite = app.loadImage("src/main/resources/lawnlayer/worm.png");
                typeEnemy = EnemyType.Worm;
            } else {
                sprite = app.loadImage("src/main/resources/lawnlayer/beetle.png");
                typeEnemy = EnemyType.Beetle;
            }

            // add new Enemy object to enemies array
            this.enemies.add(new Enemy(sprite, spriteSize, spawnPoint, typeEnemy));
        }
    }

    public void update(PApplet app, Map map, Pathway path) throws Exception {
        for (Enemy enemy : this.enemies) {

            if (enemy.isGoLeft()) {
                boolean will_change_direction = false;

                Point p1 = new Point(enemy.getX(), enemy.getY());
                Point p2 = new Point(enemy.getX(), enemy.getY()+enemy.getSpriteSize());

                if (map.getGroundFromPoint(p1).isHittableGround() && map.getGroundFromPoint(p2).isHittableGround()) {
                    will_change_direction = true;
                }

                if (map.getGroundFromPoint(p1).isHittableGround() && map.getGroundFromPoint(p1).getX()+enemy.getSpriteSize() == enemy.getX()) {
                    will_change_direction = true;
                }

                if (map.getGroundFromPoint(p2).isHittableGround() && map.getGroundFromPoint(p2).getX()+enemy.getSpriteSize() == enemy.getX()) {
                    will_change_direction = true;
                }

                if (will_change_direction) {
                    enemy.changeXdirection();
                    if (map.getGroundFromPoint(p1).getGroundType() == GroundType.GreenPath) {
                        path.createPropagate(map.getGroundFromPoint(p1));
                        map.getGroundFromPoint(p1).setGroundType(GroundType.RedPath);
                        map.getGroundFromPoint(p1).setSprite(app.loadImage("src/main/resources/lawnlayer/redPath.png"));
                    } else if (map.getGroundFromPoint(p2).getGroundType() == GroundType.GreenPath) {
                        path.createPropagate(map.getGroundFromPoint(p2));
                        map.getGroundFromPoint(p2).setGroundType(GroundType.RedPath);
                        map.getGroundFromPoint(p2).setSprite(app.loadImage("src/main/resources/lawnlayer/redPath.png"));
                    }

                    // if enemy is beetle and touching grass then change grass to dirt
                    if (enemy.getType() == EnemyType.Beetle) {
                        if (map.getGroundFromPoint(p1).getGroundType() == GroundType.Grass) {
                            map.getGroundFromPoint(p1).setGroundType(GroundType.Dirt);
                            map.getGroundFromPoint(p1).setSprite(null);
                        } else if (map.getGroundFromPoint(p2).getGroundType() == GroundType.Grass) {
                            map.getGroundFromPoint(p1).setGroundType(GroundType.Dirt);
                            map.getGroundFromPoint(p1).setSprite(null);
                        }
                    }
                }
            } else {
                boolean will_change_direction = false;

                Point p1 = new Point(enemy.getX()+enemy.getSpriteSize(), enemy.getY());
                Point p2 = new Point(enemy.getX()+enemy.getSpriteSize(), enemy.getY()+enemy.getSpriteSize());

                if (map.getGroundFromPoint(p1).isHittableGround() && map.getGroundFromPoint(p2).isHittableGround()) {
                    will_change_direction = true;
                }

                if (map.getGroundFromPoint(p1).isHittableGround() && map.getGroundFromPoint(p1).getX() == enemy.getX()) {
                    will_change_direction = true;
                }

                if (map.getGroundFromPoint(p2).isHittableGround() && map.getGroundFromPoint(p2).getX() == enemy.getX()) {
                    will_change_direction = true;
                }

                if (will_change_direction) {
                    enemy.changeXdirection();
                    if (map.getGroundFromPoint(p1).getGroundType() == GroundType.GreenPath) {
                        path.createPropagate(map.getGroundFromPoint(p1));
                        map.getGroundFromPoint(p1).setGroundType(GroundType.RedPath);
                        map.getGroundFromPoint(p1).setSprite(app.loadImage("src/main/resources/lawnlayer/redPath.png"));
                    } else if (map.getGroundFromPoint(p2).getGroundType() == GroundType.GreenPath) {
                        path.createPropagate(map.getGroundFromPoint(p2));
                        map.getGroundFromPoint(p2).setGroundType(GroundType.RedPath);
                        map.getGroundFromPoint(p2).setSprite(app.loadImage("src/main/resources/lawnlayer/redPath.png"));
                    }

                    // if enemy is beetle and touching grass then change grass to dirt
                    if (enemy.getType() == EnemyType.Beetle) {
                        if (map.getGroundFromPoint(p1).getGroundType() == GroundType.Grass) {
                            map.getGroundFromPoint(p1).setGroundType(GroundType.Dirt);
                            map.getGroundFromPoint(p1).setSprite(null);
                        } else if (map.getGroundFromPoint(p2).getGroundType() == GroundType.Grass) {
                            map.getGroundFromPoint(p1).setGroundType(GroundType.Dirt);
                            map.getGroundFromPoint(p1).setSprite(null);
                        }
                    }
                }
            }

            if (enemy.isGoUp()) {
                boolean will_change_direction = false;

                Point p1 = new Point(enemy.getX(), enemy.getY());
                Point p2 = new Point(enemy.getX()+enemy.getSpriteSize(), enemy.getY());

                if (map.getGroundFromPoint(p1).isHittableGround() && map.getGroundFromPoint(p2).isHittableGround()) {
                    will_change_direction = true;
                }

                if (map.getGroundFromPoint(p1).isHittableGround() && map.getGroundFromPoint(p1).getY()+enemy.getSpriteSize() == enemy.getY()) {
                    will_change_direction = true;
                }

                if (map.getGroundFromPoint(p2).isHittableGround() && map.getGroundFromPoint(p2).getY()+enemy.getSpriteSize() == enemy.getY()) {
                    will_change_direction = true;
                }

                if (will_change_direction) {
                    enemy.changeYdirection();
                    if (map.getGroundFromPoint(p1).getGroundType() == GroundType.GreenPath) {
                        path.createPropagate(map.getGroundFromPoint(p1));
                        map.getGroundFromPoint(p1).setGroundType(GroundType.RedPath);
                        map.getGroundFromPoint(p1).setSprite(app.loadImage("src/main/resources/lawnlayer/redPath.png"));
                    } else if (map.getGroundFromPoint(p2).getGroundType() == GroundType.GreenPath) {
                        path.createPropagate(map.getGroundFromPoint(p2));
                        map.getGroundFromPoint(p2).setGroundType(GroundType.RedPath);
                        map.getGroundFromPoint(p2).setSprite(app.loadImage("src/main/resources/lawnlayer/redPath.png"));
                    }

                    // if enemy is beetle and touching grass then change grass to dirt
                    if (enemy.getType() == EnemyType.Beetle) {
                        if (map.getGroundFromPoint(p1).getGroundType() == GroundType.Grass) {
                            map.getGroundFromPoint(p1).setGroundType(GroundType.Dirt);
                            map.getGroundFromPoint(p1).setSprite(null);
                        } else if (map.getGroundFromPoint(p2).getGroundType() == GroundType.Grass) {
                            map.getGroundFromPoint(p1).setGroundType(GroundType.Dirt);
                            map.getGroundFromPoint(p1).setSprite(null);
                        }
                    }
                }
            } else {
                boolean will_change_direction = false;

                Point p1 = new Point(enemy.getX(), enemy.getY()+enemy.getSpriteSize());
                Point p2 = new Point(enemy.getX()+enemy.getSpriteSize(), enemy.getY()+enemy.getSpriteSize());

                if (map.getGroundFromPoint(p1).isHittableGround() && map.getGroundFromPoint(p2).isHittableGround()) {
                    will_change_direction = true;
                }

                if (map.getGroundFromPoint(p1).isHittableGround() && map.getGroundFromPoint(p1).getY() == enemy.getY()) {
                    will_change_direction = true;
                }

                if (map.getGroundFromPoint(p2).isHittableGround() && map.getGroundFromPoint(p2).getY() == enemy.getY()) {
                    will_change_direction = true;
                }

                if (will_change_direction) {
                    enemy.changeYdirection();
                    if (map.getGroundFromPoint(p1).getGroundType() == GroundType.GreenPath) {
                        path.createPropagate(map.getGroundFromPoint(p1));
                        map.getGroundFromPoint(p1).setGroundType(GroundType.RedPath);
                        map.getGroundFromPoint(p1).setSprite(app.loadImage("src/main/resources/lawnlayer/redPath.png"));
                    } else if (map.getGroundFromPoint(p2).getGroundType() == GroundType.GreenPath) {
                        path.createPropagate(map.getGroundFromPoint(p2));
                        map.getGroundFromPoint(p2).setGroundType(GroundType.RedPath);
                        map.getGroundFromPoint(p2).setSprite(app.loadImage("src/main/resources/lawnlayer/redPath.png"));
                    }

                    // if enemy is beetle and touching grass then change grass to dirt
                    if (enemy.getType() == EnemyType.Beetle) {
                        if (map.getGroundFromPoint(p1).getGroundType() == GroundType.Grass) {
                            map.getGroundFromPoint(p1).setGroundType(GroundType.Dirt);
                            map.getGroundFromPoint(p1).setSprite(null);
                        } else if (map.getGroundFromPoint(p2).getGroundType() == GroundType.Grass) {
                            map.getGroundFromPoint(p1).setGroundType(GroundType.Dirt);
                            map.getGroundFromPoint(p1).setSprite(null);
                        }
                    }
                }
            }

        }
    }

    /**
     * Move all the Enemy if it's not freeze
     * @param item check if enemy allow to move
     */
    public void move(PowerUp item) {
        if (item.isActive() && item.getPowerType() == PowerType.Freeze)
            return;

        for (Enemy e : this.enemies) {
            e.move();
        }
    }

    /**
     * Draw all the Enemy on to the screen
     */
    public void draw(PApplet app) {
        for (Enemy e : this.enemies) {
            e.draw(app);
        }
    }

}
