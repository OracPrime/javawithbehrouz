package uk.co.cpsd.javaproject1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.awt.Point;

import uk.co.cpsd.javaproject1.DecisionInfo.DecisionType;

public class Lion extends Animal {

    private int lastReproductionTick = -8;

    public Lion(int x, int y) {
        super(x, y, 20);
    }

    @Override
    public boolean isHungry() {
        return energyLevel <= 20;
    }

    @Override
    public Color getColor() {
        return Color.GRAY;
    }

    @Override
    public DecisionInfo animalDecisionMaking(World world) {

        Map<Point, List<Object>> scanedNeighbourHoodByGoat = world.scanNeighbour(getX(), getY());

        // 1. Priority: Eat if hungry
        if (isHungry()) {
            for (Map.Entry<Point, List<Object>> entry : scanedNeighbourHoodByGoat.entrySet()) {
                for (Object obj : entry.getValue()) {
                    if (obj instanceof Goat) {
                        return new DecisionInfo(DecisionType.EAT, entry.getKey());
                    }
                }
            }
        }

        // 2. Priority: Reproduce (check nearby goats)
        for (Map.Entry<Point, List<Object>> entry : scanedNeighbourHoodByGoat.entrySet()) {
            for (Object obj : entry.getValue()) {
                if (obj instanceof Lion otherLion && this.canReproduceWith(otherLion, world.getTotalTicks())) {
                    return new DecisionInfo(DecisionType.REPRODUCE, entry.getKey());
                }
            }
        }

        // 4. Default: Random move
        Point randomMove = findRandomPos(scanedNeighbourHoodByGoat);
        return new DecisionInfo(DecisionType.WANDER, randomMove);
    }

    public Point findRandomPos(Map<Point, List<Object>> neighbourHoodPos) {
        List<Point> allTiles = new ArrayList<>(neighbourHoodPos.keySet());
        if (!allTiles.isEmpty()) {
            return allTiles.get(new Random().nextInt(allTiles.size()));
        }
        return new Point(this.getX(), this.getY());
    }

    public boolean canReproduceWith(Lion otherLion, int currentTick) {
        if (otherLion == this)
            return false;

        boolean oppositeGender = this.getGender() != otherLion.getGender();
        boolean pairsHaveEenergy = this.energyLevel >= 35 && otherLion.energyLevel >= 35;
        boolean sinceLastReproduce = currentTick - this.lastReproductionTick >= 3
                && currentTick - otherLion.lastReproductionTick >= 3;

        boolean canReproduce = oppositeGender && pairsHaveEenergy && sinceLastReproduce;
        return canReproduce;

    }

}
