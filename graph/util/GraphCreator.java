package graph.util;

import graph.Graph;
import graph.GraphNode;
import graph.Node;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Create graphs from file
 */
public class GraphCreator {
    private BufferedReader reader;
    public GraphCreator() {
        this.reader = null;
    }

    public Graph<Node> createGraph(String filePath) {
        Graph<Node> graph = new Graph<>(true);
        try {
            // Set<Node> seenGraphNodes = new HashSet<>();
            this.reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while(line != null) {
                if (!line.startsWith("%")) {
                    String[] tokens = line.split("\\s+"); // Split the line around whitespace
                    GraphNode src = new GraphNode(Integer.parseInt(tokens[0])-1); // 1-indexed to 0-indexed
                    GraphNode dest = new GraphNode(Integer.parseInt(tokens[1])-1);
                    // if(!seenGraphNodes.contains(src)) { seenGraphNodes.add(src); }
                    // if(!seenGraphNodes.contains(dest)) { seenGraphNodes.add(dest); }
                    graph.addEdge(src, dest);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.err.println("GraphCreator.createGraph(): IOException occurred while reading the file");
            e.printStackTrace();
        } finally {
            try {
                if (this.reader != null) {
                    this.reader.close();
                }
            } catch (IOException e) {
                System.err.println("GraphCreator.createGraph(): IOException occured while closing the reader");
            }
        }
        return graph;
    }
}
