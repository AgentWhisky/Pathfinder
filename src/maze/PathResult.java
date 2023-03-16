package maze;

import java.util.HashSet;
import java.util.LinkedList;

public record PathResult(String[][] maze, Node start, Node goal, LinkedList<Node> path,
                         LinkedList<Node> expandedOrder, HashSet<Node> expanded, int pathCost) {
    /**
     * Constructor - Creates a pathResult object to store all information to display pathfinder
     * @param maze          is the 2D String maze
     * @param start         is the start node
     * @param goal          is the goal node
     * @param path          is the found path from start to goal
     * @param expandedOrder is the order the nodes were expanded
     * @param expanded is the set of all expanded nodes
     * @param pathCost is the total costHeuristic of the path
     */
    public PathResult {
        // Push all Node Directions Back One To Accurately reflect their direction
        Node prev = null;
        for(Node n : path) {
            if(prev != null) {
                prev.setDirection(n.getDirection());
            }
            prev = n;
        }
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
