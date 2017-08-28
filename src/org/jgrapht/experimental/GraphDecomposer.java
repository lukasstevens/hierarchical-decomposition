package org.jgrapht.experimental;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.experimental.clustering.TreeVertex;
import org.jgrapht.experimental.clustering.TreeVertexType;
import org.jgrapht.experimental.decomposition.Decomposition;
import org.jgrapht.experimental.decomposition.DecompositionTree;
import org.jgrapht.experimental.decomposition.RSTSubTreeGeneratorFactoy;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class GraphDecomposer {

    public static void main(String[] args) throws IOException {

        GraphReader<Integer, DefaultWeightedEdge> graphReader =
            new GraphReader<>(new BufferedReader(new InputStreamReader(System.in)), 0);
        Graph<Integer, DefaultWeightedEdge> graph =
            new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        VertexFactory<Integer> vertexFactory = new VertexFactory<Integer>() {
            Integer node = 0;

            @Override
            public Integer createVertex() {
                return this.node++;
            }
        };
        graphReader.generateGraph(graph, vertexFactory, null);
        int nodeCount = graph.vertexSet().size();

        Decomposition<Integer, DefaultWeightedEdge> graphDecomposition =
            new Decomposition(graph, new RSTSubTreeGeneratorFactoy());
        DecompositionTree<Integer> decompositionTree =
            graphDecomposition.performDecomposition();
        DirectedGraph<TreeVertex<Integer>, DefaultWeightedEdge> decompositionTreeGraph =
            decompositionTree.getGraph();

        BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        outputWriter.write(decompositionTreeGraph.vertexSet().size() + " " +
            decompositionTreeGraph.edgeSet().size() + " 001\n");
        for (Integer node = 0; node < nodeCount; ++node) {
            outputWriter.write("\n");
        }

        Map<TreeVertex<Integer>, Integer> vertexLabels = new HashMap<>();
        Integer innerVertexLabel = nodeCount;
        for (TreeVertex<Integer> node : decompositionTreeGraph.vertexSet()) {
            if (node.getType() == TreeVertexType.LEAF) {
                vertexLabels.put(node, node.getVertex());
            } else {
                vertexLabels.put(node, innerVertexLabel);
                innerVertexLabel += 1;
            }
        }

        Queue<TreeVertex<Integer>> vertexQueue = new LinkedList<>();
        vertexQueue.add(decompositionTree.getRoot());
        while (!vertexQueue.isEmpty()) {
            TreeVertex<Integer> currTreeVertex = vertexQueue.remove();
            for (DefaultWeightedEdge e :
                decompositionTreeGraph.outgoingEdgesOf(currTreeVertex)) {
                TreeVertex<Integer> nextVertex = decompositionTreeGraph.getEdgeTarget(e);
                outputWriter.write(vertexLabels.get(nextVertex).toString());
                if (nextVertex.getType() == TreeVertexType.TREE_VERTEX) {
                    vertexQueue.add(nextVertex);
                }
                outputWriter.write(" " + decompositionTreeGraph.getEdgeWeight(e) + " ");
            }
            outputWriter.write("\n");
        }
        outputWriter.flush();
    }

}
