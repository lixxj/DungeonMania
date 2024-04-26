package dungeonmania.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dungeonmania.Dungeon;
import dungeonmania.Player.Player;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Exit;
import dungeonmania.entity.Wall;

public class DMCUtil {
    /**
     * helper function for loadGame
     */
    public static JsonObject readSavedDungeonJsonFIle(String name) {
        try {
            return JsonParser.parseReader(new FileReader("src" + File.separator + "main" + File.separator + "java" + File.separator + "dungeonmania" + File.separator + "saves" + File.separator + name + ".json")).getAsJsonObject();
        } catch (Exception e1) {
            throw new IllegalArgumentException("Invalid dungeon name");
        }
    }

    /**
     * helper function for allGames
     * lists file names within a specified non-resource directory
     */
    public static List<String> getFileNameListFromDirectory(String directory) throws IOException {
        Path root = Paths.get(directory);

        return Files.walk(root).filter(Files::isRegularFile).map(x -> {
            String nameAndExt = x.toFile().getName();
            int extIndex = nameAndExt.lastIndexOf('.');
            return nameAndExt.substring(0, extIndex > -1 ? extIndex : nameAndExt.length());
        }).collect(Collectors.toList());
    }

    /**
     * helper function for saveGame
     * convert a dungeon object -> Json -> String
     */
    public static String dungeonToJSONString(Dungeon dungeon) {

        JSONObject dungeonJson = new JSONObject();

        dungeonJson.put("id", dungeon.getDungeonId());
        dungeonJson.put("name", dungeon.getDungeonName());

        if (dungeon.getGoal() != null) dungeonJson.put("goal-condition", dungeon.getGoal().getJSONObject());

        JSONArray jsonEntities = new JSONArray();
        List<Entity> entities = dungeon.getEntities();
        for (Entity entity : entities) jsonEntities.put(entity.getEntityStateAsJSON());
        dungeonJson.put("entities", jsonEntities);

        //TODO maybe this line is not necessary because entities above contain player
        //TODO but I'm unsure, how other parts are implemented matter so we'll see later - XJ
        //dungeonJson.put("player", dungeon.getPlayer().getEntityStateAsJSON());

        dungeonJson.put("nEnemiesKilled", dungeon.getNEnemiesKilled());
        dungeonJson.put("tick", dungeon.getCurrentTick());
        dungeonJson.put("config", dungeon.getConfigName());
        
        return dungeonJson.toString();
    }

    public static void endNeighbour (boolean[][] maze) {
        int rand = (int) (Math.random() * 2);
        int xlength = maze.length;
        int ylength = maze[0].length;
        if (rand == 0) {
            maze[xlength - 2][ylength - 3] = true;
        } else {
            maze[xlength - 3][ylength - 2] = true;
        }
    }

    // base on the boolean array maze, return apporpriate dungeon 
    public static Dungeon dungeonBuilder(boolean[][] maze, String configName) {
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                // if at start point, add player
                if (i == 1 && j == 1) {
                    Player player = new Player(new Position(i, j), "player", 100, 15);
                    entities.add(player);
                // if at end point, add exit
                } else if (i == maze.length - 2 && j == maze[0].length - 2) {
                    Exit exit = new Exit(new Position(i, j));
                    entities.add(exit);
                } else {
                    // if false, add wall
                    if (!maze[i][j]) {
                        Wall wall = new Wall(new Position(i, j));
                        entities.add(wall);
                    }
                }
            }
        }

        Dungeon dungeon = new Dungeon(entities, configName);
        return dungeon;
    }

    public static Position getBetweenPosition(Position p1, Position p2) {
        int x = (p1.getX() + p2.getX()) / 2;
        int y = (p1.getY() + p2.getY()) / 2;
        return new Position(x, y);
    }

    // get neighbours that are walls (false) or empty (true)
    public static List<Position> getNeighbours(boolean[][] maze, Position next, Boolean empty) {
        List<Position> neighbours = new ArrayList<>();
        int x = next.getX();
        int y = next.getY();
        if (x <= 0 || x >= maze.length || y <= 0 || y >= maze[0].length) {
            return neighbours;
        }
        // for up neighbour
        if (y - 2 >= 1) {
            if (maze[x][y - 2] == empty) {
                neighbours.add(new Position(x, y - 2));
            }
        }
        // for left neighbour
        if (x - 2 >= 1) {
            if (maze[x - 2][y] == empty) {
                neighbours.add(new Position(x - 2, y));
            }
        }
        // for right neighbour
        if (x + 2 < maze.length - 1) {
            if (maze[x + 2][y] == empty) {
                neighbours.add(new Position(x + 2, y));
            }
        }
        // for down neighbour
        if (y + 2 < maze[0].length - 1) {
            if (maze[x][y + 2] == empty) {
                neighbours.add(new Position(x, y + 2));
            }
        }
        return neighbours;
    }
}
