package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.BuildableEntity.MidnightArmour;
import dungeonmania.BuildableEntity.Shield;
import dungeonmania.CollectableEntity.CollectableEntity;
import dungeonmania.CollectableEntity.SunStone;
import dungeonmania.CollectableEntity.Treasure;
import dungeonmania.CollectableEntity.Wood;
import dungeonmania.Player.Inventory;
import dungeonmania.Player.Player;
import dungeonmania.entity.Entity;
import dungeonmania.entity.Mercenary;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BuildableEntityTest {
    @Test
    @DisplayName("Sceptre crafted with wood")
    public void sceptreCraftWoodTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        for (Entity e : dungeon.getEntityList(new Position(4, 4))) {
            if (e instanceof CollectableEntity) {
                player.collect(e, dungeon);
            }
        }

        player.build("sceptre", dungeon.getConfigName());
    
        Inventory inventory = player.getInventory();
        assertTrue(inventory.hasItem("sceptre"));
    }

    @Test
    @DisplayName("Sceptre crafted with arrows")
    public void sceptreCraftArrowsTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "key"
            ),
            dungeon
        );
        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "sun_stone"
            ),
            dungeon
        );
        player.move(Direction.RIGHT);
        for (int i = 0; i < 2; i++) {
            player.collect(
                dungeon.getEntity(new Position(5, 4), "arrow"),
                dungeon
            );
        }

        player.build("sceptre", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertTrue(inventory.hasItem("sceptre"));
    }

    @Test
    @DisplayName("Sceptre not crafted no wood or arrows.")
    public void sceptreCraftNoWoodNoArrowsTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "key"
            ),
            dungeon
        );

        player.build("sceptre", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertFalse(inventory.hasItem("sceptre"));
    }

    @Test
    @DisplayName("Sceptre crafted with two sunstones.")
    public void sceptreCraftTwoSunstones() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "wood"
            ),
            dungeon
        );
        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "sun_stone"
            ),
            dungeon
        );
        player.move(Direction.RIGHT);
        player.collect(
            dungeon.getEntity(
                new Position(5, 4),
                "sun_stone"
            ),
            dungeon
        );

        player.build("sceptre", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertTrue(inventory.hasItem("sceptre"));
    }

    @Test
    @DisplayName("Sceptre crafted with treasure.")
    public void sceptreCraftTreasure() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "wood"
            ),
            dungeon
        );
        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "sun_stone"
            ),
            dungeon
        );
        player.move(Direction.RIGHT);
        player.collect(
            dungeon.getEntity(
                new Position(5, 4),
                "treasure"
            ),
            dungeon
        );

        player.build("sceptre", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertTrue(inventory.hasItem("sceptre"));
    }

    @Test
    @DisplayName("Sceptre not crafted no treasure no key")
    public void sceptreCraftNoTreasureNoKey() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "wood"
            ),
            dungeon
        );
        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "sun_stone"
            ),
            dungeon
        );

        player.build("sceptre", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertFalse(inventory.hasItem("sceptre"));
    }

    @Test
    @DisplayName("Sceptre used for mind control.")
    public void sceptreMindControlTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_mercenary_player",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();
        Mercenary mercenary = (Mercenary)dungeon.getEntity(
            new Position(7, 7)
        );

        Wood wood = new Wood(new Position(5, 5), "wood");
        Treasure treasure = new Treasure(
            new Position(5, 5),
            "treasure"
        );
        SunStone sunstone = new SunStone(
            new Position(5, 5),
            "sun_stone"
        );

        player.collect(wood, dungeon);
        player.collect(treasure, dungeon);
        player.collect(sunstone, dungeon);
        player.build("sceptre", dungeon.getConfigName());

        player.bribe(mercenary);

        assertFalse(mercenary.isEnemy());
    }

    @Test
    @DisplayName("MidnightArmour crafted.")
    public void midnightArmourCraftTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "sun_stone"
            ),
            dungeon
        );
        player.move(Direction.RIGHT);
        player.collect(
            dungeon.getEntity(
                new Position(5, 4),
                "sword"
            ),
            dungeon
        );

        player.build("midnight_armour", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertTrue(inventory.hasItem("midnight_armour"));
    }

    @Test
    @DisplayName("MidnightArmour not crafted no sword and no sunstone.")
    public void midnightArmourCraftNoSwordNoSunstone() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.build("midnight_armour", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertFalse(inventory.hasItem("midnight_armour"));
    }

    @Test
    @DisplayName("MidnightArmour not crafted no sword.")
    public void midnightArmourCraftNoSword() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "sun_stone"
            ),
            dungeon
        );

        player.build("midnight_armour", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertFalse(inventory.hasItem("midnight_armour"));
    }

    @Test
    @DisplayName("MidnightArmour not crafted no sunstone.")
    public void midnightArmourCraftNoSunstone() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.move(Direction.RIGHT);
        player.collect(
            dungeon.getEntity(
                new Position(5, 4),
                "sword"
            ),
            dungeon
        );

        player.build("midnight_armour", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertFalse(inventory.hasItem("midnight_armour"));
    }

    @Test
    @DisplayName("MidnightArmour attack.")
    public void midnightArmourAttackTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();
        
        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "sun_stone"
            ),
            dungeon
        );
        player.move(Direction.RIGHT);
        player.collect(
            dungeon.getEntity(
                new Position(5, 4),
                "sword"
            ),
            dungeon
        );

        player.build("midnight_armour", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        MidnightArmour armour = (MidnightArmour)inventory
                                                .getBuildItems()
                                                .get(0);
        assertEquals(1, armour.getArmourAttack());
    }

    @Test
    @DisplayName("Shield crafted with sunstone.")
    public void shieldCraftSunstoneTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "wood"
            ),
            dungeon
        );
        player.move(Direction.RIGHT);
        player.collect(
            dungeon.getEntity(
                new Position(5, 4),
                "wood"
            ),
            dungeon
        );
        player.collect(
            dungeon.getEntity(
                new Position(5, 4),
                "sun_stone"
            ),
            dungeon
        );

        player.build("shield", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertTrue(inventory.hasItem("shield"));
    }

    @Test
    @DisplayName("Shield not crafted only wood.")
    public void shieldCraftOnlyWoodTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "wood"
            ),
            dungeon
        );
        player.move(Direction.RIGHT);
        player.collect(
            dungeon.getEntity(
                new Position(5, 4),
                "wood"
            ),
            dungeon
        );

        player.build("shield", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertFalse(inventory.hasItem("shield"));
    }

    @Test
    @DisplayName("Shield defence.")
    public void shieldDefenceTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "wood"
            ),
            dungeon
        );
        player.move(Direction.RIGHT);
        player.collect(
            dungeon.getEntity(
                new Position(5, 4),
                "wood"
            ),
            dungeon
        );
        player.collect(
            dungeon.getEntity(
                new Position(5, 4),
                "sun_stone"
            ),
            dungeon
        );

        player.build("shield", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        Shield shield = (Shield)inventory.getBuildItems().get(0);

        assertEquals(1, shield.getDefence());
    }

    @Test
    @DisplayName("Bow not crafted no wood no arrows.")
    public void bowCraftNoWoodNoArrows() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();
        player.build("bow", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertFalse(inventory.hasItem("bow"));
    }

    @Test
    @DisplayName("Bow not crafted no arrows.")
    public void bowCraftNoArrowsTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.collect(
            dungeon.getEntity(
                new Position(4, 4),
                "wood"
            ),
            dungeon
        );

        player.build("bow", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertFalse(inventory.hasItem("bow"));
    }

    @Test
    @DisplayName("Bow not crafted no wood.")
    public void bowCraftNoWoodTest() {
        Entity.clearIdCount();

        Dungeon dungeon = new Dungeon(
            "d_buildable_entities",
            "c_buildable_entities"
        );

        Player player = dungeon.getPlayer();

        player.move(Direction.RIGHT);
        for (int i = 0; i < 3; i++) {
            player.collect(
                dungeon.getEntity(
                    new Position(5, 4),
                    "arrow"
                ),
                dungeon
            );
        }

        player.build("bow", dungeon.getConfigName());

        Inventory inventory = player.getInventory();
        assertFalse(inventory.hasItem("bow"));
    }
}
