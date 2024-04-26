package dungeonmania.entity;

import dungeonmania.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity{
    private double zombie_attack;
    private double zombie_health;
    private double zombie_spawn_rate;
    private double tick = 0;
    private Position up = getPosition().translateBy(Direction.UP);
    private Position down = getPosition().translateBy(Direction.DOWN);
    private Position left = getPosition().translateBy(Direction.LEFT);
    private Position right = getPosition().translateBy(Direction.RIGHT);
    
    public ZombieToastSpawner (Position p, double rate, double zombie_health, double zombie_attack) {
        super(p, "zombie_toast_spawner");
        zombie_spawn_rate = rate;
        this.setCrossAble(true);
        this.zombie_attack = zombie_attack;
        this.zombie_health = zombie_health;
    }

    @Override
    public void tick(Dungeon dungeon) {
        if (zombie_spawn_rate == 0) {
            return;
        }
        tick++;
        if (tick == zombie_spawn_rate) {
            Position spawnPos = chooseSpawnPos(dungeon);
            if (spawnPos != null) {
                dungeon.getEntities().add(new ZombieToast(spawnPos, "zombie_toast", zombie_health, zombie_attack));
            }
            tick = 0;
        }
    }

    public Position chooseSpawnPos(Dungeon dungeon) {
        if (isSpawnable(dungeon, up)) {
            return up;
        } else if (isSpawnable(dungeon, down)) {
            return down;
        } else if (isSpawnable(dungeon, left)) {
            return left;
        } else if (isSpawnable(dungeon, right)) {
            return right;
        }
        return null;
    }

    public boolean isSpawnable(Dungeon dungeon, Position p) {
        Entity e = dungeon.getEntity(p);
        if (e == null || e.isOpenSquare()) {
            return true;
        }
        return false;
    }
}
