package dungeonmania.BuildableEntity;

import dungeonmania.Player.Inventory;
import dungeonmania.util.Position;

public class Shield extends BuildableEntity {
    private int defence;
    public Shield(Position position, String type, int duration, int defence) {
        super(position, type, duration);
        this.defence = defence;
    }
    public int getDefence() {
        return defence;
    }

    @Override
    public boolean build(Inventory i, String config) {
        boolean succeed = false;
        if (i.countWood() >= 2) {
            if (i.countSunStone() >= 1) {
                i.removeItem("wood", 2);
                succeed = true;
            } else if (i.countTreasure() >= 1) {
                i.removeItem("wood", 2);
                i.removeItem("treasure", 1);
                succeed = true;
            } else if (i.hasItem("key")) {
                i.removeItem("wood", 2);
                i.removeItem("key", 1);
                succeed = true;
            }
            if (succeed) {
                i.getBuildItems().add(this);
                return true;
            }
        }
        return false;
    }
}
