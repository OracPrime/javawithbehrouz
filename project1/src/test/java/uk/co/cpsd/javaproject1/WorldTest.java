package uk.co.cpsd.javaproject1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

public class WorldTest {

    private World world;
    @BeforeEach
    public void worldSetUp(){
         world=new World(10);
    }

    private void clearAllGrass(){
        //it removes all the grass from the panel
        for(int i=0;i<world.size;i++){
            for(int j=0;j<world.size;j++){
                world.removeGrass(i, j);
            }

        }
    }

    @Test
    public void testRemoveGrass(){

        clearAllGrass();
        int numOfGrass=world.findNumOfGrass();
        Assertions.assertEquals(numOfGrass, 0);
    }

    @Test
    public void testGrassGrows(){

        // remove all the grass from the panel
        clearAllGrass();

        // and check if one grass will be added to the panel
        world.growGrass();
        int currentNumOfGrass=world.findNumOfGrass();
        Assertions.assertEquals(currentNumOfGrass,1);

    }

    @Test
    public void testFindNumOfGoats(){

        int numOfGoats=world.findNumOfGoats();
        Assertions.assertEquals(numOfGoats, 10);
    }

    @Test
    public void testHasGrassMethod(){

        world.growGrass();
        world.growGrass();
        world.growGrass();

        int grassCounter=0;
        for(int i=0;i<world.size;i++){
            for(int j=0 ; j<world.size;j++){
                if(world.hasGrass(i, j)){
                    grassCounter++;
                }
            }
        }

        int numOfGrass=world.findNumOfGrass();
        Assertions.assertEquals(grassCounter, numOfGrass);
    }

    @Test
    public void testAgeOfGrassIncreases(){

        int x = -1;
        int y = -1;
        int age = -1;
        world.growGrass();
        world.growGrass();
        
        for(int i=0;i<world.size;i++){
            for(int j=0;j<world.size;j++){
                if(world.getGrassAgeAt(i, j)>0){
                    x = i;
                    y = j;
                    age=world.getGrassAgeAt(i, j);
                }
            }
        }

        // Ensure that at least one grass cell was found
        Assertions.assertTrue(x != -1 && y != -1 && age != -1, "No grass cell found to test age increment.");

        world.ageIncreaser(10);
        Assertions.assertEquals(age+1, world.getGrassAgeAt(x, y));

    }
    
}
