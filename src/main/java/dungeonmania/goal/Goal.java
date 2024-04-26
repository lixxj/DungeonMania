package dungeonmania.goal;

import org.json.JSONObject;

import dungeonmania.Dungeon;

public interface Goal {
    public String getGoalName();

    public String getGoalNameResponse();

    public boolean getGoalStatus(Dungeon dungeon);

    public JSONObject getJSONObject();
}
