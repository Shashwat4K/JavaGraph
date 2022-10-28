package graph;

/**
 * A class modelling the vertices of a graph.
 */
public class GraphNode {
    
    private int value;
    private String label;
    private boolean isAlive;
    /**
     * Constructor: Constructs a `GraphNode` object with given value.
     * @param value value of the vertex
     */
    public GraphNode(int value) {
        this.value = value;
        this.label = String.valueOf(value);
        this.isAlive = true; // A node is alive by default.
    }

    /**
     * Get the value of the vertex.
     * @return value of the vertex.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Get the label of the vertex
     * @return label of the vertex (string format of `value`)
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Get the alive status of the node
     * @return `isAlive`
     */
    public boolean getAliveStatus() {
        return this.isAlive;
    }

    public void setAliveStatus(boolean aliveStatus) {
        this.isAlive = aliveStatus;
    }

    /**
     * Get the hash code of this node.
     * Standard implementation of hashCode() (https://www.baeldung.com/java-hashcode)
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (value ^ (value >>> 32));
        hash = 31 * hash + (label == null ? 0:label.hashCode());
        return hash;
    }

    /**
     * Returns the string representation of the graph node.
     */
    @Override
    public String toString() {
        return label;
    }

    /**
     * Compares two objects and returns true if their attributes match.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GraphNode graphNode = (GraphNode) obj;
        return this.label.equals(graphNode.label) && this.value == graphNode.value;
    }
}
