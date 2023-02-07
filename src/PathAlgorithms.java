import java.util.*;

public class PathAlgorithms {

    /**
     * Method to find a path in the given board from [start] to [finish] using DFS
     * @param start is the start of the path
     * @param finish is the end of the path
     * @param b is the given board
     * @return a path between start and finish or null if none exists
     */
    public static PathResults depthFirstSearch(Node start, Node finish, Board b) {

        Stack<NodeData> stack = new Stack<>();

        ArrayList<Node> path = new ArrayList<>();
        path.add(start);
        stack.push(new NodeData(start, path));

        HashSet<Node> visitedNodes = new HashSet<>();

        while(!stack.isEmpty()) {
            NodeData nodeData = stack.pop();

            Node curNode = nodeData.node();

            // Point is the finish
            if(curNode.equals(finish)) {
                nodeData.path().add(curNode);

                return new PathResults(b, visitedNodes, nodeData.getPathAsArray());
            }

            // Expand Node
            if(!visitedNodes.contains(curNode)) {
                visitedNodes.add(curNode);

                Node[] neighbors = b.getNeighbors(curNode);

                for(Node n : neighbors) {
                    ArrayList<Node> newPath = new ArrayList<>(nodeData.path()); // Copy Path
                    newPath.add(n); // Add Current Node to Path
                    stack.push(new NodeData(n, newPath));
                }
            }
        }

        return null;
    }
    /**
     * Method to find a path in the given board from [start] to [finish] using BFS
     * @param start is the start of the path
     * @param finish is the end of the path
     * @param b is the given board
     * @return a path between start and finish or null if none exists
     */
    public static PathResults breadthFirstSearch(Node start, Node finish, Board b) {

        Queue<NodeData> queue = new LinkedList<>();

        ArrayList<Node> path = new ArrayList<>();
        path.add(start);
        queue.add(new NodeData(start, path));

        HashSet<Node> visitedNodes = new HashSet<>();


        while(!queue.isEmpty()) {
            NodeData nodeData = queue.poll();

            Node curNode = nodeData.node();

            // Point is the finish
            if(curNode.equals(finish)) {
                nodeData.path().add(curNode);

                return new PathResults(b, visitedNodes, nodeData.getPathAsArray());
            }

            // Expand Node
            if(!visitedNodes.contains(curNode)) {
                visitedNodes.add(curNode);

                Node[] neighbors = b.getNeighbors(curNode);

                for(Node n : neighbors) {
                    ArrayList<Node> newPath = new ArrayList<>(nodeData.path()); // Copy Path
                    newPath.add(n); // Add Current Node to Path
                    queue.add(new NodeData(n, newPath));
                }
            }
        }

        return null;
    }




    /**
     * Method to generate a new board string of given size
     * @param width is the given width
     * @param height is the given height
     * @param max is the maximum cost (0-max)
     */
    public static void genBoard(int width, int height, int max) {
        Random rand = new Random();

        System.out.println("-----------------");
        System.out.println(width + "," + height);
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                System.out.print(rand.nextInt(max));
                if(j < height-1) {
                    System.out.print(",");
                }
            }
            System.out.println();
        }

        System.out.println("-----------------");
    }
}
