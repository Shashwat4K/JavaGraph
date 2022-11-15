package graph;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.time.*;
/**
 * A class containing basic graph operations.
 */
public class GraphOps {

    /**
     * Private constructor to hide the default one.
     */
    private GraphOps() {}
    // private static final int NO_PARENT = -1;
    // private static int time = 0;
    /**
     * A new integer class to pass the objects as reference in methods.
     * // TODO: Rename this class with some meaningful name.
     */
    private static class MyInteger {
        public Integer integer;
        MyInteger(Integer value) {
            this.integer = value;
        }
        public String toString() {
            return String.valueOf(integer);
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
        for(Node v: graph.getVertices()) {
            if(v.getAliveStatus() && !isVisited[v.getValue()]) {
                stack.push(v);
            }
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
    }

    /**
     * Performs iterative BFS on the provided graph
     * @param graph the graph object
     */
    public static void bfsIterative(Graph<Node> graph) {
        Queue<Node> queue = new LinkedList<>();
        boolean[] isVisited = new boolean[graph.getVertexCount()];
        queue.add(graph.getSource());
        for(Node v: graph.getVertices()) {
            if(v.getAliveStatus() && !isVisited[v.getValue()]) {
                queue.add(v);
            }
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
    }

    /**
     * A private auxilliary method to traverse the graph in depth-first manner for counting the connected components.
     * @param graph Graph object reference
     * @param isVisited Map containing status of nodes
     * @param current Vertex currently being visited
     */
    private static void connectedComponentsUtil(Graph<Node> graph, Map<Node, Boolean> isVisited, Node current) throws Exception {
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
        try{
            for(Node v: graph.getVertices()) {
                if (graph.hasVertex(v) && v.getAliveStatus() && Boolean.TRUE.equals(!isVisited.get(v))) {
                    connectedComponentsUtil(graph, isVisited, v);
                    connectedComponentsCount += 1;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return connectedComponentsCount;
    }

    private static void articulationPointDetectionUtil (
        Node v,
        Node parentNode,
        Graph<Node> graph,
        boolean[] visited,
        int[] discovery,
        int[] low,
        MyInteger timer,
        Set<Node> articulationPoints
    ) {
        int children = 0;
        visited[v.getValue()] = true;
        timer.integer = timer.integer + 1;
        discovery[v.getValue()] = low[v.getValue()] = timer.integer;

        for (Node w: graph.getAdjList(v)) {
            if (w.getAliveStatus() && !visited[w.getValue()]) {
                children++;
                articulationPointDetectionUtil(w, v, graph, visited, discovery, low, timer, articulationPoints);

                low[v.getValue()] = Math.min(low[v.getValue()], low[w.getValue()]);

                if (parentNode.getValue() != -1 && low[w.getValue()] >= discovery[v.getValue()]) {
                    articulationPoints.add(v);
                }
            } else if (v.getValue() != parentNode.getValue()) {
                low[v.getValue()] = Math.min(low[v.getValue()], discovery[w.getValue()]);
            }
        }

        if (parentNode.getValue() == -1 && children > 1) {
            articulationPoints.add(v);
        }
    }

    public static Set<Node> detectArticulationPoints(Graph<Node> graph) {
        MyInteger timer = new MyInteger(0);
        int[] discovery = new int[graph.getVertexCount()];
        int[] low = new int[graph.getVertexCount()];
        boolean[] visited = new boolean[graph.getVertexCount()];
        Arrays.fill(discovery, 0);
        Arrays.fill(visited, false);
        Set<Node> vertices = graph.getVertices();
        Set<Node> articulationPoints = new HashSet<>();
        Node parent = new GraphNode(-1);
        for (Node v: vertices) {
            if (graph.hasVertex(v) && v.getAliveStatus() && !visited[v.getValue()]) {
                articulationPointDetectionUtil(
                    v, 
                    parent,
                    graph,
                    visited,
                    discovery,
                    low,
                    timer,
                    articulationPoints
                );
            }
        }
        return articulationPoints;
    }    
}
