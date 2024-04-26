package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Player.Player;
import dungeonmania.entity.Assassin;
import dungeonmania.entity.Entity;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AssassinTest {
    @Test
    @DisplayName("Test Assassin Hostile Movement - dijkstra") 
    public void testAssassinHostileMovement() {
        Entity.clearIdCount();
        Dungeon dungeon = new Dungeon("d_assassin_movement", "c_assassin_test");
        Assassin assassin = (Assassin) dungeon.getEntity(new Position(3, 3));
        assertEquals(new Position(3, 3), assassin.getPosition());
        assassin.move(dungeon);
        assertEquals(new Position(3, 2), assassin.getPosition());
        assassin.move(dungeon);
        assertEquals(new Position(4, 2), assassin.getPosition());
        assassin.move(dungeon);
        assertEquals(new Position(5, 2), assassin.getPosition());
    }

    @Test
    @DisplayName("Test assassin interaction with player")
    public void testAssassinInterationWithPlayer() throws IllegalArgumentException, InvalidActionException {
        Entity.clearIdCount();
        // generate dungeon
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_assassin_test", "c_assassin_test");
        Dungeon dungeon = dmc.getDungeon();
        Player player = dungeon.getPlayer();
        Assassin assassin = (Assassin) dungeon.getEntity(new Position(19, 19));
        
        // player move right 3 block which collect 3 treasures
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // player try to bribe Assassin -- failed cuz not within radius
        assertThrows(Exception.class, () -> {dmc.interact(assassin.getId());});
        assertEquals(false, player.bribe(assassin));
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        // player try to bribe Assassin again -- within radius but may fail due to assassin_bribe_fail_rate
        if (player.bribe(assassin)) {
            // success
            // check if assassin follow player after bribed
            dmc.tick(Direction.DOWN);
            assertEquals(player.getPrePosition(), assassin.getPosition());
        } else {
            // failed 
            assertTrue(assassin.isEnemy());
        }
    }

    @Test
    @DisplayName("Test assassin fight against player -- assassin die")
    public void testAssassinFightPlayer_AssassinDie() {
        Entity.clearIdCount();
        // generate dungeon
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_assassin_fight", "c_assassin_basicFight_assassinDie");
        Dungeon dungeon = dmc.getDungeon();
        Assassin assassin = (Assassin) dungeon.getEntity(new Position(6, 6), "assassin");

        // player move to right, battle starts
        dmc.tick(Direction.RIGHT);
        // Assassin die
        assertTrue(assassin.getHealth() <= 0);
        // Assassin is removed from dungeon
        assertTrue(dungeon.getEntity(assassin.getId()) == null);
    }

    @Test
    @DisplayName("Test assassin fight against Player -- player die")
    public void testAssassinFightPlayer_PlayerDie() {
        Entity.clearIdCount();
        // generate dungeon
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_assassin_fight", "c_assassin_basicFight_playerDie");
        Dungeon dungeon = dmc.getDungeon();
        Player player = dungeon.getPlayer();
        // player move to right, battle starts
        dmc.tick(Direction.RIGHT);
        // Player die
        assertTrue(player.getHealth() <= 0);
        // Player is removed from dungeon
        assertTrue(dungeon.getEntity(player.getId()) == null);
    }

    @Test
    @DisplayName("Test mind control assassin")
    public void testMindControlAssassin() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_assassin_test", "c_assassin_test");
        Dungeon dungeon = dmc.getDungeon();
        Player player = dungeon.getPlayer();
        Assassin assassin = (Assassin) dungeon.getEntity(new Position(19, 19));

        // player moves to collect treasure, wood and sunstone
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
    
        // build sceptre
        assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertTrue(player.getInventory().hasItem("sceptre"));
        // player try to mind control assassin -- success because player has sceptre
        assertDoesNotThrow(() -> dmc.interact(assassin.getId()));
        assertEquals(false, assassin.isEnemy());
        // mind controlled duration 3 -> 2 and assassin is teleported to player's pre position
        dmc.tick(Direction.DOWN);
        assertEquals(player.getPrePosition(), assassin.getPosition());
        // mind controlled duration 2 -> 1 
        dmc.tick(Direction.DOWN);
        assertEquals(player.getPrePosition(), assassin.getPosition());
        // assassin is still mind controlled
        assertTrue(!assassin.isEnemy());

        // mind controlled duration 1 -> 0, assassin is now back to hostile 
        dmc.tick(Direction.DOWN);
        assertTrue(assassin.isEnemy());

        // player move up, battle starts
        dmc.tick(Direction.UP);
        // assassin die
        assertTrue(assassin.getHealth() <= 0);
        // assassin is removed from dungeon
        assertTrue(dungeon.getEntity(assassin.getId()) == null);
    }
}


