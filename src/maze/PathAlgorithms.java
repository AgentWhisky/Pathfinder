package maze;

import java.util.*;

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

                int pathCost = 0;
                for(Node n : np.path()) {
                    pathCost += m.getCost(n);
                }

                return new PathResult(
                        m.getMaze(),
                        start,
                        goal,
                        np.path(),
                        m.getExpandedOrder(),
                        m.getExpanded(),
                        pathCost
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

    /**
     * Method to run a Breadth First Search Pathfinding Algorithm
     * @param start is the given start node
     * @param goal is the given goal node
     * @param m is the maze to search through
     * @return a PathResult object with result
     */
    public static PathResult breadthFirstSearch(Node start, Node goal, Maze m) {

        // Setup
        Queue<NodePath> npQueue = new LinkedList<>(); // Setup Queue

        LinkedList<Node> path = new LinkedList<>();
        path.add(start);
        npQueue.add(new NodePath(start, path));

        HashSet<Node> visited = new HashSet<>(); // Store Visited Nodes

        // Search
        while(!npQueue.isEmpty()) {
            NodePath np = npQueue.poll();
            Node curNode = np.node();

            // Check if current node is the goal
            if(curNode.equals(goal)) {
                np.path().add(curNode);

                int pathCost = 0;
                for(Node n : np.path()) {
                    pathCost += m.getCost(n);
                }

                return new PathResult(
                        m.getMaze(),
                        start,
                        goal,
                        np.path(),
                        m.getExpandedOrder(),
                        m.getExpanded(),
                        pathCost
                );
            }

            // Expand Node
            if(!visited.contains(curNode)) {
                visited.add(curNode);

                LinkedList<Node> neighbors = m.getNeighbors(curNode, true);

                for(Node n : neighbors) {
                    LinkedList<Node> newPath = new LinkedList<>(np.path());

                    newPath.add(n);
                    npQueue.add(new NodePath(n, newPath));
                }
            }
        }


        return null;
    }

    // *** Algorithm Utilities ***
    private static final String DFS = "Depth First Search";
    private static final String BFS = "Breadth First Search";


    /**
     * Method to get all available pathfinding algorithm names
     * @return String Array of Algorithm names
     */
    public static String[] getAlgorithmList() {
        return new String[]{DFS, BFS};
    }

    /**
     * Method to run the given algorithm on the given maze
     * @param algorithm is the given algorithm
     * @param start is the start node
     * @param goal is the goal node
     * @param maze given maze
     * @return
     */
    public static PathResult runAlgorithm(String algorithm, Node start, Node goal, Maze maze) {
        switch (algorithm) {
            case DFS -> {
                return depthFirstSearch(start, goal, maze);
            }
            case BFS -> {
                return breadthFirstSearch(start, goal, maze);
            }
        }
        return null;
    }
}
