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
        for (Node node : queue) {
            node.state.printBoard();
        }
        System.out.println("___________");
    }

    public static void printOpenList(Stack<Node> stack) {
        for (Node node : stack) {
            if(!node.isOut)
                node.state.printBoard();
        }
        System.out.println("___________");
    }


}
