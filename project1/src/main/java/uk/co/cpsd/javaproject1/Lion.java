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
    public final int HUNGER_TRESHHOLDS = 50;
    private static final int Lion_MAX_AGE = 60;

    public Lion(int x, int y) {
        super(x, y, 20);
    }

    @Override
    public boolean isHungry() {
        return energyLevel <= HUNGER_TRESHHOLDS;
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

        // 3. Default: Stay in place or Random move
        boolean moveChance = Math.random() < 0.25;
        if (moveChance) {
            Point randomMove = findRandomPos(scanedNeighbourHoodByGoat);
            return new DecisionInfo(DecisionType.WANDER, randomMove);
        } else {
            Point sameLocation = new Point(getX(), getY());
            return new DecisionInfo(DecisionType.WANDER, sameLocation);
        }

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

    @Override
    public Animal reproduceWith(Animal partner, int currentTick) {
        Lion LionPartner = (Lion) partner;
        this.lastReproductionTick = currentTick;
        LionPartner.lastReproductionTick = currentTick;
        this.energyLevel = this.getGender() == Gender.FEMALE ? energyLevel - 10 : energyLevel - 8;
        partner.energyLevel = partner.getGender() == Gender.FEMALE ? energyLevel - 10 : energyLevel - 8;

        Lion babyLion = new Lion(getX(), getY());
        babyLion.energyLevel = 15;

        return babyLion;
    }

    public void eatGoat() {
        this.energyLevel += 20;
    }

    @Override
    public void act(World world, List<Animal> babyAnimalHolder, List<Animal> removedAnimalsHolder) {
        DecisionInfo decisionInfo = animalDecisionMaking(world);
        Point target = decisionInfo.getNextPos();

        switch (decisionInfo.getType()) {
            case EAT -> {
                if (world.getAnimalAt(target.x, target.y) instanceof Goat) {
                    eatGoat();
                    world.removeAnimal(target.x, target.y, removedAnimalsHolder);
                    setPosition(target, 5);
                }
            }
            case REPRODUCE -> {
                Point partnerlocation = decisionInfo.getNextPos();
                Animal partnerLion = world
                        .getAnimalAt(partnerlocation.x, partnerlocation.y);

                if (partnerLion instanceof Lion otherLion && this.canReproduceWith(otherLion, world.getTotalTicks())) {
                    Animal babyLion = this.reproduceWith(otherLion, world.getTotalTicks());
                    babyAnimalHolder.add(babyLion);
                }
            }
            case FLEE -> {
                Point safeRandomPoint = decisionInfo.getNextPos();
                setPosition(safeRandomPoint, 5);
            }
            case WANDER -> {
                Point randomMove = decisionInfo.getNextPos();
                if (randomMove.x == getX() && randomMove.y == getY()) {
                    setPosition(randomMove, 0);
                    System.out.println("Stay at same location===========================");
                } else {
                    setPosition(randomMove, 1);
                }

            }

        }
    }

    public boolean isTooOld() {
        return this.getAge() > Lion_MAX_AGE;
    }

}
