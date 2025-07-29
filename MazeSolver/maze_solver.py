import tkinter as tk
import random
from collections import deque
from tkinter import messagebox

CELL_SIZE = 20
ROWS, COLS = 21, 21  # Should be odd for perfect maze
WINDOW_WIDTH = COLS * CELL_SIZE
WINDOW_HEIGHT = ROWS * CELL_SIZE

class MazeSolverApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Maze Solver (DFS/BFS)")
        self.canvas = tk.Canvas(root, width=WINDOW_WIDTH, height=WINDOW_HEIGHT)
        self.canvas.pack()

        self.grid = [[1 for _ in range(COLS)] for _ in range(ROWS)]
        self.start = (0, 0)
        self.end = (ROWS - 1, COLS - 1)

        self.control_frame = tk.Frame(root)
        self.control_frame.pack()

        tk.Button(self.control_frame, text="Generate New Maze", command=self.generate_maze).pack(side=tk.LEFT)
        tk.Button(self.control_frame, text="Solve with DFS", command=self.solve_dfs).pack(side=tk.LEFT)
        tk.Button(self.control_frame, text="Solve with BFS", command=self.solve_bfs).pack(side=tk.LEFT)

        self.generate_maze()

    def generate_maze(self):
        self.grid = [[1 for _ in range(COLS)] for _ in range(ROWS)]

        def carve_passages(r, c):
            directions = [(0, 2), (0, -2), (2, 0), (-2, 0)]
            random.shuffle(directions)
            for dr, dc in directions:
                nr, nc = r + dr, c + dc
                if 0 <= nr < ROWS and 0 <= nc < COLS and self.grid[nr][nc] == 1:
                    self.grid[nr][nc] = 0
                    self.grid[r + dr // 2][c + dc // 2] = 0
                    carve_passages(nr, nc)

        self.grid[0][0] = 0
        carve_passages(0, 0)
        self.grid[ROWS - 1][COLS - 1] = 0
        self.draw_grid()

    def draw_grid(self, path=None):
        self.canvas.delete("all")
        for r in range(ROWS):
            for c in range(COLS):
                color = "black" if self.grid[r][c] == 1 else "white"
                if (r, c) == self.start:
                    color = "green"
                elif (r, c) == self.end:
                    color = "red"
                elif path and (r, c) in path:
                    color = "yellow"
                self.canvas.create_rectangle(
                    c * CELL_SIZE, r * CELL_SIZE,
                    (c + 1) * CELL_SIZE, (r + 1) * CELL_SIZE,
                    fill=color, outline="gray"
                )
        self.root.update()

    def solve_dfs(self):
        visited = set()
        path = []

        def dfs(r, c):
            # ✅ Boundary check
            if not (0 <= r < ROWS and 0 <= c < COLS):
                return False
            if (r, c) in visited or self.grid[r][c] == 1:
                return False

            visited.add((r, c))
            path.append((r, c))
            self.draw_grid(path)
            self.root.after(10)

            if (r, c) == self.end:
                return True

            for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                if dfs(r + dr, c + dc):
                    return True

            path.pop()
            return False

        if not dfs(*self.start):
            tk.messagebox.showinfo("Result", "No path found!")
        else:
            tk.messagebox.showinfo("Result", "Path found using DFS!")

    def solve_bfs(self):
        visited = set()
        queue = deque()
        prev = {}
        queue.append(self.start)
        visited.add(self.start)

        while queue:
            r, c = queue.popleft()
            if (r, c) == self.end:
                break
            for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                nr, nc = r + dr, c + dc
                if 0 <= nr < ROWS and 0 <= nc < COLS and self.grid[nr][nc] == 0 and (nr, nc) not in visited:
                    queue.append((nr, nc))
                    visited.add((nr, nc))
                    prev[(nr, nc)] = (r, c)
            self.draw_grid(visited)
            self.root.after(5)

        path = []
        curr = self.end
        while curr in prev:
            path.append(curr)
            curr = prev[curr]
        path.append(self.start)
        path.reverse()

        if self.end not in prev:
            tk.messagebox.showinfo("Result", "No path found!")
        else:
            self.draw_grid(path)
            tk.messagebox.showinfo("Result", "Path found using BFS!")

if __name__ == "__main__":
    root = tk.Tk()
    app = MazeSolverApp(root)
    root.mainloop()
