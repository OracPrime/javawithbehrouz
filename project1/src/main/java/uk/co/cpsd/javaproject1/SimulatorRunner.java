package uk.co.cpsd.javaproject1;

public class SimulatorRunner {
    public static void noGUISimulation(int ticks, int numOfGoats) {
        World world = new World(numOfGoats);

        for (int i = 1; i <= ticks; i++) {
            world.tick();
        }

        world.writeToCSV(world.getGoatPopulationHistory(), world.getGrassPopulationHistory());
        GoatChart chart = new GoatChart(world.getGoatPopulationHistory(), world.getGrassPopulationHistory());
        chart.setVisible(true);
    }

}
