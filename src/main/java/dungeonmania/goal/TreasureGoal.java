package dungeonmania.goal;


import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.Player.Inventory;
import dungeonmania.Player.Player;
import dungeonmania.util.ExtractFromJson;

public class TreasureGoal implements Goal {
    private final String name = "treasure";
    private int N_Treasure = 0;

    public TreasureGoal(String configName) {
        this.N_Treasure = ExtractFromJson.getConfigIntFromJson(configName, "treasure_goal");
    }

    @Override
    public String getGoalName() {
        return this.name;
    }

    public int getN_Treasure() {
        return this.N_Treasure;
    }

    @Override
    public String getGoalNameResponse() {
        return ":treasure";
    }

    @Override
    public boolean getGoalStatus(Dungeon dungeon) {
        Player player = dungeon.getPlayer();
        Inventory inventory = player.getInventory();
        int TreasureCount = inventory.countTreasure();
        
        return TreasureCount >= getN_Treasure();
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject goalJson = new JSONObject();
        goalJson.put("goal", getGoalName());
        return goalJson;
    }
}
