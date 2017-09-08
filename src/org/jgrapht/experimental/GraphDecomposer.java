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
import java.util.List;
import java.util.Map;

public class GraphDecomposer {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer =
            new BufferedWriter(new OutputStreamWriter(System.out));
        if (args.length == 1) {
            int inputCount = Integer.parseInt(args[0]);
            for (int inputIdx = 0; inputIdx < inputCount; ++inputIdx) {
                readGraphOutputTree(reader, writer);
            }
        } else {
            readGraphOutputTree(reader, writer);
        }
    }

    private static void readGraphOutputTree(BufferedReader reader, BufferedWriter writer)
        throws IOException {
        GraphReader<Integer, DefaultWeightedEdge> graphReader =
            new GraphReader<>(reader);
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
        int graphNodeCount = graph.vertexSet().size();

        Decomposition<Integer, DefaultWeightedEdge> graphDecomposition =
            new Decomposition(graph, new RSTSubTreeGeneratorFactoy());
        DecompositionTree<Integer> decompositionTree =
            graphDecomposition.performDecomposition();
        DirectedGraph<TreeVertex<Integer>, DefaultWeightedEdge> decompositionTreeGraph =
            decompositionTree.getGraph();


        outputTree(graphNodeCount, graphReader.getNodeWeights(),
            decompositionTreeGraph, writer);
    }

    private static void outputTree(
        int graphNodeCount,
        int[] graphNodeWeights,
        DirectedGraph<TreeVertex<Integer>, DefaultWeightedEdge> decompositionTreeGraph,
        BufferedWriter writer
    ) throws IOException {

        Map<Integer, TreeVertex<Integer>> labelToVertex = new HashMap<>();
        Map<TreeVertex<Integer>, Integer> vertexToLabel = new HashMap<>();
        Integer innerVertexLabel = graphNodeCount;
        List<TreeVertex<Integer>> verticesToRemove = new LinkedList<>();
        for (TreeVertex<Integer> node : decompositionTreeGraph.vertexSet()) {
            if (node.getType() == TreeVertexType.LEAF) {
                if (labelToVertex.containsKey(node.getVertex())) {
                    verticesToRemove.add(node);
                } else {
                    labelToVertex.put(node.getVertex(), node);
                    vertexToLabel.put(node, node.getVertex());
                }
            } else {
                labelToVertex.put(innerVertexLabel, node);
                vertexToLabel.put(node, innerVertexLabel);
                innerVertexLabel += 1;
            }
        }

        for (TreeVertex<Integer> vertex : verticesToRemove) {
            decompositionTreeGraph.removeVertex(vertex);
        }

        writer.write(decompositionTreeGraph.vertexSet().size() + " " +
            decompositionTreeGraph.edgeSet().size() + " 011\n");


        for (Integer node = 0; node < innerVertexLabel; ++node){
            TreeVertex<Integer> currTreeVertex = labelToVertex.get(node);
            if (currTreeVertex.getType() == TreeVertexType.LEAF) {
                writer.write("" + graphNodeWeights[node]);
            } else {
                // All inner nodes have weight 0
                writer.write("" + 0);
            }

            for (DefaultWeightedEdge e :
                decompositionTreeGraph.outgoingEdgesOf(currTreeVertex)) {
                TreeVertex<Integer> nextVertex = decompositionTreeGraph.getEdgeTarget(e);
                writer.write(" " + vertexToLabel.get(nextVertex));

                if (decompositionTreeGraph.getEdgeWeight(e) == Double.POSITIVE_INFINITY) {
                    writer.write(" " + Integer.MAX_VALUE);
                } else {
                    writer.write(" " +
                        (int)Math.round(decompositionTreeGraph.getEdgeWeight(e)));
                }
            }
            writer.write("\n");
        }
        writer.write("\n");
        writer.flush();
    }

}
