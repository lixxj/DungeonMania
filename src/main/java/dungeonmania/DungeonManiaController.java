package dungeonmania;

import dungeonmania.Player.Inventory;
import dungeonmania.Player.Player;
import dungeonmania.entity.Entity;
import dungeonmania.entity.MovingEntity;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goal.Goal;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.DMCUtil;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import dungeonmania.util.ExtractFromJson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DungeonManiaController {
    private Dungeon dungeon;

    private Map<Integer, String> dungeonStateRecorder = new HashMap<>();

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        if (!dungeons().contains(dungeonName) || !configs().contains(configName)) {
            throw new IllegalArgumentException();
        }
        
        Dungeon.clearDungeonIdCount();
        dungeon = new Dungeon(dungeonName, configName);
        return dungeon.generateDungeonResponse();
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return dungeon.generateDungeonResponse();
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        // 1. check if itemUsedId represents a item in the player's inventory [can have a method in inventory to check]
        Inventory inventory = dungeon.getPlayer().getInventory();
        Entity item = inventory.getEntity(itemUsedId);
        
        // if(item == null) throw new InvalidActionException("item not exist"); 
        if (item == null) throw new InvalidActionException("item not exist");
        
        // 2. check if the itemUsedId represents any of the "bomb", "invincibility_potion", "invisibility_potion"
        List<String> list = Arrays.asList("invincibility_potion", "invisibility_potion", "bomb");
        if (list.contains(item.getType())) {
            dungeon.playerConsume(itemUsedId);
        } else {
            throw new IllegalArgumentException("item cannot be used");
        }
        otherEntityTick();

        dungeonStateRecorder.put(dungeon.getCurrentTick() - 1, DMCUtil.dungeonToJSONString(dungeon));

        return dungeon.generateDungeonResponse();
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        // 1. Player move first
        dungeon.playerMove(dungeon.getPlayer().getPosition(), movementDirection);
        // 2. other entities move/act second
        otherEntityTick();

        // save current dungeon state
        dungeonStateRecorder.put(dungeon.getCurrentTick() - 1, DMCUtil.dungeonToJSONString(dungeon));

        return dungeon.generateDungeonResponse();
    }

    public void otherEntityTick() {
        List<Entity> copy = new ArrayList<>(dungeon.getEntities());
        for (Entity e: copy) {
            e.tick(dungeon);
        }
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        // 1. check if buildable is either bow or shield
        List<String> buildables = Arrays.asList("bow", "shield", "sceptre", "midnight_armour");
        if (!buildables.contains(buildable)) throw new IllegalArgumentException("buildable can only be bow/shield/sceptre/midnight_armour");

        // 2. check if player can build the bow/shield
        if (!dungeon.getPlayer().build(buildable, dungeon.getConfigName())) {
            throw new InvalidActionException("not enough items to build " + buildable);
        }
        
        return dungeon.generateDungeonResponse();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        // get the entity
        Entity entity = dungeon.getEntity(entityId);
        // 1. check if entityId is a valid entity id 
        if (entity == null) throw new IllegalArgumentException("entityId is not valid");

        // 2. check if entity is one of the "mercenary", "assassin", "zombie_toast_spawner"
        List<String> interactable = Arrays.asList("mercenary", "assassin", "zombie_toast_spanwer");
        if (!interactable.contains(entity.getType())) throw new InvalidActionException("cannot interact with " + entity.getType());
        
        // get player
        Player player = dungeon.getPlayer();
        switch(entity.getType()) {
            case "zombie_toast_spawner":
                // interact with the spawner, destroy it if player has sword
                if (!player.getInventory().hasItem("sword")) throw new InvalidActionException("player does not have sword to destroy zombie toast spawner");
                dungeon.removeEntity(entity);
                break;
            default: // case "mercenary" and "assassin"
                if (!player.bribe((MovingEntity) entity)) throw new InvalidActionException("can not bribe " + entity.getType());
        }

        return dungeon.generateDungeonResponse();   
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        String dungeonJson = DMCUtil.dungeonToJSONString(dungeon);
        
        /*try {
            String path = "/games" + name + ".json";
            FileWriter file = new FileWriter(path);
            file.write(dungeonJson);
            file.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }*/

        FileWriter filewriter;
        try {
            File file = new File("src" + File.separator + "main" + File.separator + "java" + File.separator + "dungeonmania" + File.separator + "saves" + File.separator + name + ".json");
            file.setWritable(true);
            file.setReadable(true);
            file.createNewFile();
            filewriter = new FileWriter(file);
            file.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }

        try {
            filewriter.write(dungeonJson);
            filewriter.close();

        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }

        return dungeon.generateDungeonResponse();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        List<String> allGames = this.allGames();
        boolean hasGame = allGames.stream().anyMatch(g -> g.equals(name));

        if (hasGame) { 
            JsonObject savedDungeon = DMCUtil.readSavedDungeonJsonFIle(name);
            String dungeonName = savedDungeon.get("name").getAsString();
            String configName = savedDungeon.get("config").getAsString();
            dungeon = new Dungeon(dungeonName, configName);

            int currentTick = savedDungeon.get("tick").getAsInt();
            dungeon.setCurrentTick(currentTick);

            int nEnemiesKilled = savedDungeon.get("nEnemiesKilled").getAsInt();
            dungeon.setNEnemiesKilled(nEnemiesKilled);

            JsonObject goalJsonObject = savedDungeon.get("goal-condition").getAsJsonObject();
            Goal goal = ExtractFromJson.JsonToGoal(goalJsonObject, configName);
            dungeon.setGoal(goal);

            JsonObject entitiesJsonObject = savedDungeon.get("entities").getAsJsonObject();
            String entitiesString = entitiesJsonObject.toString();
            dungeon.setEntities(ExtractFromJson.getEntityFromJsonHelper(entitiesString, configName));

            return dungeon.generateDungeonResponse();
        } 
        else {
            throw new IllegalArgumentException("id is not a valid game name");
        }
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        List<String> allGames = new ArrayList<>();
        
        try { // return all the files in the directory
            allGames.addAll(DMCUtil.getFileNameListFromDirectory("/games"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return allGames;
    }

    public DungeonResponse rewind(int ticks) {
        if (ticks <= 0) throw new IllegalArgumentException("Ticks is <= 0");

        int rewindTickN = this.dungeon.getCurrentTick() - ticks - 1;
        if (rewindTickN <= 0) throw new IllegalArgumentException("The number of ticks have not occurred yet");

        // read from dungeonStateRecorder
        String rewindJsonString = dungeonStateRecorder.get(rewindTickN);
        JsonObject rewindJson = JsonParser.parseString(rewindJsonString).getAsJsonObject();

        String dungeonName = this.dungeon.getDungeonName();
        String configName = this.dungeon.getConfigName();

        this.dungeon = new Dungeon(dungeonName, configName);

        // modify current dungeon
        JsonObject goalJsonObject = rewindJson.get("goal-condition").getAsJsonObject();
        Goal goal = ExtractFromJson.JsonToGoal(goalJsonObject, configName);
        dungeon.setGoal(goal);

        JsonObject entitiesJsonObject = rewindJson.get("entities").getAsJsonObject();
        String entitiesString = entitiesJsonObject.toString();
        dungeon.setEntities(ExtractFromJson.getEntityFromJsonHelper(entitiesString, configName));

        // set tick for new dungeon state
        this.dungeon.setCurrentTick(rewindTickN);

        // reset dungeonStateRecorder
        for (int i = rewindTickN + 1; i < dungeonStateRecorder.size(); i++) dungeonStateRecorder.remove(i);

        return getDungeonResponseModel();
    }

    // dungeon builder
    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String configName)  throws IllegalArgumentException {
        if (!configs().contains(configName)) {
            throw new IllegalArgumentException();
        }
        int xlength = xEnd - xStart + 3;
        int ylength = yEnd - yStart + 3;
        // true represent empty space, false represent wall, initially all wall
        boolean[][] maze = new boolean[xlength][ylength];
        // let start point to be a empty space
        maze[1][1] = true;
        // add all neighbours of distance 2 of start not on boundary that are of distance 2 away and are walls
        List<Position> options = DMCUtil.getNeighbours(maze, new Position(1, 1), false);

        while (!options.isEmpty()) {
            // let next = remove random from options
            int rand = (int) (Math.random() * options.size());
            Position next = options.get(rand);
            options.remove(rand);
            
            // let neighbours = each neighbour of distance 2 from next not on boundary that are empty
            List<Position> neighbours = DMCUtil.getNeighbours(maze, next, true);
            if (!neighbours.isEmpty()) {
                rand = (int) (Math.random() * neighbours.size());
                Position neighbour = neighbours.get(rand);
                Position between = DMCUtil.getBetweenPosition(next, neighbour);
                maze[next.getX()][next.getY()] = true;
                maze[between.getX()][between.getY()] = true;
                maze[neighbour.getX()][neighbour.getY()] = true;
            }
            //  add to options all neighbours of 'next' not on boundary that are of distance 2 away and are walls
            List<Position> nextNeighbours = DMCUtil.getNeighbours(maze, next, false);
            options.addAll(nextNeighbours);
        }
        // try connect the end with the path
        DMCUtil.endNeighbour(maze);
        dungeon = DMCUtil.dungeonBuilder(maze, configName);
        return dungeon.generateDungeonResponse();
    }
}
