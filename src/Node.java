import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Node implements Comparable<Node> {
    private static int creationCounter = 0; // Static counter for creation order
    private final int creationTime;        // Unique creation time

    State state;                // Current state
    Node parent;                // Previous node (parent state)
    State.Operation action;     // Action that led to this state
    int movedMarblePos;         // 0-based position of the marble that was moved
    int gCost;                  // Cost to reach this state
    int hCost;                  // Heuristic cost to the goal
    int fCost;                  // Total cost (f = g + h)
    boolean isOut;              // Marker for "out" status

    // Constructor for non-root nodes
    public Node(State state, Node parent, State.Operation action, int movedMarblePos, State goal) {
        this.creationTime = creationCounter++; // Increment counter for each new node
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.movedMarblePos = movedMarblePos;
        this.gCost = parent.gCost + state.calculateCost(movedMarblePos);
        this.hCost = state.calculateHeuristic(goal);
        this.fCost = gCost + hCost;
        this.isOut = false; // Default to not being "out"
    }

    // Constructor for root node
    public Node(State state) {
        this.creationTime = creationCounter++; // Increment counter for each new node
        this.state = state;
        this.parent = null;
        this.action = null;
        this.movedMarblePos = -1; // No marble moved for the root node
        this.gCost = 0;
        this.hCost = 0;
        this.fCost = 0;
        this.isOut = false;
    }

    // Method to reconstruct the path with details
    public String reconstructPath() {
        StringBuilder path = new StringBuilder();
        Node current = this;

        while (current.parent != null) { // Traverse back to the root
            int[] endPos = State.toTuple(current.movedMarblePos); // Ending position
            int[] startPos = State.toTuple(State.getNewLinearIndex(current.movedMarblePos, State.reverseAction(current.action)));

            char marble = current.state.board.charAt(current.movedMarblePos); // Moved marble
            path.insert(0, String.format("(%d,%d):%c:(%d,%d)--",
                    startPos[0] + 1, startPos[1] + 1, marble, endPos[0] + 1, endPos[1] + 1)); // Convert to 1-based

            current = current.parent; // Move to the parent node
        }

        // Remove the trailing "--"
        if (path.length() > 2) {
            path.setLength(path.length() - 2);
        }
        return path.toString();
    }

    public static String reconstructPathFromOutNodes(HashMap<State, Node> openList) {
        // Collect all nodes marked as "out" in the open list
        List<Node> pathNodes = new ArrayList<>();
        for (Node node : openList.values()) {
            if (node.isOut) {
                pathNodes.add(node);
            }
        }

        // Sort the nodes by their gCost (or depth) to reconstruct the path in order
        pathNodes.sort(Comparator.comparingInt(n -> n.gCost));

        // Build the path string
        StringBuilder path = new StringBuilder();
        for (Node node : pathNodes) {
            if (node.parent != null) { // Skip the root node as it has no action
                int[] endPos = State.toTuple(node.movedMarblePos); // Ending position
                int[] startPos = State.toTuple(State.getNewLinearIndex(node.movedMarblePos, State.reverseAction(node.action)));

                char marble = node.state.board.charAt(node.movedMarblePos); // Moved marble
                path.append(String.format("(%d,%d):%c:(%d,%d)--",
                        startPos[0] + 1, startPos[1] + 1, marble, endPos[0] + 1, endPos[1] + 1)); // Convert to 1-based
            }
        }

        // Remove the trailing "--" if any
        if (path.length() > 2) {
            path.setLength(path.length() - 2);
        }

        return path.toString();
    }


    @Override
    public int compareTo(Node other) {
        if (this.fCost != other.fCost) {
            return Integer.compare(this.fCost, other.fCost); // Compare by total cost
        }
        return Integer.compare(this.creationTime, other.creationTime); // Final tie-break by creation time
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node other = (Node) obj;
        return state.equals(other.state); // Compare only the state
    }

    @Override
    public int hashCode() {
        return state.hashCode(); // Hash based on the state
    }

    public boolean isGoal(State goal){
        return state.isGoalState(goal);
    }
}
