/*
 * This program simulates a two-player treasure hunt game on a directed graph.
 *
 * - Player 1 starts at node 1, Player 2 at node 2.
 * - Player 1 wins if they reach node 0 (treasure).
 * - Player 2 wins if they catch Player 1 (both on the same node).
 * - The game is a draw if it cycles indefinitely without either player winning.
 *
 * The `dfs` function uses recursion with memoization and cycle detection (via `visited` set)
 * to determine the outcome from any given state. It alternates turns between players.
 *
 * The result is returned as:
 *   0 → Draw
 *   1 → Player 1 wins
 *   2 → Player 2 wins
 *
 * The `main` method sets up a sample graph and prints the result of the game.
 */
// Since no player forces a win, the game results in a draw (0).
// Final Output:
// Output: 0 (Draw)




import java.util.*;

public class Game4b {

    static final int DRAW = 0;
    static final int PLAYER1_WIN = 1;
    static final int PLAYER2_WIN = 2;

    public int treasureGame(int[][] graph) {
        int n = graph.length;
        int[][][] memo = new int[n][n][2]; // [p1Pos][p2Pos][turn]
        return dfs(graph, 1, 2, 0, memo, new HashSet<>());
    }

    private int dfs(int[][] graph, int p1, int p2, int turn, int[][][] memo, Set<String> visited) {
        if (p1 == 0) return PLAYER1_WIN;
        if (p1 == p2) return PLAYER2_WIN;

        String stateKey = p1 + "," + p2 + "," + turn;
        if (visited.contains(stateKey)) return DRAW;

        if (memo[p1][p2][turn] != 0) return memo[p1][p2][turn];

        visited.add(stateKey);
        int result = turn == 0 ? PLAYER2_WIN : PLAYER1_WIN; // Default fallback

        if (turn == 0) {
            for (int next : graph[p1]) {
                int nextResult = dfs(graph, next, p2, 1, memo, visited);
                if (nextResult == PLAYER1_WIN) {
                    result = PLAYER1_WIN;
                    break;
                } else if (nextResult == DRAW) {
                    result = DRAW;
                }
            }
        } else {
            for (int next : graph[p2]) {
                if (next == 0) continue; // forbidden node
                int nextResult = dfs(graph, p1, next, 0, memo, visited);
                if (nextResult == PLAYER2_WIN) {
                    result = PLAYER2_WIN;
                    break;
                } else if (nextResult == DRAW) {
                    result = DRAW;
                }
            }
        }

        visited.remove(stateKey);
        memo[p1][p2][turn] = result;
        return result;
    }

    public static void main(String[] args) {
        Game4b game = new Game4b();
        int[][] graph = {
            {2, 5},
            {3},
            {0, 4, 5},
            {1, 4, 5},
            {2, 3},
            {0, 2, 3}
        };

        int result = game.treasureGame(graph);
        System.out.println("Game Result: " + result); // Expected output: 0 (Draw)
    }
}