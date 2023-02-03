import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Class - Store a board used for Pathfinding that stores Walls and Tile Costs
 */
public class Board {
    private static final String WALL = "_";
    private String[][] board;

    /**
     * Default Constructor - Builds a board of given size
     */
    public Board(int height, int width) {
        board = new String[height][width];

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                board[i][j] = "0";
            }
        }
    }
    /**
     * Constructor - Import a board from file
     * @param filePath - Path to text file
     */
    public Board(String filePath) {
        importBoard(filePath);
    }

    /**
     * Import a board from a given file
     * @param filepath - Path to text file
     */
    private void importBoard(String filepath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line;

            // Create Board
            line = br.readLine();
            String[] size = line.split(",");
            int height = Integer.parseInt(size[0]);
            int width = Integer.parseInt(size[1]);
            board = new String[height][width];

            // Read Board
            for(int i = 0; i < height; i++) {
                line = br.readLine();
                String[] row = line.split(",");

                for(int j = 0; j < width; j++) {
                    String nodeInfo = row[j];

                    // Check for invalid data in imported file
                    if(!WhiskyUtils.isNumeric(nodeInfo) && !nodeInfo.equalsIgnoreCase(WALL)) {
                        throw new InvalidBoardException("Board File Contains Invalid Data");
                    }

                    board[i][j] = row[j];
                }

            }

            br.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // *** Booleans ***
    /**
     * Method to determine if a given point is valid (On the board)
     * @param n is the given point
     * @return if the point is on the board
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
            return getTile(n).equalsIgnoreCase(WALL);
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
            return WhiskyUtils.isNumeric(getTile(n));
        }
        return false;
    }

    // *** Get Methods ***
    /**
     * Method to get the String at given point
     * @param n is the given point
     * @return the String at point
     */
    public String getTile(Node n) {
        return board[n.getX()][n.getY()];
    }
    /**
     * Method to get the Height of the board
     * @return board height
     */
    public int getHeight() {
        return board.length;
    }

    /**
     * Method to get the Width of the board
     * @return board width
     */
    public int getWidth() {
        return board[0].length;
    }

    /**
     * Method to get the cost of a point on the board
     * @param n is the given point
     * @return the cost at point or default 0 if point is a wall (Ensure no given point is a wall)
     */
    public int getCost(Node n) {
        String s = getTile(n);

        if(isWall(n)) {
            return 0;
        }
        return Integer.parseInt(s);
    }

    // *** Set Methods ***

    /**
     * Method to set a given point as a wall (Overwrites cost)
     * Ignores invalid points
     * @param n is the given point
     */
    public void setWall(Node n) {
        if(isValidNode(n)) {
            board[n.getX()][n.getY()] = WALL;
        }
    }

    /**
     * Method to set a given point as a given cost (Overwrites Walls)
     * Ignores invalid points
     * @param n is the given point
     * @param cost is the given cost
     */
    public void setCost(Node n, int cost) {
        if(isValidNode(n)) {
            board[n.getX()][n.getY()] = "" + cost;
        }
    }

    // *** Utility Methods ***

    /**
     * Method to get all Open (Non-Wall) Neighbors of a given node
     * @param n is the given node
     * @return an array of Open Neighbors (Can be empty)
     */
    public Node[] getNeighbors(Node n) {

        ArrayList<Node> neighbors = new ArrayList<>();

        int x = n.getX();
        int y = n.getY();

        // N,S,E,W
        Node north = new Node(x-1, y);
        Node south = new Node(x+1, y);
        Node east = new Node(x, y+1);
        Node west = new Node(x, y-1);

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

        // Shuffle ArrayList to prevent path algorithms favoring specific directions
        Collections.shuffle(neighbors);

        // Convert ArrayList to Array
        Node[] nodes = new Node[neighbors.size()];
        nodes = neighbors.toArray(nodes);

        return nodes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < getWidth(); i++) {
            sb.append("[");
            for(int j = 0; j < getHeight(); j++) {
                sb.append(board[i][j]);
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
     * Invalid Board Exception
     */
    public static class InvalidBoardException extends RuntimeException {
        public InvalidBoardException(String msg) {
            super(msg);
        }
    }
}
