import java.util.*;

public class PathAlgorithms {

    /**
     * Method to find a path in the given board from [start] to [finish] using DFS
     * @param start is the start of the path
     * @param finish is the end of the path
     * @param b is the given board
     * @return a path between start and finish or null if none exists
     */
    public static Node[] depthFirstSearch(Node start, Node finish, Board b) {

        Stack<NodeData> stack = new Stack<>();
        stack.push(new NodeData(start, new ArrayList<>()));

        HashSet<Node> visitedNodes = new HashSet<>();

        while(!stack.isEmpty()) {
            NodeData nodeData = stack.pop();

            Node curNode = nodeData.getNode();

            // Point is the finish
            if(curNode.equals(finish)) {
                nodeData.getPath().add(curNode);
                return nodeData.getPathAsArray();
            }

            // Expand Node
            if(!visitedNodes.contains(curNode)) {
                visitedNodes.add(curNode);

                Node[] neighbors = b.getNeighbors(curNode);

                for(Node n : neighbors) {
                    ArrayList<Node> newPath = new ArrayList<>(nodeData.getPath()); // Copy Path
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
    public static Node[] breadthFirstSearch(Node start, Node finish, Board b) {

        Queue<NodeData> queue = new LinkedList<>();
        queue.add(new NodeData(start, new ArrayList<>()));

        HashSet<Node> visitedNodes = new HashSet<>();

        while(!queue.isEmpty()) {
            NodeData nodeData = queue.poll();

            Node curNode = nodeData.getNode();

            // Point is the finish
            if(curNode.equals(finish)) {
                nodeData.getPath().add(curNode);
                return nodeData.getPathAsArray();
            }

            // Expand Node
            if(!visitedNodes.contains(curNode)) {
                visitedNodes.add(curNode);

                Node[] neighbors = b.getNeighbors(curNode);

                for(Node n : neighbors) {
                    ArrayList<Node> newPath = new ArrayList<>(nodeData.getPath()); // Copy Path
                    newPath.add(n); // Add Current Node to Path
                    queue.add(new NodeData(n, newPath));
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        Board b = new Board("boards/board-easy1");
        System.out.println(b);


        Node start = new Node(2, 0);
        Node finish = new Node(4, 4);
        Node[] dfsPath = depthFirstSearch(start, finish, b);
        Node[] bfsPath = breadthFirstSearch(start, finish, b);

        System.out.println("---DFS---");
        for(Node n : dfsPath) {
            System.out.println(n);
        }

        System.out.println("---BFS---");
        for(Node n : bfsPath) {
            System.out.println(n);
        }
    }
}
