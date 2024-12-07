import java.util.*;

public class IDA_STAR {
    public static String search(Node start, State goal) {
        // already goal
        if(start.state.isGoalState(goal)){
            return "nNum: " + 0 + "\nCost: " + 0;
        }

        int totalNodeCount = 0;       // Counter for total created nodes
        int t = start.hCost;  // Initial threshold is the heuristic value of the start node

        while (true) {
            int minF = Integer.MAX_VALUE; // min threshold found

            Stack<Node> L = new Stack<>();
            HashMap<State, Node> H = new HashMap<>(); // stack fast tracking

            start.isOut = false;
            L.push(start);
            H.put(start.state, start);

            while (!L.isEmpty()) {
                Node n = L.pop(); // current node

                if (n.isOut) { // remove it if should
                    H.remove(n.state);
                    continue;
                }

                n.isOut = true; // mark as removed from stack
                L.push(n); // push it back to the end

                for (int marbleIndex = 0; marbleIndex < 9; marbleIndex++) { // for each cell

                    List<State.Operation> allowedOps = Helpers.getAllowedOperators(n, marbleIndex); // generate all possible operations

                    for (State.Operation op : allowedOps) { // for each possible operation
                        try {
                            State gState = State.operator(n.state, marbleIndex, op); // generate new state

                            Node g = new Node(gState, n, op, State.getNewLinearIndex(marbleIndex,op),goal); // generate a node for new state
                            totalNodeCount++;

                            if (g.fCost > t) { // Check if fCost exceeds the current threshold
                                minF = Math.min(minF, g.fCost); // update min fCost found
                                continue; // go to next operation
                            }

                            if (g.isGoal(goal)) { // goal reached
                                g.isOut = true;
                                H.put(gState, g);
                                return g.reconstructPathFromOutNodes(H) +
                                        "\nNum: " + totalNodeCount +
                                        "\nCost: " + g.gCost;
                            }

                            // Handle open list (with loop avoidance)
                            if (H.containsKey(gState)) {
                                Node existingNode = H.get(gState);
                                if (!existingNode.isOut && existingNode.fCost > g.fCost) {
                                    L.remove(existingNode);
                                    H.remove(gState);
                                } else {
                                    continue;
                                }
                            }

                            // Add new node to the stack and open list
                            L.push(g);
                            H.put(gState, g);

                        } catch (IllegalArgumentException e) {
                            // Ignore invalid moves
                        }
                    }
                }
            }

            // If no solution found within the current threshold
            if (minF == Integer.MAX_VALUE) {
                return "no path" + "\nNum: " + totalNodeCount + "\nCost: inf"; // No solution found
            }

            t = minF; // Update threshold
        }
    }
}
