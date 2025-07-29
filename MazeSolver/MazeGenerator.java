import java.util.*;

public class MazeGenerator {
    private final int rows, cols;
    private final Node[][] grid;
    private final Random rand = new Random();

    public MazeGenerator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new Node[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = new Node(i, j);
    }

    public Node[][] generateMaze() {
        dfsGenerate(0, 0);
        return grid;
    }

    private void dfsGenerate(int r, int c) {
        grid[r][c].isWall = false;
        int[] dr = {0, 1, 0, -1};
        int[] dc = {1, 0, -1, 0};
        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs);

        for (int dir : dirs) {
            int nr = r + dr[dir] * 2;
            int nc = c + dc[dir] * 2;

            if (inBounds(nr, nc) && grid[nr][nc].isWall) {
                grid[r + dr[dir]][c + dc[dir]].isWall = false;
                dfsGenerate(nr, nc);
            }
        }
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && c >= 0 && r < rows && c < cols;
    }
}
