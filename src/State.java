import java.util.List;

public class State {

    // Data Members
    String board;  // String representation of the board

    // Constructors
    public State() {
        this.board = "_________"; // Default 3x3 board
    }

    public State(String board) {
        if (board == null || board.length() != 9) {
            throw new IllegalArgumentException("Board must be a 9-character string");
        }
        this.board = board;
    }

    public State(State other) {
        this.board = other.board;
    }

    // Public Methods
    public static State convertInputToState(List<String> lines) {
        StringBuilder boardBuilder = new StringBuilder();
        for (String line : lines) {
            String[] cells = line.split(","); // Split cells by commas
            for (String cell : cells) {
                boardBuilder.append(cell.charAt(0)); // Append the symbol
            }
        }
        return new State(boardBuilder.toString());
    }

    public static State operator(State currentState, int linearIndex, Operation operation) {
        if (currentState == null) {
            throw new IllegalArgumentException("State cannot be null");
        }

        if (linearIndex < 0 || linearIndex >= 9) {
            throw new IllegalArgumentException("Invalid linear index: " + linearIndex);
        }

        if (!currentState.canMakeMove(linearIndex, operation)) {
            throw new IllegalArgumentException("Move not possible for the given state and operation.");
        }

        int targetIndex = getNewLinearIndex(linearIndex, operation);
        return currentState.swap(linearIndex, targetIndex);
    }

    public boolean canMakeMove(int index, Operation operation) {
        char marble = board.charAt(index);
        if (marble == '_' || marble == 'X') {
            return false; // Cannot move empty or blocked cells
        }

        int targetIndex = getNewLinearIndex(index, operation);
        return board.charAt(targetIndex) == '_'; // Target cell must be empty
    }

    public void printBoard() {
        for (int i = 0; i < board.length(); i++) {
            System.out.print(board.charAt(i) + " ");
            if ((i + 1) % 3 == 0) { // Print a new line after every row
                System.out.println();
            }
        }
        System.out.println(); // Add a blank line after the board
    }

    public boolean isGoalState(State goalState) {
        return board.equals(goalState.board);
    }

    public int calculateCost(int marbleIndex) {
        if (marbleIndex < 0 || marbleIndex >= board.length()) {
            throw new IllegalArgumentException("Invalid marble index: " + marbleIndex);
        }

        char marble = board.charAt(marbleIndex);
        return calculateMarbleCost(marble);
    }

    // Helpers
    private State swap(int index1, int index2) {
        char[] boardArray = board.toCharArray();
        char temp = boardArray[index1];
        boardArray[index1] = boardArray[index2];
        boardArray[index2] = temp;
        return new State(new String(boardArray));
    }

    public static int toLinear(int row, int col) {
        return row * 3 + col; // Convert row and column to linear index
    }

    public static int[] toTuple(int index) {
        return new int[]{index / 3, index % 3}; // Convert linear index to row and column
    }

    public static int getNewLinearIndex(int index, Operation operation) {
        int row = index / 3;  // Convert to row
        int col = index % 3;  // Convert to column

        switch (operation) {
            case UP -> row = (row == 0) ? 2 : row - 1; // Wrap to bottom
            case DOWN -> row = (row == 2) ? 0 : row + 1; // Wrap to top
            case LEFT -> col = (col == 0) ? 2 : col - 1; // Wrap to right
            case RIGHT -> col = (col == 2) ? 0 : col + 1; // Wrap to left
        }

        return toLinear(row, col); // Convert back to linear index
    }

    public static Operation reverseAction(Operation action) {
        return switch (action) {
            case UP -> Operation.DOWN;
            case DOWN -> Operation.UP;
            case LEFT -> Operation.RIGHT;
            case RIGHT -> Operation.LEFT;
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same object reference
        if (obj == null || getClass() != obj.getClass()) return false; // Null or different class
        State other = (State) obj;
        return board.equals(other.board); // Compare board strings
    }

    @Override
    public int hashCode() {
        return board.hashCode(); // Use the hash code of the board string
    }

    @Override
    public String toString(){
        return board;
    }


    // Enum for Operations
    public enum Operation {
        UP, DOWN, LEFT, RIGHT
    }

    public int calculateHeuristic(State goal) {
        int heuristic = 0;

        for (int i = 0; i < board.length(); i++) {
            char marble = board.charAt(i);

            // Ignore empty or blocked cells
            if (marble == '_' || marble == 'X') {
                continue;
            }

            // Add the cost of the marble if it's not in the correct position
            if (marble != goal.board.charAt(i)) {
                heuristic += calculateMarbleCost(marble);
            }
        }
        return heuristic;
    }

    private int calculateMarbleCost(char marble) {
        return switch (marble) {
            case 'R' -> 10; // Red marble
            case 'G' -> 3;  // Green marble
            case 'B' -> 1;  // Blue marble
            default -> 0;   // Invalid characters
        };
    }



}
