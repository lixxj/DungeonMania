package dungeonmania.goal;

import java.util.List;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;

public class ExitGoal implements Goal {
    private final String name = "exit";

    @Override
    public String getGoalName() {
        return this.name;
    }

    @Override
    public String getGoalNameResponse() {
        return ":exit";
    }

    @Override
    public boolean getGoalStatus(Dungeon dungeon) {
        List<Entity> entities = dungeon.getEntitiesNonStatic();

        Entity player = entities.stream().filter(entity -> entity.getType().equals("player")).findFirst().orElse(null);
        
        if (player == null) return false; // player is dead

        Entity exit = entities.stream().filter(entity -> entity.getType().equals("exit")).findFirst().orElse(null);

        if (exit == null) return true; // no exit

        return exit.getPosition().equals(player.getPosition());
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject goalJson = new JSONObject();
        goalJson.put("goal", getGoalName());
        
        return goalJson;
    }
}
