package graph;

public abstract class Node {
    protected int value;
    protected String label;
    protected boolean isAlive;
    
    public abstract int getValue();
    public abstract String getLabel();
    public abstract boolean getAliveStatus();
    public abstract void setAliveStatus(boolean status);
    // Aux methods
    public abstract int hashCode();
    public abstract boolean equals(Object o);
    public abstract String toString();
}
