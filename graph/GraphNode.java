package graph;

public class GraphNode {
    
    private int value;
    private String label;

    public GraphNode(int value) {
        this.value = value;
        this.label = String.valueOf(value);
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
