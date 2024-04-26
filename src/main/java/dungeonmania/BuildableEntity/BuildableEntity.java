package dungeonmania.BuildableEntity;

import dungeonmania.Player.Inventory;
import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public abstract class BuildableEntity extends Entity {

    // Attributes
    private int duration;

    // Constructor
    public BuildableEntity(Position position, String type, int duration) {
        super(position, type);
        this.duration = duration;
    }

    public abstract boolean build(Inventory i, String configName);

    // Getters
    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
