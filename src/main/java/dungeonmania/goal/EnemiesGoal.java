package dungeonmania.goal;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.ZombieToastSpawner;
import dungeonmania.util.ExtractFromJson;

public class EnemiesGoal implements Goal {
    private final String name = "enemies";
    private int N_Enemies = 0;

    public EnemiesGoal(String configName) {
        this.N_Enemies = ExtractFromJson.getConfigIntFromJson(configName, "enemy_goal");;
    }

    @Override
    public String getGoalName() {
        return this.name;
    }

    public int getN_Enemies() {
        return this.N_Enemies;
    }

    @Override
    public String getGoalNameResponse() {
        return ":enemies";
    }

    @Override
    public boolean getGoalStatus(Dungeon dungeon) {
        
        // check all spawners have been destroyed
        for (Entity entity: dungeon.getEntitiesNonStatic()) {
            if (entity instanceof ZombieToastSpawner) return false;
        }

        // check a certain number of enemies have been destroyed
        if (dungeon.getNEnemiesKilled() >= getN_Enemies()) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject goalJson = new JSONObject();
        goalJson.put("goal", getGoalName());
        return goalJson;
    }
}
