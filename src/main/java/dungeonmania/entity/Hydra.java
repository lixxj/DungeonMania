package dungeonmania.entity;

import dungeonmania.util.Position;

public class Hydra extends ZombieToast{
    private double hydra_health_increase_rate;
    private double hydra_health_increase_amount;

    public Hydra(Position position, String type, double health, double attackDamage, 
        double hydra_health_increase_rate, double hydra_health_increase_amount) {
        super(position, type, health, attackDamage);
        this.hydra_health_increase_rate = hydra_health_increase_rate;
        this.hydra_health_increase_amount = hydra_health_increase_amount;
    }
    
    @Override
    public void setHealth(double health) {
        if (isHealthIncrease()) {
            super.setHealth(getHealth() + hydra_health_increase_amount);
            return;
        }
        super.setHealth(health);
    }

    // return true if the Hydra increase health, otherwise return false
    public boolean isHealthIncrease() {
        double random = Math.random();
        double chance = hydra_health_increase_rate;
        // apply rate, if the chance is greater or equal to random formed number, increase health
        if (chance >= random) {
            return true;
        }
        return false;
    }

}
