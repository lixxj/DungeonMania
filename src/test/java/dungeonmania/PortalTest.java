package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Entity;
import dungeonmania.Player.Player;
import dungeonmania.entity.Portal;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PortalTest {
    @Test
    @DisplayName("Test teleport player")
    public void testTeleportPlayer() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("portals", "simple");
        Dungeon dungeon = dmc.getDungeon();
        Portal portal = (Portal)dungeon.getEntity(new Position(1, 0), "portal");
        assertNotEquals(null, portal);
        Player player = (Player)dungeon.getEntity(new Position(3, 0), "player");
        assertNotEquals(null, player);
        Position newPos = portal.teleport(dungeon, Direction.UP);
        player.setPosition(newPos);
        assertEquals(player.getPosition(), new Position(1, 1));
    }

    @Test
    @DisplayName("Test teleport player in advanced json file")
    public void testTeleportPlayerInAdvanced() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("portals_advanced", "simple");
        Dungeon dungeon = dmc.getDungeon();
        Portal portal = (Portal)dungeon.getEntity(new Position(1, 5), "portal");
        assertNotEquals(null, portal);
        Player player = (Player)dungeon.getEntity(new Position(3, 0), "player");
        assertNotEquals(null, player);
        Position newPos = portal.teleport(dungeon, Direction.DOWN);
        player.setPosition(newPos);
        assertEquals(player.getPosition(), new Position(2, 4));
    }

}
