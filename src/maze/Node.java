package maze;

import java.util.Objects;

/**
 * Record for storing a 2D Node
 * (Can Include a Direction for reference to the next node)
 */
public class Node {
    // Directions
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;

    private final int x;
    private final int y;
    private int direction;

    /**
     * Constructor - Initializes a node with given x and y
     * @param x is given x
     * @param y is given y
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor - Special Constructor for setting x and y as well as the node direction
     * @param x is given x
     * @param y is given y
     * @param direction is given direction
     */
    public Node(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    // *** Get Methods ***
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    /**
     * Method to update the direction of a node
     * @param direction is the given direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Method to check if the direction is valid
     * @return if direction is valid
     */
    public boolean isValidDirection() {
        return this.direction >= NORTH && this.direction <= WEST;
    }

    /**
     * Method Override to ignore direction on object comparison
     * @param obj is the other object
     * @return is whether the given object equals the current
     */
    @Override
    public boolean equals(Object obj) {
        // Check if given object is a node
        if(!(obj instanceof Node other)) {
            return false;
        }
        // Only check if x and y match; Ignore Direction
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Method Override to ignore direction during hashing
     * @return hash of x and y
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Method to get String representation of Node
     * @return String represenation of the Node
     */
    public String toString() {
        return String.format("Node[%d,%d]-%d", x, y, direction);
    }
}