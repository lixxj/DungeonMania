package dungeonmania.entity;

import dungeonmania.util.Position;

public class Door extends StaticEntity{
    private int key;
    private boolean open;

    public Door (Position p, int key) {
        super(p, "door");
        this.setCrossAble(false);
        this.key = key;
    }

    public boolean open(int key) {
        if (key == this.key) {
            this.open = true;
            setCrossAble(true);
            return true;
        }
        return false;
    }

    // case pass in sun stone 
    public boolean open(Entity e) {
        if (e.getType().equals("sun_stone")) {
            this.open = true;
            setCrossAble(true);
            return true;
        }
        return false;
    }
    
    public boolean isOpen() {
        return open;
    }

}
