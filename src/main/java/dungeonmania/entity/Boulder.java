package dungeonmania.entity;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends StaticEntity{
    private boolean onSwitch;

    public Boulder(Position p) {
        super(p, "boulder");
        this.setCrossAble(false);
    }


    public boolean push(Direction direction, Dungeon dungeon) {
        Position newPos = getPosition().translateBy(direction);
        if (!isPushable(newPos, dungeon)) {
           return false;
        }
        //check if current position has a switch
        FloorSwitch fs = (FloorSwitch)dungeon.getEntity(getPosition(), "switch");
        if (onSwitch) {
            if (fs != null) {
                setOnSwitch(false);
                fs.untrigger();
            } 
        }
        FloorSwitch fs_new = (FloorSwitch)dungeon.getEntity(newPos, "switch");
        if (fs_new != null) {
            setOnSwitch(true);
            // tigger the switch, if there is bomb around, use the bomb
            fs_new.trigger();
            fs_new.detectBomb(dungeon);
        }
        setPosition(newPos);
        return true;
    }

    public Boolean isPushable(Position newPos, Dungeon dungeon) {
        List<Entity> entities = dungeon.getEntityList(newPos);
        if (!entities.isEmpty()) {
            for (Entity e : entities) {
                if (e.isMovingEntity() || !e.isOpenSquare()) {
                    return false;
                }
            }
        }
        return true;
    }



    public void setOnSwitch(boolean onSwitch) {
        this.onSwitch = onSwitch;
    }
}
