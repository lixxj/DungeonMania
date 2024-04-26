package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Entity;
import dungeonmania.util.ExtractFromJson;


public class DungeonTest {
    @Test
    @DisplayName("Test creat new dungeon with correct entities")
    public void testDuneonInitialise() {
        // clear the idCount that may appear in previous tests
        Entity.clearIdCount();
        Dungeon.clearDungeonIdCount();
        List<Entity> expectEntities = ExtractFromJson.getEntityFromJson("2_doors", "c_movementTest_testMovementDown");
        Dungeon dungeon = new Dungeon("2_doors", "c_movementTest_testMovementDown");
        List<Entity> entities = dungeon.getEntitiesNonStatic();
        for (int i = 0; i < entities.size(); i++) {
            assertEquals(expectEntities.get(i).getType(), entities.get(i).getType());
        }
        assertEquals("2_doors", dungeon.getDungeonName());
    }
}
