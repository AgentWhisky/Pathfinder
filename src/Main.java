public class Main {
    public static void main(String[] args) {
        Board b = new Board("Resources/boards/board-hard1");

        Node start = new Node(2, 0);
        Node finish = new Node(13, 15);

        PathResults pr = PathAlgorithms.breadthFirstSearch(start, finish, b);
        //PathResults pr = PathAlgorithms.depthFirstSearch(start, finish, b);

        PathDisplay dp = new PathDisplay(pr, 0.5);
    }
}
