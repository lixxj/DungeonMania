package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntity.Arrow;
import dungeonmania.CollectableEntity.Bomb;
import dungeonmania.CollectableEntity.InvincibilityPotion;
import dungeonmania.CollectableEntity.InvisibilityPotion;
import dungeonmania.CollectableEntity.Key;
import dungeonmania.CollectableEntity.Sword;
import dungeonmania.CollectableEntity.Treasure;
import dungeonmania.CollectableEntity.Wood;
import dungeonmania.entity.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class CollectableEntityTests {

    // Unit Tests
    
    @Test
    @DisplayName("Create an arrow")
    public void testCreateArrow() {
        
        // Clear the entity list
        Entity.clearIdCount();
        
        Arrow arrow = new Arrow(new Position(1, 1), "arrow");
        EntityResponse expected = new EntityResponse("1", "arrow", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(arrow.getId(), arrow.getType(), arrow.getPosition(), arrow.isInteractable());
        assertEquals(arrow.getId(), "1");
        assertEquals(arrow.getType(), "arrow");
        assertEquals(arrow.getPosition(), new Position(1, 1));
        assertEquals(arrow.isInteractable(), false);
        assertTrue(expected.equals(actual));
    }
    
    @Test
    @DisplayName("Create a bomb")
    public void testCreateBomb() {

        // Clear the entity list
        Entity.clearIdCount();

        Bomb bomb = new Bomb(new Position(1, 1), "bomb", 1);
        EntityResponse expected = new EntityResponse("1", "bomb", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(bomb.getId(), bomb.getType(), bomb.getPosition(), bomb.isInteractable());
        assertEquals(bomb.getId(), "1");
        assertEquals(bomb.getBombRadius(), 1);
        assertTrue(expected.equals(actual));
    }

    @Test
    @DisplayName("Create an Invincibility Potion")
    public void testCreateInvincinbilityPotion() {

        // Clear the entity list
        Entity.clearIdCount();

        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(new Position(1, 1), "invincibility_potion", 1);
        EntityResponse expected = new EntityResponse("1", "invincibility_potion", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(invincibilityPotion.getId(), invincibilityPotion.getType(), invincibilityPotion.getPosition(), invincibilityPotion.isInteractable());
        assertEquals(invincibilityPotion.getId(), "1");
        assertEquals(invincibilityPotion.getInvincibility_potion_duration(), 1);
        assertTrue(expected.equals(actual));
    }

    @Test
    @DisplayName("Create an Invisibility Potion")
    public void testCreateInvisibilityPotion() {

        // Clear the entity list
        Entity.clearIdCount();

        InvisibilityPotion invisibilityPotion = new InvisibilityPotion(new Position(1, 1), "invisibility_potion", 1);
        EntityResponse expected = new EntityResponse("1", "invisibility_potion", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(invisibilityPotion.getId(), invisibilityPotion.getType(), invisibilityPotion.getPosition(), invisibilityPotion.isInteractable());
        assertEquals(invisibilityPotion.getId(), "1");
        assertEquals(invisibilityPotion.getInvisibility_potion_duration(), 1);
        assertTrue(expected.equals(actual));
    }

    @Test
    @DisplayName("Create a key")
    public void testCreateKey() {

        // Clear the entity list
        Entity.clearIdCount();

        Key key = new Key(new Position(1, 1), "key", 1);
        EntityResponse expected = new EntityResponse("1", "key", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(key.getId(), key.getType(), key.getPosition(), key.isInteractable());
        assertEquals(key.getKey(), 1);
        assertTrue(expected.equals(actual));
    }

    @Test
    @DisplayName("Create a sword")
    public void testCreateSword() {

        // Clear the entity list
        Entity.clearIdCount();

        Sword sword = new Sword(new Position(1, 1), "sword", 1, 2);
        EntityResponse expected = new EntityResponse("1", "sword", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(sword.getId(), sword.getType(), sword.getPosition(), sword.isInteractable());
        assertEquals(sword.getId(), "1");
        assertEquals(sword.getSwordAttack(), 1);
        assertEquals(sword.getSwordDurability(), 2);
        assertTrue(expected.equals(actual));
    }

    @Test
    @DisplayName("Create a treasure")
    public void testCreateTreasure() {

        // Clear the entity list
        Entity.clearIdCount();
        Treasure treasure = new Treasure(new Position(1, 1), "treasure");
        EntityResponse expected = new EntityResponse("1", "treasure", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(treasure.getId(), treasure.getType(), treasure.getPosition(), treasure.isInteractable());
        assertEquals(treasure.getId(), "1");
        assertTrue(expected.equals(actual));
    }

    @Test
    @DisplayName("Create a wood")
    public void testCreateWood() {

        // Clear the entity list
        Entity.clearIdCount();
        Wood wood = new Wood(new Position(1, 1), "wood");
        EntityResponse expected = new EntityResponse("1", "wood", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(wood.getId(), wood.getType(), wood.getPosition(), wood.isInteractable());
        assertEquals(wood.getId(), "1");
        assertTrue(expected.equals(actual));
    }

    @Test
    @DisplayName("Test id numbering of enties")
    public void testIdOfEntities() {

        // Clear the entity list
        Entity.clearIdCount();
        Wood wood = new Wood(new Position(1, 1), "wood");
        EntityResponse expected = new EntityResponse("1", "wood", new Position(1, 1), false);
        EntityResponse actual = new EntityResponse(wood.getId(), wood.getType(), wood.getPosition(), wood.isInteractable());
        assertEquals(wood.getId(), "1");
        assertTrue(expected.equals(actual));

        // Create treasure
        Treasure treasure = new Treasure(new Position(1, 1), "treasure");
        EntityResponse expected1 = new EntityResponse("2", "treasure", new Position(1, 1), false);
        EntityResponse actual1 = new EntityResponse(treasure.getId(), treasure.getType(), treasure.getPosition(), treasure.isInteractable());
        assertEquals(treasure.getId(), "2");
        assertTrue(expected1.equals(actual1));

        // Create sword
        Sword sword = new Sword(new Position(1, 1), "sword", 1, 2);
        EntityResponse expected2 = new EntityResponse("3", "sword", new Position(1, 1), false);
        EntityResponse actual2 = new EntityResponse(sword.getId(), sword.getType(), sword.getPosition(), sword.isInteractable());
        assertEquals(sword.getId(), "3");
        assertTrue(expected2.equals(actual2));
    }
    
}
