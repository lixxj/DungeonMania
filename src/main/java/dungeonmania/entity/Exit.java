package dungeonmania.entity;

import dungeonmania.util.Position;

public class Exit extends StaticEntity{
    public Exit(Position p) {
        super(p, "exit");
        this.setCrossAble(true);
    }
}
