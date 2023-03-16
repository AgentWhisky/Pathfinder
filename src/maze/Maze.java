package maze;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.WeakHashMap;

/**
 * Class to store a maze.Maze Object with useful functions for pathfinding
 */
public class Maze {
    private static final String WALL = "_";
    private final String[][] map;

    // Expanded Nodes
    private LinkedList<Node> expandedOrder;
    private HashSet<Node> expanded;

    /**
     * Constructor - Create a new maze.Maze Object with the given 2D maze.Maze Map
     * @param map is the given 2D String Array storing the map
     */
    public Maze(String[][] map) {
        this.map = map;

        resetExpanded();
    }

    /**
     * Constructor - Create a new maze.Maze Object with the given filepath to a maze file
     * @param filepath is the given path to a maze file
     */
    public Maze(String filepath) {
        this.map = FileUtils.importMazeFile(filepath);

        resetExpanded();
    }
    // *** Booleans ***

    /**
     * Method to determine if a given node is valid (In the maze)
     * @param n is the given node
     * @return if the node is valid
     */
    public boolean isValidNode(Node n) {
        return (n.getX() >= 0 && n.getX() < getHeight()) && (n.getY() >= 0 && n.getY() < getWidth());
    }

    /**
     * Method to return if a given point is a wall
     * @param n is the given point
     * @return if the point is a wall
     */
    public boolean isWall(Node n) {
        if(isValidNode(n)) {
            return getTileStr(n).equalsIgnoreCase(WALL);
        }
        return false;
    }

    /**
     * Method to return if a given point is open (Not a wall)
     * @param n is the given point
     * @return if the point is open
     */
    public boolean isOpen(Node n) {
        if(isValidNode(n)) {
            return WhiskyUtils.isNumeric(getTileStr(n));
        }
        return false;
    }

    // *** Get Methods ***

    /**
     * Method to return the 2D String Array storing the maze
     * @return 2D String array of maze
     */
    public String[][] getMaze() {
        return map;
    }

    /**
     * Method to return the current maze.Node Expanded Order
     * @return the current Expanded Order
     */
    public LinkedList<Node> getExpandedOrder() {
        return expandedOrder;
    }

    /**
     * Method to return the set of all expanded nodes
     * @return set of all expanded nodes
     */
    public HashSet<Node> getExpanded() {
        return expanded;
    }

    /**
     * Method to get the String at given point
     * @param n is the given point
     * @return the String at point
     */
    public String getTileStr(Node n) {
        return map[n.getX()][n.getY()];
    }

    /**
     * Method to get the Height of the board
     * @return board height
     */
    public int getHeight() {
        return map.length;
    }

    /**
     * Method to get the Width of the board
     * @return board width
     */
    public int getWidth() {
        return map[0].length;
    }

    /**
     * Method to get the costHeuristic of a point on the board
     * @param n is the given point
     * @return the costHeuristic at point or default 0 if point is a wall (Ensure no given point is a wall)
     */
    public int getCost(Node n) {
        String s = getTileStr(n);

        if(isWall(n)) {
            return 0;
        }
        return Integer.parseInt(s);
    }

    // *** Utility Methods ***

    /**
     * Method to reset the current expanded order
     */
    public void resetExpanded() {
        expandedOrder = new LinkedList<>();
        expanded = new HashSet<>();
    }

    /**
     * Method to get all Open (Non-Wall) Neighbors of a given node
     * Automatically adds given node to expanded order if not already in list
     * @param n is the given node
     * @param shuffle is whether result should be shuffled (Without favors order N,S,E,W)
     * @return an array of Open Neighbors (Can be empty)
     */
    public LinkedList<Node> getNeighbors(Node n, boolean shuffle) {

        // Handle Expansion
        if(!expanded.contains(n)) {
            expanded.add(n);
            expandedOrder.add(n);
        }

        LinkedList<Node> neighbors = new LinkedList<>();

        int x = n.getX();
        int y = n.getY();

        // N,S,E,W
        Node north = new Node(x-1, y, Node.NORTH);
        Node south = new Node(x+1, y, Node.SOUTH);
        Node east = new Node(x, y+1, Node.EAST);
        Node west = new Node(x, y-1, Node.WEST);

        if(isOpen(north)) {
            neighbors.add(north);
        }
        if(isOpen(south)) {
            neighbors.add(south);
        }
        if(isOpen(east)) {
            neighbors.add(east);
        }
        if(isOpen(west)) {
            neighbors.add(west);
        }

        if(shuffle) {
            // Shuffle LinkedList to prevent path algorithms favoring specific directions
            Collections.shuffle(neighbors);
        }
        return neighbors;
    }

    /**
     * Method to run a given pathfinding algorithm through the maze
     * (Intended for use through Pathfinder Application, not Separate)
     * @param algorithm is the algorithm string
     * @param start is the start node
     * @param goal is the goal node
     * @return the PathResult of the pathfinding algorithm
     */
    public PathResult runAlgorithm(String algorithm, Node start, Node goal) {
        resetExpanded(); // Reset Expanded Nodes
        return PathAlgorithms.runAlgorithm(algorithm, start, goal, this); // Run Pathfinding Algorithm
    }

    /**
     * Method to return string representation of the maze.Maze
     * @return String representation of the maze
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < getHeight(); i++) {
            sb.append("[");
            for(int j = 0; j < getWidth(); j++) {
                sb.append(map[i][j]);
                if(j < getHeight()-1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            if(i < getWidth()-1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Method to get the manhattan distance between two nodes
     * @param a is the first node
     * @param b is the second node
     * @return manhattan distance between given nodes
     */
    public int getManhattanDistance(Node a, Node b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    /**
     * Given a path (Including Start and Goal) get the costHeuristic
     * which includes only nodes moved INTO (which excludes the start node)
     * @param path is the given path which must include the Start and Goal nodes
     * @return the costHeuristic of the given path
     */
    public int getCostFromPath(LinkedList<Node> path) {
        int pathCost = 0;

        boolean skipFirst = false;
        for(Node n : path) {
            if(skipFirst) {
                pathCost += getCost(n);
            }
            else {
                skipFirst = true;
            }
        }
        return pathCost;
    }
}
