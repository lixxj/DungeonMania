package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Entity;
import dungeonmania.entity.Wall;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class WallTest {
    @Test
    @DisplayName("Test create Wall")
    public void testCreateWall() {
        Entity.clearIdCount();
        Wall wall = new Wall(new Position(1, 1));
        EntityResponse expect = new EntityResponse("1", "wall", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(wall.getId(), wall.getType(), wall.getPosition(), wall.isInteractable());
        assertEquals(wall.getId(), "1");
        assertFalse(wall.isCrossAble());
        assertTrue(expect.equals(actual));
    }
}
