package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Player.Player;
import dungeonmania.entity.Entity;
import dungeonmania.entity.ZombieToast;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class ZombieToastTest {
    @Test
    @DisplayName("Test create ZombieToast")
    public void testCreateZombieToast() {
        Entity.clearIdCount();
        ZombieToast zt = new ZombieToast(new Position(1, 1), "zombie_toast", 5, 1);
        EntityResponse expect = new EntityResponse("1", "zombie_toast", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(zt.getId(), zt.getType(), zt.getPosition(), zt.isInteractable());
        assertTrue(expect.equals(actual));
    }

    @Test
    @DisplayName("Test tick ZombieToast")
    public void testTickZombieToast() {
        Entity.clearIdCount();
        List<Entity> entities = new ArrayList<>();
        ZombieToast zt = new ZombieToast(new Position(1, 1), "zombie_toast", 5, 1);
        entities.add(zt);
        Dungeon dungeon = new Dungeon(entities, "simple");
        for (int i = 0; i < 10; i++) {
            zt.tick(dungeon);
        }
        assertEquals(1, entities.size());
    }

    @Test
    @DisplayName("Test ZombieToast attack")
    public void testZombieToastAttack() {
        Entity.clearIdCount();
        ZombieToast zt = new ZombieToast(new Position(1, 1), "zombie_toast", 5, 10);
        Player player = new Player(new Position(1, 1), "player", 50, 15);
        zt.attack(player);
        assertEquals(49, player.getHealth());
    }
}
