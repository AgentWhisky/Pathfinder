package maze;

import java.util.*;

/**
 * Class - Used to store all Pathfinder path algorithms
 *
 * >>> IMPORTANT <<<
 * All Methods in this class MUST:
 *      Take a Start maze.Node, Goal Node, and Maze Object
 *      Return a PathResult Object
 *
 *      Calculate a Path to Return if possible
 *      The Path Must Include Both Start and Goal Nodes
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
        // Setup Stack
        Stack<NodePath> npStack = new Stack<>();

        // Add Start Node to Stack
        LinkedList<Node> path = new LinkedList<>();
        path.add(start);
        npStack.push(new NodePath(start, path));

        // Setup Visited Node Set
        HashSet<Node> visited = new HashSet<>();

        // DFS
        while(!npStack.isEmpty()) {
            // Pop Node and Path
            NodePath np = npStack.pop();
            Node curNode = np.node();

            // Check if current node is the goal
            if(curNode.equals(goal)) {
                // Get Path Cost
                int pathCost = m.getCostFromPath(np.path());

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
                // Add Node to Visited
                visited.add(curNode);

                // Get Neighbor Nodes
                LinkedList<Node> neighbors = m.getNeighbors(curNode, true);

                // Add All Neighbors to Stack
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
        Queue<NodePath> npQueue = new LinkedList<>(); // Setup Queue

        // Add Start Node to Queue
        LinkedList<Node> path = new LinkedList<>();
        path.add(start);
        npQueue.add(new NodePath(start, path));

        // Setup Visited Node Set
        HashSet<Node> visited = new HashSet<>();

        // BFS
        while(!npQueue.isEmpty()) {
            // Dequeue Node and Path
            NodePath np = npQueue.poll();
            Node curNode = np.node();

            // Check if current node is the goal
            if(curNode.equals(goal)) {
                // Get Path Cost
                int pathCost = m.getCostFromPath(np.path());

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
                // Add Node to Visited
                visited.add(curNode);

                // Get Neighbor Nodes
                LinkedList<Node> neighbors = m.getNeighbors(curNode, true);

                // Add All Neighbors to Queue
                for(Node n : neighbors) {
                    LinkedList<Node> newPath = new LinkedList<>(np.path());

                    newPath.add(n);
                    npQueue.add(new NodePath(n, newPath));
                }
            }
        }
        return null;
    }

    /**
     * Method to run a Uniform Cost Search Algorithm
     * This Algorithm utilizes the assigned cost of the nodes to choose the least-cost-path
     * @param start is the given start node
     * @param goal is the given goal node
     * @param m is the maze to search through
     * @return a PathResult object with result
     */
    public static PathResult uniformCostSearch(Node start, Node goal, Maze m) {
        // Priority Queue - Cumulative Action Cost

        // Record for Storing NodePath and Priority Cost
        record UCSNodePath(NodePath nodePath, int cost) {}

        // Setup Priority Queue
        PriorityQueue<UCSNodePath> npPQueue = new PriorityQueue<>(Comparator.comparingInt(UCSNodePath::cost));

        // Add Start to Priority Queue
        LinkedList<Node> path = new LinkedList<>();
        path.add(start);
        npPQueue.add(new UCSNodePath(new NodePath(start, path), 0));

        // Setup Visited Node Set
        HashSet<Node> visited = new HashSet<>();

        // UCS
        while(!npPQueue.isEmpty()) {
            // Pop from Priority Queue
            UCSNodePath pnp = npPQueue.poll();
            int curCost = pnp.cost();
            Node curNode = pnp.nodePath().node();

            // Check if current node is the goal
            if(curNode.equals(goal)) {
                // Get Path Cost
                int pathCost = m.getCostFromPath(pnp.nodePath().path());

                return new PathResult(
                        m.getMaze(),
                        start,
                        goal,
                        pnp.nodePath().path(),
                        m.getExpandedOrder(),
                        m.getExpanded(),
                        pathCost
                );
            }

            // Expand Node
            if(!visited.contains(curNode)) {
                // Add Node to Visited
                visited.add(curNode);

                // Get Neighbor Nodes
                LinkedList<Node> neighbors = m.getNeighbors(curNode, true);

                // Add All Neighbors to Queue
                for(Node n : neighbors) {
                    LinkedList<Node> newPath = new LinkedList<>(pnp.nodePath().path());
                    newPath.add(n);

                    // Get New Cost for Priority
                    int newCost = m.getCost(n) + curCost;

                    // Add To Priority Queue
                    npPQueue.add(new UCSNodePath(new NodePath(n, newPath), newCost));
                }
            }
        }

        return null;
    }

    /**
     * Method to run an A* Search Algorithm
     * This Algorithm utilizes the total cost of each node + a distance-to-goal heuristic
     * @param start is the given start node
     * @param goal is the given goal node
     * @param m is the maze to search through
     * @return a PathResult object with result
     */
    public static PathResult aStarSearch(Node start, Node goal, Maze m) {
        // Priority Queue - (Cumulative Action Cost + Heuristic)
        // The Heuristic used in this algorithm is the Manhattan Distance between a Node and the given Goal

        // Record for Storing NodePath and Priority Cost
        record AStarNodePath(NodePath nodePath, int costHeuristic) {}

        // Setup Priority Queue
        PriorityQueue<AStarNodePath> npPQueue = new PriorityQueue<>(Comparator.comparingInt(AStarNodePath::costHeuristic));

        // Add Start to Priority Queue
        LinkedList<Node> path = new LinkedList<>();
        path.add(start);
        npPQueue.add(new AStarNodePath(new NodePath(start, path), 0));

        // Setup Visited Node Set
        HashSet<Node> visited = new HashSet<>();

        // A* Search
        while(!npPQueue.isEmpty()) {
            // Pop from Priority Queue
            AStarNodePath pnp = npPQueue.poll();
            int curCost = pnp.costHeuristic();
            Node curNode = pnp.nodePath().node();

            // Check if current node is the goal
            if(curNode.equals(goal)) {
                // Get Path Cost
                int pathCost = m.getCostFromPath(pnp.nodePath().path());

                return new PathResult(
                        m.getMaze(),
                        start,
                        goal,
                        pnp.nodePath().path(),
                        m.getExpandedOrder(),
                        m.getExpanded(),
                        pathCost
                );
            }

            // Expand Node
            if(!visited.contains(curNode)) {
                // Add Node to Visited
                visited.add(curNode);

                // Get Neighbor Nodes
                LinkedList<Node> neighbors = m.getNeighbors(curNode, true);

                // Add All Neighbors to Queue
                for(Node n : neighbors) {
                    LinkedList<Node> newPath = new LinkedList<>(pnp.nodePath().path());
                    newPath.add(n);

                    // Get New Cost for Priority = Total Cost + Heuristic Distance to Goal
                    int newCost = (m.getCost(n) + curCost) + m.getManhattanDistance(n, goal);

                    // Add To Priority Queue
                    npPQueue.add(new AStarNodePath(new NodePath(n, newPath), newCost));
                }
            }

        }
        return null;
    }

    /* TO ADD A NEW ALGORITHM
    1. Add an algorithm ID below
    2. Add algorithm ID to algorithm list method
    3. Add to runAlgorithm method
     */

    // *** Algorithm Utilities ***
    private static final String DFS = "Depth First Search";
    private static final String BFS = "Breadth First Search";
    private static final String UCS = "Uniform Cost Search";
    private static final String A_STAR = "A* Search";


    /**
     * Method to get all available pathfinding algorithm names
     * @return String Array of Algorithm names
     */
    public static String[] getAlgorithmList() {
        return new String[]{DFS, BFS, UCS, A_STAR};
    }

    /**
     * Method to run the given algorithm on the given maze
     * @param algorithm is the given algorithm
     * @param start is the start node
     * @param goal is the goal node
     * @param maze given maze
     * @return Result of Given Algorithm
     */
    public static PathResult runAlgorithm(String algorithm, Node start, Node goal, Maze maze) {
        switch (algorithm) {
            case DFS -> {
                return depthFirstSearch(start, goal, maze);
            }
            case BFS -> {
                return breadthFirstSearch(start, goal, maze);
            }
            case UCS -> {
                return uniformCostSearch(start, goal, maze);
            }
            case A_STAR -> {
                return aStarSearch(start, goal, maze);
            }
        }
        return null;
    }
}
