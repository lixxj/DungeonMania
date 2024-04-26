package dungeonmania.entity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.CollectableEntity.Bomb;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity{
    private boolean floorSwitch = false;

    public FloorSwitch (Position p) {
        super(p, "switch");
        this.setCrossAble(true);
    }

    // when switch is activate, detect if there is a bomb adjacently and try use it
    public boolean detectBomb(Dungeon dungeon) {
        List<Position> adjPos = new ArrayList<>();
        Position pos = getPosition();
        adjPos.add(pos.translateBy(Direction.UP));
        adjPos.add(pos.translateBy(Direction.LEFT));
        adjPos.add(pos.translateBy(Direction.RIGHT));
        adjPos.add(pos.translateBy(Direction.DOWN));
        for (Position p : adjPos) {
            Entity e = dungeon.getEntity(p, "bomb");
            if (e != null) {
                Bomb bomb = (Bomb)e;
                bomb.use(dungeon);
                return true;
            }
        }
        return false;
    }

    // activate the switch
    public void trigger() {
        this.floorSwitch = true;
    }

    // close the swtich
    public void untrigger() {
        this.floorSwitch = false;
    }

    // return if switch is on
    public boolean isFloorSwitchOn() {
        return floorSwitch;
    }
}
