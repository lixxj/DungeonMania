package dungeonmania.entity;


import dungeonmania.Dungeon;
import dungeonmania.Player.Player;

public class Ally implements State {

    @Override
    public void move(Mercenary mercenary, Dungeon dungeon) {
        mercenary.setPosition(dungeon.getPlayer().getPrePosition());
    }

    @Override
    public void attack(Player player, double damage) {
        // ally does not attack, leave blank
    }

    @Override
    public void bonus(Player player, double attackBonus, double defenceBonus) {
        player.addAttack(attackBonus);
        player.addDefence(defenceBonus);
    }
}
