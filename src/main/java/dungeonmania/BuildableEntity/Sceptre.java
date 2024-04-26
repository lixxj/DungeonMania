package dungeonmania.BuildableEntity;

import dungeonmania.Player.Inventory;
import dungeonmania.entity.Mercenary;
import dungeonmania.entity.MovingEntity;
import dungeonmania.util.ExtractFromJson;
import dungeonmania.util.Position;

public class Sceptre extends BuildableEntity {
    public Sceptre(Position position, String type, int duration) {
        super(position, type, duration);
    }

    @Override
    public boolean build(Inventory i, String config) {
        String remove1 = "";
        int amount1 = 0;
        if (i.countWood() >= 1) {
            remove1 = "wood";
            amount1 = 1;
        } else if (i.countArrow() >= 2) {
            remove1 = "arrow";
            amount1 = 2;
        }
        String remove2 = "";
        if (i.countSunStone() >= 2) {
            remove2 = "sun_stone";
        } else if (i.countSunStone() >= 1) {
            if (i.countTreasure() >= 1) {
                remove2 = "treasure";
            } else if (i.hasItem("key")) {
                remove2 = "key";
            }
        }
        if (!remove1.equals("") && !remove2.equals("")) {
            i.removeItem(remove1, amount1);
            i.removeItem(remove2, 1);
            i.getBuildItems().add(new Sceptre(new Position(1, 1), "sceptre", ExtractFromJson.getConfigIntFromJson(config, "mind_control_duration")));
            return true;
        }
        return false;
    }

    public boolean mindControl(MovingEntity enemy){
        // mind control the enemy 
        return ((Mercenary) enemy).mindControlled(getDuration());
    }
}
