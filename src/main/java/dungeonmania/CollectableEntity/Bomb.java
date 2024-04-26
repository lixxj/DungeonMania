package dungeonmania.CollectableEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.FloorSwitch;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity {

    // Attributes
    private double bombRadius;
    private boolean collected = false; 
    // Constructor
    public Bomb(Position position, String type, double bombRadius) {
        super(position, type);
        this.bombRadius = bombRadius;
    }

    // Getters
    public double getBombRadius() {
        return this.bombRadius;
    }

    // @Override
    // public void tick(Dungeon dungeon) {
    //     use(dungeon);
    // }

    // use the bomb
    public void use(Dungeon dungeon) {
        Position position = dungeon.getPlayer().getPosition();
        if (collected && detectSwitch(position, dungeon)) {
            explode(position, dungeon);
        }
    }

    // destory entities
    public void explode(Position pos, Dungeon dungeon) {
        int x = pos.getX();
        int y = pos.getY();
        for (int i = x - (int)bombRadius; i <= x + (int)bombRadius; i++) {
            for (int j = y - (int)bombRadius; j <= y + (int)bombRadius; j++) {
                Position p = new Position(i, j);
                Entity e = dungeon.getEntity(p);
                if (e != null && (e.getType().equals("boulder") || e.getType().equals("switch"))) {
                    dungeon.removeEntity(p);
                }
                dungeon.removeEntity(p);
            }
        }
    }

    // detect if there is any active switch
    public boolean detectSwitch(Position pos, Dungeon dungeon) {
        List<Position> adjPos = new ArrayList<>();
        adjPos.add(pos.translateBy(Direction.UP));
        adjPos.add(pos.translateBy(Direction.LEFT));
        adjPos.add(pos.translateBy(Direction.RIGHT));
        adjPos.add(pos.translateBy(Direction.DOWN));
        for (Position p : adjPos) {
            Entity e = dungeon.getEntity(p, "switch");
            if (e != null) {
                FloorSwitch fs = (FloorSwitch)e;
                if (fs.isFloorSwitchOn()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void collected() {
        this.collected = true;
    }

    public boolean isCollected() {
        return collected;
    }
}