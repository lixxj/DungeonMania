package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntity.InvisibilityPotion;
import dungeonmania.Player.Player;
import dungeonmania.entity.Assassin;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Mercenary;
import dungeonmania.entity.ZombieToast;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class InvisibilityPotionTest {
    @Test
    @DisplayName("Test create InvisibilityPotion")
    public void testCreateInvisibilityPotion() {
        Entity.clearIdCount();
        InvisibilityPotion ip = new InvisibilityPotion(new Position(1, 1), "invisibility_potion", 5);
        assertEquals(5, ip.getInvisibility_potion_duration());
    }

    @Test
    @DisplayName("Test enemy run away, test invincible for strong enemy")
    public void testComsumeInvisibilityPotion() {
        Entity.clearIdCount();
        List<Entity> entities = new ArrayList<>();
        Player player = new Player(new Position(2, 2), "player", 20, 50);
        InvisibilityPotion ip = new InvisibilityPotion(new Position(1, 2), "invisibility_potion", 100);
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
        zt.tick(dungeon);
        mercenary.tick(dungeon);
        assassin.tick(dungeon);
        
        // in the invisible mode, encounter with zombie toast
        player.setPosition(zt.getPosition().translateBy(Direction.DOWN));
        dungeon.playerMove(player.getPosition(), Direction.UP);
        assertEquals(100, zt.getHealth());
        assertEquals(20, player.getHealth());

        // in the invisible mode, encounter with mercanery
        player.setPosition(mercenary.getPosition().translateBy(Direction.DOWN));
        assertTrue(player.isInvisible());
        dungeon.playerMove(player.getPosition(), Direction.UP);
        assertEquals(100, mercenary.getHealth());
        assertEquals(20, player.getHealth());

        // in the invisible mode, encounter with assassin 
        player.setPosition(assassin.getPosition().translateBy(Direction.DOWN));
        dungeon.playerMove(player.getPosition(), Direction.UP);
        assertEquals(100, assassin.getHealth());
        assertEquals(20, player.getHealth());
    }
}