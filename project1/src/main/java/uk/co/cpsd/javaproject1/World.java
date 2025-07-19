package uk.co.cpsd.javaproject1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.awt.Point;

public class World {
    public final int size = 10;
    private List<Animal> animals;
    private int totalTicks;
    private final int MAX_GRASS_AGE = 100;
    private int[][] grassDeathTime = new int[size][size];
    private List<Integer> goatPopulationHistory = new ArrayList<>();
    private List<Integer> grassPopulationHistory = new ArrayList<>();

    public World(int numOfGoats) {
        animals = new ArrayList<>();
        for (int i = 0; i < numOfGoats; i++) {
            animals.add(new Goat((int) (Math.random() * size), (int) (Math.random() * size)));
        }
    }

    public List<Integer> getGoatPopulationHistory() {
        return goatPopulationHistory;
    }

    public List<Integer> getGrassPopulationHistory() {
        return grassPopulationHistory;
    }

    public boolean hasGrass(int x, int y) {
        return grassDeathTime[x][y] > totalTicks;
    }

    public int findNumOfGoats() {
        return (int) animals.stream().filter(animal -> animal instanceof Goat).count();
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public int findNumOfGrass() {
        int numOfGrass = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (hasGrass(x, y)) {
                    numOfGrass++;
                }
            }
        }
        return numOfGrass;
    }

    public int getTicksElapsed() {
        return totalTicks;
    }

    public void growGrass() {
        int x = (int) (Math.random() * size);
        int y = (int) (Math.random() * size);
        if (!hasGrass(x, y)) {
            grassDeathTime[x][y] = totalTicks + MAX_GRASS_AGE;
        }
    }

    public void removeGrass(int x, int y) {
        grassDeathTime[x][y] = 0;
    }

    public Stream<Animal> animals() {
        return animals.stream();
    }

    public int getTotalTicks() {
        return totalTicks;
    }

    // =============================

    public void writeToCSV(List<Integer> goatHistory, List<Integer> grassHistory) {

        try {

            FileWriter csvData = new FileWriter("data.csv");
            csvData.write("Tick, NumOfGoat, NumOfGrass \n");
            for (int i = 0; i < goatHistory.size(); i++) {
                csvData.write(i + ", " + goatHistory.get(i) + ", " + grassHistory.get(i));
                csvData.write("\n");
            }
            csvData.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void tick() {
        List<Animal> babyAnimalHolder = new ArrayList<>();
        totalTicks++;
        for (int i = 0; i < 10; i++) {
            growGrass();
        }

        goatPopulationHistory.add(findNumOfGoats());
        grassPopulationHistory.add(findNumOfGrass());
        List<Animal> deadAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            animal.increaseAge();

            if (animal instanceof Goat goat && goat.isTooOld()) {
                deadAnimals.add(goat);
            }
            boolean isDead = animal.isEnergyZero(totalTicks);
            if (isDead) {
                deadAnimals.add(animal);
            }
            animal.act(this, babyAnimalHolder);
        }
        animals.addAll(babyAnimalHolder);
        animals.removeAll(deadAnimals);
    }

    public Map<Point, List<Object>> scanNeighbour(int x, int y) {

        Map<Point, List<Object>> resultOfScan = new HashMap<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0)
                    continue;

                int nx = x + dx;
                int ny = y + dy;
                if (!isValid(nx, ny, size))
                    continue;
                Point position = new Point(nx, ny);
                List<Object> infoOfPosition = new ArrayList<>();
                if (hasGrass(nx, ny)) {
                    infoOfPosition.add("grass");
                }

                Animal animalAtPos = getAnimalAt(nx, ny);
                if (animalAtPos != null && animalAtPos instanceof Goat) {
                    infoOfPosition.add(animalAtPos);

                }

                resultOfScan.put(position, infoOfPosition);
            }
        }

        return resultOfScan;
    }

    public boolean isValid(int x, int y, int worldSize) {
        return x >= 0 && y >= 0 && x < worldSize && y < worldSize;
    }

    public Animal getAnimalAt(int x, int y) {
        for (Animal animal : animals) {
            if (animal.getX() == x && animal.getY() == y) {
                return animal;
            }
        }
        return null;
    }

}
