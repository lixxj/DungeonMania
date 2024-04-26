package dungeonmania.entity;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class Entity {

    // Attributes
    private String id;
    private boolean isInteractable;
    private Position position;
    private String type;
    private static int idCount = 0;
    private boolean isCollectable = false;
    private boolean isStatic = false; 
    private boolean isMovable = false;
    // Constructor
    public Entity(Position position, String type) {

        this.position = position;
        this.type = type;
        idCount++;
        this.id = String.valueOf(idCount);
        if (type.equals("mercenary") || type.equals("zombie_toast_spawner")) {
            isInteractable = true;
        }
        this.isCollectable = isCollectableEntity();
        this.isStatic = isStaticEntity();
        this.isMovable = isMovingEntity();
    }

    //check if the entity is standable
    public boolean isOpenSquare() {
        if (type.equals("door")) {
            Door d = (Door)this;
            if (d.isOpen()) {
                return true;
            }
        }
        List<String> list = Arrays.asList("wall", "boulder", "door");
        return !list.contains(type);
    }

    // check if the entity is moving entity
    public boolean isMovingEntity() {
        List<String> list = Arrays.asList("spider", "mercenary", "zombie_toast", "assassin", "hydra");
        return list.contains(type);
    }

    // check if the entity is collectable entity
    private boolean isCollectableEntity() {
        List<String> list = Arrays.asList("treasure", "key", "invincibility_potion", "invisibility_potion", "wood", 
        "arrow", "bomb", "sword");
        return list.contains(type);
    }
    // check if the entity is static entity
    private boolean isStaticEntity() {
        List<String> list = Arrays.asList("wall", "exit", "boulder", "switch", "door", "portal", "zombie_spawn_toast");
        return list.contains(type);
    }
    
    public void tick(Dungeon dungeon){}
    
    // for testing, initialise the idCount
    public static void clearIdCount() {
        idCount = 0;
    }

    // Getters
    public Position getPosition() {
        return position;
    }
    
    public String getType() {
        return type;
    }
    
    public String getId() {
        return id;
    }

    public Boolean isInteractable(){
        return isInteractable;
    }

    public Boolean isCollectable(){
        return isCollectable;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public boolean hasId(String Id) {
        return (id.equals(Id));
    }

    // Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setCollectable(boolean isCollectable) {
        this.isCollectable = isCollectable;
    }

    public JSONObject getEntityStateAsJSON() {
        JSONObject entityObject = new JSONObject();
        entityObject.put("id", this.getId());
        entityObject.put("isInteractable", this.isInteractable());
        entityObject.put("x", this.getPosition().getX());
        entityObject.put("y", this.getPosition().getY());
        entityObject.put("type", this.getType());
        entityObject.put("isCollectible", isCollectable());
        entityObject.put("isStatic", isStatic());
        entityObject.put("isMovable", isMovable());

        return entityObject;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        

        if (!(obj instanceof Entity)) return false;
    
        Entity entity = (Entity) obj;
         
        return this.id.equals(entity.getId());
    }
}
