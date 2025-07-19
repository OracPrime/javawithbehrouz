package uk.co.cpsd.javaproject1;

import java.awt.Color;
import java.util.List;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Animal {

    protected int x;
    protected int y;
    protected int energyLevel;
    protected int animalId;
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    protected int lastEnergyDecreaseTick = 0;
    protected int lastReproductionTick = -1;
    private int age = 0;

    private Gender gender;

    public enum Gender {
        MALE,
        FEMALE
    }

    public void increaseAge() {
        // System.out.println("animal by id" + this.animalId + " is age:" + getAge());
        this.age++;
    }

    public int getAge() {
        return age;
    }

    public Animal(int x, int y, int energyLevel) {
        this.x = x;
        this.y = y;
        this.energyLevel = energyLevel;
        this.gender = Math.random() < .5 ? Gender.MALE : Gender.FEMALE;
        this.animalId = idCounter.getAndIncrement();
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isEnergyZero(int currentTime) {
        return energyLevel <= 0 ? true : false;
    }

    public abstract Color getColor();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return animalId;
    }

    public abstract void act(World world, List<Animal> babyAnimalHolder, List<Animal> removedAnimalsHolder);

    public int getEnergy() {
        return energyLevel;
    }

    public abstract boolean isHungry();

    public abstract DecisionInfo animalDecisionMaking(World world);

    public void setPosition(Point point, int cost) {
        setPositionCost(cost);
        this.x = point.x;
        this.y = point.y;
    }

    public Animal reproduceWithTwo(Animal partner, int currentTick) {
        // both animals are of the same species
        if (!this.getClass().equals(partner.getClass())) {
            throw new IllegalArgumentException("Animals must be of the same species to reproduce.");
        }

        // Update reproduction tick
        this.lastReproductionTick = currentTick;
        partner.lastReproductionTick = currentTick;

        // Subtract energy based on species and gender
        this.energyLevel -= getEnergyCost(this.gender);
        partner.energyLevel -= getEnergyCost(partner.gender);

        // Create baby
        Animal baby = createBaby(this.x, this.y);
        baby.energyLevel = getInitialBabyEnergy();

        return baby;
    }

    public int getLastReproductionTick() {
        return lastReproductionTick;
    }

    public void setLastReproductionTick(int tick) {
        this.lastReproductionTick = tick;
    }

    public boolean isFertile(Animal otherAnimal, int currentTick) {
        if (otherAnimal == this)
            return false;

        boolean oppositeGender = this.getGender() != otherAnimal.getGender();
        boolean pairsHaveEenergy = this.energyLevel >= getEnergyCost(this.gender)
                && otherAnimal.energyLevel >= getEnergyCost(otherAnimal.gender);
        boolean sinceLastReproduce = currentTick - this.lastReproductionTick >= getReproductionCooldown(this.gender)
                && currentTick - otherAnimal.lastReproductionTick >= getReproductionCooldown(otherAnimal.gender);
        boolean isFertile = oppositeGender && pairsHaveEenergy && sinceLastReproduce;
        return isFertile;

    }

    protected abstract int getEnergyCost(Gender gender);

    protected abstract int getInitialBabyEnergy();

    protected abstract Animal createBaby(int x, int y);

    protected abstract int getReproductionCooldown(Gender gender);

    public void setPositionCost(int cost) {
        energyLevel = energyLevel - cost;
    };

    public void setPositionCost(int cost) {
        energyLevel = energyLevel - cost;
    };

    public abstract boolean isTooOld();
}
