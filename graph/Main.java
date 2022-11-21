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
                            + String.valueOf(bruteForceTotalTime.integer);
            br.write(line);
            br.newLine();
        } catch(IOException e) {
            System.err.println("Error happened at line 91");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        GraphCreator g = new GraphCreator();
        try {
            String fileName = "power-bcspwr09.mtx";
            Graph<Node> myGraph2 = g.createGraph("data/power-bcspwr09/" + fileName, true);
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
            writeData(
                "/data/demo_data/running_time_data", 
                fileName, 
                efficientAlgorithmTotalTime, 
                bruteForceTotalTime
            );
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
// 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
