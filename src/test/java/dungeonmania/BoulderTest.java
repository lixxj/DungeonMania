package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Boulder;
import dungeonmania.entity.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BoulderTest {
    @Test
    @DisplayName("Test create boulder")
    public void testCreateBoulder() {
        Entity.clearIdCount();
        Boulder boulder = new Boulder(new Position(1, 1));
        EntityResponse expect = new EntityResponse("1", "boulder", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(boulder.getId(), boulder.getType(), boulder.getPosition(), boulder.isInteractable());
        assertEquals(boulder.getId(), "1");
        assertEquals(boulder.getType(), "boulder");
        assertFalse(boulder.isCrossAble());
        assertEquals(boulder.getPosition(), new Position(1, 1));
        assertEquals(false, boulder.isInteractable());
        assertTrue(expect.equals(actual));
    }

    @Test
    @DisplayName("Test push boulder")
    public void testPushBoulder() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("boulders", "simple");
        Dungeon dungeon = dmc.getDungeon();
        Boulder boulder = (Boulder)dungeon.getEntity(new Position(3, 2), "boulder");
        assertNotEquals(null, boulder);
        boulder.push(Direction.LEFT, dungeon);
        assertEquals(boulder.getPosition(), new Position(2, 2));
    }
}
