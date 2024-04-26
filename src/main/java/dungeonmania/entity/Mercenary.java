package dungeonmania.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.Player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity {
    private final double bribeAmount;
    private final double bribeRadius;
    private double ally_attack;
    private double ally_defence;
    private double mindControledDuration = 0;
    private boolean isMindControlled = false; 
    private State state = new Hostile();
    private Random random = new Random();

    public Mercenary(Position position, double health, double attackDamage,
                double bribeAmount, double bribeRadius, double ally_attack,
                double ally_defence) {
        super(position, "mercenary", health, attackDamage);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.ally_attack = ally_attack;
        this.ally_defence = ally_defence;
    }

    public double getBribeAmount() {
        return bribeAmount;
    }

    public double getBribeRadius() {
        return bribeRadius;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void tick(Dungeon dungeon) {
        Player player = dungeon.getPlayer();
        if (player.isInvisible() && this.isEnemy()) {
            moveRandom(dungeon);
        } else if (player.isInvincible() && this.isEnemy()) {
            moveAway(player, dungeon);
        } else {
            move(dungeon);
        }
        updateMindControlledDuration();
        if (getPosition().equals(dungeon.getPlayer().getPosition()) && !dungeon.getPlayer().isInvisible()) {
            dungeon.battle(dungeon.getPlayer(), this);   
        }
    }

    @Override
    public void move(Dungeon dungeon) {
        state.move(this, dungeon);
    }

    @Override
    public void attack(Player player) {
        state.attack(player, getAttackDamage());
    }

    public boolean toAlly(Player player) {
        setState(new Ally());
        super.toAlly();
        state.bonus(player, this.ally_attack, this.ally_defence);
        return true;
    }

    public boolean mindControlled(double duration) {
        super.toAlly();
        setState(new Ally());
        this.isMindControlled = true;
        this.mindControledDuration = duration;
        return true;
    }

    public void updateMindControlledDuration() {
        if (this.isMindControlled) {
            this.mindControledDuration--;
            if (this.mindControledDuration == 0) {
                this.isMindControlled = false;
                setState(new Hostile());
                super.toEnemy();
            }
        }
    }

    public void moveRandom(Dungeon dungeon) {
        int dir = random.nextInt(4); 
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
        if (canMove(getPosition(), direction, dungeon)) {
            setPosition(getPosition().translateBy(direction));
        }
    }

    public boolean canMove(Position p, Direction d, Dungeon dungeon){
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
            if (newRadius > currentRadius && canMove(this.getPosition(), dir, dungeon)) {
                setPosition(newPos);
                return;
            }
        }
    }

    // For testing purposes.
    public void setRandom(Random random) {
        this.random = random;
    }
}