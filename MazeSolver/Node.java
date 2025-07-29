public class Node {
    public int row, col;
    public boolean isWall, visited, isPath;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        this.isWall = true;
        this.visited = false;
        this.isPath = false;
    }
}
