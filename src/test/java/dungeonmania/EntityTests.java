package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Door;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Wall;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;


public class EntityTests {
    @Test
    @DisplayName("Test create entity")
    public void testCreateEntity() {
        Entity.clearIdCount();
        Entity e = new Entity(new Position(1, 1), "Spider");
        EntityResponse expectEntity = new EntityResponse("1", "Spider", new Position(1, 1), false);
        EntityResponse actualEentiry = new EntityResponse(e.getId(), e.getType(), e.getPosition(), e.isInteractable());
        assertEquals(e.getId(), "1");
        assertTrue(expectEntity.equals(actualEentiry));
    }

    @Test
    @DisplayName("Test open square for door")
    public void testOpenSquare_door() {
        Entity.clearIdCount();
        Door door = new Door(new Position(2, 1), 1);
        assertFalse(door.isOpenSquare());
        door.open(1);
        assertTrue(door.isOpenSquare());
    }

    @Test
    @DisplayName("Test open square for wall")
    public void testOpenSquare_wall() {
        Entity.clearIdCount();
        Wall w = new Wall(new Position(2, 1));
        assertFalse(w.isOpenSquare());
    }
}