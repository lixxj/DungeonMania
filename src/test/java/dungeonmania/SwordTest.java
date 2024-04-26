package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntity.InvincibilityPotion;
import dungeonmania.CollectableEntity.Sword;
import dungeonmania.Player.Player;
import dungeonmania.entity.Assassin;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Mercenary;
import dungeonmania.entity.ZombieToast;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwordTest {
    @Test
    @DisplayName("Test battle with sword")
    public void testBattleWithSword() {
        List<Entity> entities = new ArrayList<>();
        Player player = new Player(new Position(2, 2), "player", 20, 50);
        Sword sword = new Sword(new Position(2, 1), "sword", 5, 5);
        ZombieToast zt = new ZombieToast(new Position(3, 1), "zombie_toast", 11, 50);
        entities.add(player);
        entities.add(sword);
        entities.add(zt);
        Dungeon dungeon = new Dungeon(entities, "simple");
        dungeon.playerMove(player.getPosition(), Direction.UP);
        assertTrue(player.getInventory().hasItem("sword"));
        dungeon.playerMove(player.getPosition(), Direction.RIGHT);
        assertEquals(0, zt.getHealth());
    }
}
