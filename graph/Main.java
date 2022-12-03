package graph;

import graph.util.GraphCreator;

import java.util.Set;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import static graph.GraphOps.countConnectedComponents;
import static graph.GraphOps.MyInteger;
import static graph.GraphOps.detectArticulationPoints;
import static graph.GraphOps.detectArticulationPoints_BruteForce;
/**
 * Main class to test and debug the graph operations.
 */
public class Main {
    private static void writeData(String filePath, String fileName, MyInteger efficientAlgorithmTotalTime, MyInteger bruteForceTotalTime) {
        try(
            FileWriter fr = new FileWriter(filePath + fileName + ".txt");
            BufferedWriter br = new BufferedWriter(fr)
        ) {
            String line = String.valueOf(efficientAlgorithmTotalTime.integer) 
                            + " " 
                            + bruteForceTotalTime.integer;
            br.write(line);
            br.newLine();
        } catch(IOException e) {
            System.err.println("Error happened at line 28");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        GraphCreator g = new GraphCreator();
        try {
            String fileName = "power-bcspwr09.mtx";
            Graph<Node> myGraph = g.createGraph("data/power-bcspwr09/" + fileName, true);
            System.out.println(fileName + ": V = " + myGraph.getVertexCount() + " E = " + myGraph.getEdgesCount());
            MyInteger efficientAlgorithmTotalTime = new MyInteger(0);
            MyInteger bruteForceTotalTime = new MyInteger(0);
            Set<Node> articulationPoints = detectArticulationPoints(myGraph, false, efficientAlgorithmTotalTime);
            Set<Node> articulationPointsBruteForce = detectArticulationPoints_BruteForce(myGraph, false, bruteForceTotalTime);
            // Set<Node> apUSGraph = detectArticulationPoints(usGraph, false, new MyInteger(0));
            for(Node node: articulationPoints) { 
                if (!articulationPointsBruteForce.contains(node)) {
                    System.out.println("Node " + node.getValue() + " is not in both sets!");
                    break;
                }
                System.out.print("Number of CCs after disconnecting AP " + node.getLabel() + " are: ");
                myGraph.disableVertex(node);
                int cc = countConnectedComponents(myGraph);
                System.out.println(cc);
                myGraph.enableVertex(node);
            }
            writeData(
                "data/demo_data/running_time_data/", // TODO: Change the path to an appropriate location in your device.
                fileName, 
                efficientAlgorithmTotalTime, 
                bruteForceTotalTime
            );
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
