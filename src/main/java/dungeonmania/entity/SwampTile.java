package dungeonmania.entity;

import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private double movementFactor;
    public SwampTile(Position p, String type, int movementFactor) {
        super(p, type);
        this.movementFactor = movementFactor;
    }

    public double getMovementFactor() {
        return movementFactor;
    }
}
