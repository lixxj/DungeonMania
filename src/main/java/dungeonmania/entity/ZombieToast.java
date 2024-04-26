package dungeonmania.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.Player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity{
    public ZombieToast(Position position, String type, double health, double attackDamage) {
        super(position, type, health, attackDamage);
    }

    @Override
    public void tick(Dungeon dungeon) {
        if (dungeon.getPlayer() != null && dungeon.getPlayer().isInvincible()) {
            moveAway(dungeon.getPlayer(), dungeon);
        } else {
            move(dungeon);
        }
    }

    @Override
    public void move(Dungeon dungeon) {
        Random r = new Random();
        int dir = r.nextInt(4); 
        Direction direction = Direction.UP;
        switch(dir) {
            case 0:
                direction = Direction.UP;
                break;
            case 1:
                direction = Direction.LEFT;
                break;
            case 2:
                direction = Direction.RIGHT;
                break;
            case 3:
                direction = Direction.DOWN;
                break;
        }
        if (canZombieToastMove(getPosition(), direction, dungeon)) {
            setPosition(getPosition().translateBy(direction));
        } else {
            this.move(dungeon);
        }
    }

    public boolean canZombieToastMove(Position p, Direction d, Dungeon dungeon){
        Position newPos = p.translateBy(d);
        Entity e = dungeon.getEntity(newPos);
        if (e == null) {
            return true;
        }
        if (e.isOpenSquare()){
            return true;
        }
        return false;
    }

    @Override
    public void attack(Player player) {
        player.takeDamage(getAttackDamage());
    }

    public void moveAway(Player player, Dungeon dungeon) {
        List<Direction> dirs = new ArrayList<>();
        dirs.add(Direction.UP);
        dirs.add(Direction.DOWN);
        dirs.add(Direction.LEFT);
        dirs.add(Direction.RIGHT);
        double currentRadius = this.getPosition().distance(player.getPosition());
        for (Direction dir : dirs) {
            Position newPos = this.getPosition().translateBy(dir);
            double newRadius = newPos.distance(player.getPosition());
            if (newRadius > currentRadius && canZombieToastMove(this.getPosition(), dir, dungeon)) {
                setPosition(newPos);
                return;
            }
        }
    }

}
