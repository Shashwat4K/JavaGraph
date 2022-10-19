package graph;

import graph.GraphNode;

public class GraphEdge<T> {
    private T src;
    private T dest;
    private boolean isUndirected;
    private double weight;

    /**
     * Constructor (default weight: 0 units)
     * @param src Source vertex
     * @param dest Destination vertex
     * @param isUndirected whether the edge is directed or not.
     */
    public GraphEdge(T src, T dest, boolean isUndirected) {
        this.src = src;
        this.dest = dest;
        this.isUndirected = isUndirected; 
        this.weight = 0.0;   
    }

    /**
     * Sets weight of the edge
     * @param weight weight value (double)
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Gets weight of this edge
     * @return weight value (double)
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Gets the hashcode of this GraphEdge object.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (src.hashCode());
        hash = 47 * hash + (dest.hashCode());
        hash = hash + (int) weight;
        return hash;
    }

    /**
     * Checks equality of two edges.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GraphEdge graphEdge = (GraphEdge) o;
        return this.src.equals(graphEdge.src) 
            && this.dest.equals(graphEdge.dest) 
            && this.isUndirected == graphEdge.isUndirected;
    }

    /**
     * Gets string representation of this edge.
     */
    @Override
    public String toString() {
        return "(" + this.src.toString() +") "
            + "--" + (this.weight == 0.0 ? "": String.valueOf(this.weight)) + (isUndirected ? "--":"->") 
            + " (" + this.dest.toString() + ")";
    }

    public static void main(String[] args) {
        GraphNode node0 = new GraphNode(0);
        GraphNode node1 = new GraphNode(1);
        GraphEdge<GraphNode> edge = new GraphEdge<>(node0, node1, true);
        System.out.println(edge);
    }
}
