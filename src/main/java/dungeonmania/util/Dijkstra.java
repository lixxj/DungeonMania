package dungeonmania.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import dungeonmania.Dungeon;
import dungeonmania.entity.Door;
import dungeonmania.entity.Entity;
import dungeonmania.entity.SwampTile;

public class Dijkstra {
    public static Map<Position, Position> dijkstra(Entity mercenary, Dungeon dungeon) {
        /**
         * let dist be a Map<Position, Double>
         * let prev be a Map<Position, Position>
         */
        Map<Position, Double> dist = new HashMap<Position, Double>();
        Map<Position, Position> previous = new HashMap<Position, Position>();
        
        /**
         * for each Position p in grid:
         *      dist[p] := infinity
         *      previous[p] := null
         */
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Position pos = new Position(i, j);
                dist.put(pos, Double.MAX_VALUE);
                previous.put(pos, null);
            }
        }

        dist.put(mercenary.getPosition(), 0.0);
        
        Queue<Position> queue = new PriorityQueue<>((pos1, pos2) -> ((int) (dist.get(pos1) - dist.get(pos2))));
        queue.add(mercenary.getPosition());
        Set<Position> settled = new HashSet<>();
        // while queue is not empty:
        while (!queue.isEmpty()) {
            // u := next node in queue with the smallest dist
            Position u = queue.remove();
            List<Position> neighbours = cardinalNeighbour(u);
            /**
             * for each cardinal neighbour v of u:
             *      if dist[u] + cost(u, v) < dist[v]:
             *          dist[v] := dist[u] + cost(u, v)
             *          previous[v] := u
             */
            for (Position v : neighbours) {
                if (!settled.contains(v)) {
                    // calculate cost of moving from u to v, if v has a entity that is restricted, cost(u,v) is inifinity 
                    Entity entity = dungeon.getEntity(v);
                    double cost = getCost(entity);
                    if ((dist.get(u) + cost) < dist.get(v)) {
                        dist.put(v, dist.get(u) + cost);
                        previous.put(v, u);
                    }
                    if (!queue.contains(v)) {
                        queue.add(v);
                    }
                }
            }
            settled.add(u);
        }

        return previous;
    }

    private static double getCost(Entity entity) {
        // entity == null represents a free space 
        if (entity == null) {
            return 1;
        }

        if (entity.isStatic()) {
            switch(entity.getType()) {
                case "wall":
                    return Double.MAX_VALUE;
                case "boulder":
                    return Double.MAX_VALUE;
                case "door":
                    if (((Door) entity).isOpen()) {
                        return 1;
                    }
                    return Double.MAX_VALUE;
                case "swamp_tile":
                    return ((SwampTile) entity).getMovementFactor();
            } 
        }

        // any other entity does not restrict mercenary's movement
        return 1;
    }

    public static List<Position> cardinalNeighbour(Position pos) {
        List<Position> cardinalNeighbour = new ArrayList<>();

        if (pos.translateBy(Direction.UP).getY() >= 0) {
            cardinalNeighbour.add(pos.translateBy(Direction.UP));
        }

        if (pos.translateBy(Direction.LEFT).getX() >= 0) {
            cardinalNeighbour.add(pos.translateBy(Direction.LEFT));
        }
        
        if (pos.translateBy(Direction.DOWN).getY() < 20) {
            cardinalNeighbour.add(pos.translateBy(Direction.DOWN));
        }
        

        if (pos.translateBy(Direction.RIGHT).getX() < 20) {
            cardinalNeighbour.add(pos.translateBy(Direction.RIGHT));
        }
        
        return cardinalNeighbour;
    }
}
