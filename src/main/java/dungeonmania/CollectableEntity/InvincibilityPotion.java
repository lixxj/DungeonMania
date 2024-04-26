package dungeonmania.CollectableEntity;
import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntity {
    // Attributes
    private double invincibility_potion_duration;

    // Constructor
    public InvincibilityPotion(Position position, String type, double invincibility_potion_duration) {
        super(position, type);
        this.invincibility_potion_duration = invincibility_potion_duration;
    }

    // Getters
    public double getInvincibility_potion_duration() {
        return this.invincibility_potion_duration;
    }

}
