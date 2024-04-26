package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntity.Bomb;
import dungeonmania.Player.Player;
import dungeonmania.entity.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BombTest {
    @Test
    @DisplayName("Test use bomb when switch active")
    public void testBombWithActiveSwitchAround() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("bombs", "simple");
        Dungeon dungeon = dmc.getDungeon();
        Bomb bomb = (Bomb)dungeon.getEntity(new Position(3, 3), "bomb");
        Player player = dungeon.getPlayer();
        
        // active the swtich by push boulder to the swtich
        dungeon.playerMove(player.getPosition(), Direction.RIGHT);
        dungeon.playerMove(player.getPosition(), Direction.DOWN);
        dungeon.playerMove(player.getPosition(), Direction.RIGHT);
        assertEquals(new Position(4, 3), player.getPosition());
        
        // before the bomb use, there is a wall in pos (5, 2)
        assertEquals("wall", dungeon.getEntity(new Position(5, 2)).getType());
        //e.tick(dungeon);
        player.consume(bomb.getId(), dungeon);
        
        // after the bomb used, the switch and boulder is gone, so as the wall
        assertEquals(null, dungeon.getEntity(new Position(4, 2)));
        assertEquals(null, dungeon.getEntity(new Position(5, 2)));
    }

    @Test
    @DisplayName("Test use bomb when switch not active")
    public void testBombWithNonActiveSwitch() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("bombs", "simple");
        Dungeon dungeon = dmc.getDungeon();
        Bomb bomb = (Bomb)dungeon.getEntity(new Position(3, 3), "bomb");
        Player player = dungeon.getPlayer();
        
        // pick up bomb but do not active switch 
        dungeon.playerMove(player.getPosition(), Direction.DOWN);
        dungeon.playerMove(player.getPosition(), Direction.RIGHT);
        dungeon.playerMove(player.getPosition(), Direction.RIGHT);
        assertEquals(new Position(4, 3), player.getPosition());
        
        // before the bomb use, there is a wall in pos (5, 2)
        assertEquals("wall", dungeon.getEntity(new Position(5, 2)).getType());
        bomb.use(dungeon);
        
        // after the bomb used, the switch, boulder and wall remain becuase the swtich is not active
        assertNotEquals(null, dungeon.getEntity(new Position(4, 2)));
        assertEquals("wall", dungeon.getEntity(new Position(5, 2)).getType());
    }

    @Test
    @DisplayName("Test use bomb when there is no swich around")
    public void testBombWithNonSwitchAround() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("bombs", "simple");
        Dungeon dungeon = dmc.getDungeon();
        Player player = dungeon.getPlayer();

        Bomb bomb = (Bomb)dungeon.getEntity(new Position(3, 3), "bomb");
        assertEquals("wall", dungeon.getEntity(new Position(5, 2)).getType());
        
        // pick up bomb and move to the place where there is no switch around
        dungeon.playerMove(player.getPosition(), Direction.DOWN);
        dungeon.playerMove(player.getPosition(), Direction.RIGHT);
        dungeon.playerMove(player.getPosition(), Direction.DOWN);
        dungeon.playerMove(player.getPosition(), Direction.LEFT);
        dungeon.playerMove(player.getPosition(), Direction.DOWN);
        
        bomb.use(dungeon);
        
        // after the bomb used, the switch, boulder and wall remain becuase the swtich is not active
        assertNotEquals(null, dungeon.getEntity(new Position(4, 2)));
        assertEquals("wall", dungeon.getEntity(new Position(5, 2)).getType());
    }

    @Test
    @DisplayName("Test use bomb with range 2")
    public void testBombWithRange2() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("bombs", "bomb_radius_2");
        Dungeon dungeon = dmc.getDungeon();
        Bomb bomb = (Bomb)dungeon.getEntity(new Position(3, 3), "bomb");
        Player player = dungeon.getPlayer();
        
        // active the swtich by push boulder to the swtich
        dungeon.playerMove(player.getPosition(), Direction.RIGHT);
        dungeon.playerMove(player.getPosition(), Direction.DOWN);
        dungeon.playerMove(player.getPosition(), Direction.RIGHT);
        assertEquals(new Position(4, 3), player.getPosition());
        
        // before the bomb use, there is a wall in pos (5, 2)
        assertEquals("treasure", dungeon.getEntity(new Position(4, 5)).getType());
        bomb.use(dungeon);
        
        // after the bomb used, the switch and boulder is gone, so as the wall
        assertEquals(null, dungeon.getEntity(new Position(4, 5)));
    }
}
