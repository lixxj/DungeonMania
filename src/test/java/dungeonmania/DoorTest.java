package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Door;
import dungeonmania.entity.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class DoorTest {
    @Test
    @DisplayName("Test create Door")
    public void testCreateDoor() {
        Entity.clearIdCount();
        Door door = new Door(new Position(1, 1), 1);
        EntityResponse expect = new EntityResponse("1", "door", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(door.getId(), door.getType(), door.getPosition(), door.isInteractable());
        assertTrue(expect.equals(actual));
    }

    @Test
    @DisplayName("Test open Door")
    public void testOpenDoor() {
        Entity.clearIdCount();
        Door door = new Door(new Position(1, 1), 1);
        door.open(1);
        assertTrue(door.isOpen());
    }

    @Test
    @DisplayName("Test open Door with incorrect key")
    public void testOpenDoorWithIncorrectKey() {
        Entity.clearIdCount();
        Door door = new Door(new Position(1, 1), 1);
        door.open(2);
        assertFalse(door.isOpen());
    }
}
