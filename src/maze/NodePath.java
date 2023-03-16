package maze;

import java.util.LinkedList;

/**
 * Record for storing a maze.Node and its Path
 */
public record NodePath(Node node, LinkedList<Node> path) {}
