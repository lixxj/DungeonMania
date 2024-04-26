package dungeonmania.entity;

import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.Player.Player;
import dungeonmania.util.Position;

public class Assassin extends Mercenary{
    private double bribeFailRate;
    private double reconRadius;

    public Assassin(Position position, String type, double health, double attackDamage, double bribeAmount, double bribeFailRate, double reconRadius, double bribeRadius, double attackBonus, double defenceBonus) {
        super(position, health, attackDamage, bribeAmount, bribeRadius, attackBonus, defenceBonus);
        setType(type);
        this.bribeFailRate = bribeFailRate;
        this.reconRadius = reconRadius;
    }

    @Override
    public void move(Dungeon dungeon) {
        getState().move(this, dungeon);
    }

    @Override
    public void attack(Player player) {
        getState().attack(player, getAttackDamage());
    }
    
    @Override
    public void tick(Dungeon dungeon) {
        Player player = dungeon.getPlayer();
        if (player.isInvisible() && getPosition().distance(player.getPosition()) > reconRadius && this.isEnemy()) {
            moveRandom(dungeon);
        } else if (player.isInvincible() && this.isEnemy()) {
            moveAway(player, dungeon);
        } else {
            move(dungeon);
        }
        updateMindControlledDuration();
        if (getPosition().equals(dungeon.getPlayer().getPosition()) && this.isEnemy() &&!dungeon.getPlayer().isInvisible()) {
            dungeon.battle(dungeon.getPlayer(), this);   
        }
    }

    @Override
    public boolean toAlly(Player player) {
        Random random = new Random();
        double chance = random.nextDouble();
        if (chance > bribeFailRate) {
            return super.toAlly(player);
        }
        return false;
    }
}
