package graph;

import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * A class containing basic graph operations.
 */
public class GraphOps {

    /**
     * Private constructor to hide the default one.
     */
    private GraphOps() {}

    /**
     * Performs iterative DFS on the provided graph.
     * @param graph The graph object
     */
    public static void dfsIterative(Graph<GraphNode> graph) {
        Stack<GraphNode> stack = new Stack<>();
        boolean[] isVisited = new boolean[graph.getVertexCount()];
        stack.push(graph.getSource());
        while (!stack.isEmpty()) {
            GraphNode current = stack.pop();
            if(current.getAliveStatus() && !isVisited[current.getValue()]) {
                isVisited[current.getValue()] = true;
                System.out.println(current.getLabel());
                for (GraphNode dest : graph.getAdjList(current)) {
                    if (current.getAliveStatus() && !isVisited[dest.getValue()]) {
                        stack.push(dest);
                    }
                }
            }
        }
    }

    /**
     * Performs iterative BFS on the provided graph
     * @param graph the graph object
     */
    public static void bfsIterative(Graph<GraphNode> graph) {
        Queue<GraphNode> queue = new LinkedList<>();
        boolean[] isVisited = new boolean[graph.getVertexCount()];
        queue.add(graph.getSource());
        while(!queue.isEmpty()) {
            GraphNode current = queue.poll();
            if (current.getAliveStatus() && !isVisited[current.getValue()]) {
                isVisited[current.getValue()] = true;
                System.out.println(current.getLabel());
                for (GraphNode dest: graph.getAdjList(current)) {
                    if (current.getAliveStatus() && !isVisited[dest.getValue()]) {
                        queue.add(dest);
                    }
                }
            }
        }
    }

    /**
     * A private auxilliary method to traverse the graph in depth-first manner for counting the connected components.
     * @param graph Graph object reference
     * @param isVisited Map containing status of nodes
     * @param current Vertex currently being visited
     */
    private static void connectedComponentsUtil(Graph<GraphNode> graph, Map<GraphNode, Boolean> isVisited, GraphNode current) {
        isVisited.put(current, true);
        for(GraphNode dest: graph.getAdjList(current)) {
            if (graph.hasVertex(dest) && dest.getAliveStatus() && Boolean.TRUE.equals(!isVisited.get(dest))) {
                connectedComponentsUtil(graph, isVisited, dest);
            }
        }
    }

    /**
     * Get the number of connected components in the graph
     * @param graph Graph object reference
     * @return number of connected components
     */
    public static int countConnectedComponents(Graph<GraphNode> graph) {
        int connectedComponentsCount = 0;
        Map<GraphNode, Boolean> isVisited = new HashMap<>();
        for (GraphNode graphNode: graph.getVertices()) {
            isVisited.put(graphNode, false);
        }
        for(GraphNode v: graph.getVertices()) {
            if (graph.hasVertex(v) && v.getAliveStatus() && Boolean.TRUE.equals(!isVisited.get(v))) {
                connectedComponentsUtil(graph, isVisited, v);
                connectedComponentsCount += 1;
            }
        }
        return connectedComponentsCount;
    }
}
