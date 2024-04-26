package dungeonmania.CollectableEntity;

import dungeonmania.util.Position;

public class Key extends CollectableEntity {

    // Attributes
    private int key;

    // Constructor
    public Key(Position position, String type,int key) {
        super(position, type);
        this.key = key;
    }

    // Getter
    public int getKey() {
        return this.key;
    }
    
}
