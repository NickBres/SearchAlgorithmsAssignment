# Search Algorithms Assignment

This project implements several search algorithms to solve a 3x3 board game problem. The goal is to find the optimal sequence of moves to transition from a given initial board state to a specified goal state, minimizing the overall cost. The algorithms implemented include BFS, DFID, A*, IDA*, and DFBnB.

---

## Input File Format

The program reads input from a file formatted as follows:

1. **Algorithm Name**: Specifies the search algorithm to use (`BFS`, `DFID`, `A*`, `IDA*`, or `DFBnB`).
2. **Runtime Options**: Choose whether to include execution time and open list output (`with time`, `no time`, `with open`, or `no open`).
3. **Initial State**: A comma-separated representation of the 3x3 board. Example:
    ```
    R,R,_
    B,B,_
    G,G,X
    ```
4. **Goal State**: Prefaced with "Goal state:", specifying the target board configuration:
    ```
    Goal state:
    G,R,R
    B,B,_
    _,G,X
    ```

### Example Input File
```
BFS
with time
with open
R,R,_
B,B,_
G,G,X
Goal state:
G,R,R
B,B,_
_,G,X
```

---

## Output File Format

The program writes the solution to an output file. The format includes:

1. **Move Sequence**: Describes each move using the format `(start_row,start_col):Color:(end_row,end_col)`.
2. **Statistics**:
    - `Num:` Total number of nodes generated during the search.
    - `Cost:` Total cost of the solution.
    - Runtime (if requested): Execution time in seconds.

### Example Output
```
(1,1):R:(1,3)--(3,1):G:(1,1)
Num: 10
Cost: 13
0.024 seconds
```

---

## Additional Features

### Running Multiple Algorithms
The program can be modified to evaluate multiple algorithms on the same input. To enable this, update the code in **Ex1.java**:
```java
static boolean RUN_COMPARISON = true; // Run multiple algorithms on the input
static Task.Algorithm ALGO_NAME[] = {
    Task.Algorithm.BFS,
    Task.Algorithm.DFID,
    Task.Algorithm.A_STAR,
    Task.Algorithm.IDA_STAR,
    Task.Algorithm.DFBNB
};
```

### Output Examples
You can view example outputs in the repository:
[Outputs Folder](https://github.com/NickBres/SearchAlgorithmsAssignment/tree/master/outputs)

### Input Examples
A sample input file is provided here:
[Sample Inputs](https://github.com/NickBres/SearchAlgorithmsAssignment/blob/master/outputs/tests-inputs.txt)

---

## How to Run

1. Place the `input.txt` file in the same directory as the program.
2. Compile the code:
   ```bash
   javac *.java
   ```
3. Run the program:
   ```bash
   java Ex1
   ```
4. The output will be generated in `output.txt`.

---

## Notes

- Follow the exact input and output format to ensure correctness.
- Implemented algorithms may vary in optimality (e.g., BFS and DFID may not find the least-cost path).

---
