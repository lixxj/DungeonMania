package dungeonmania.CollectableEntity;

import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class CollectableEntity extends Entity {

    //  Constructor
    public CollectableEntity(Position position, String type) {
        super(position, type);
        setCollectable(true);
        //TODO Auto-generated constructor stub
    }
}
