package graph;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import static graph.GraphOps.countConnectedComponents;
import static graph.GraphOps.dfsIterative;
import static graph.GraphOps.bfsIterative;

import static graph.GraphOps.detectArticulationPoints;
/**
 * Main class to test and debug the graph operations.
 */
public class Main {
    public static void main(String[] args) {
        Graph<Node> myGraph = new Graph<>(true);

        List<GraphNode> allNodes = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            allNodes.add(new GraphNode(i));
        }

        myGraph.addEdge(allNodes.get(0), allNodes.get(1));
        myGraph.addEdge(allNodes.get(0), allNodes.get(2));
        myGraph.addEdge(allNodes.get(1), allNodes.get(3));
        myGraph.addEdge(allNodes.get(2), allNodes.get(4));
        myGraph.addEdge(allNodes.get(3), allNodes.get(5));
        myGraph.addEdge(allNodes.get(4), allNodes.get(5));
        myGraph.addEdge(allNodes.get(5), allNodes.get(6));
        myGraph.addEdge(allNodes.get(5), allNodes.get(7));
        myGraph.addEdge(allNodes.get(5), allNodes.get(9));
        myGraph.addEdge(allNodes.get(7), allNodes.get(8));
        myGraph.addEdge(allNodes.get(8), allNodes.get(9));
        myGraph.addEdge(allNodes.get(5), allNodes.get(10));
        myGraph.addEdge(allNodes.get(10), allNodes.get(11));
        myGraph.addEdge(allNodes.get(11), allNodes.get(13));
        myGraph.addEdge(allNodes.get(10), allNodes.get(12));
        myGraph.addEdge(allNodes.get(12), allNodes.get(14));
        // Experimental edges
        // myGraph.addEdge(allNodes.get(13), allNodes.get(14));
        // myGraph.addEdge(allNodes.get(3), allNodes.get(6));
        // myGraph.addEdge(allNodes.get(6), allNodes.get(7));
        // myGraph.addEdge(allNodes.get(9), allNodes.get(10));
        // myGraph.addEdge(allNodes.get(4), allNodes.get(11));
        System.out.println("Graph:\n" + myGraph.toString());
        int cc1 = countConnectedComponents(myGraph);
        System.out.println("Number of CCs in the original graph are: " + cc1);
        Set<Node> apList = detectArticulationPoints(myGraph);
        // System.out.print("Articulation points: ");
        for(Node node: apList) {
            System.out.print("Number of CCs after disconnecting AP " + node.getLabel() + " are: ");
            node.setAliveStatus(false);
            int cc = countConnectedComponents(myGraph);
            System.out.println(cc);
            node.setAliveStatus(true);
        }
        System.out.println();
    }
}
