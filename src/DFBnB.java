import java.util.*;

public class DFBnB {

    final private static int MAX_LIMIT = 25; // threshold bound

    public static String search(Node start, State goal) {
        // already goal
        if (start.state.isGoalState(goal)) {
            return "nNum: " + 0 + "\nCost: " + 0;
        }

        int totalNodeCount = 0;     // Counter for created nodes
        int t = MAX_LIMIT;  // Initial threshold

        Stack<Node> L = new Stack<>();
        HashMap<State, Node> H = new HashMap<>(); // stack fast track
        String result = "";

        L.push(start);
        H.put(start.state, start);

        while (!L.isEmpty()) {
            Node n = L.pop();

            if (n.isOut) { // if it marked remove it
                H.remove(n.state);
                continue;
            }

            n.isOut = true; // mark as removed
            L.push(n);      // push it back to the end

            List<Node> N = new ArrayList<>(); // list of new generated successors

            for (int marbleIndex = 0; marbleIndex < 9; marbleIndex++) { // for each cell

                List<State.Operation> allowedOps = Helpers.getAllowedOperators(n, marbleIndex); // generate all possible operations

                for (State.Operation op : allowedOps) {  // for each possible operation
                    try {
                        State gState = State.operator(n.state, marbleIndex, op); // generate new state

                        Node g = new Node(gState, n, op, State.getNewLinearIndex(marbleIndex, op), goal); // generate node for new state
                        totalNodeCount++;

                        N.add(g); // add it to the list
                    } catch (IllegalArgumentException e) {
                        // Ignore invalid moves
                    }
                }
            }

            // Sort successors by fCost
            N.sort(Comparator.comparingInt(node -> node.fCost));

            List<Node> toAdd = new ArrayList<>(); // List for valid nodes to add
            for (Node current : N) {
                // Prune nodes exceeding the threshold
                if (current.fCost >= t) {
                    break;
                }

                Node compare = H.get(current.state);

                if (compare != null && compare.isOut) {
                    continue; // Skip g if it's already "out"
                } else if (compare != null) {
                    if (compare.fCost <= current.fCost) {
                        continue; // Skip worse nodes
                    } else {
                        L.remove(compare);
                        H.remove(compare.state);
                    }
                }

                // Goal check
                if (current.state.isGoalState(goal)) {
                    t = current.fCost; // Update threshold
                    current.isOut = true;           // for reconstruction only
                    H.put(current.state, current);  // for reconstruction only
                    result = current.reconstructPathFromOutNodes(H) +
                            "\nNum: " + totalNodeCount +
                            "\nCost: " + current.gCost;
                    current.isOut = false;
                    H.remove(current.state);
                    break;
                }

                toAdd.add(current); // Add to list for stack
            }

            // Add successors to the stack in reverse order
            Collections.reverse(toAdd);
            for (Node g : toAdd) {
                L.push(g);
                H.put(g.state, g);
            }
        }

        // if result wasn't updated return no path else return result
        return result.isEmpty() ? "no path" + "\nNum: " + totalNodeCount + "\nCost: inf" : result;
    }
}
