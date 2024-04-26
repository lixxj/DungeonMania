package dungeonmania.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dungeonmania.CollectableEntity.Arrow;
import dungeonmania.CollectableEntity.Bomb;
import dungeonmania.CollectableEntity.InvincibilityPotion;
import dungeonmania.CollectableEntity.InvisibilityPotion;
import dungeonmania.CollectableEntity.Key;
import dungeonmania.CollectableEntity.SunStone;
import dungeonmania.CollectableEntity.Sword;
import dungeonmania.CollectableEntity.Treasure;
import dungeonmania.CollectableEntity.Wood;
import dungeonmania.entity.Assassin;
import dungeonmania.entity.Boulder;
import dungeonmania.entity.Door;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Exit;
import dungeonmania.entity.FloorSwitch;
import dungeonmania.entity.Hydra;
import dungeonmania.entity.Mercenary;
import dungeonmania.Player.Player;
import dungeonmania.entity.Portal;
import dungeonmania.entity.Spider;
import dungeonmania.entity.Wall;
import dungeonmania.entity.ZombieToast;
import dungeonmania.entity.ZombieToastSpawner;
import dungeonmania.goal.AndGoal;
import dungeonmania.goal.BouldersGoal;
import dungeonmania.goal.EnemiesGoal;
import dungeonmania.goal.ExitGoal;
import dungeonmania.goal.Goal;
import dungeonmania.goal.OrGoal;
import dungeonmania.goal.TreasureGoal;
import dungeonmania.response.models.EntityResponse;

public class ExtractFromJson {

    public static List<Entity> getEntityFromJson(String dungeonName, String configName) {
        String fileContent;
        try {
            fileContent = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        return getEntityFromJsonHelper(fileContent, configName);
    }

    public static List<Entity> getEntityFromJsonHelper(String fileContent, String configName) {
        JSONObject obj = new JSONObject(fileContent);
        JSONArray ary = obj.getJSONArray("entities");
        List<Entity> entities = new ArrayList<>();
        Player playerSlot = null;
        double zombie_health = getConfigFromJson(configName, "zombie_health");
        double zombie_attack = getConfigFromJson(configName, "zombie_attack");
        double spider_health = getConfigFromJson(configName, "spider_health");
        double spider_attack = getConfigFromJson(configName, "spider_attack");
        double spider_spawn_rate = getConfigFromJson(configName, "spider_spawn_rate");
        for (int i = 0; i < ary.length(); i++) {
            String type = ary.getJSONObject(i).getString("type");
            int x = Integer.valueOf(ary.getJSONObject(i).getInt("x"));
            int y = Integer.valueOf(ary.getJSONObject(i).getInt("y"));
            Position pos = new Position(x, y);
            switch(type) {
                case "player":
                    double health = getConfigFromJson(configName, "player_health");
                    double attackDamage = getConfigFromJson(configName, "player_attack");
                    playerSlot = new Player(pos, type, health, attackDamage);
                    entities.add(playerSlot);
                    break;
                case "sun_stone":
                    entities.add(new SunStone(pos, type));
                    break;
                case "hydra":
                    double hydra_attack = getConfigFromJson(configName, "hydra_attack");
                    double hydra_health = getConfigFromJson(configName, "hydra_health");
                    double hydra_health_increase_rate = getConfigFromJson(configName, "hydra_health_increase_rate");
                    double hydra_health_increase_amount = getConfigFromJson(configName, "hydra_health_increase_amount");    
                    entities.add(new Hydra(pos, type, hydra_health, hydra_attack, hydra_health_increase_rate, hydra_health_increase_amount));
                    break;
                case "wall":
                    entities.add(new Wall(pos));
                    break;
                case "spider":
                    entities.add(new Spider(pos, "spider", spider_health, spider_attack, spider_spawn_rate));
                    break;
                case "exit":
                    entities.add(new Exit(pos));
                    break;
                case "boulder":
                    entities.add(new Boulder(pos));
                    break;
                case "switch":
                    entities.add(new FloorSwitch(pos));
                    break;
                case "door":
                    int key = Integer.valueOf(ary.getJSONObject(i).getInt("key"));
                    entities.add(new Door(pos, key));
                    break;
                case "portal":
                    String color = ary.getJSONObject(i).getString("colour");
                    entities.add(new Portal(pos, color));
                    break;
                case "zombie_toast_spawner":
                    double zombie_spawn_rate = getConfigFromJson(configName, "zombie_spawn_rate");
                    entities.add(new ZombieToastSpawner(pos, zombie_spawn_rate, zombie_health, zombie_attack));
                    break;
                // Collectable entities
                case "arrow":
                    entities.add(new Arrow(pos, type));
                    break;
                case "mercenary":
                    double mercenary_health = getConfigFromJson(configName, "mercenary_health");
                    double mercenary_attack = getConfigFromJson(configName, "mercenary_attack");
                    double bribe_amount = getConfigFromJson(configName, "bribe_amount");
                    double bribe_radius = getConfigFromJson(configName, "bribe_radius");
                    double attackBonus = getConfigFromJson(configName, "ally_attack");
                    double defenceBonus = getConfigFromJson(configName, "ally_defence");
                    entities.add(new Mercenary(pos, mercenary_health, mercenary_attack, bribe_amount, bribe_radius, attackBonus, defenceBonus));
                    break;
                case "bomb":
                    double bombRadius = getConfigFromJson(configName, "bomb_radius");
                    entities.add(new Bomb(pos, type, bombRadius));
                    break;
                case "invincibility_potion":
                    double invincibilityDuration = getConfigFromJson(configName, "invincibility_potion_duration");
                    entities.add(new InvincibilityPotion(pos, type, invincibilityDuration));
                    break;
                case "invisibility_potion":
                    double invisibilityDuration = getConfigFromJson(configName, "invisibility_potion_duration");
                    entities.add(new InvisibilityPotion(pos, type,  invisibilityDuration));
                    break;
                case "key":
                    int keyId = Integer.valueOf(ary.getJSONObject(i).getInt("key"));
                    entities.add(new Key(pos, type, keyId));
                    break;
                case "sword":
                    double sword_attack = getConfigFromJson(configName, "sword_attack");
                    double sword_durability = getConfigFromJson(configName, "sword_durability");
                    entities.add(new Sword(pos, type, sword_attack, sword_durability));
                    break;
                case "treasure":
                    entities.add(new Treasure(pos, type));
                    break;
                case "wood":
                    entities.add(new Wood(pos, type));
                    break;
                case "zombie_toast":
                    entities.add(new ZombieToast(pos, type, zombie_health, zombie_attack));
                    break;
                case "assassin":
                    double assassinDamage = getConfigFromJson(configName, "assassin_attack");
                    double assassinHealth = getConfigFromJson(configName, "assassin_health");
                    double aassassinBribeAmount = getConfigFromJson(configName, "assassin_bribe_amount");
                    double assassinBribeFailRate = getConfigFromJson(configName, "assassin_bribe_fail_rate");
                    double assassinReconRadius = getConfigFromJson(configName, "assassin_recon_radius");
                    double assassinBribeRadius = getConfigFromJson(configName, "bribe_radius");
                    double assassinAttackBonus = getConfigFromJson(configName, "ally_attack");
                    double assassinDefenceBonus = getConfigFromJson(configName, "ally_defence");
                    entities.add(new Assassin(pos, "assassin", assassinHealth, assassinDamage, aassassinBribeAmount, assassinBribeFailRate, assassinReconRadius, assassinBribeRadius, assassinAttackBonus, assassinDefenceBonus));
                    break;
                default:
                    entities.add(new Entity(pos, type));
            }
        }
        return entities;
    }

    public static List<EntityResponse> getResponsesFromEntities(List<Entity> entities) {
        List<EntityResponse> erList = new ArrayList<>();
        for (Entity e : entities) {
            EntityResponse er = new EntityResponse(e.getId(), e.getType(), e.getPosition(), e.isInteractable());
            erList.add(er);
        }
        return erList;
    }

    /***
     * @pre valid config file name and a valid key string that is contained in the config file
     * @post the value of the provided key in double will be given
     * @param configName congfig file name
     * @param key key contained in the file
     * @return value of the provided key in double
     */
    public static double getConfigFromJson(String configName, String key) {
        String fileContent;
        try {
            fileContent = FileLoader.loadResourceFile("/configs/" + configName + ".json");
        } catch (IOException e) {
            throw new IllegalArgumentException("config file: " + configName + " not exist");
        } 
        JSONObject obj = new JSONObject(fileContent);
        return obj.getDouble(key);
    }

    /***
     * @pre valid config file name and a valid key string that is contained in the config file
     * @post the value of the provided key in double will be given
     * @param configName congfig file name
     * @param key key contained in the file
     * @return value of the provided key in int
     */
    public static int getConfigIntFromJson(String configName, String key) {
        String fileContent;
        try {
            fileContent = FileLoader.loadResourceFile("/configs/" + configName + ".json");
        } catch (IOException e) {
            throw new IllegalArgumentException("config file: " + configName + " not exist");
        } 
        JSONObject obj = new JSONObject(fileContent);
        return obj.getInt(key);
    }

    public static Goal getGoalFromDungeonName(String dungeonName, String configName) {
        JsonObject dungeonJsonObject = dungeonNameToJson(dungeonName);
        JsonObject goalJsonObject = dungeonJsonObject.get("goal-condition").getAsJsonObject();
        
        return JsonToGoal(goalJsonObject, configName);
    }
    
    public static JsonObject dungeonNameToJson(String name) {
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + name + ".json");
            
            return JsonParser.parseString(file).getAsJsonObject();
        } 
        catch (Exception e1) {
            try {
                return JsonParser.parseReader(new FileReader("src/test/resources/dungeons/" + name + ".json")).getAsJsonObject();
            } 
            catch (Exception e2) {
                try {
                    return JsonParser.parseReader(new FileReader("src/main/resources/dungeons/" + name + ".json")).getAsJsonObject();
                } 
                catch (Exception e3) {
                    throw new IllegalArgumentException("Invalid dungeon name");
                }
            }
        }
    }

    private static Goal basicGoal(JsonObject jsonObject, String configName) {
        switch (jsonObject.get("goal").getAsString()) {
            default:
                return null;

            case "boulders":
                return new BouldersGoal();

            case "enemies":
                return new EnemiesGoal(configName);
            
            case "exit":
                return new ExitGoal();
            
            case "treasure":
                return new TreasureGoal(configName);
            
        }
    }

    public static Goal JsonToGoal(JsonObject goalJsonObject, String configName) {

        if (goalJsonObject.has("goal")) {
            if (!goalJsonObject.has("subgoals")) return basicGoal(goalJsonObject, configName);

            if (goalJsonObject.has("subgoals")) {
                JsonArray jsonSubGoals = goalJsonObject.get("subgoals").getAsJsonArray();
                
                Goal subGoal1 = JsonToGoal(jsonSubGoals.get(0).getAsJsonObject(), configName);
                Goal subGoal2 = JsonToGoal(jsonSubGoals.get(1).getAsJsonObject(), configName);
                
                String condition = goalJsonObject.get("goal").getAsString();
                
                if (condition.equals("AND")) return new AndGoal(subGoal1, subGoal2);
                if (condition.equals("OR")) return new OrGoal(subGoal1, subGoal2);
            }
        }
        return null;
    }


}
