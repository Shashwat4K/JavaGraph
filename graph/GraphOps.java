package graph;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A class containing basic graph operations.
 */
public class GraphOps {

    /**
     * Private constructor to hide the default one.
     */
    private GraphOps() {}

    private static class Timer {
        public Integer timer;
        Timer(Integer value) {
            this.timer = value;
        }
        public String toString() {
            return String.valueOf(timer);
        }
    }

    /**
     * Performs iterative DFS on the provided graph.
     * @param graph The graph object
     */
    public static void dfsIterative(Graph<Node> graph) {
        Deque<Node> stack = new LinkedList<>();
        boolean[] isVisited = new boolean[graph.getVertexCount()];
        stack.push(graph.getSource());
        while (!stack.isEmpty()) {
            Node current = stack.pop();
            if(current.getAliveStatus() && !isVisited[current.getValue()]) {
                isVisited[current.getValue()] = true;
                System.out.println(current.getLabel());
                for (Node dest : graph.getAdjList(current)) {
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
    public static void bfsIterative(Graph<Node> graph) {
        Queue<Node> queue = new LinkedList<>();
        boolean[] isVisited = new boolean[graph.getVertexCount()];
        queue.add(graph.getSource());
        while(!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.getAliveStatus() && !isVisited[current.getValue()]) {
                isVisited[current.getValue()] = true;
                System.out.println(current.getLabel());
                for (Node dest: graph.getAdjList(current)) {
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
    private static void connectedComponentsUtil(Graph<Node> graph, Map<Node, Boolean> isVisited, Node current) {
        isVisited.put(current, true);
        for(Node dest: graph.getAdjList(current)) {
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
    public static int countConnectedComponents(Graph<Node> graph) {
        int connectedComponentsCount = 0;
        Map<Node, Boolean> isVisited = new HashMap<>();
        for (Node graphNode: graph.getVertices()) {
            isVisited.put(graphNode, false);
        }
        for(Node v: graph.getVertices()) {
            if (graph.hasVertex(v) && v.getAliveStatus() && Boolean.TRUE.equals(!isVisited.get(v))) {
                connectedComponentsUtil(graph, isVisited, v);
                connectedComponentsCount += 1;
            }
        }
        return connectedComponentsCount;
    }

    private static void articulationPointDetectionUtil
    (
        Node v,
        Node w,
        Graph<Node> graph,
        Map<Node, Boolean> visited,
        Map<Node, Integer> timeIn,
        Map<Node, Integer> low,
        Timer timer,
        Set<Node> articulationPoints
    ) {
        visited.put(v, true);
        timeIn.put(v, timer.timer);
        low.put(v, timer.timer);
        timer.timer = timer.timer + 1;
        int children = 0;
        for (Node dest: graph.getAdjList(v)) {
            if (dest.equals(w)) { continue; }
            if (dest.getAliveStatus() && Boolean.TRUE.equals(visited.get(dest))) {
                low.put(v, Math.min(low.get(v), timeIn.get(dest)));
            } else {
                articulationPointDetectionUtil(dest, v, graph, visited, timeIn, low, timer, articulationPoints);
                low.put(v, Math.min(low.get(v), low.get(dest)));
                if (low.get(dest) >= timeIn.get(v) && w.getValue() != -1) {
                    articulationPoints.add(v);
                }
                ++children;
            }
        }
        if (w.getValue() == -1 && children > 1) {
            articulationPoints.add(v);
        }
    }

    public static Set<Node> detectArticulationPoints(Graph<Node> graph) {
        Timer timer = new Timer(0);
        Map<Node, Boolean> visited = new HashMap<>();
        Map<Node, Integer> timeIn = new HashMap<>();
        Map<Node, Integer> low = new HashMap<>();
        Set<Node> vertices = graph.getVertices();
        Set<Node> articulationPoints = new HashSet<>();
        GraphNode p = new GraphNode(-1);
        for (Node v: vertices) {
            visited.putIfAbsent(v, false);
            timeIn.putIfAbsent(v, -1);
            low.putIfAbsent(v, -1);
        }
        for (Node v: vertices) {
            if (v.getAliveStatus() && Boolean.FALSE.equals(visited.get(v))) {
                articulationPointDetectionUtil(
                    v, 
                    p,
                    graph,
                    visited,
                    timeIn,
                    low,
                    timer,
                    articulationPoints
                );
            }
        }
        return articulationPoints;
    }
}
