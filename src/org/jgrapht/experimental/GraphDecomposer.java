package org.jgrapht.experimental;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.experimental.clustering.TreeVertex;
import org.jgrapht.experimental.clustering.TreeVertexType;
import org.jgrapht.experimental.decomposition.Decomposition;
import org.jgrapht.experimental.decomposition.DecompositionTree;
import org.jgrapht.experimental.decomposition.RSTSubTreeGeneratorFactoy;
import org.jgrapht.graph.ClassBasedVertexFactory;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Queue;

public class GraphDecomposer {
	
	public static String decomposeGraph(String graphString) {
		try {
			GraphReader<Integer, DefaultWeightedEdge> graphReader =
					new GraphReader<>(new StringReader(graphString));
			Graph<Integer, DefaultWeightedEdge> graph =
				new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
			VertexFactory<Integer> vertexFactory =
				new ClassBasedVertexFactory<>(Integer.class);
			graphReader.generateGraph(graph, vertexFactory, null);
			int nodeCount = graph.vertexSet().size();

			Decomposition<Integer, DefaultWeightedEdge> graphDecomposition =
				new Decomposition(graph, new RSTSubTreeGeneratorFactoy());
			DecompositionTree<Integer> decompositionTree =
				graphDecomposition.performDecomposition();
			DirectedGraph<TreeVertex<Integer>,DefaultWeightedEdge> decompositionTreeGraph =
				decompositionTree.getGraph();

			BufferedWriter stringWriter = new BufferedWriter(new StringWriter());
			stringWriter.write(nodeCount + " " +
				decompositionTreeGraph.edgeSet().size() + " 001\n");
			for (Integer node = 0; node < nodeCount; ++node){
				if (decompositionTree.getLeaf(node) == null) {
					return null;
				}
				stringWriter.write("\n");
			}

			Queue<TreeVertex<Integer>> vertexQueue = new LinkedList<>();
			Queue<Integer> vertexLabelQueue = new LinkedList<>();
			vertexQueue.add(decompositionTree.getRoot());
			vertexLabelQueue.add(nodeCount);

			while(!vertexQueue.isEmpty()) {
				TreeVertex<Integer> currTreeVertex = vertexQueue.remove();
				Integer nextVertexLabel = vertexLabelQueue.remove() + 1;
				for (DefaultWeightedEdge e :
					decompositionTreeGraph.outgoingEdgesOf(currTreeVertex)) {
					TreeVertex<Integer> nextVertex = decompositionTreeGraph.getEdgeTarget(e);
					if (nextVertex.getType() == TreeVertexType.LEAF) {
						stringWriter.write(nextVertex.getVertex());
					} else {
						stringWriter.write(nextVertexLabel);
						vertexQueue.add(nextVertex);
						vertexLabelQueue.add(nextVertexLabel);
						nextVertexLabel += 1;
					}
					stringWriter.write(" " + decompositionTreeGraph.getEdgeWeight(e) + " ");
				}
				stringWriter.write("\n");
			}
			stringWriter.flush();
			return stringWriter.toString();
		} catch (IOException e) {
			return null;			
		}
	}

}
