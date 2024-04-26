package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Entity;
import dungeonmania.entity.Exit;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class ExitTest {
    @Test
    @DisplayName("Test create exit")
    public void testCreateExit() {
        Entity.clearIdCount();
        Exit e = new Exit(new Position(1, 1));
        EntityResponse expect = new EntityResponse("1", "exit", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(e.getId(), e.getType(), e.getPosition(), e.isInteractable());
        assertEquals(e.getId(), "1");
        assertTrue(e.isCrossAble());
        assertTrue(expect.equals(actual));
    }
}
