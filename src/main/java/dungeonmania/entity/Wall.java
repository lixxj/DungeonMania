package dungeonmania.entity;

import dungeonmania.util.Position;

public class Wall extends StaticEntity{
    public Wall(Position p) {
        super(p, "wall");
        this.setCrossAble(false);
    }
}
