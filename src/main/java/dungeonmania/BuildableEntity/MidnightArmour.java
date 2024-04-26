package dungeonmania.BuildableEntity;

import dungeonmania.Player.Inventory;
import dungeonmania.util.Position;

public class MidnightArmour extends Shield{
    double armourAttack;
    public MidnightArmour(Position position, String type, int duration, int defence, double armourAttack) {
        super(position, type, 2147483647, defence);
        this.armourAttack = armourAttack;
    }

    @Override
    public boolean build(Inventory i, String config) {
        if (i.hasItem("sword") && i.countSunStone() >= 1) {
            i.removeItem("sword", 1);
            i.removeItem("sun_stone", 1);
            i.getBuildItems().add(this);
            return true;
        }
        return super.build(i, config);
    }
    public double getArmourAttack() {
        return armourAttack;
    }
}
