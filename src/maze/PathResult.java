package maze;

import java.util.LinkedList;

public record PathResult(String[][] maze, Node start, Node goal, LinkedList<Node> path,
                         LinkedList<Node> expandedOrder) {
    /**
     * Constructor - Creates a pathResult object to store all information to display pathfinder
     * @param maze          is the 2D String maze
     * @param start         is the start node
     * @param goal          is the goal node
     * @param path          is the found path from start to goal
     * @param expandedOrder is the order the nodes were expanded
     */
    public PathResult {
    }

    public String[][] getMaze() {
        return maze;
    }

    public Node getStart() {
        return start;
    }

    public Node getGoal() {
        return goal;
    }

    public LinkedList<Node> getPath() {
        return path;
    }

    public LinkedList<Node> getExpandedOrder() {
        return expandedOrder;
    }

    /**
     * Method to return the length of the current path
     * @return length of the path or -1 for no path
     */
    public int pathLength() {
        if (path != null) {
            return path.size();
        }
        return -1;
    }

}
