import javax.swing.*;
import java.awt.*;

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
