import java.util.*;

public class DFID {

    final private static int MAX_DEPTH = 12; // depth bound

    public static String search(Node start, State goal) {
        // start is a goal
        if(start.state.isGoalState(goal)){
            return "nNum: " + 0 + "\nCost: " + 0;
        }

        int totalNodeCount = 0; // Count of generated nodes
        int depth = 0;          // depth threshold


        while (depth < MAX_DEPTH) { // depth bound
            HashSet<State> H = new HashSet<>();

            // Perform limited DFS for the current depth
            Result result = Limited_DFS(start, goal, depth, H, 0);

            // Accumulate total node count
            totalNodeCount += result.nodeCount;

            if (!result.isCutoff) { // If not cutoff, the result is either a solution or a failure
                if (result.found) {
                    return result.path + "\nNum: " + totalNodeCount + "\nCost: " + result.cost;
                } else {
                    return "no path" + "\nNum: " + totalNodeCount + "\nCost: inf"; // No solution found
                }
            }
            depth++; // Increment depth for the next iteration
        }

        return "no path" + "\nNum: " + totalNodeCount + "\nCost: inf"; // No solution found

    }

    private static Result Limited_DFS(Node n, State goal, int limit, HashSet<State> H, int nodeCount) {

        // Goal check
        if (n.isGoal(goal)) {
            return new Result(n.reconstructPath(), true, nodeCount, n.gCost, false); // Found the goal
        }

        // Depth limit reached
        if (limit == 0) {
            return new Result(null, false, nodeCount, 0, true); // Return cutoff
        }

        H.add(n.state); // Add the current state to the hash table
        boolean isCutoff = false;


        for (int marbleIndex = 0; marbleIndex < 9; marbleIndex++) { // for cell
            List<State.Operation> allowedOps = Helpers.getAllowedOperators(n, marbleIndex); // generate allowed operations

            for (State.Operation op : allowedOps) { // for each allowed operation
                try {
                    State gState = State.operator(n.state, marbleIndex, op); // generate new state

                    Node g = new Node(gState, n, op, State.getNewLinearIndex(marbleIndex, op),goal); // generate node for state
                    nodeCount++;

                    // Skip if already visited
                    if (H.contains(gState)) {
                        continue;
                    }

                    // Recursively call limitedDFS for the child
                    Result result = Limited_DFS(g, goal, limit - 1, H, nodeCount);

                    // Accumulate node counts from the recursive result
                    nodeCount = result.nodeCount;

                    if (result.isCutoff) {
                        isCutoff = true; // Mark that a cutoff occurred
                    } else if (result.found) {
                        return result; // Return the successful path
                    }
                } catch (IllegalArgumentException e) {
                    // Ignore invalid moves
                }
            }
        }

        H.remove(n.state); // Remove the state to release memory
        if (isCutoff) {
            return new Result(null, false, nodeCount, 0, true); // Return cutoff
        } else {
            return new Result(null, false, nodeCount, 0, false); // Return failure
        }
    }

    // Helper class to store search results
    private static class Result {
        String path;
        boolean found;      // True if goal is found
        int nodeCount;      // Count of created nodes
        int cost;           // Total cost of the solution
        boolean isCutoff;   // True if the search was cut off due to depth limit

        Result(String path, boolean found, int nodeCount, int cost, boolean isCutoff) {
            this.path = path;
            this.found = found;
            this.nodeCount = nodeCount;
            this.cost = cost;
            this.isCutoff = isCutoff;
        }
    }
}
