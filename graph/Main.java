package graph;

import graph.util.GraphCreator;

import java.util.Set;
import java.util.Scanner;
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
    /**
     * User menu
     */
    private static void menu() {
        System.out.println("1) power-494-bus");
        System.out.println("2) power-662-bus");
        System.out.println("3) power-685-bus");
        System.out.println("4) power-1138-bus");
        System.out.println("5) power-bcspwr09");
        System.out.println("0) Exit");
    }

    /**
     * Write the run time data on files.
     * Writes a single line with two columns: efficient time <space> brute-force time
     * @param filePath File Path
     * @param fileName Name of the file
     * @param efficientAlgorithmTotalTime time taken by efficient algorithm
     * @param bruteForceTotalTime time taken by brute-force algorithm
     */
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
    
    /**
     * Main method
     * @param args command line args
     */
    public static void main(String[] args) {
        GraphCreator g = new GraphCreator();
        Scanner sc = new Scanner(System.in);
        try {
            menu();
            String graphFile = "";
            int userInput = sc.nextInt();
            switch (userInput) {
                case 1:
                    graphFile = "power-494-bus";
                    break;
                case 2:
                    graphFile = "power-662-bus";
                    break;
                case 3:
                    graphFile = "power-685-bus";
                    break;
                case 4:
                    graphFile = "power-1138-bus";
                    break;
                case 5:
                    graphFile = "power-bcspwr09";
                    break;
                case 0:
                default:
                    System.out.println("Exitting...");
                    break;
            }
            if (userInput > 0 && userInput <= 5) {
                String fileName = graphFile + ".mtx";
                Graph<Node> myGraph = g.createGraph("src/data/" + graphFile + "/" + fileName, true);
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
                    "src/data/demo_data/running_time_data/", // TODO: Change the path to an appropriate location in your device.
                    fileName, 
                    efficientAlgorithmTotalTime, 
                    bruteForceTotalTime
                );
            }
            sc.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
