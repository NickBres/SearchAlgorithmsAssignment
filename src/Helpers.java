import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

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


}
