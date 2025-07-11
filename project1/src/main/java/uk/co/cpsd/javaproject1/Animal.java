package uk.co.cpsd.javaproject1;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Animal {
    
    protected int x;
    protected int y;
    protected int energyLevel;
    protected int animalId;
    private static final AtomicInteger idCounter=new AtomicInteger(0);

    public Animal(int x,int y, int energyLevel){
        this.x=x;
        this.y=y;
        this.energyLevel=energyLevel;
        this.animalId=idCounter.getAndIncrement();
    }

    public abstract void move(int worldSize);

    public abstract Color getColor();

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public int getId(){
        return animalId;
    }

    public abstract void act(World world);
    
}
