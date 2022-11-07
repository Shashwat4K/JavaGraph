package graph;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
/**
 * A class containing basic graph operations.
 */
public class GraphOps {

    /**
     * Private constructor to hide the default one.
     */
    private GraphOps() {}
    private static final int NO_PARENT = -1;
    private static int time = 0;
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
        Node currentVertex,
        Graph<Node> graph,
        boolean[] visited,
        int[] discovery,
        int[] minimum,
        int[] parentNode,
        boolean[] isAP
    ) {
        // int children = 0;
        // visited[v.getValue()] = true;
        // timer.integer = timer.integer + 1;
        // discovery[v.getValue()] = low[v.getValue()] = timer.integer;

        // for (Node w: graph.getAdjList(v)) {
        //     if (w.getAliveStatus() && !visited[w.getValue()]) {
        //         children++;
        //         articulationPointDetectionUtil(w, v, graph, visited, discovery, low, timer, isAP);

        //         low[v.getValue()] = Math.min(low[v.getValue()], low[w.getValue()]);

        //         if (parent.getValue() != -1 && low[w.getValue()] >= discovery[v.getValue()]) {
        //             isAP[v.getValue()] = true;
        //         }
        //     } else if (v.getValue() != parent.getValue()) {
        //         low[v.getValue()] = Math.min(low[v.getValue()], discovery[w.getValue()]);
        //     }
        // }

        // if (parent.getValue() == -1 && children > 1) {
        //     isAP[v.getValue()] = true;
        // }
        int children = 0; 
        visited[currentVertex.getValue()] = true; 
        discovery[currentVertex.getValue()] = minimum[currentVertex.getValue()] = ++time;
        for(Node adj: graph.getAdjList(currentVertex)) {
            if(adj.getAliveStatus() && !visited[adj.getValue()]) {
                children++;
                parentNode[adj.getValue()] = currentVertex.getValue();
                articulationPointDetectionUtil(adj, graph, visited, discovery, minimum, parentNode, isAP);

                minimum[currentVertex.getValue()] = Math.min(minimum[currentVertex.getValue()], minimum[adj.getValue()]);

                if (parentNode[currentVertex.getValue()] == NO_PARENT && children > 1) {
                    isAP[currentVertex.getValue()] = true; 
                }
  
                // if currentVertex.getValue() is not root and minimum value of one of its adj_vertex is more than discovery value of currentVertex.getValue(). 
                if (parentNode[currentVertex.getValue()] != NO_PARENT && minimum[adj.getValue()] >= discovery[currentVertex.getValue()]) {
                    isAP[currentVertex.getValue()] = true; 
                }
            } else if (adj.getValue() != parentNode[currentVertex.getValue()]) {
                minimum[currentVertex.getValue()]  = Math.min(minimum[currentVertex.getValue()], discovery[adj.getValue()]);
            }
        } 

    }

    public static boolean[] detectArticulationPoints(Graph<Node> graph) {
        // MyInteger timer = new MyInteger(0);
        // Integer[] discovery = new Integer[graph.getVertexCount()];
        // Integer[] low = new Integer[graph.getVertexCount()];
        // boolean[] visited = new boolean[graph.getVertexCount()];
        // boolean[] isAP = new boolean[graph.getVertexCount()];
        // Arrays.fill(discovery, 0);
        // Arrays.fill(visited, false);
        // Set<Node> vertices = graph.getVertices();
        // Set<Node> articulationPoints = new HashSet<>();

        // for (Node v: vertices) {
        //     if (v.getAliveStatus() && !visited[v.getValue()]) {
        //         articulationPointDetectionUtil(
        //             v, 
        //             new GraphNode(-1),
        //             graph,
        //             visited,
        //             discovery,
        //             low,
        //             timer,
        //             isAP
        //         );
        //     }
        // }
        // return isAP;
        int vertexCount = graph.getVertexCount();
        boolean[] visited = new boolean[vertexCount]; 
        int[] discovery = new int[vertexCount]; // array for discovery time of each vertex. 
        int[] minimum = new int[vertexCount]; // array for minimum time of each node. 
        int[] parentNode = new int[vertexCount]; // array for storing parent of each vertex.
        boolean[] isAP = new boolean[vertexCount]; // To store articulation points.

        for(Node v: graph.getVertices()) {
            parentNode[v.getValue()] = NO_PARENT;
            visited[v.getValue()] = false;
            isAP[v.getValue()] = false;
        }
  
        // Call the recursive helper function to find articulation points in graph for every vertex iteratively. 
        for (Node v: graph.getVertices()){
            if (visited[v.getValue()] == false) { 
                articulationPointDetectionUtil(v, graph, visited, discovery, minimum, parentNode, isAP);
            }
        }

        return isAP;
    }    
}
