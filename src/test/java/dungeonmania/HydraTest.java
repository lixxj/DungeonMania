package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Player.Player;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Hydra;
import dungeonmania.util.Position;

public class HydraTest {
    @Test
    @DisplayName("Test the case when health increase rate = 1/0 (always/never increase health)")
    public void testHealthIncreaseRate1AndRate0() {
        Entity.clearIdCount();
        Hydra hydra = new Hydra(new Position(4, 4), "hydra", 10, 4, 1, 1);
        assertTrue(hydra.isHealthIncrease());
        Hydra hydra2 = new Hydra(new Position(4, 4), "hydra", 10, 4, 0, 1);
        assertFalse(hydra2.isHealthIncrease());
    }

    @Test
    @DisplayName("Test player attack hydra")
    public void testPlayerAttackHydra() {
        Entity.clearIdCount();
        Hydra hydra = new Hydra(new Position(4, 4), "hydra", 10, 4, 0, 1);
        Player player = new Player(new Position(4, 4), "player", 10, 10);
        player.attack(hydra);
        assertEquals(hydra.getHealth(), 8);
    }
}
