package dungeonmania.entity;

import dungeonmania.util.Position;

public class StaticEntity extends Entity{
    private boolean crossAble;

    public StaticEntity(Position p, String type) {
        super(p, type);
    }

    public void setCrossAble(boolean crossAble) {
        this.crossAble = crossAble;
    }
    
    public boolean isCrossAble() {
        return crossAble;
    }
}
