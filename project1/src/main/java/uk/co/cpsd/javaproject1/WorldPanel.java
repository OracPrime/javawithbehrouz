package uk.co.cpsd.javaproject1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;

import javax.swing.JPanel;

public class WorldPanel extends JPanel {
    private final World world;

    public WorldPanel(World world) {
        this.world = world;
        setPreferredSize(new Dimension(500, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = getWidth() / world.size;

        for (int x = 0; x < world.size; x++) {
            for (int y = 0; y < world.size; y++) {
                // Draw grass
                if (world.hasGrass(x, y)) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }

                g.setColor(Color.GRAY);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
        
        world.animals().forEach(animal->{
            
            g.setColor(animal.getColor());
            g.fillRect(animal.getX() * cellSize+10, animal.getY() * cellSize+10, cellSize-15,cellSize-15);

            
            // Goats have number id, and is shown in the panel. Code has been written by AI

            g.setColor(Color.BLACK); // Set color for the text (e.g., black)
            // Adjust font size based on cell size for better visibility
            g.setFont(new Font("Arial", Font.BOLD, Math.max(10, cellSize / 3))); // <--- NEW: Set font

            // Calculate text position to try and center it within the animal's drawn area
            String idText = String.valueOf(animal.getId());
            int textWidth = g.getFontMetrics().stringWidth(idText);
            int textHeight = g.getFontMetrics().getHeight();

            int textX = animal.getX() * cellSize + (cellSize / 2) - (textWidth / 2);
            int textY = animal.getY() * cellSize + (cellSize / 2) + (textHeight / 4); // Adjust for baseline

            g.drawString(idText, textX, textY);
        });
    }
}
