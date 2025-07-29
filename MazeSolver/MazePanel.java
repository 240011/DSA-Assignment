import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MazePanel extends JPanel {
    private Node[][] maze;
    private final int rows, cols, cellSize = 20;
    private Node start, end;

    public MazePanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        generateNewMaze();
    }

    public void generateNewMaze() {
        MazeGenerator generator = new MazeGenerator(rows, cols);
        maze = generator.generateMaze();
        start = maze[0][0];
        end = maze[rows - 1][cols - 1];
        repaint();
    }

    public void solveDFS() {
        resetMaze();
        Stack<Node> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            current.visited = true;

            if (current == end) break;

            for (Node neighbor : getNeighbors(current)) {
                if (!neighbor.visited && !neighbor.isWall) {
                    neighbor.visited = true;
                    stack.push(neighbor);
                }
            }

            repaint();
            sleep();
        }

        markPathDFS(start);
        repaint();
    }

    public void solveBFS() {
        resetMaze();
        Queue<Node> queue = new LinkedList<>();
        Map<Node, Node> parent = new HashMap<>();
        queue.offer(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            current.visited = true;

            if (current == end) break;

            for (Node neighbor : getNeighbors(current)) {
                if (!neighbor.visited && !neighbor.isWall && !parent.containsKey(neighbor)) {
                    parent.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }

            repaint();
            sleep();
        }

        Node curr = end;
        while (curr != null && curr != start) {
            curr.isPath = true;
            curr = parent.get(curr);
        }
        repaint();
    }

    private void resetMaze() {
        for (Node[] row : maze)
            for (Node node : row) {
                node.visited = false;
                node.isPath = false;
            }
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
        for (int[] dir : dirs) {
            int nr = node.row + dir[0];
            int nc = node.col + dir[1];
            if (inBounds(nr, nc)) neighbors.add(maze[nr][nc]);
        }
        return neighbors;
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && c >= 0 && r < rows && c < cols;
    }

    private void markPathDFS(Node current) {
        if (current == null || current.visited == false || current == end) return;
        current.isPath = true;
        for (Node neighbor : getNeighbors(current)) {
            if (neighbor.visited && !neighbor.isPath) {
                markPathDFS(neighbor);
                break;
            }
        }
    }

    private void sleep() {
        try { Thread.sleep(10); } catch (InterruptedException e) {}
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Node[] row : maze) {
            for (Node node : row) {
                int x = node.col * cellSize;
                int y = node.row * cellSize;
                if (node.isWall) g.setColor(Color.BLACK);
                else if (node == start) g.setColor(Color.GREEN);
                else if (node == end) g.setColor(Color.RED);
                else if (node.isPath) g.setColor(Color.YELLOW);
                else if (node.visited) g.setColor(Color.CYAN);
                else g.setColor(Color.WHITE);

                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cols * cellSize, rows * cellSize);
    }
}
