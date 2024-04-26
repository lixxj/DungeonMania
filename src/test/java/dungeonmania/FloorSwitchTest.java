package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Boulder;
import dungeonmania.entity.Entity;
import dungeonmania.entity.FloorSwitch;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FloorSwitchTest {
    @Test
    @DisplayName("Test create FloorSwitch")
    public void testCreateFloorSwitch() {
        Entity.clearIdCount();
        FloorSwitch fs = new FloorSwitch(new Position(1, 1));
        EntityResponse expect = new EntityResponse("1", "switch", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(fs.getId(), fs.getType(), fs.getPosition(), fs.isInteractable());
        assertEquals(fs.getId(), "1");
        assertEquals(fs.getType(), "switch");
        assertTrue(fs.isCrossAble());
        assertEquals(fs.getPosition(), new Position(1, 1));
        assertEquals(false, fs.isInteractable());
        assertTrue(expect.equals(actual));
    }

    @Test
    @DisplayName("Test push boulder to Floor Switch")
    public void testPushBoulderToFloorSwitch() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("boulders", "simple");
        Dungeon dungeon = dmc.getDungeon();
        Boulder boulder = (Boulder)dungeon.getEntity(new Position(3, 2), "boulder");
        assertNotEquals(null, boulder);
        FloorSwitch fs = (FloorSwitch)dungeon.getEntity(new Position(1, 2), "switch");
        assertNotEquals(null, fs);
        boulder.push(Direction.LEFT, dungeon);
        boulder.push(Direction.LEFT, dungeon);
        assertEquals(boulder.getPosition(), new Position(1, 2));
        assertTrue(fs.isFloorSwitchOn());
    }

    @Test
    @DisplayName("Test push boulder off the Floor Switch")
    public void testPushBoulderOffFloorSwitch() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("boulders", "simple");
        Dungeon dungeon = dmc.getDungeon();
        Boulder boulder = (Boulder)dungeon.getEntity(new Position(4, 6), "boulder");
        assertNotEquals(null, boulder);
        FloorSwitch fs = (FloorSwitch)dungeon.getEntity(new Position(4, 7), "switch");
        assertNotEquals(null, fs);
        boulder.push(Direction.DOWN, dungeon);
        assertTrue(fs.isFloorSwitchOn());
        boulder.push(Direction.RIGHT, dungeon);
        assertFalse(fs.isFloorSwitchOn());
    }
}
