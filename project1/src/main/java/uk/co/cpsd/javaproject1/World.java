package uk.co.cpsd.javaproject1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class World {
    public final int size = 10;
    private int[][] grassAge = new int[size][size];
    private List<Animal> animals;
    private int nextAnimalId=0;
    private int totalTicks;

    public World(int numOfGoats){
        animals=new ArrayList<>();
        for(int i=0;i<numOfGoats;i++){
            animals.add(new Goat((int)(Math.random() * size),(int)(Math.random() * size),nextAnimalId++));
        }

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                grassAge[i][j]=0;
            }
        }
    }


    public void growGrass() {
        int x = (int)(Math.random() * size);
        int y = (int)(Math.random() * size);
        
        if(grassAge[x][y]==0){
            grassAge[x][y]=1;
        }
        System.out.println("Grass grew at (" + x + "," + y + ") at total tick: " + totalTicks);
    }

    public boolean hasGrass(int x, int y) {
        return grassAge[x][y]>0;
    }

    public void removeGrass(int x, int y) {
        grassAge[x][y] = 0;
    }

    public Stream<Animal> animals(){
        return animals.stream();
    }

    public void ageIncreaser(int worldSize){
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(grassAge[i][j]>0){
                    grassAge[i][j]++;
                }
            }
        }
    }

    public void grassDies(int worldSize,int maxAge){
        for(int i=0;i<size;i++){
            for(int j=0; j<size; j++){
                totalTicks++;
                if(grassAge[i][j]>maxAge){
                    removeGrass(i, j);
                    System.out.println("Grass at [" + i + " , " + j + "] died of old age after " + maxAge + " ticks (at total tick: " + totalTicks + ").");
                }
            }
        }
    }
    public void tick() {

        ageIncreaser(size);

        growGrass();

        final int MAX_GRASS_AGE=20;
        grassDies(size, MAX_GRASS_AGE);
    }

    public void moveAnimals(){
        animals.forEach(animal-> {
            animal.move(size);

            if(animal instanceof Goat){
                Goat currentGoat=(Goat) animal;
                if(hasGrass(currentGoat.getX(), currentGoat.getY())){
                    currentGoat.eatGrass();
                    removeGrass(currentGoat.getX(), currentGoat.getY());
                }
            }
        });

    }
}
