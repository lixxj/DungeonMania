package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Player.Player;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Spider;
import dungeonmania.util.Position;

public class SipderTest {
    @Test
    @DisplayName("Test Spider movement and spider spawn")
    public void testSpiderMovement() {
        Entity.clearIdCount();
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("boulders", "simple");
        Dungeon dungeon = dmc.getDungeon();
        Spider spider = new Spider(new Position(2, 1), "spider", 3, 1, 3);
        int size = dungeon.getEntities().size();
        spider.move(dungeon);
        assertEquals(new Position(2, 0), spider.getPosition());
        spider.move(dungeon);
        assertEquals(new Position(3, 0), spider.getPosition());
        spider.move(dungeon);
        assertEquals(new Position(3, 1), spider.getPosition());
        spider.move(dungeon);
        assertEquals(new Position(3, 0), spider.getPosition());
        // new spider has been spawned
        int size2 = dungeon.getEntities().size();
        assertEquals(size + 1, size2);
    }

    @Test
    @DisplayName("Test Spider attacked by player")
    public void testPlayerAttackSpider() {
        Entity.clearIdCount();
        Spider spider = new Spider(new Position(1, 1), "spider", 5, 2, 0);
        Player player = new Player(new Position(1, 1), "player", 20, 25);
        player.attack(spider);
        assertEquals(0, spider.getHealth());
    }

    @Test
    @DisplayName("Test Spider attack player")
    public void testSpiderAttack() {
        Entity.clearIdCount();
        Spider spider = new Spider(new Position(1, 1), "spider", 5, 10, 0);
        Player player = new Player(new Position(1, 1), "player", 20, 25);
        spider.attack(player);
        assertEquals(19, player.getHealth());
    }



}
