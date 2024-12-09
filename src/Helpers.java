import java.util.*;

public class Helpers {


    public static List<State.Operation> getAllowedOperators(Node node, int marbleIndex) {
        List<State.Operation> allowedOperators = new ArrayList<>();

        for (State.Operation op : State.Operation.values()) {
            // Skip the reverse of the previous move
            if (node.action != null && isReverse(node.action, op) && node.movedMarblePos == marbleIndex) {
                continue;
            }

            // Check if the move is valid
            if (node.state.canMakeMove(marbleIndex, op)) {
                allowedOperators.add(op);
            }
        }
        return allowedOperators;
    }

    // Helper: Check if the operation is the reverse of the last move
    private static boolean isReverse(State.Operation lastOp, State.Operation currentOp) {
        return State.reverseAction(currentOp) == lastOp;
    }

    public static void printOpenList(Queue<Node> queue) {
        printBoards(queue);
    }

    public static void printOpenList(Stack<Node> stack) {
        List<Node> filteredNodes = new ArrayList<>();
        for (Node node : stack) {
            if (!node.isOut) {
                filteredNodes.add(node);
            }
        }
        printBoards(filteredNodes);
    }

    private static void printBoards(Iterable<Node> nodes) {
        List<String[]> boards = new ArrayList<>();

        // Collect rows of each board
        for (Node node : nodes) {
            String[] boardRows = new String[3];
            for (int i = 0; i < 3; i++) {
                boardRows[i] = node.state.board.substring(i * 3, (i + 1) * 3).replace("", " ").trim();
            }
            boards.add(boardRows);
        }

        // Print rows side by side
        for (int row = 0; row < 3; row++) {
            for (String[] board : boards) {
                System.out.print(board[row] + "   "); // Add space between boards
            }
            System.out.println(); // Move to the next row
        }

        for(Node node : nodes) {
            System.out.print("________");
        }
        System.out.println();
    }

    public static boolean CheckInput(State start, State goal) {
        // Convert board states to character arrays
        char[] startBoard = start.board.toCharArray();
        char[] goalBoard = goal.board.toCharArray();

        // Maps to store the count of each marble and closed cells
        Map<Character, Integer> startCount = new HashMap<>();
        Map<Character, Integer> goalCount = new HashMap<>();

        // Check the positions of 'X' and count all characters
        for (int i = 0; i < startBoard.length; i++) {
            char startCell = startBoard[i];
            char goalCell = goalBoard[i];

            // Check if 'X' positions match
            if (startCell == 'X' && goalCell != 'X') {
                return false; // 'X' position mismatch
            }
            if (goalCell == 'X' && startCell != 'X') {
                return false; // 'X' position mismatch
            }

            // Count marbles and other characters
            startCount.put(startCell, startCount.getOrDefault(startCell, 0) + 1);
            goalCount.put(goalCell, goalCount.getOrDefault(goalCell, 0) + 1);
        }

        // Compare counts between start and goal
        for (Map.Entry<Character, Integer> entry : startCount.entrySet()) {
            char marble = entry.getKey();
            int startCountValue = entry.getValue();
            int goalCountValue = goalCount.getOrDefault(marble, 0);

            if (startCountValue != goalCountValue) {
                return false; // Mismatch in marble counts
            }
        }

        return true; // Input is valid
    }


}
