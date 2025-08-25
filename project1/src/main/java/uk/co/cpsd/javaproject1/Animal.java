package uk.co.cpsd.javaproject1;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.awt.Point;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Animal {

    protected int energyLevel;
    protected int animalId;
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    protected int lastEnergyDecreaseTick = 0;
    protected int lastReproductionTick = -1;
    private int age = 0;
    protected Point position;
    protected Map<String,Double> dna; // DNA carries animal traits and makes it easy for newborns to inherit their parents' traits
    protected double generation; // define generation of the animal ,
    protected double speed; // common traits among all Species
    protected double reproductionPower; // common traits among all Species

    Random random=new Random();


    private final Gender gender;

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
        this.position = new Point(x, y);
        this.energyLevel = energyLevel;
        this.gender = Math.random() < .5 ? Gender.MALE : Gender.FEMALE;
        this.animalId = idCounter.getAndIncrement();

        // Initialize DNA only if not set (for babies)
        if (dna == null) {
            dna = new HashMap<>();
            dna.put("reproductionPower", 5.0 + random.nextDouble() * 2); // Random 5-7
            dna.put("speed", 5.0 + random.nextDouble() * 2); // Random 5-7
            dna.put("generation", 1.0); // Initial animals are Gen 1
            dna.put("animalId", (double) animalId);
        }

        reproductionPower = dna.getOrDefault("reproductionPower", 5.0);
        speed=dna.getOrDefault("speed",5.0);
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isEnergyZero(int currentTime) {
        return energyLevel <= 0;
    }

    public abstract Color getColor();

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
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
        applyMovementCost(cost);
        this.position = new Point(point); // returns a copy
    }

    public Animal reproduceWith(Animal partner, int currentTick) {
        // both animals are of the same species
        if (!this.getClass().equals(partner.getClass())) {
            throw new IllegalArgumentException("Animals must be of the same species to reproduce.");
        }

        // Update reproduction tick
        this.lastReproductionTick = currentTick;
        partner.lastReproductionTick = currentTick;

        // Subtract energy based on species and gender
        this.energyLevel -= getReproductionEnergyCost(this.gender);
        partner.energyLevel -= getReproductionEnergyCost(partner.gender);

        Map<String,Double> babyDNA=new HashMap<>();

        for(String key:dna.keySet()){
            if(!key.equals("generation")&& !key.equals("animalId")){
                double parent1Value=dna.getOrDefault(key,5.0);
                double parent2Value=dna.getOrDefault(key,5.0);
                double avgValue=(parent1Value+parent2Value)/2;
                if(random.nextDouble()<0.1){
                        avgValue+=random.nextGaussian()*1;
                        avgValue=Math.max(0,Math.min(avgValue,12));
                    }
                babyDNA.put(key,avgValue);
                }

        }

        double parent1Gen=this.dna.get("generation");
        double parent2Gen=partner.dna.get("generation");
        babyDNA.put("generation",Math.max(parent1Gen,parent2Gen)+1);

        // Create baby
        Animal baby = createBaby(this.position.x, this.position.y);
        babyDNA.put("animalId", (double) animalId);
        baby.dna=babyDNA;
        baby.energyLevel = getInitialBabyEnergy();

        return baby;
    }

    public int getLastReproductionTick() {
        return lastReproductionTick;
    }

    public void setLastReproductionTick(int tick) {
        this.lastReproductionTick = tick;
    }

    public boolean willMate(Animal otherAnimal, int currentTick) {

        boolean bothAnimalHaveEnergy = this.isFertile(currentTick) && otherAnimal.isFertile(currentTick);
        boolean isOppositeGender = this.gender != otherAnimal.gender;

        return bothAnimalHaveEnergy && isOppositeGender;
    }

    public abstract boolean isFertile(int tick);

    protected abstract int getReproductionEnergyCost(Gender gender);

    protected abstract int getInitialBabyEnergy();

    protected abstract Animal createBaby(int x, int y);

    protected abstract int getReproductionCooldown(Gender gender);

    public void applyMovementCost(int cost) {
        energyLevel = energyLevel - cost;
    };

    /**
     * Determines if the animal has reached the age at which it should be considered
     * deceased or removed from the simulation.
     */
    public abstract boolean hasReachedEndOfLife();

    public Point getAnimalCoordinates() {
        return new Point(position);
    }
}
