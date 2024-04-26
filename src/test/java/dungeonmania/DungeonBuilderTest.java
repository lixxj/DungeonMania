package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class DungeonBuilderTest {
    @Test
    @DisplayName("Test generate random maze")
    public void testGenerateRandomMaze() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.generateDungeon(0, 0, 10, 10, "simple");
        Dungeon dungeon = dmc.getDungeon();
        Entity player = dungeon.getEntity(new Position(1, 1), "player");
        Entity exit = dungeon.getEntity(new Position(11, 11), "exit");
        assertNotEquals(null, player);
        assertNotEquals(null, exit);
    }

    @Test
    @DisplayName("Test throw exception")
    public void testException() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(Exception.class, () -> {dmc.generateDungeon(0, 0, 10, 10, "simsimsimple");});
    }
}