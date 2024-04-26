package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Player.Player;
import dungeonmania.entity.Boulder;
import dungeonmania.entity.Door;
import dungeonmania.entity.Entity;
import dungeonmania.entity.FloorSwitch;
import dungeonmania.entity.MovingEntity;
import dungeonmania.entity.Portal;
import dungeonmania.goal.ExitGoal;
import dungeonmania.goal.Goal;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.ExtractFromJson;
import dungeonmania.util.Position;

public class Dungeon {
    private String dungeonId;
    private static int dungeonIdCount = 0;
    private String dungeonName;
    private Goal goal;
    private List<Entity> entities;
    private Player player;
    private int nEnemiesKilled = 0; 
    private int currentTick;
    private String configName;
    private List<BattleResponse> battles = new ArrayList<BattleResponse>();
    
    public Dungeon (List<Entity> entities, String configName) {
        Entity.clearIdCount();
        dungeonIdCount++;
        this.entities = entities;
        this.goal = new ExitGoal(); 
        this.dungeonId = String.valueOf(dungeonIdCount);
        this.player = getPlayerFromEntity(entities);
        this.configName = configName;
    }

    public Dungeon(String dungeonName, String configName) {
        Entity.clearIdCount();
        clearEntities();
        this.entities = ExtractFromJson.getEntityFromJson(dungeonName, configName);
        this.goal = ExtractFromJson.getGoalFromDungeonName(dungeonName, configName);
        dungeonIdCount++;
        this.dungeonId = String.valueOf(dungeonIdCount);
        this.dungeonName = dungeonName;
        this.player = getPlayerFromEntity(entities);
        this.configName = configName;
    }

    public String getConfigName() {
        return this.configName;
    }

    public void move(Direction movementDirection) {
        // 1. player move first
        playerMove(player.getPosition(), movementDirection);
        // 2. then enemies move 
        
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void setNEnemiesKilled(int nEnemiesKilled) {
        this.nEnemiesKilled = nEnemiesKilled;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public void clearEntities() {
        if (entities !=null) {
            entities.removeAll(entities);
        }
    }

    public Player getPlayerFromEntity(List<Entity> entities) {
        for (Entity e : entities) {
            if (e.getType().equals("player")) {
                return (Player)e;
            }
        }
        return null;
    }

    public void playerMove(Position p, Direction d) {
        Position newPos = p.translateBy(d);
        //List<Entity> entityList = getEntityList(newPos);
        Entity e = getEntity(newPos);
        if (e == null) {
            player.move(d);
            return;
        }

        // the next position has a collectable entity, play move first then collect it 
        if (e.isCollectable()) {
            player.move(d);
            player.collect(e, this);
            return;
        }

        // the next position has a static entity, perform different actions accordingly
        String type = e.getType();
        if (e.isStatic()) {
            switch(type) {
                case "wall":
                    // can't move onto a wall
                    break;
                case "boulder":
                    Boulder boulder = (Boulder) e;
                    // check if boulder can be pushed
                    if (boulder.push(d, this)) {
                        player.move(d);
                    }
                    break;
                case "door":
                    Door door = (Door) e;
                    if (door.isCrossAble()) { // door is opened
                        player.move(d);
                    } else if (door.open(player.getInventory().getKeyValue())) { 
                        // open is not opened, if the current key match the door, open the door 
                        player.move(d);
                    }
                    break;
                case "portal":
                    Portal portal = (Portal)e;
                    player.setPosition(portal.teleport(this, d));
                    break;
                case "switch":
                    FloorSwitch floorSwitch = (FloorSwitch)e;
                    if (floorSwitch.isFloorSwitchOn()) {
                        Entity e2 = getEntity(newPos, "boulder");
                        Boulder boulder2 = (Boulder)e2;
                        if (boulder2.push(d, this)) {
                            player.move(d);
                        }
                    } else {
                        player.move(d);
                    }
                    break;
                default: // this is case "exit", "zombie_toast_spawner" and "switch"
                    player.move(d);
                    break;
            }
        }

        // the next position has a moving entity, this can be ally or enemy
        if (e.isMovingEntity() && ((MovingEntity) e).isEnemy() && !player.isInvisible()) {
            battle(player, (MovingEntity) e);
        }
    }

    public void battle(Player player, MovingEntity enemy) {
        // keep track of initial health
        double initialEnemyHealth = enemy.getHealth();
        double initialPlayerHealth = player.getHealth();

        // record rounds
        List<RoundResponse> rounds = new ArrayList<RoundResponse>();
        while (enemy.getHealth() > 0 && player.getHealth() > 0) {
            // health before fight
            double beforePlayerHealth = player.getHealth();
            double beforeEnemyHealth = enemy.getHealth();
            // attacks
            List<ItemResponse> weaponryUsed =  player.attack(enemy);
            if (enemy.getHealth() != 0) {
                enemy.attack(player);
            }
            // health difference
            double deltaPlayerHealth = player.getHealth() - beforePlayerHealth;
            double deltaEnemyHealth = enemy.getHealth() - beforeEnemyHealth;
            rounds.add(new RoundResponse(deltaPlayerHealth, deltaEnemyHealth, weaponryUsed));
        }
        // generate battleResponse
        battles.add(new BattleResponse(enemy.getType(), rounds, initialPlayerHealth, initialEnemyHealth));
        
        // enemy is killed 
        if (enemy.getHealth() <= 0) {
            incrementNEnemiesKilled();
            synchronized(getEntities()) {
                entities.remove(enemy);
            }
        }
        // player is killed
        if (player.getHealth() <= 0) entities.remove(player); 
    }


    public Player getPlayer() {
        return player;
    }

    public int getNEnemiesKilled() {
        return nEnemiesKilled;
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    public void incrementNEnemiesKilled() {
        nEnemiesKilled++;
    }

    // check if two entity are in the same position
    public boolean isSamePos(Entity e1, Entity e2) {
        return e1.getPosition().equals(e2.getPosition());
    }

    // get entities of this position 
    public List<Entity> getEntityList(Position pos) {
        List<Entity> entityList = new ArrayList<Entity>();
        for (Entity e : entities) {
            if (e.getPosition().equals(pos)) {
                entityList.add(e);
            }
        }
        return entityList;
    }

    public Entity getEntity(Position p) {
        for (Entity e : entities) {
            if (e.getPosition().equals(p)) {
                return e;
            }
        }
        return null;
    }

    public Entity getEntity(Position p, String type) {
        for (Entity e : entities) {
            if (e.getPosition().equals(p) && e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

    public Entity getEntity(String id) {
        for (Entity e : entities) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public void removeEntity(Position p) {
        for (Entity e : entities) {
            if (e.getPosition().equals(p) && !e.getType().equals("player") ) {
                entities.remove(e);
                return;
            }
        }
    }

    public boolean hasEntityType(String type) {
        for (Entity e : entities) {
            if (e.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static void clearDungeonIdCount() {
        dungeonIdCount = 0;
    }

    public String getDungeonId() {
        return this.dungeonId;
    }

    public String getDungeonName() {
        return this.dungeonName;
    }

    public synchronized List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntitiesNonStatic() {
        return this.entities;
    }

    public Goal getGoal() {
        return this.goal;
    }

    public String getGoalString() { // get the Goal as a String for DungeonResponse
        // An empty string denotes the game has been won
        if (this.getGoal() == null || this.getGoal().getGoalStatus(this)) return ""; 
        return this.getGoal().getGoalNameResponse();
    }

    public void playerConsume(String itemUsedId) {
        // player consume
        player.consume(itemUsedId, this);
    }

    public DungeonResponse generateDungeonResponse() {
        return new DungeonResponse(this.dungeonId, this.dungeonName, ExtractFromJson.getResponsesFromEntities(entities), player.getInventory().generateItemResponse(), battles, player.getInventory().getBuildables(), getGoalString());
    }
}

