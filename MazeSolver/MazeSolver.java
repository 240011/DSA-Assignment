import java.awt.*;
import javax.swing.*;

public class MazeSolver {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Solver (DFS/BFS)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            MazePanel mazePanel = new MazePanel(21, 21);
            frame.add(mazePanel, BorderLayout.CENTER);

            JPanel controlPanel = new JPanel();
            JButton dfsBtn = new JButton("Solve with DFS");
            JButton bfsBtn = new JButton("Solve with BFS");
            JButton genBtn = new JButton("Generate New Maze");

            dfsBtn.addActionListener(e -> mazePanel.solveDFS());
            bfsBtn.addActionListener(e -> mazePanel.solveBFS());
            genBtn.addActionListener(e -> mazePanel.generateNewMaze());

            controlPanel.add(dfsBtn);
            controlPanel.add(bfsBtn);
            controlPanel.add(genBtn);
            frame.add(controlPanel, BorderLayout.SOUTH);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
/*
 * This is the main entry point for the Maze Solver application.
 * It creates a GUI window using Swing with a maze display panel and control buttons.
 * Users can:
 *  - Solve the maze using DFS (Depth-First Search)
 *  - Solve the maze using BFS (Breadth-First Search)
 *  - Generate a new random maze
 * 
 * The application runs on the Swing event dispatch thread to ensure thread-safe UI operations.
 */
