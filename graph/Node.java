package graph;

public interface Node {
    public int getValue();
    public String getLabel();
    public boolean getAliveStatus();
    public void setAliveStatus(boolean status);
    // Aux methods
    public int hashCode();
    public boolean equals(Object o);
    public String toString();
}
