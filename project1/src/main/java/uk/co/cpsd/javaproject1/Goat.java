package uk.co.cpsd.javaproject1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import uk.co.cpsd.javaproject1.DecisionInfo.DecisionType;

import java.awt.Point;

public class Goat extends Animal {

    public final int HUNGER_TRESHHOLDS = 50;
    public final int GOAT_MAX_AGE = 60;

    public Goat(int x, int y) {
        super(x, y, 10);
        this.setLastReproductionTick(0);
    }

    public void eatGrass() {
        this.energyLevel += 12;
        System.out.println(energyLevel);
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public boolean isHungry() {
        return this.energyLevel < HUNGER_TRESHHOLDS;
    }

    @Override
    public void act(World world, List<Animal> babyAnimalHolder, List<Animal> removedAnimalsHolder) {
        DecisionInfo decisionInfo = animalDecisionMaking(world);

        switch (decisionInfo.getType()) {
            case EAT:
                if (world.hasGrass(decisionInfo.getNextPos().x, decisionInfo.getNextPos().y)) {
                    eatGrass();
                    world.removeGrass(decisionInfo.getNextPos().x, decisionInfo.getNextPos().y);
                    setPosition(decisionInfo.getNextPos(), 1);
                    ;
                }
                break;
            case REPRODUCE:
                Point partnerlocation = decisionInfo.getNextPos();
                Animal partnerGoat = world.getAnimalAt(partnerlocation.x, partnerlocation.y);

                if (partnerGoat instanceof Goat otherGoat && this.isFertile(otherGoat, world.getTotalTicks())) {
                    Animal babyGoat = this.reproduceWithTwo(otherGoat, world.getTotalTicks());
                    babyAnimalHolder.add(babyGoat);
                }
                break;
            case FLEE:
                Point safeRandomPoint = decisionInfo.getNextPos();
                setPosition(safeRandomPoint, 5);
            case WANDER:
                Point randomMove = decisionInfo.getNextPos();
                setPosition(randomMove, 1);

        }
    }

    @Override
    public DecisionInfo animalDecisionMaking(World world) {

        Map<Point, List<Object>> scanedNeighbourHoodByGoat = world.scanNeighbour(getX(), getY());

        // 1. Priority: Eat if hungry
        if (isHungry()) {
            for (Map.Entry<Point, List<Object>> entry : scanedNeighbourHoodByGoat.entrySet()) {
                if (entry.getValue().contains("grass")) {
                    return new DecisionInfo(DecisionType.EAT, entry.getKey());
                }
            }
        }

        // 2. Priority: Reproduce (check nearby goats)
        for (Map.Entry<Point, List<Object>> entry : scanedNeighbourHoodByGoat.entrySet()) {
            for (Object obj : entry.getValue()) {
                if (obj instanceof Goat otherGoat && this.isFertile(otherGoat, world.getTotalTicks())) {
                    return new DecisionInfo(DecisionType.REPRODUCE, entry.getKey());
                }
            }
        }

        // 3. Priority: Flee from danger
        Point safe = findRandomSafePos(scanedNeighbourHoodByGoat);
        if (!safe.equals(new Point(getX(), getY()))) {
            return new DecisionInfo(DecisionType.FLEE, safe);
        }

        // 4. Default: Random move
        Point randomMove = findRandomPos(scanedNeighbourHoodByGoat);
        return new DecisionInfo(DecisionType.WANDER, randomMove);
    }

    public Point findRandomSafePos(Map<Point, List<Object>> neighbourHoodPos) {
        List<Point> safeTiles = neighbourHoodPos.entrySet()
                .stream()
                .filter(e -> !e.getValue().contains("lion"))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!safeTiles.isEmpty()) {
            return safeTiles.get(new Random().nextInt(safeTiles.size()));
        }

        // All tiles are dangerous stay in its place
        return new Point(this.getX(), this.getY());
    }

    public Point findRandomPos(Map<Point, List<Object>> neighbourHoodPos) {
        List<Point> allTiles = new ArrayList<>(neighbourHoodPos.keySet());
        if (!allTiles.isEmpty()) {
            return allTiles.get(new Random().nextInt(allTiles.size()));
        }
        return new Point(this.getX(), this.getY());
    }

    @Override
    public int getEnergyCost(Gender gender) {
        return gender == Gender.FEMALE ? 7 : 5;

    }

    @Override
    public int getInitialBabyEnergy() {
        return 10;
    }

    @Override
    public Animal createBaby(int x, int y) {
        return new Goat(x, y);
    }

    @Override
    public int getReproductionCooldown(Gender gender) {
        return gender == Gender.FEMALE ? 4 : 2;
    }

    public boolean hasReachedEndOfLife() {
        return this.getAge() > GOAT_MAX_AGE;
    }

}
