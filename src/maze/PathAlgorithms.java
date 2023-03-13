package maze;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Class - Used to store all Pathfinder path algorithms
 *
 * >>> IMPORTANT <<<
 * All Methods in this class MUST:
 *      Take a Start maze.Node, Goal maze.Node, and maze.Maze Object
 *      Return a maze.PathResult Object
 *
 *      Calculate a Path to Return if possible
 */
public class PathAlgorithms {

    /**
     * Method to run a Depth First Search Pathfinding Algorithm
     * @param start is the given start node
     * @param goal is the given goal node
     * @param m is the maze to search through
     * @return a maze.PathResult object with result
     */
    public static PathResult depthFirstSearch(Node start, Node goal, Maze m) {
        // Setup
        Stack<NodePath> npStack = new Stack<>(); // Setup Stack

        LinkedList<Node> path = new LinkedList<>();
        path.add(start);
        npStack.push(new NodePath(start, path));

        HashSet<Node> visited = new HashSet<>(); // Store Visited Nodes

        // Search
        while(!npStack.isEmpty()) {
            NodePath np = npStack.pop();
            Node curNode = np.node();

            // Check if current node is the goal
            if(curNode.equals(goal)) {
                np.path().add(curNode);

                return new PathResult(
                        m.getMaze(),
                        start,
                        goal,
                        np.path(),
                        m.getExpandedOrder()
                );
            }

            // Expand maze.Node
            if(!visited.contains(curNode)) {
                visited.add(curNode);

                LinkedList<Node> neighbors = m.getNeighbors(curNode, true);

                for(Node n : neighbors) {
                    LinkedList<Node> newPath = new LinkedList<>(np.path());

                    newPath.add(n);
                    npStack.push(new NodePath(n, newPath));
                }
            }
        }


        return null;
    }
}
