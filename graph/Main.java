package graph;

import graph.Graph;
import graph.GraphNode;

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

        myGraph.addEdge(node0, node1);
        myGraph.addEdge(node0, node4);
        myGraph.addEdge(node1, node2);
        myGraph.addEdge(node1, node3);
        myGraph.addEdge(node1, node4);
        myGraph.addEdge(node2, node3);
        myGraph.addEdge(node3, node4);
        myGraph.addEdge(node5, node6);
        
        System.out.println(myGraph.toString());
    }
}
