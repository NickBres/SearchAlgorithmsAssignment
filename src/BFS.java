import java.util.*;

public class BFS {

    public static String search(Node start, State goal,boolean printOpen) {
        // start is already a goal
        if(start.state.isGoalState(goal)){
            return "nNum: " + 0 + "\nCost: " + 0;
        }

        int step = 0;               // steps counter
        int totalNodesCount = 0;    // generated nodes counter

        Queue<Node> L = new LinkedList<>();
        HashSet<State> H = new HashSet<>(); // closed and open lists

        L.add(start);
        H.add(start.state);

        while (!L.isEmpty()) {
            if (printOpen) { // print open list for each step
                System.out.println("Step " + (++step) + ":");
                Helpers.printOpenList(L); // Print the open list at each step
            }

            Node n = L.poll();

            for (int marbleIndex = 0; marbleIndex < 9; marbleIndex++) { // Iterate over all positions
                List<State.Operation> allowedOps = Helpers.getAllowedOperators(n, marbleIndex); // get all allowed operations for current cell

                for (State.Operation op : allowedOps) { // iterate over allowed operations
                    try {
                        // Apply the operator to generate a new state
                        State gState = State.operator(n.state, marbleIndex, op);

                        // Generate new node for the state
                        Node g = new Node(gState, n, op, State.getNewLinearIndex(marbleIndex,op),goal);
                        totalNodesCount++;
                        if (g.isGoal(goal)) {
                            return g.reconstructPath() + "\nNum: " + totalNodesCount + "\nCost: " + g.gCost;
                        }
                        if(!H.contains(gState)) { // check closed and open lists
                            H.add(gState);
                            L.add(g);
                        }

                    } catch (IllegalArgumentException e) {
                        // Ignore invalid moves
                    }
                }
            }
        }

        // no solution
        return "no path\nNum: " + totalNodesCount + "\nCost: inf";
    }


}
