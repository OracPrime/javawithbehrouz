package uk.co.cpsd.javaproject1;

import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        try {

            Scanner userInput = new Scanner(System.in);

            System.out.println(
                    "-->if you want to run just a simulation press 1 , \n-->if you want to run the app press 2.");
            int typeOfSimulation = userInput.nextInt();

            System.out.println("For how long do you want to run it? Enter the time in seconds.");
            int timeOfSimulation = userInput.nextInt();

            System.out.println("How many goats do you want to be in simulation?");
            int numOfGoats = userInput.nextInt();

            System.out.println("How many lions do you want to be in simulation?");
            int numOfLions = userInput.nextInt();

            userInput.close();

            if (typeOfSimulation == 1) {
                SimulatorRunner.noGUISimulation(timeOfSimulation, numOfGoats, numOfLions);
            } else if (typeOfSimulation == 2) {
                SwingUtilities.invokeLater(() -> {
                    @SuppressWarnings("unused")
                    SimulatorFrame frame = new SimulatorFrame(timeOfSimulation, numOfGoats, numOfLions);

                });
            }

        } catch (InputMismatchException e) {
            System.out.println(e);
            System.out.println("--------->Invalid input.");
        }

    }
}
