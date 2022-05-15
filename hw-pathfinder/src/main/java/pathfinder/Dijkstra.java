package pathfinder;

import graph.Graph;

import java.util.*;

/**
 * This is an util class which provides Dijkstra algorithm.
 */
public class Dijkstra{

    /**
     * @param graph a graph with node, edges
     * @param srcNode starting node for dijkstra algorithm
     * @param dstNode destination node for dijkstra algorithm
     * @throws RuntimeException when graph doesn't contain srcNode
     * @return a list of shortest path from srcNode to dstNode and total costs.
     * @spec.requires Graph contains srcNode
     */
    public static <T> Path<T> findPath(Graph<T, Double> graph, T srcNode, T dstNode){

        if (!graph.listNodes().contains(srcNode)){
            throw new RuntimeException("graph doesn't contain srcNode");
        }

        T start = srcNode;
        T dest = dstNode;
        PriorityQueue<Path<T>> active = new PriorityQueue<>();
        Set<T> finished = new HashSet<>();

        active.add(new Path<>(new Graph.Edge<T, Double>(start, start, 0.0)));

        while (!active.isEmpty()){
            Path<T> minPath = active.remove();
            T minDest = minPath.pairs.get(minPath.pairs.size() - 1 ).getdstName();

            if (dest.equals(minDest)){
                return minPath;
            }
            if (finished.contains(minDest)) continue;

            List<Graph.Edge<T, Double>> edges = new ArrayList<>(graph.listChildren(dstNode));

            edges.sort((o1, o2) -> o1.getLabel().compareTo(o2.getLabel()));

            for (Graph.Edge<T, Double> child : graph.listChildren(minDest)) {
                if (!finished.contains(child.getdstName())) {
                    active.add(new Path<>(minPath, child));
                }
            }
            finished.add(minDest);
        }
        return null;
    }


    /**
     * immutable path class.
     */
    public static class Path<T> implements Comparable<Path<T>> {
        private final List<Graph.Edge<T, Double>> pairs;
        private final double totalCost;

        public Path(Graph.Edge<T, Double> e){
            List<Graph.Edge<T, Double>> tmp = new ArrayList<>();
            tmp.add(e);
            this.pairs = tmp;
            this.totalCost = 0f;
        }

        public Path(Path<T> prev, Graph.Edge<T, Double> e){
            List<Graph.Edge<T, Double>> tmp = new ArrayList<>(prev.pairs);
            tmp.add(e);
            this.pairs = tmp;
            this.totalCost = prev.totalCost + e.getLabel();
        }

        @Override
        public int compareTo(Path<T> o) {
            return Double.compare(this.totalCost, o.totalCost);
        }
    }
}
