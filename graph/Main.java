package graph;

import static graph.GraphOps.countConnectedComponents;
import static graph.GraphOps.dfsIterative;

/**
 * Main class to test and debug the graph operations.
 */
public class Main {
    public static void main(String[] args) {
        Graph<GraphNode> myGraph = new Graph<>(true);

        GraphNode node0 = new GraphNode(0);
        GraphNode node1 = new GraphNode(1);
        GraphNode node2 = new GraphNode(2);
        GraphNode node3 = new GraphNode(3);
        GraphNode node4 = new GraphNode(4);
        GraphNode node5 = new GraphNode(5);
        GraphNode node6 = new GraphNode(6);
        GraphNode node7 = new GraphNode(7);

        myGraph.addEdge(node0, node1);
        myGraph.addEdge(node0, node4);
        myGraph.addEdge(node1, node2);
        myGraph.addEdge(node1, node3);
        myGraph.addEdge(node1, node4);
        myGraph.addEdge(node2, node3);
        // myGraph.addEdge(node3, node4);
        myGraph.addEdge(node5, node6);
        myGraph.addVertex(node7);
        int cc1 = countConnectedComponents(myGraph);
        System.out.println("number of connected components in the graph are: " + cc1);
        node1.setAliveStatus(false);
        int cc2 =  countConnectedComponents(myGraph);
        System.out.println("number of connected components in the graph are: " + cc2);
    }
}
