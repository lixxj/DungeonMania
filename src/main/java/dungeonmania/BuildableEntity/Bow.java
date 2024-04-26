package dungeonmania.BuildableEntity;

import dungeonmania.Player.Inventory;
import dungeonmania.util.Position;

public class Bow extends BuildableEntity {
    
    public Bow(Position position, String type, int duration) {
        super(position, type, duration);
    }

    @Override
    public boolean build(Inventory i, String config) {
        if (i.countWood() >= 1 && i.countArrow() >= 3) {
            i.removeItem("wood", 1);
            i.removeItem("arrow", 3);
            i.getBuildItems().add(this);
            return true;
        }
        return false;
    }
}
