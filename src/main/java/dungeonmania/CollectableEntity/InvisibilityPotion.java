package dungeonmania.CollectableEntity;

import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity {
    
    // Attributes
    private double invisibility_potion_duration;

    // Constructor
    public InvisibilityPotion(Position position, String type, double invisibility_potion_duration) {
        super(position, type);
        this.invisibility_potion_duration = invisibility_potion_duration;
    }

    // Getters
    public double getInvisibility_potion_duration() {
        return this.invisibility_potion_duration;
    }
}
