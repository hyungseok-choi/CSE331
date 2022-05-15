package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;

/**
 * This is an util class which provides Dijkstra algorithm.
 */
public class Dijkstra{

    /**
     * @param graph a graph with node, edges
     * @param srcNode starting node for dijkstra algorithm
     * @param dstNode destination node for dijkstra algorithm
     * @throws NoSuchElementException when graph doesn't contain srcNode or dstNode
     * @throws RuntimeException when there are no paths from srcNode to dstNode
     * @return a list of shortest path from srcNode to dstNode and total costs.
     * @spec.requires Graph contains srcNode and a path from srcNode to dstNode.
     */
    public static <T> Path<T> findPath(Graph<T, Double> graph, T srcNode, T dstNode){

        if (!graph.listNodes().contains(srcNode) || !graph.listNodes().contains(dstNode)){
            throw new NoSuchElementException("graph doesn't contain Node");
        }

        T start = srcNode;
        T dest = dstNode;
        PriorityQueue<Path<T>> active = new PriorityQueue<>(new Comparator<Path<T>>() {
            @Override
            public int compare(Path<T> o1, Path<T> o2) {
                return Double.compare(o1.getCost(), o2.getCost());
            }
        });
        Set<T> finished = new HashSet<>();

        active.add(new Path<T>(srcNode));

        while (!active.isEmpty()){
            Path<T> minPath = active.remove();
            T minDest = minPath.getEnd();

            if (dest.equals(minDest)){
                return minPath;
            }
            if (finished.contains(minDest)) continue;

            List<Graph.Edge<T, Double>> edges = new ArrayList<>(graph.listChildren(dstNode));

            edges.sort((o1, o2) -> o1.getLabel().compareTo(o2.getLabel()));

            for (Graph.Edge<T, Double> child : graph.listChildren(minDest)) {
                if (!finished.contains(child.getdstName())) {
                    active.add(minPath.extend(child.getdstName(), child.getLabel()));
                }
            }
            finished.add(minDest);
        }
        throw new RuntimeException("no path found");
    }
}
