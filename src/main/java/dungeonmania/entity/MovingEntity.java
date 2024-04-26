package dungeonmania.entity;

import dungeonmania.Dungeon;
import dungeonmania.Player.Player;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity{
    private double health;
    private double attackDamage;
    private int swampTick = 0;
    private boolean isEnemy = true;

    public MovingEntity(Position position, String type, double health, double attackDamage) {
        super(position, type);
        this.health = health;
        this.attackDamage = attackDamage;
    }

    public abstract void move(Dungeon d);

    public void setSwampTick(int swampTick) {
        this.swampTick = swampTick;
    }

    public double getHealth() {
        return this.health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getAttackDamage() {
        return this.attackDamage;
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    public boolean isEnemy() {
        return this.isEnemy;
    }

    public void toAlly() {
        this.isEnemy = false;
    }

    public void toEnemy() {
        this.isEnemy = true;
    }
    
    public abstract void attack(Player player);
}
