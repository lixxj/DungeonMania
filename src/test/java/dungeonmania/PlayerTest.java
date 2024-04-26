package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Player.Player;
import dungeonmania.entity.Entity;
import dungeonmania.entity.MovingEntity;
import dungeonmania.entity.ZombieToast;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.ExtractFromJson;
import dungeonmania.util.Position;

public class PlayerTest {
    @Test
    @DisplayName("Test player move up")
    public void testPlayerMoveUp() {
        Entity.clearIdCount();
        List<Entity> entities =  ExtractFromJson.getEntityFromJson("d_player_spawn_simple", "c_movementTest_testMovementDown");

        // x = 5, y = 11
        Player player = (Player)entities.get(0);
        player.move(Direction.UP);
        assertEquals(new Position(5, 10), player.getPosition());
    }

    @Test
    @DisplayName("Test player move down")
    public void testPlayerMoveDown() {
        Entity.clearIdCount();
        List<Entity> entities =  ExtractFromJson.getEntityFromJson("d_player_spawn_simple", "c_movementTest_testMovementDown");

        Player player = (Player)entities.get(0);
        player.move(Direction.DOWN);
        assertEquals(new Position(5, 12), player.getPosition());
    }

    @Test
    @DisplayName("Test player move left")
    public void testPlayerMoveLeft() {
        Entity.clearIdCount();
        List<Entity> entities =  ExtractFromJson.getEntityFromJson("d_player_spawn_simple", "c_movementTest_testMovementDown");

        // x = 5, y = 11
        Player player = (Player)entities.get(0);
        player.move(Direction.LEFT);
        assertEquals(new Position(4, 11), player.getPosition());
    }

    @Test
    @DisplayName("Test player move right")
    public void testPlayerMoveRight() {
        Entity.clearIdCount();
        List<Entity> entities =  ExtractFromJson.getEntityFromJson("d_player_spawn_simple", "c_movementTest_testMovementDown");

        // x = 5, y = 11
        Player player = (Player)entities.get(0);
        player.move(Direction.RIGHT);
        assertEquals(new Position(6, 11), player.getPosition());
    }

    @Test
    @DisplayName("Test player attack zombie toast simple - basic fight (no weapon)")
    public void testPlayerAttackZombieToastSimpleBasic() {
        Entity.clearIdCount();
        Player player = new Player(new Position(3, 3), "player", 100, 5);
        MovingEntity enemy = new ZombieToast(new Position(3, 3), "zombie_toast", 50, 5);
        List<ItemResponse> weaponaryUsed = player.attack(enemy);
        assertEquals(50-5/5, enemy.getHealth());
        // there is no weapon used
        assertEquals(0, weaponaryUsed.size());
    }

    @Test
    @DisplayName("Test player attack zombie toast simple - has sword")
    public void testPlayerAttackZombieToastSimpleSword() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon("d_inventoryTest_simple", "c_movementTest_testMovementDown");
        List<Entity> entities =  dungeon.getEntities();
        Player player = new Player(new Position(3, 3), "player", 100, 5);
        MovingEntity enemy = new ZombieToast(new Position(3, 3), "zombie_toast", 50, 5);
        player.collect(entities.get(7), dungeon);
        List<ItemResponse> weaponaryUsed = player.attack(enemy);
        assertEquals(50.0-7.0/5, enemy.getHealth());
        // only sword is used
        assertEquals(1, weaponaryUsed.size());
        assertEquals("8", weaponaryUsed.get(0).getId());
    }

    @Test
    @DisplayName("Test player attack zombie toast simple - has bow")
    public void testPlayerAttackZombieToastSimpleBow() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon("d_inventoryTest_simple", "c_movementTest_testMovementDown");
        List<Entity> entities =  dungeon.getEntities();
        Player player = new Player(new Position(3, 3), "player", 100, 5);
        MovingEntity enemy = new ZombieToast(new Position(3, 3), "zombie_toast", 50, 5);
        // collect necessary items to build bow
        player.collect(entities.get(0), dungeon); // arrow
        player.collect(entities.get(0), dungeon); // arrow
        player.collect(entities.get(0), dungeon); // arrow
        player.collect(entities.get(6), dungeon); // wood
        player.build("bow", "c_movementTest_testMovementDown");
        List<ItemResponse> weaponaryUsed = player.attack(enemy);
        assertEquals(50-2*5/5, enemy.getHealth());
        // only bow is used
        assertEquals(1, weaponaryUsed.size());
        assertEquals("14", weaponaryUsed.get(0).getId()); // 11 items from json file, 1 player, 1 enemy, so id = 14 for bow
    }

    @Test
    @DisplayName("Test player attack zombie toast simple - has bow and sword")
    public void testPlayerAttackZombieToastSimpleBowAndSword() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon("d_inventoryTest_simple", "c_movementTest_testMovementDown");
        List<Entity> entities =  dungeon.getEntities();
        Player player = new Player(new Position(3, 3), "player", 100, 5);
        MovingEntity enemy = new ZombieToast(new Position(3, 3), "zombie_toast", 50, 5);
        // take sword 
        player.collect(entities.get(7), dungeon);
        // collect necessary items to build bow
        player.collect(entities.get(0), dungeon); // arrow
        player.collect(entities.get(0), dungeon); // arrow
        player.collect(entities.get(0), dungeon); // arrow
        player.collect(entities.get(5), dungeon); // wood
        player.build("bow", "c_movementTest_testMovementDown");
        List<ItemResponse> weaponaryUsed = player.attack(enemy);
        assertEquals(50-2.0*(5.0+2.0)/5.0, enemy.getHealth());
        // weapons: bow + sword
        assertEquals(2, weaponaryUsed.size());
    }

    @Test
    @DisplayName("Test player attack zombie toast simple - used invincibility potion")
    public void testPlayerAttackZombieToastSimpleInvincibilityPotion() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon("d_inventoryTest_simple", "c_movementTest_testMovementDown");
        List<Entity> entities =  dungeon.getEntities();
        Player player = new Player(new Position(3, 3), "player", 100, 5);
        MovingEntity enemy = new ZombieToast(new Position(3, 3), "zombie_toast", 50, 5);
        // collect potion
        player.collect(entities.get(4), dungeon);
        // consume potion
        player.consume("5", dungeon);
        List<ItemResponse> weaponaryUsed = player.attack(enemy);
        assertEquals(0, enemy.getHealth());
        // only invinbility potion is used
        assertEquals(1, weaponaryUsed.size());
        assertEquals("5", weaponaryUsed.get(0).getId());
    }

    @Test 
    @DisplayName("Test player collect items simple")
    public void testPlayerCollectSimple() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon("d_inventoryTest_simple", "c_movementTest_testMovementDown");
        List<Entity> entities =  dungeon.getEntities();
        Player player = new Player(new Position(3, 3), "player", 100, 5);
        while (!entities.isEmpty()) {
            Entity entity = entities.get(0);
            assertTrue(player.collect(entities.get(0), dungeon));
            assertTrue(player.getInventory().getCollectItems().contains(entity));
        }
        
    }

    @Test 
    @DisplayName("Test player collect items that is not collectable")
    public void testPlayerCollectNotCollectable() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon("d_collectNotCollectableItem_simple", "c_movementTest_testMovementDown");
        List<Entity> entities =  dungeon.getEntities();
        Player player = new Player(new Position(3, 3), "player", 100, 5);
        assertTrue(player.collect(entities.get(0), dungeon) == false);
        assertTrue(!player.getInventory().getCollectItems().contains(entities.get(0)));
    }


    @Test
    @DisplayName("Test player build shield - using 2 woods + 1 treasure")
    public void testPlayerBuildShield1() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon("d_inventoryTest_simple", "c_movementTest_testMovementDown");
        List<Entity> entities =  dungeon.getEntities();
        Player player = new Player(new Position(3, 3), "player", 100, 5);
        // collect necessary items to build shield
        player.collect(entities.get(10), dungeon); // wood
        player.collect(entities.get(9), dungeon); // wood
        player.collect(entities.get(8), dungeon); // treasure
        player.build("shield", "c_movementTest_testMovementDown");

        assertTrue(player.getInventory().getCollectItems().isEmpty());
        assertTrue(player.getInventory().getBuildItems().get(0).getType().equals("shield"));
    }

    @Test
    @DisplayName("Test player build shield - using 2 woods + 1 key")
    public void testPlayerBuildShield2() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon("d_inventoryTest_simple", "c_movementTest_testMovementDown");
        List<Entity> entities =  dungeon.getEntities();
        Player player = new Player(new Position(3, 3), "player", 100, 5);
        // collect necessary items to build shield
        player.collect(entities.get(10), dungeon); // wood
        player.collect(entities.get(9), dungeon); // wood
        player.collect(entities.get(6), dungeon); // key
        player.build("shield", "c_movementTest_testMovementDown");

        assertTrue(player.getInventory().getCollectItems().isEmpty());
        assertTrue(player.getInventory().getBuildItems().get(0).getType().equals("shield"));
    }
}
