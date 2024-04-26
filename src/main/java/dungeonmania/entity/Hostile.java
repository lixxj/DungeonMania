package dungeonmania.entity;

import java.util.Map;

import dungeonmania.Dungeon;
import dungeonmania.Player.Player;
import dungeonmania.util.Dijkstra;
import dungeonmania.util.Position;

public class Hostile implements State {
    @Override
    public void attack(Player player, double damage) {
        player.takeDamage(damage);
    }

    @Override
    public void bonus(Player player, double attackBonus, double defenceBonus) {
        // hostile does not provide bonus to player, do nothing
    }

    @Override
    public void move(Mercenary mercenary, Dungeon dungeon) {
        Map<Position, Position> previous = Dijkstra.dijkstra(mercenary, dungeon);
        // start from the player's position, track backwards to find the mercenary's next move
        Position prev = dungeon.getPlayer().getPosition();
        
        // mercenary's next move p is representing by previous.get(p) = mercenary's position
        while (!previous.get(prev).equals(mercenary.getPosition())) {
            prev = previous.get(prev);
        }

        // if prev is not null, mercenary will move 
        if (prev != null) {
            mercenary.setPosition(prev);
        }
    }
}
