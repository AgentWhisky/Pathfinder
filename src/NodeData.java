import java.util.ArrayList;

/**
 * Record for storing a Node and a Path
 */
public record NodeData(Node n, ArrayList<Node> path) {

    public Node getNode() {
        return n;
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    /**
     * Method to get the current path as a Node Array
     * @return a Point Array
     */
    public Node[] getPathAsArray() {
        // Convert ArrayList to Array
        Node[] arrPath = new Node[path.size()];
        arrPath = path.toArray(arrPath);
        return arrPath;
    }


}