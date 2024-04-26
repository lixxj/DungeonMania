package dungeonmania.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.Player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    private double spider_spawn_rate;
    private double tick = 0;
    private boolean clockwise = true;
    private boolean firstStep = true;
    private Position p0 = this.getPosition().translateBy(Direction.UP).translateBy(Direction.LEFT);
    private Position p1 = this.getPosition().translateBy(Direction.UP);
    private Position p2 = this.getPosition().translateBy(Direction.UP).translateBy(Direction.RIGHT);
    private Position p3 = this.getPosition().translateBy(Direction.RIGHT);
    private Position p4 = this.getPosition().translateBy(Direction.RIGHT).translateBy(Direction.DOWN);
    private Position p5 = this.getPosition().translateBy(Direction.DOWN);
    private Position p6 = this.getPosition().translateBy(Direction.DOWN).translateBy(Direction.LEFT);
    private Position p7 = this.getPosition().translateBy(Direction.LEFT);

    private List<Position> posList = new ArrayList<>();

    public Spider(Position position, String type, double health, double attackDamage, double spider_spawn_rate) {
        super(position, type, health, attackDamage);
        this.spider_spawn_rate = spider_spawn_rate;
        Collections.addAll(posList, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void tick(Dungeon dungeon) {
        move(dungeon);
    }

    @Override
    public void move(Dungeon dungeon) {
        tick++;
        if (tick == spider_spawn_rate) {
            spwanSpider(dungeon);
        }
        if (firstStep) {
            setPosition(p1);
            firstStep = false;
            return;
        }
        int i = posList.indexOf(getPosition());
        Position newPos;
        if (clockwise) {
            if (i == 7) {
                newPos = p0;
            } else {
                newPos = posList.get(i + 1);
            }
        } else {
            if (i == 0) {
                newPos = p7;
            } else {
                newPos = posList.get(i - 1);
            }
        }
        Entity e = dungeon.getEntity(newPos);
        if (e != null && e.getType().equals("boulder")) {
            turnAround();
            this.move(dungeon);
        } else {
            setPosition(newPos);
        }
    }

    public void turnAround() {
        if (clockwise) {
            clockwise = false;
        } else{
            clockwise = true;
        }
    }

    // spawn spider around player in a range 20
    public void spwanSpider(Dungeon dungeon) {
        Position playerPos = dungeon.getPlayer().getPosition();
        int playerX = playerPos.getX();
        int playerY = playerPos.getY();
        Random r = new Random();
        int newX = r.nextInt(playerX + 20); 
        int newY = r.nextInt(playerY + 20);
        Position newSpiderPos = new Position(newX, newY);
        dungeon.getEntities().add(new Spider(newSpiderPos, "spider", getHealth(), getAttackDamage(), spider_spawn_rate));
    }

    @Override
    public void attack(Player player) {
        player.takeDamage(getAttackDamage());
    }
}
