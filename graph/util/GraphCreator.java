package graph.util;

import graph.Graph;
import graph.GraphNode;
import graph.Node;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Create graphs from file
 */
public class GraphCreator {
    private BufferedReader reader;
    public GraphCreator() {
        this.reader = null;
    }

    /**
     * Creates a graph from a file
     * @param filePath absolute (or relative to this directory) path (including the name of the file).
     * @param isUndirected Whether the graph is directed or not. `true` if undirected.
     * @throws IOException when an error occurs while opening the file, reading the file, or closing the reader object.
     * @return graph with adjacency list representation.
     */
    public Graph<Node> createGraph(String filePath, boolean isUndirected) throws IOException {
        Graph<Node> graph = new Graph<>(isUndirected);
        FileReader fr = new FileReader(filePath);
        this.reader = new BufferedReader(fr);
        String line = reader.readLine();
        while(line != null) {
            if (!line.startsWith("%")) {
                String[] tokens = line.split("\\s+"); // Split the line around whitespace
                GraphNode src = new GraphNode(Integer.parseInt(tokens[0])-1); // 1-indexed to 0-indexed
                GraphNode dest = new GraphNode(Integer.parseInt(tokens[1])-1);
                // Filtering self-edges
                if(!src.equals(dest)) { 
                    graph.addEdge(src, dest);
                }
            }
            line = reader.readLine();
        }
        this.reader.close();
        return graph;
    }

    /**
     * Creates a graph from a file. (US cities demo version)
     * @param filePath absolute (or relative to this directory) path (including the name of the file).
     * @param isUndirected Whether the graph is directed or not. `true` if undirected.
     * @return graph with adjacency list representation.
     * @throws IOException when an error occurs while opening the file, reading the file, or closing the reader object.
     */
    public Graph<Node> createGraphForDemo(String filePath, boolean isUndirected) throws IOException {
        Graph<Node> graph = new Graph<>(isUndirected);
        FileReader fr = new FileReader(filePath);
        this.reader = new BufferedReader(fr);
        String line = reader.readLine();
        while(line != null) {
            try {
                if (!line.startsWith("%")) {
                    String[] tokens = line.split("#"); // Split the line around whitespace
                    GraphNode src = new GraphNode(Integer.parseInt(tokens[0]), tokens[2]); 
                    GraphNode dest = new GraphNode(Integer.parseInt(tokens[1]), tokens[3]);
                    // Filtering self-edges
                    if(!src.equals(dest)) { 
                        graph.addEdge(src, dest);
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.err.println("There are less/more tokens in a line! 4 needed.");
            }
            line = reader.readLine();
        }
        this.reader.close();
        return graph;
    }
}
