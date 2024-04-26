package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.goal.BouldersGoal;
import dungeonmania.goal.EnemiesGoal;
import dungeonmania.goal.ExitGoal;
import dungeonmania.goal.Goal;
import dungeonmania.goal.TreasureGoal;
import dungeonmania.util.ExtractFromJson;

public class GoalTests {
    @Test
    @DisplayName("Test Boulders Goal 1")
    public void testBouldersGoal1() {
        BouldersGoal BG = new BouldersGoal();
        Dungeon dungeon = new Dungeon("2_doors", "c_movementTest_testMovementDown");
        assertEquals("boulders", BG.getGoalName());
        assertEquals(":boulders", BG.getGoalNameResponse());
        assertEquals(true, BG.getGoalStatus(dungeon));
    }

    @Test
    @DisplayName("Test Boulders Goal 2")
    public void testBouldersGoal2() {
        BouldersGoal BG = new BouldersGoal();
        Dungeon dungeon = new Dungeon("boulders", "c_movementTest_testMovementDown");
        assertEquals("boulders", BG.getGoalName());
        assertEquals(":boulders", BG.getGoalNameResponse());
        assertEquals(false, BG.getGoalStatus(dungeon));
    }

    @Test
    @DisplayName("Test Enemies Goal")
    public void testEnemiesGoal() {
        EnemiesGoal EG = new EnemiesGoal("bomb_radius_2");
        Dungeon dungeon = new Dungeon("2_doors", "bomb_radius_2");
        assertEquals("enemies", EG.getGoalName());
        assertEquals(":enemies", EG.getGoalNameResponse());
        assertEquals(false, EG.getGoalStatus(dungeon));
    }

    @Test
    @DisplayName("Test Enemies Goal readconfig bomb_radius_2")
    public void testEnemiesGoalReadConfigbomb_radius_2() {
        EnemiesGoal EG = new EnemiesGoal("bomb_radius_2");
        assertEquals(1, EG.getN_Enemies());
    }

    @Test
    @DisplayName("Test Enemies Goal readconfig bribe_amount_3")
    public void testEnemiesGoalReadConfigbribe_amount_3() {
        EnemiesGoal EG = new EnemiesGoal("bribe_amount_3");
        assertEquals(1, EG.getN_Enemies());
    }

    @Test
    @DisplayName("Test Enemies Goal readconfig bribe_radius_1")
    public void testEnemiesGoalReadConfigbribe_radius_1() {
        EnemiesGoal EG = new EnemiesGoal("bribe_radius_1");
        assertEquals(1, EG.getN_Enemies());
    }

    @Test
    @DisplayName("Test Enemies Goal readconfig ")
    public void testEnemiesGoalReadConfigno_zombie_spawning() {
        EnemiesGoal EG = new EnemiesGoal("no_zombie_spawning");
        assertEquals(1, EG.getN_Enemies());
    }

    @Test
    @DisplayName("Test Enemies Goal readconfig no_spider_spawning")
    public void testEnemiesGoalReadConfigno_spider_spawning() {
        EnemiesGoal EG = new EnemiesGoal("no_spider_spawning");
        assertEquals(1, EG.getN_Enemies());
    }

    @Test
    @DisplayName("Test Enemies Goal readconfig simple")
    public void testEnemiesGoalReadConfigsimple() {
        EnemiesGoal EG = new EnemiesGoal("simple");
        assertEquals(1, EG.getN_Enemies());
    }

    @Test
    @DisplayName("Test Exit Goal 1")
    public void testExitGoal1() {
        ExitGoal EG = new ExitGoal();
        Dungeon dungeon = new Dungeon("2_doors", "c_movementTest_testMovementDown");
        assertEquals("exit", EG.getGoalName());
        assertEquals(":exit", EG.getGoalNameResponse());
        assertEquals(false, EG.getGoalStatus(dungeon));
    }

    @Test
    @DisplayName("Test Exit Goal 2")
    public void testExitGoal2() {
        ExitGoal EG = new ExitGoal();
        Dungeon dungeon = new Dungeon("advanced", "c_movementTest_testMovementDown");
        assertEquals("exit", EG.getGoalName());
        assertEquals(":exit", EG.getGoalNameResponse());
        assertEquals(true, EG.getGoalStatus(dungeon));
    }

    @Test
    @DisplayName("Test Treasure Goal 1")
    public void testTreasureGoal1() {
        TreasureGoal TG = new TreasureGoal("simple");
        Dungeon dungeon = new Dungeon("advanced", "c_movementTest_testMovementDown");
        assertEquals("treasure", TG.getGoalName());
        assertEquals(":treasure", TG.getGoalNameResponse());
        assertEquals(false, TG.getGoalStatus(dungeon));
    }

    @Test
    @DisplayName("Test Treasure Goal 2")
    public void testTreasureGoal2() {
        TreasureGoal TG = new TreasureGoal("simple");
        Dungeon dungeon = new Dungeon("2_doors", "c_movementTest_testMovementDown");
        assertEquals("treasure", TG.getGoalName());
        assertEquals(":treasure", TG.getGoalNameResponse());
        assertEquals(false, TG.getGoalStatus(dungeon));
    }

    @Test
    @DisplayName("Test Treasure Goal readconfig bomb_radius_2")
    public void testTreasureGoalReadConfigbomb_radius_2() {
        TreasureGoal TG = new TreasureGoal("bomb_radius_2");
        assertEquals(1, TG.getN_Treasure());
    }

    @Test
    @DisplayName("Test Treasure Goal readconfig bribe_amount_3")
    public void testTreasureGoalReadConfigbribe_amount_3() {
        TreasureGoal TG = new TreasureGoal("bribe_amount_3");
        assertEquals(1, TG.getN_Treasure());
    }

    @Test
    @DisplayName("Test Treasure Goal readconfig bribe_radius_1")
    public void testTreasureGoalReadConfigbribe_radius_1() {
        TreasureGoal TG = new TreasureGoal("bribe_radius_1");
        assertEquals(1, TG.getN_Treasure());
    }

    @Test
    @DisplayName("Test Treasure Goal readconfig no_spider_spawning")
    public void testTreasureGoalReadConfigno_spider_spawning() {
        TreasureGoal TG = new TreasureGoal("no_spider_spawning");
        assertEquals(1, TG.getN_Treasure());
    }

    @Test
    @DisplayName("Test Treasure Goal readconfig no_zombie_spawning")
    public void testTreasureGoalReadConfigno_zombie_spawning() {
        TreasureGoal TG = new TreasureGoal("no_zombie_spawning");
        assertEquals(1, TG.getN_Treasure());
    }

    @Test
    @DisplayName("Test Enemies Goal readconfig simple")
    public void testTreasureGoalReadConfigsimple() {
        TreasureGoal TG = new TreasureGoal("simple");
        assertEquals(1, TG.getN_Treasure());
    }

    @Test
    @DisplayName("Test And Goal")
    public void testAndGoal() {
        Goal goal = ExtractFromJson.getGoalFromDungeonName("advanced", "simple");
        Dungeon dungeon = new Dungeon("advanced", "simple");
        assertEquals("enemies AND treasure", goal.getGoalName());
        assertEquals(":enemies AND :treasure", goal.getGoalNameResponse());
        assertEquals(false, goal.getGoalStatus(dungeon));
    }

    @Test
    @DisplayName("Test Or Goal")
    public void testOrGoal() {
        Goal goal = ExtractFromJson.getGoalFromDungeonName("OrGoalTest", "simple");
        Dungeon dungeon = new Dungeon("OrGoalTest", "simple");
        assertEquals("boulders OR treasure", goal.getGoalName());
        assertEquals(":boulders OR :treasure", goal.getGoalNameResponse());
        assertEquals(true, goal.getGoalStatus(dungeon));
        assertEquals(true, goal.getGoalStatus(dungeon));
    }

    @Test
    @DisplayName("Test And & Or Goal")
    public void testComplexGoal1() {
        //TODO
    }

    @Test
    @DisplayName("Test And & And Goal")
    public void testComplexGoal2() {
        Goal goal = ExtractFromJson.getGoalFromDungeonName("exit_goal_order", "simple");
        Dungeon dungeon = new Dungeon("exit_goal_order", "simple");
        assertEquals("boulders AND exit AND treasure", goal.getGoalName());
        assertEquals(":boulders AND :exit AND :treasure", goal.getGoalNameResponse());
        assertEquals(false, goal.getGoalStatus(dungeon));
    }
}
