package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

public class DMCTests {
    
    // test template
    @Test
    @DisplayName("")
    public void Test() {
        
    }

    @Test
    public void invalidDungeonNameTest() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class ,() -> {
            dungeonManiaController.newGame("Invalid dungeonName", "Invalid configName");
        });
    }

    @Test
    public void invalidCongigNameTest() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class ,() -> {
            dungeonManiaController.newGame("advanced-2", "Invalid configName");
        });
    }

    @Test
    public void invalidInteractTest() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("2_doors", "simple");
        assertThrows(IllegalArgumentException.class ,() -> {
            dungeonManiaController.interact("Invalid ID");
        });
    }

    @Test
    public void invalidBuildTest() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("2_doors", "simple");
        assertThrows(IllegalArgumentException.class ,() -> {
            dungeonManiaController.build("sword");
        });
    }

    @Test
    public void testInteractTooFarToDestroy() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        DungeonResponse response =  dungeonManiaController.newGame("zombies", "simple");
        EntityResponse zombieToastSpawner = response.getEntities().stream().filter(entityResponse -> entityResponse.getType().equals("zombie_toast_spawner")).findFirst().orElse(null);
        assertThrows(InvalidActionException.class ,() -> {
            dungeonManiaController.interact(zombieToastSpawner.getId());
        });
    }

    @Test
    public void testInteractTooFarToBribe() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        DungeonResponse response =  dungeonManiaController.newGame("mercenary", "simple");
        EntityResponse mercenary = response.getEntities().stream().filter(entityResponse -> entityResponse.getType().equals("mercenary")).findFirst().orElse(null);
        assertThrows(InvalidActionException.class ,() -> {
            dungeonManiaController.interact(mercenary.getId());
        });
    }

    @Test
    public void testBuildInsufficientItemsForCrafting() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("build_bow", "simple");
        assertThrows(InvalidActionException.class ,() -> {
            dungeonManiaController.build("bow");
        });
    }

    @Test
    public void testTickItemUsedNotInInventory() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced", "simple");
        assertThrows(InvalidActionException.class ,() -> {
            dungeonManiaController.tick("Invalid item");
        });
    }
}
