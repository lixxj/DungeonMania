package dungeonmania.entity;

import dungeonmania.Dungeon;
import dungeonmania.Player.Player;

public interface State {
    public void move(Mercenary mercenary, Dungeon dungeon);
    public void attack(Player player, double damage);
    public void bonus(Player player, double attackBonus, double defenceBonus);
}
