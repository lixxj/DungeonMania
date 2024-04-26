package dungeonmania.CollectableEntity;

import dungeonmania.util.Position;

public class Sword extends CollectableEntity {

    // Attributes
    private double sword_attack;
    private double sword_durability;

    // Constructor
    public Sword(Position position, String type, double sword_attack, double sword_durability) {
        super(position, type);
        this.sword_attack = sword_attack;
        this.sword_durability = sword_durability;
    }

    // Getters
    public double getSwordAttack() {
        return this.sword_attack;
    }

    public double getSwordDurability() {
        return this.sword_durability;
    }

    public void setSword_durability(double sword_durability) {
        this.sword_durability = sword_durability;
    }
}