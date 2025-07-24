package uk.co.cpsd.javaproject1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GoatTest {
    
    private Goat goat;

    @BeforeEach
    public void setUp() {
        goat = new Goat(5, 6, 10);
    }

    @Test
    public void testGetXCoordination(){

        int xCoord=goat.getX();
        int expected=5;
        Assertions.assertEquals(xCoord, expected);
    }

    @Test
    public void testGetYCoordination(){
        int yCoord=goat.getY();
        int expected=6;
        Assertions.assertEquals(yCoord, expected);
    }

    @Test
    public void testGoatsMove(){
        goat.move(10);
        int xCoord= goat.getX();
        int yCoord= goat.getY();

        Assertions.assertTrue(yCoord>=0 && yCoord<10);
        Assertions.assertTrue(xCoord>=0 && xCoord<10);
    }

    @Test 
    public void testGender(){
        Goat.Gender goatGender = goat.getGender();
        Assertions.assertTrue(goatGender==Goat.Gender.MALE || goatGender==Goat.Gender.FEMALE);
    }

    @Test
    public void testGetEnergy(){
        int energy=goat.getEnergy();
        int expected=10;
        Assertions.assertEquals(energy, expected);
    }

    @Test
    public void testDecreaseEnergyBy(){

        goat.decreaseEnergyBy(5);
        int energy =goat.getEnergy();
        int expected=5;
        Assertions.assertEquals(energy, expected);
    }

    @Test
    public void testDecreaseEnergy(){
        
        int startingenergy=goat.getEnergy();
        boolean isDead = goat.decreaseEnergy(5);
        int currentEnergy=goat.getEnergy();
        Assertions.assertEquals(startingenergy-2,currentEnergy);
        Assertions.assertFalse(isDead);
    }

    @Test
    public void testEatGrass(){
        int startingEnergy=goat.getEnergy();
        goat.eatGrass();
        int currentEnergy=goat.getEnergy();
        Assertions.assertEquals(startingEnergy+5, currentEnergy);
    }

    @Test
    public void testIfGoatsDies(){
        int tick=0;
        boolean isDead=false;

        while (!isDead) {
            isDead=goat.decreaseEnergy(tick);
            tick+=5;
        }

        Assertions.assertTrue(isDead);
        Assertions.assertTrue(goat.getEnergy()<=0);
    }

    

}
