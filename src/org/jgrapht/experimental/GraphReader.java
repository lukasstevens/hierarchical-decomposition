/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -------------------
 * GraphReader.java
 * -------------------
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: GraphReader.java 725 2010-11-26 01:24:28Z perfecthash $
 *
 * Changes
 * -------
 * 16-Sep-2003 : Initial revision (BN);
 *
 */
package org.jgrapht.experimental;

import java.io.*;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.generate.*;


public class GraphReader<V, E>
    implements GraphGenerator<V, E, V>
{
    private BufferedReader in;

    private int[] nodeWeights;

    GraphReader(BufferedReader in) {
        this.in = in;
    }

    /**
     * {@inheritDoc}
     */
    public void generateGraph(
        Graph<V, E> target,
        VertexFactory<V> vertexFactory,
        Map<String, V> resultMap)
    {
        if (resultMap == null) {
            resultMap = new HashMap<>();
        }

        try {
            String line = in.readLine();
            while (line.isEmpty()) {
                line = in.readLine();
            }
            String[] lineSplit = line.split(" ");
            int nodeCount = Integer.parseInt(lineSplit[0]);
            int edgeCount = Integer.parseInt(lineSplit[1]);
            boolean[] fmt = new boolean[3];
            if (lineSplit.length >= 3) {
                String fmtString = lineSplit[2];
                for (int i = fmtString.length() - 1; i >= 0; --i) {
                    if (fmtString.charAt(i) == '1') {
                        fmt[i] = true;
                    }
                }
            }

            boolean hasNodeWeights = fmt[1];
            boolean hasEdgeWeights = fmt[2];
            nodeWeights = new int[nodeCount];
            Arrays.fill(nodeWeights, 1);

            for (int node = 0; node < nodeCount; ++node) {
                V vertex = vertexFactory.createVertex();
                target.addVertex(vertex);
                resultMap.put("" + node, vertex);
            }

            for (int node = 0; node < nodeCount; ++node) {
                lineSplit = in.readLine().split(" ");
                int edgeIdx = 0;
                if (hasNodeWeights) {
                    nodeWeights[node] = Integer.parseInt(lineSplit[0]);
                    edgeIdx += 1;
                }

                for (; edgeIdx < lineSplit.length; ++edgeIdx) {
                    int toNode = Integer.parseInt(lineSplit[edgeIdx]);
                    double weight = 1.0;
                    V fromVertex = resultMap.get("" + node);
                    V toVertex = resultMap.get("" + toNode);

                    if (hasEdgeWeights) {
                        edgeIdx += 1;
                        weight = Integer.parseInt(lineSplit[edgeIdx]);
                    }

                    if (!target.containsEdge(fromVertex, toVertex)) {
                        E edge = target.addEdge(fromVertex, toVertex);
                        ((WeightedGraph<V, E>) target).setEdgeWeight(edge, weight);
                    }
                }
            }
        } catch(IOException ex) {
            System.err.println(ex);
        }
    }


    public int[] getNodeWeights() {
        return nodeWeights;
    }

}

// End GraphReader.java
