package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.Entity;
import dungeonmania.goal.Goal;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.ExtractFromJson;

public class ExtractFromJsonTest {
    @Test
    @DisplayName("Test extract the right thing from json file")
    public void testLoadInfoFromJson() {
        // clear the idCount that may appear in previous tests
        Entity.clearIdCount();
        List<EntityResponse> erList = ExtractFromJson.getResponsesFromEntities(ExtractFromJson.getEntityFromJson("2_doors", "c_movementTest_testMovementDown"));
        EntityResponse er = erList.get(0);
        EntityResponse expectEr = new EntityResponse("1", "player", new Position(2, 2), false);
        assertTrue(er.equals(expectEr));
    }

    @Test
    @DisplayName("Test GetGoal 2_doors")
    public void testGetGoal2_doors() {
        Goal goal = ExtractFromJson.getGoalFromDungeonName("2_doors", "simple");
        assertEquals("exit", goal.getGoalName());
    }

    @Test
    @DisplayName("Test GetGoal advanced")
    public void testGetGoaladvanced() {
        Goal goal = ExtractFromJson.getGoalFromDungeonName("advanced", "simple");
        assertEquals("enemies AND treasure", goal.getGoalName());
    }

    @Test
    @DisplayName("Test GetGoal bombs")
    public void testGetGoalbombs() {
        Goal goal = ExtractFromJson.getGoalFromDungeonName("bombs", "simple");
        assertEquals("exit", goal.getGoalName());
    }

    @Test
    @DisplayName("Test GetGoal boulders")
    public void testGetGoalboulders() {
        Goal goal = ExtractFromJson.getGoalFromDungeonName("boulders", "simple");
        assertEquals("boulders", goal.getGoalName());
    }

    @Test
    @DisplayName("Test GetGoal exit_goal_order")
    public void testGetGoalexit_goal_order() {
        Goal goal = ExtractFromJson.getGoalFromDungeonName("exit_goal_order", "simple");
        assertEquals("boulders AND exit AND treasure", goal.getGoalName());
    }

    @Test
    @DisplayName("Test extract the right thing from json file")
    public void testLoadCollectableInfoFromJson1() {
        // clear the idCount that may appear in previous tests
        Entity.clearIdCount();

        List<Entity> entities = ExtractFromJson.getEntityFromJson("advanced", "c_movementTest_testMovementDown");
        List<Entity> collectableEntities = entities.stream().filter (e -> e.isCollectable()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        List<EntityResponse> actualList = ExtractFromJson.getResponsesFromEntities(collectableEntities);

        EntityResponse actual = actualList.get(0);
        EntityResponse expected = new EntityResponse("2", "invincibility_potion", new Position(2, 1), false);
        assertTrue(actual.equals(expected));

        EntityResponse actual2 = actualList.get(1);
        EntityResponse expected2 = new EntityResponse("3", "invisibility_potion", new Position(3, 1), false);
        assertTrue(actual2.equals(expected2));

        EntityResponse actual3 = actualList.get(2);
        EntityResponse expected3 = new EntityResponse("23", "sword", new Position(6, 1), false);
        assertTrue(actual3.equals(expected3));

    }

    @Test
    @DisplayName("Test extract the right thing from json file")
    public void testLoadCollectableInfoFromJson2() {
        // clear the idCount that may appear in previous tests
        Entity.clearIdCount();
        List<EntityResponse> actualList = ExtractFromJson.getResponsesFromEntities(ExtractFromJson.getEntityFromJson("2_doors", "c_movementTest_testMovementDown"));
        EntityResponse actual = actualList.get(17);
        EntityResponse expected = new EntityResponse("18", "key", new Position(2, 3), false);
        assertTrue(actual.equals(expected));
        EntityResponse actual2 = actualList.get(18);
        EntityResponse expected2 = new EntityResponse("19", "key", new Position(3, 2), false);
        assertTrue(actual2.equals(expected2));
    }

    @Test
    @DisplayName("Test GetGoal")
    public void testGetGoal() {
        Goal goal = ExtractFromJson.getGoalFromDungeonName("d_complexGoalsTest_andAll", "c_complexGoalsTest_andAll");
        
        assertEquals("exit AND treasure AND boulders AND enemies", goal.getGoalName());
    }

}
