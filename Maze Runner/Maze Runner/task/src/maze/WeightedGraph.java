package maze;

import java.util.HashMap;
import java.util.Map;

public class WeightedGraph {

    // Map<Vertex, Map<NeighbourVertex, Weight>>
    protected Map<Integer, Map<Integer, Integer>> edges = new HashMap<>();

    public WeightedGraph() {
    }

    public void addVertex(int i) {
        if (!edges.containsKey(i)) {
            edges.put(i, new HashMap<Integer, Integer>());
        }
    }

    public void addEdge(int i, int j, int weight) {
        if (edges.containsKey(i) && edges.containsKey(j)) {
            edges.get(i).put(j, weight);
            edges.get(j).put(i, weight);
        }
    }

    public boolean ifContainsVertex(int i) {
        return edges.containsKey(i);
    }


    public boolean ifContainsEdge(int i, int j) {
        return edges.containsKey(i) && edges.get(i).containsKey(j);
    }

    public int getWeight(int i, int j) {
       return edges.get(i).get(j);
    }

    public int getVertexNumber() {
        return edges.keySet().size();
    }

    public WeightedGraph getMinimalSpanningTree() {
        WeightedGraph mst = new WeightedGraph();

        // Поместить первую вершину в граф
        mst.addVertex(edges.keySet().iterator().next());

        // Последовательно добавляем вершины по следующему принципу
        while (mst.getVertexNumber() < getVertexNumber()) {

            // Среди всех соседей всех вершин в mst ищем ту вершину не из mst,
            // ребро до которой имеет минимальный вес.
            int nextVertex = -1;
            int nextVertexParent = -1;
            int nextEdgeWeight = Integer.MAX_VALUE;

            for (Integer vertex : mst.edges.keySet()) {
                for (Integer candidate : edges.get(vertex).keySet()) {
                    if (!mst.ifContainsVertex(candidate) && getWeight(vertex, candidate) < nextEdgeWeight) {
                        nextVertex = candidate;
                        nextVertexParent = vertex;
                        nextEdgeWeight = getWeight(vertex, candidate);
                    }
                }
            }

            mst.addVertex(nextVertex);
            mst.addEdge(nextVertex, nextVertexParent, nextEdgeWeight);
        }

        return mst;
    }
}
