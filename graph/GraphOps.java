package graph;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
/**
 * A class containing basic graph operations.
 */
public class GraphOps {
        
    private static final int STEP_INTERVAL = 10;
    private static int stepCount = 0;
    private static long totalTime = 0;
    private static long startTime = 0;
    private static long endTime = 0;
    /**
     * Private constructor to hide the default one.
     */
    private GraphOps() {}

    /**
     * A new integer class to pass the objects as reference in methods.
     */
    public static class MyInteger {
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

    /**
     * Save the data for demo purposes.
     * @param graph The gaph object
     * @param visited Boolean array indicating visited vertices
     * @param articulationPoints Set of articulation point vertices
     * @param step Step at which the data is being saved.
     */
    private static void saveTheData(Graph<Node> graph, boolean[] visited, Set<Node> articulationPoints, String step) {
        try (
            FileWriter fw = new FileWriter("data/demo_data/demo_step_" + step + ".txt");
            BufferedWriter bw = new BufferedWriter(fw)
        ) {
            for(Node vertex: graph.getVertices()) {
                String line = "";
                line = line + String.valueOf(vertex.getValue()) + " ";
                if(visited[vertex.getValue()]) {
                    line = line + "1" + " ";
                } else {
                    line = line + "0" + " ";
                }
                if (articulationPoints.contains(vertex)) {
                    line = line + "1" + " ";
                } else {
                    line = line + "0" + " ";
                }
                bw.write(line);
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pad the step count with zeros
     * @param step The step number
     * @param len The final length of padded string
     * @return The zero padded (prepended) string.
     */
    private static String stepPadded(String step, int len) {
        int zeroPadLen = len - step.length();
        StringBuilder pad = new StringBuilder();
        for(int i = 0; i < zeroPadLen; i++) {
            pad.append("0");
        }
        return pad.toString() + step;
    }

    /**
     * Depth first search auxilliary function for detecting articulation points
     * @param v Reference to the current `GraphNode`
     * @param parentNode Reference to the parent `GraphNode`
     * @param graph The `Graph` object
     * @param visited Boolean array showing which vertices are visited
     * @param discovery integer array storing the depth (or discovery time) of a vertex in the DFS tree.
     * @param low integer array storing the low-point values of each vertex
     * @param timer Integer that helps in calculating the depth of each vertex in the DFS tree
     * @param articulationPoints a container to store the references of detected of Articulation points.
     * @param saveData a boolean flag indicating whether to store data ot not (pass `true` to save the data).
     */
    private static void articulationPointDetectionUtil (
        Node v,
        Node parentNode,
        Graph<Node> graph,
        boolean[] visited,
        int[] discovery,
        int[] low,
        MyInteger timer,
        Set<Node> articulationPoints,
        boolean saveData
    ) {
        int children = 0;
        visited[v.getValue()] = true;
        timer.integer = timer.integer + 1;
        discovery[v.getValue()] = low[v.getValue()] = timer.integer;

        for (Node w: graph.getAdjList(v)) {
            stepCount += 1;
            if(saveData && stepCount % STEP_INTERVAL == 0) {
                // Save the data on device
                endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
                saveTheData(graph, visited, articulationPoints, stepPadded(String.valueOf(stepCount), 5));
                // System.out.println("Saving the data on device! stepCount = " + stepCount);
                startTime = System.currentTimeMillis();
            }
            if (w.getAliveStatus() && !visited[w.getValue()]) {
                children++;
                articulationPointDetectionUtil(w, v, graph, visited, discovery, low, timer, articulationPoints, saveData);

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

    /**
     * Detect all articulation points in the graph
     * @param graph Reference to the `Graph` object.
     * @param saveData a boolean flag indicating whether to store data ot not (pass `true` to save the data).
     * @param time Integer that helps in calculating the depth of each vertex in the DFS tree
     * @return set of all the detected Articulation points.
     */
    public static Set<Node> detectArticulationPoints(
        Graph<Node> graph, 
        boolean saveData,
        MyInteger time
    ) {
        // Initialize step count
        stepCount = 0;
        MyInteger timer = new MyInteger(0);
        int[] discovery = new int[graph.getVertexCount()];
        int[] low = new int[graph.getVertexCount()];
        boolean[] visited = new boolean[graph.getVertexCount()];
        Arrays.fill(discovery, 0);
        Arrays.fill(visited, false);
        Set<Node> vertices = graph.getVertices();
        Set<Node> articulationPoints = new HashSet<>();
        Node parent = new GraphNode(-1);
        // Start time
        startTime = System.currentTimeMillis();
        for (Node v: vertices) {
            stepCount += 1;
            if(saveData && stepCount % STEP_INTERVAL == 0) {
                // End time
                endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
                // Save the data on device
                saveTheData(graph, visited, articulationPoints, stepPadded(String.valueOf(stepCount), 5));
                startTime = System.currentTimeMillis();
            }
            if (graph.hasVertex(v) && v.getAliveStatus() && !visited[v.getValue()]) {
                articulationPointDetectionUtil(
                    v, 
                    parent,
                    graph,
                    visited,
                    discovery,
                    low,
                    timer,
                    articulationPoints,
                    saveData
                );
            }
        }
        endTime = System.currentTimeMillis();
        totalTime += (endTime - startTime);
        time.integer = (int) (totalTime);
        System.out.println("Total time taken: " + totalTime + " ms");
        // Save the final data
        if (saveData) {
            saveTheData(graph, visited, articulationPoints, "final");
        }
        return articulationPoints;
    }    

    /**
     * Detect all the articulation points in a graph using brute force.
     * @param graph Reference to the `Graph` object.
     * @param saveData a boolean flag indicating whether to store data ot not (pass `true` to save the data).
     * @param time Integer that helps in calculating the depth of each vertex in the DFS tree
     * @return set of all the detected Articulation points.
     */
    public static Set<Node> detectArticulationPoints_BruteForce(
        Graph<Node> graph,
        boolean saveData,
        MyInteger time
    ) {
        int ccBefore = 0;
        int ccAfter = 0;
        startTime = System.currentTimeMillis();
        Set<Node> articulationPoints = new HashSet<>();
        ccBefore = countConnectedComponents(graph);
        for(Node v: graph.getVertices()) {
            graph.disableVertex(v);
            ccAfter = countConnectedComponents(graph);
            if (ccAfter != ccBefore) {
                articulationPoints.add(v);
            }
            graph.enableVertex(v);
        }
        endTime = System.currentTimeMillis();
        time.integer = (int) (endTime - startTime);
        System.out.println("Time taken by brute force: " + (endTime-startTime) + " ms");
        return articulationPoints;
    }
}
