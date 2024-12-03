public class Task {
    public enum Algorithm {
        BFS, DFID, A_STAR, IDA_STAR, DFBNB
    }

    Algorithm algorithm;  // Algorithm type
    boolean withTime;     // Flag for time calculation
    boolean printOpen;    // Flag for open state printing
    State initialState;   // Initial state of the board
    State goalState;      // Goal state of the board

    public Task(Algorithm algorithm, boolean withTime, boolean printOpen, State initialState, State goalState) {
        this.algorithm = algorithm;
        this.withTime = withTime;
        this.printOpen = printOpen;
        this.initialState = initialState;
        this.goalState = goalState;
    }
}
