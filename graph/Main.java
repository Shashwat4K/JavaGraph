package graph;

import graph.util.GraphCreator;

import java.util.Set;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import static graph.GraphOps.countConnectedComponents;
import static graph.GraphOps.dfsIterative;
import static graph.GraphOps.MyInteger;
import static graph.GraphOps.detectArticulationPoints;
import static graph.GraphOps.detectArticulationPoints_BruteForce;
/**
 * Main class to test and debug the graph operations.
 */
public class Main {
    public static void main(String[] args) {
        /*
        Graph<Node> myGraph1 = new Graph<>(true);

        List<GraphNode> allNodes = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            allNodes.add(new GraphNode(i));
        }

        myGraph1.addEdge(allNodes.get(0), allNodes.get(1));
        myGraph1.addEdge(allNodes.get(0), allNodes.get(2));
        myGraph1.addEdge(allNodes.get(1), allNodes.get(3));
        myGraph1.addEdge(allNodes.get(2), allNodes.get(4));
        myGraph1.addEdge(allNodes.get(3), allNodes.get(5));
        myGraph1.addEdge(allNodes.get(4), allNodes.get(5));
        myGraph1.addEdge(allNodes.get(5), allNodes.get(6));
        myGraph1.addEdge(allNodes.get(5), allNodes.get(7));
        myGraph1.addEdge(allNodes.get(5), allNodes.get(9));
        myGraph1.addEdge(allNodes.get(7), allNodes.get(8));
        myGraph1.addEdge(allNodes.get(8), allNodes.get(9));
        myGraph1.addEdge(allNodes.get(5), allNodes.get(10));
        myGraph1.addEdge(allNodes.get(10), allNodes.get(11));
        myGraph1.addEdge(allNodes.get(11), allNodes.get(13));
        myGraph1.addEdge(allNodes.get(10), allNodes.get(12));
        myGraph1.addEdge(allNodes.get(12), allNodes.get(14));
        // Experimental edges
        // myGraph1.addEdge(allNodes.get(13), allNodes.get(14));
        // myGraph1.addEdge(allNodes.get(3), allNodes.get(6));
        // myGraph1.addEdge(allNodes.get(6), allNodes.get(7));
        // myGraph1.addEdge(allNodes.get(9), allNodes.get(10));
        // myGraph1.addEdge(allNodes.get(4), allNodes.get(11));
        
        System.out.println("Graph:\n" + myGraph1.toString());
        int cc1 = countConnectedComponents(myGraph1);
        System.out.println("Number of CCs in the original graph are: " + cc1);
        Set<Node> articulationPoints = detectArticulationPoints(myGraph1);
        for(Node node: articulationPoints) { 
            System.out.print("Number of CCs after disconnecting AP " + node.getLabel() + " are: ");
            myGraph1.disableVertex(node);
            int cc = countConnectedComponents(myGraph1);
            System.out.println(cc);
            myGraph1.enableVertex(node);
        }
        System.out.println();
        */
        
        GraphCreator g = new GraphCreator();
        try {
            String fileName = "power-bcspwr09.mtx";
            Graph<Node> myGraph2 = g.createGraph("data/power-bcspwr09/" + fileName, true);
            // System.out.println(myGraph2);
            MyInteger efficientAlgorithmTotalTime = new MyInteger(0);
            MyInteger bruteForceTotalTime = new MyInteger(0);
            Set<Node> articulationPoints = detectArticulationPoints(myGraph2, false, efficientAlgorithmTotalTime);
            Set<Node> articulationPointsBruteForce = detectArticulationPoints_BruteForce(myGraph2, false, bruteForceTotalTime);
            for(Node node: articulationPoints) { 
                if (!articulationPointsBruteForce.contains(node)) {
                    System.out.println("Node " + node.getValue() + " is not in both sets!");
                    break;
                }
                System.out.print("Number of CCs after disconnecting AP " + node.getLabel() + " are: ");
                myGraph2.disableVertex(node);
                int cc = countConnectedComponents(myGraph2);
                System.out.println(cc);
                myGraph2.enableVertex(node);
            }
            try(
                FileWriter fr = new FileWriter("data/demo_data/running_time_data/" + fileName + ".txt");
                BufferedWriter br = new BufferedWriter(fr)
            ) {
                String line = String.valueOf(efficientAlgorithmTotalTime.integer) 
                              + " " 
                              + String.valueOf(bruteForceTotalTime.integer);
                br.write(line);
                br.newLine();
            } catch(IOException e) {
                System.err.println("Error happened at line 91");
                e.printStackTrace();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
