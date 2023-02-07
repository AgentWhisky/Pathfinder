import java.util.HashSet;

public record PathResults(Board board, HashSet<Node> expanded, Node[] path) {

}
