package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntity.InvincibilityPotion;
import dungeonmania.CollectableEntity.InvisibilityPotion;
import dungeonmania.Player.Player;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Mercenary;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MercenaryTest {
    @Test
    @DisplayName("Test mercenary hostile movement - dijkstra")
    public void testMercenaryHostileMovement() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon("d_mercenary_movement", "c_movementTest_testMovementDown");
        List<Entity> entities = dungeon.getEntities();
        Mercenary mercenary = null;
        for (Entity entity : entities) {
            if (entity.getType().equals("mercenary")) {
                mercenary = (Mercenary) entity;
            }
        }
        assertEquals(new Position(3, 3), mercenary.getPosition());
        mercenary.move(dungeon);
        assertEquals(new Position(3, 2), mercenary.getPosition());
        mercenary.move(dungeon);
        assertEquals(new Position(4, 2), mercenary.getPosition());
        mercenary.move(dungeon);
        assertEquals(new Position(5, 2), mercenary.getPosition());
    }

    @Test
    @DisplayName("Test mercenary interaction with player")
    public void testMercenaryInterationWithPlayer() throws IllegalArgumentException, InvalidActionException {
        Entity.clearIdCount();
        // generate dungeon
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_mercenary_bribe", "c_mercenary_bribe");
        Dungeon dungeon = dmc.getDungeon();
        Player player = dungeon.getPlayer();
        Mercenary mercenary = (Mercenary) dungeon.getEntity(new Position(10, 15), "mercenary");
        
        // player move right 3 block which collect 3 treasures
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // player try to bribe mercenary -- failed cuz not within radius
        assertThrows(Exception.class, () -> {dmc.interact(mercenary.getId());});
        assertEquals(false, player.bribe(mercenary));
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        // player try to bribe mercenary again -- success because within radius and have enough gold
        assertDoesNotThrow(() -> dmc.interact(mercenary.getId()));
        assertEquals(false, mercenary.isEnemy());

        // check if mercenary follow player after bribed
        dmc.tick(Direction.DOWN);
        assertEquals(player.getPrePosition(), mercenary.getPosition());
    }

    @Test
    @DisplayName("Test mercenary fight against player -- mercenary die")
    public void mercenaryFightPlayer_MercenaryDie() {
        Entity.clearIdCount();
        // generate dungeon
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_mercenary_fight", "c_battleTests_basicMercenaryMercenaryDies");
        Dungeon dungeon = dmc.getDungeon();
        Mercenary mercenary = (Mercenary) dungeon.getEntity(new Position(6, 6), "mercenary");

        // player move to right, battle starts
        dmc.tick(Direction.RIGHT);
        // mercenary die
        assertTrue(mercenary.getHealth() <= 0);
        // mercenary is removed from dungeon
        assertTrue(dungeon.getEntity(mercenary.getId()) == null);
    }

    @Test
    @DisplayName("Test mercenary fight against player -- player die")
    public void mercenaryFightPlayer_PlayerDie() {
        Entity.clearIdCount();
        // generate dungeon
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_mercenary_fight", "c_battleTests_basicMercenaryPlayerDies");
        Dungeon dungeon = dmc.getDungeon();
        Player player = dungeon.getPlayer();

        // player move to right, battle starts
        dmc.tick(Direction.RIGHT);
        // Player die
        assertTrue(player.getHealth() <= 0);
        // player is removed from dungeon
        assertTrue(dungeon.getEntity(player.getId()) == null);
    }

    @Test
    @DisplayName("mind control mercenary")
    public void mindControlMercenary(){
        Entity.clearIdCount();
        // generate dungeon
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_mercenary_mindControl", "c_mercenary_bribe");
        Dungeon dungeon = dmc.getDungeon();
        Player player = dungeon.getPlayer();
        Mercenary mercenary = (Mercenary) dungeon.getEntity(new Position(10, 15), "mercenary");
        
        // player move right 3 block which collect treasure, wood and sunstone
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        // build sceptre
        assertDoesNotThrow(() -> dmc.build("sceptre"));
        
        // player try to mind control mercenary again -- success because player has sceptre even though not within bride radius
        assertDoesNotThrow(() -> dmc.interact(mercenary.getId()));
        assertEquals(false, mercenary.isEnemy());
        // mind controlled duration 3 -> 2 
        dmc.tick(Direction.DOWN);
        // mind controlled duration 2 -> 1 
        dmc.tick(Direction.DOWN);
        // mercenary is still mind controlled
        assertEquals(player.getPrePosition(), mercenary.getPosition());
        assertTrue(!mercenary.isEnemy());

        // mind controlled duration 1 -> 0, mercenary is now back to hostile 
        dmc.tick(Direction.DOWN);
        assertTrue(mercenary.isEnemy());

        // player move up, battle starts
        dmc.tick(Direction.UP);
        // mercenary die
        assertTrue(mercenary.getHealth() <= 0);
        // mercenary is removed from dungeon
        assertTrue(dungeon.getEntity(mercenary.getId()) == null);
    }
    
    @Test
    @DisplayName("Test invisible player")
    public void invisiblePlayerTest() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_mercenary_player", 
        "c_movementTest_testMovementDown");
        Dungeon dungeon = dmc.getDungeon();
        Player player = dungeon.getPlayer();
        InvisibilityPotion potion = new InvisibilityPotion(
            new Position(5, 5), "invisibility_potion",
            10);
        player.collect(potion, dungeon);
        player.consume(potion.getId(), dungeon);

        Mercenary mercenary = (Mercenary)dungeon.getEntity(
            new Position(7, 7)
        );
        Dungeon secondDungeon = new Dungeon(
            "d_mercenary_player",
            "c_movementTest_testMovementDown"
        );
        Mercenary secondMercenary = (Mercenary)secondDungeon.getEntity(
            new Position(7, 7)
        );

        mercenary.setRandom(new Random(20));
        secondMercenary.setRandom(new Random(20));
        for (int i = 0; i < 10; i++) {
            mercenary.tick(dungeon);
            secondMercenary.moveRandom(secondDungeon);
            assertEquals(secondMercenary.getPosition(), mercenary.getPosition());
        }
    }

    @Test
    @DisplayName("Test invincible player")
    public void invinciblePlayerTest() {
        Entity.clearIdCount();
        Dungeon real = new Dungeon(
            "d_mercenary_player",
            "c_movementTest_testMovementDown"
        );
        Dungeon expected = new Dungeon(
            "d_mercenary_player",
            "c_movementTest_testMovementDown"
        );
        InvincibilityPotion potion = new InvincibilityPotion(
            new Position(5, 5),
            "invincibility_potion",
            10
        );

        Player player = real.getPlayer();
        player.collect(potion, real);
        player.consume(potion.getId(), real);

        Mercenary mercenaryReal = (Mercenary)real.getEntity(
            new Position(7, 7)
        );

        Mercenary mercenaryExpected = (Mercenary)expected.getEntity(
            new Position(7, 7)
        );

        for (int i = 0; i < 10; i++) {
            mercenaryReal.tick(real);
            mercenaryExpected.moveAway(player, expected);
            assertEquals(
                mercenaryExpected.getPosition(),
                mercenaryReal.getPosition()
            );
        }
    }
}
