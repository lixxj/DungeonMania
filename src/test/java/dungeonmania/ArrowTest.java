package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntity.Arrow;
import dungeonmania.entity.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class ArrowTest {
    @Test
    @DisplayName("Test create arrow")
    public void testCreateArrow() {
        Entity.clearIdCount();
        Arrow arrow = new Arrow(new Position(1, 1), "arrow");
        EntityResponse expect = new EntityResponse("1", "arrow", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(arrow.getId(), arrow.getType(), arrow.getPosition(), arrow.isInteractable());
        assertTrue(expect.equals(actual));
    }
}
