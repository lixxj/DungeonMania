package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Entity;
import dungeonmania.entity.ZombieToastSpawner;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class ZombieToastSpawnerTest {
    @Test
    @DisplayName("Test create ZombieToastSpawner")
    public void testCreateZombieToastSpawner() {
        Entity.clearIdCount();
        ZombieToastSpawner zts = new ZombieToastSpawner(new Position(3, 3), 2, 5, 1);
        EntityResponse expect = new EntityResponse("1", "zombie_toast_spawner", new Position(3, 3), true);
        EntityResponse actual = new EntityResponse(zts.getId(), zts.getType(), zts.getPosition(), zts.isInteractable());
        assertTrue(expect.equals(actual));
    }

    @Test
    @DisplayName("Test tick ZombieToastSpawner")
    public void testZombieToastSpawnerTick() {
        Entity.clearIdCount();
        List<Entity> entities = new ArrayList<>();
        ZombieToastSpawner zts = new ZombieToastSpawner(new Position(3, 3), 2, 5, 1);
        entities.add(zts);
        Dungeon dungeon = new Dungeon(entities, "simple");
        zts.tick(dungeon);
        zts.tick(dungeon);
        assertEquals(2, entities.size());
    }

    @Test
    @DisplayName("Test spawn rate 0")
    public void testZombieToastSpawnerRateZero() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon(
            "d_zombie_toast_spawner",
            "c_zombie_toast_zero_rate"
        );
        
        for (int i = 0; i < 10; i++) {
            dungeon.getEntities().get(0).tick(dungeon);
            assertEquals(1, dungeon.getEntities().size());
        }
    }

    @Test
    @DisplayName("Test wall up")
    public void testZombieToastSpawnerWallUp() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon(
            "d_zombie_toast_spawner_wall_up",
            "c_zombie_toast_one_rate"
        );

        dungeon.getEntities().get(0).tick(dungeon);
        Entity zombie = dungeon.getEntity(new Position(4, 5));
        assertTrue(zombie != null);
    }

    @Test
    @DisplayName("Test wall up and down")
    public void testZombieToastSpawnerWallUpDown() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon(
            "d_zombie_toast_spawner_wall_up_down",
            "c_zombie_toast_one_rate"
        );

        dungeon.getEntities().get(0).tick(dungeon);
        Entity zombie = dungeon.getEntity(new Position(3, 4));
        assertTrue(zombie != null);
    }

    @Test
    @DisplayName("Test wall up, down, and left")
    public void testZombieToastSpawnerWallUpDownLeft() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon(
            "d_zombie_toast_spawner_wall_up_down_left",
            "c_zombie_toast_one_rate"
        );

        dungeon.getEntities().get(0).tick(dungeon);
        Entity zombie = dungeon.getEntity(new Position(5, 4));
        assertTrue(zombie != null);
    }

    @Test
    @DisplayName("Test all walls")
    public void testZombieToastSpawnerAllWalls() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon(
            "d_zombie_toast_spawner_all_walls",
            "c_zombie_toast_one_rate"
        );

        dungeon.getEntities().get(0).tick(dungeon);
        assertEquals(5, dungeon.getEntities().size());
    }
}
