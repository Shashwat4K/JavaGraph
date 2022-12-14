package graph;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
// import java.lang.reflect.*;

/**
 * A generic Graph data structure.
 */
public class Graph<T extends Node> {

    private Map<T, List<T>> map;

    private Set<GraphEdge<T>> edgeSet;

    private boolean isUndirected;

    private T source;

    /**
     * Constructor
     * @param isUndirected Whether the graph is undirected or not.
     */
    public Graph (boolean isUndirected) {
        this.map = new HashMap<>();
        this.edgeSet = new HashSet<>();
        this.isUndirected = isUndirected;
        this.source = null;
    }

    /**
     * Adds vertex in the graph
     * @param s Vertex
     */
    public void addVertex(T s) {
        map.putIfAbsent(s, new ArrayList<>());
        if (source == null) {
            source = s;
        }
    }
 
    /**
     * Adds edge between `source` and `destination` vertices
     * @param source Source Vertex
     * @param destination Destination vertex.
     */
    public void addEdge(T source, T destination) {
        if (!map.containsKey(source)) {
            addVertex(source);
        }
 
        if (!map.containsKey(destination)) {
            addVertex(destination);
        }
 
        map.get(source).add(destination);
        if (isUndirected) {
            map.get(destination).add(source);
        }
        GraphEdge<T> edge = new GraphEdge<>(source, destination, isUndirected);
        edgeSet.add(edge);
    }

    /**
     * Disables a vertex from the graph as if it is not present in the graph.
     * @param v reference to the vertex object
     * @throws NullPointerException when the given vertex reference is not found in the graph.
     */
    public void disableVertex(T v) throws NullPointerException {
        List<T> adjList = this.map.get(v);
        // Disable the vertex
        v.setAliveStatus(false);
        // Disable the vertex in everyone of its neighbors
        for(T neighbor: adjList) {
            for(T w: this.map.get(neighbor)) {
                if (w.getValue() == v.getValue()) {
                    w.setAliveStatus(false);
                }
            }
        }
    }
    
    /**
     * Enables a vertex in the graph if it is disabled
     * @param v reference to the vertex object
     * @throws NullPointerException when the given vertex reference is not found in the graph.
     */
    public void enableVertex(T v) throws NullPointerException {
        List<T> adjList = this.map.get(v);
        // Enable the vertex
        v.setAliveStatus(true);
        // Enable the vertex in everyone of its neighbors
        for(T neighbor: adjList) {
            for(T w: this.map.get(neighbor)) {
                if(w.getValue() == v.getValue()) {
                    w.setAliveStatus(true);
                }
            }
        }
    }

    /**
     * Remove the edge between the vertices `src` and `dest` (if it exists)
     * @param src Source vertex
     * @param dest Destination vertex
     */
    public void removeEdge(T src, T dest) {
        // TODO: implement this method
    }

    /**
     * Get the number of vertices in the graph (i.e., |V|)
     * @return Number of vertices in the graph
     */
    public int getVertexCount() {
        int count = 0;
        for(T v: this.map.keySet()) {
            if (v.getAliveStatus()) {
                count++;
            }
        }
        return count;        
    }
 
    /**
     * Get the number of edges in the graph
     * @return Number of edges in the graph
     */
    public int getEdgesCount() {
        int count = 0;
        Iterator<Map.Entry<T, List<T>>> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<T, List<T>> entry = itr.next();
            T v = entry.getKey();
            if(v.getAliveStatus()) {
                for(T w: entry.getValue()) {
                    if(w.getAliveStatus()) {
                        count += 1;
                    }
                }
            }
        }
        if (isUndirected) {
            count = count / 2;
        }
        return count;
    }
 
    /**
     * Get the source vertex
     * @return reference to the source vertex object
     */
    public T getSource() {
        return this.source;
    }

    /**
     * Sets the source vertex for traversals
     * @param src Reference to the source vertex object.
     */
    public void setSource(T src) {
        this.source = src;
    }

    /**
     * Get the adjacency list of the provided vertex reference
     * @param v Reference to the vertex
     * @return Adjacency list of the provided vertex.
     */
    public List<T> getAdjList(T v) {
        return map.get(v);
    }

    /**
     * Get the collection of vertices in the graph
     * @return set of vertices in the graph
     */
    public Set<T> getVertices() {
        return map.keySet();
    }

    /**
     * Check if the given vertex is in the graph.
     * @param s Vertex to find in the graph
     * @return True if `s` is in the graph, else False
     */
    public boolean hasVertex(T s) {
        return map.containsKey(s);
    }
 
    /**
     * Check if there's an edge between the vertices `s` and `d`.
     * @param s Source vertex
     * @param d Destination vertex.
     * @return True if there's an edge between `s` and `d`. False, otherwise.
     */
    public boolean hasEdge(T s, T d) {
        return map.get(s).contains(d); 
    }
 
    /**
     * Convert the graph into its equivalent string format.
     * @return the string format of the graph.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        map.forEach((vertex, adjList) -> {
            if(vertex.getAliveStatus()) {
                builder.append(vertex.toString() + ": ");
                for (T w : adjList) {
                    if(w.getAliveStatus()) {
                        builder.append(w.toString() + " ");
                    }
                }
                builder.append("\n");
            }
        });
        return (builder.toString());
    }
}
