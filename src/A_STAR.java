import java.util.*;

public class A_STAR {

    public static String search(Node start, State goal, boolean printOpen) {
        // already goal
        if(start.state.isGoalState(goal)){
            return "nNum: " + 0 + "\nCost: " + 0;
        }

        // check if goal may be reached (has same amount of marbles and same closed cells)
        if(!Helpers.CheckInput(start.state,goal)){
            return "no path\nNum: 0\nCost: inf" ;
        }

        PriorityQueue<Node> L = new PriorityQueue<>(); // open list
        HashSet<Node> H = new HashSet<>();             // close list

        int totalNodeCount = 0; // generated nodes counter
        int step = 0;           // steps counter

        L.add(start);

        while (!L.isEmpty()) {
            if (printOpen) {
                System.out.println("Step " + (++step) + ":");
                Helpers.printOpenList(L);
            }

            Node n = L.poll(); // Get the node with the smallest fCost

            H.add(n);

            for (int marbleIndex = 0; marbleIndex < 9; marbleIndex++) { // for each cell

                List<State.Operation> allowedOps = Helpers.getAllowedOperators(n, marbleIndex); // generate possible operations

                for (State.Operation op : allowedOps) { // for each possible operation
                    try {
                        State gState = State.operator(n.state, marbleIndex, op); // generate new state

                        Node g = new Node(gState, n, op, State.getNewLinearIndex(marbleIndex, op), goal); // generate node for the state
                        totalNodeCount++;

                        if (g.isGoal(goal)) {  // If the goal is reached, reconstruct the path
                            return g.reconstructPath() + "\nNum: " + totalNodeCount + "\nCost: " + g.gCost;
                        }

                        if (H.contains(g)) { // Check if this state is in the closed set
                            for (Node closedNode : H) { // find it
                                if (closedNode.state.equals(g.state)) {
                                    if (g.fCost < closedNode.fCost) { // choose better path
                                        H.remove(closedNode);
                                        L.add(g);
                                    }
                                    break;
                                }
                            }
                            continue; // continue to the next operation
                        }

                        if (!L.contains(g)) { // add state to open list if needed
                            L.add(g);
                        } else { // if it already in open list
                            for (Node openNode : L) { // choose better path
                                if (openNode.state.equals(g.state) && g.fCost < openNode.fCost) {
                                    L.remove(openNode);
                                    L.add(g);
                                    break;
                                }
                            }
                        }

                    } catch (IllegalArgumentException e) {
                        // Ignore invalid moves
                    }
                }
            }
        }

        return "no path\nNum: " + totalNodeCount + "\nCost: inf"; // No solution found
    }
}
