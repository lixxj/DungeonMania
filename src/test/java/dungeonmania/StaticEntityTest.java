package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Entity;
import dungeonmania.entity.StaticEntity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;


public class StaticEntityTest {
    @Test
    @DisplayName("Test create StaticEntity")
    public void testCreateStaticEntity() {
        Entity.clearIdCount();
        StaticEntity se = new StaticEntity(new Position(1, 1), "wall");
        EntityResponse expect = new EntityResponse("1", "wall", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(se.getId(), se.getType(), se.getPosition(), se.isInteractable());
        assertEquals(se.getId(), "1");
        assertTrue(expect.equals(actual));
    }
}