package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntity.InvincibilityPotion;
import dungeonmania.Player.Player;
import dungeonmania.entity.Assassin;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Mercenary;
import dungeonmania.entity.ZombieToast;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class InvincibilityPotionTest {
    @Test
    @DisplayName("Test create InvicibilityPotion")
    public void testCreateInvicibilityPotion() {
        Entity.clearIdCount();
        InvincibilityPotion ip = new InvincibilityPotion(new Position(1, 1), "invincibility_potion", 5);
        assertEquals(5, ip.getInvincibility_potion_duration());
    }

    @Test
    @DisplayName("Test enemy run away, test invincible for strong enemy")
    public void testComsumeInvicibilityPotion() {
        Entity.clearIdCount();
        List<Entity> entities = new ArrayList<>();
        Player player = new Player(new Position(2, 2), "player", 20, 50);
        InvincibilityPotion ip = new InvincibilityPotion(new Position(1, 2), "invincibility_potion", 100);
        ZombieToast zt = new ZombieToast(new Position(1, 3), "zombie_toast", 100, 200);
        Mercenary mercenary = new Mercenary(new Position(1, 2), 100, 200, 1, 1, 1, 1);
        Assassin assassin = new Assassin(new Position(1, 1), "assassin", 100, 200, 1, 0, 0, 1, 1, 1);
        entities.add(player);
        entities.add(ip);
        entities.add(zt);
        entities.add(mercenary);
        entities.add(assassin);
        Dungeon dungeon = new Dungeon(entities, "simple");
        // collect potion
        dungeon.playerMove(player.getPosition(), Direction.LEFT);
        
        // consume potion
        player.consume(ip.getId(), dungeon);
        // check if zombie move away from player
        double ztOriginDistance = zt.getPosition().distance(player.getPosition());
        zt.tick(dungeon);
        double ztRunAwayDistance = zt.getPosition().distance(player.getPosition());
        assertTrue(ztOriginDistance < ztRunAwayDistance);
        // check if mercenary move away from player
        double mercenaryOriginDistance = mercenary.getPosition().distance(player.getPosition());
        mercenary.tick(dungeon);
        double mercenaryRunAwayDistance = mercenary.getPosition().distance(player.getPosition());
        assertTrue(mercenaryOriginDistance < mercenaryRunAwayDistance);
        // check if assasin move away from player
        double assassinOriginDistance = assassin.getPosition().distance(player.getPosition());
        assassin.tick(dungeon);
        double assassinRunAwayDistance = assassin.getPosition().distance(player.getPosition());
        assertTrue(assassinOriginDistance < assassinRunAwayDistance);
        
        // in the invincible mode, battle with zombie toast
        player.setPosition(zt.getPosition().translateBy(Direction.DOWN));
        dungeon.playerMove(player.getPosition(), Direction.UP);
        assertEquals(0, zt.getHealth());
        assertEquals(20, player.getHealth());

        // in the invincible mode, battle with mercanery
        player.setPosition(mercenary.getPosition().translateBy(Direction.DOWN));
        assertTrue(player.isInvincible());
        dungeon.playerMove(player.getPosition(), Direction.UP);
        assertEquals(0, mercenary.getHealth());

        // in the invincible mode, battle with assassin
        player.setPosition(assassin.getPosition().translateBy(Direction.DOWN));
        assertTrue(player.isInvincible());
        dungeon.playerMove(player.getPosition(), Direction.UP);
        assertEquals(0, assassin.getHealth());
    }
}
