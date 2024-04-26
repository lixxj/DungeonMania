package dungeonmania.goal;

import java.util.List;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.FloorSwitch;

public class BouldersGoal implements Goal {
    private final String name = "boulders";

    @Override
    public String getGoalName() {
        return this.name;
    }

    @Override
    public String getGoalNameResponse() {
        return ":boulders";
    }

    @Override
    public boolean getGoalStatus(Dungeon dungeon) {
        List<Entity> entities = dungeon.getEntities();

        for (Entity entity: entities) if (entity instanceof FloorSwitch && !((FloorSwitch) entity).isFloorSwitchOn()) return false;

        return true;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject goalJson = new JSONObject();
        goalJson.put("goal", getGoalName());
        
        return goalJson;
    }
}
