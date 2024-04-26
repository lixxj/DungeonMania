package dungeonmania.entity;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Portal extends StaticEntity{
    private String color;

    public Portal (Position p, String color) {
        super(p, "portal");
        this.setCrossAble(true);
        this.color = color;
    }

    // if the entity is a player or mercenary, teleport the entity to new position
    // public void teleport(Entity e, Dungeon d, Direction direction) {
    //     Position p = getAnotherPortalPos(d).translateBy(direction);
    //     if (e.getType().equals("player") || e.getType().equals("mercenary")) {
    //         e.setPosition(p);
    //     }
    // }

    // return the new position after teleport, return null if cannot teleport
    public Position teleport(Dungeon dungeon, Direction direction) {
        Position p = getAnotherPortalPos(dungeon.getEntities()).translateBy(direction);
        if (dungeon.getEntity(p) == null || dungeon.getEntity(p).isOpenSquare()) {
            return p;
        }
        return null;
    }

    // get the position of the portal with the same color
    public Position getAnotherPortalPos(List<Entity> entities) {
        
        for (Entity e: entities) {
            // search for portal differ from this portal
            if (e.getType().equals("portal") && !e.getId().equals(this.getId())) {
                Portal p = (Portal)e;
                if(p.getColor().equals(this.color)) {
                    return p.getPosition();
                }
            }
        }
        return null;
    }

    public String getColor() {
        return color;
    }
}
