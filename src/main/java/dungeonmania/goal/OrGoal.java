package dungeonmania.goal;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Dungeon;

public class OrGoal implements Goal {
    private final Goal subgoal1;
    private final Goal subgoal2;

    public OrGoal(Goal subgoal1, Goal subgoal2) {
        this.subgoal1 = subgoal1;
        this.subgoal2 = subgoal2;
    }

    @Override
    public String getGoalName() {
        return subgoal1.getGoalName() + " OR " + subgoal2.getGoalName();
    }

    @Override
    public String getGoalNameResponse() {
        return subgoal1.getGoalNameResponse() + " OR " + subgoal2.getGoalNameResponse();
    }

    @Override
    public boolean getGoalStatus(Dungeon dungeon) {
        return subgoal1.getGoalStatus(dungeon) || subgoal2.getGoalStatus(dungeon);
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject goal = new JSONObject();
        goal.put("goal", "OR");
        
        JSONArray subgoals = new JSONArray();
        subgoals.put(subgoal1.getJSONObject());
        subgoals.put(subgoal2.getJSONObject());
        
        goal.put("subgoals", subgoals);
        
        return goal;
    }
}
