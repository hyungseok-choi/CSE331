package pathfinder.junitTests;
import graph.Graph;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import pathfinder.Dijkstra;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class DijkstraTest {

    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    static Graph<String, Double> g1;
    static Graph<String, Double> g2;
    @Before
    public void graphCreation(){
        g1 = new Graph<>();
        g2 = new Graph<>();

        g2.addNode("A");
        g2.addNode("B");
        g2.addNode("C");
        g2.addNode("D");

        g2.addEdge("A", "B", 1.0);
        g2.addEdge("A", "B", 5.0);
        g2.addEdge("B", "C", 2.0);
        g2.addEdge("B", "C", 4.0);
        g2.addEdge("B", "D", 10.0);
        g2.addEdge("C", "D", 2.0);
        g2.addEdge("A", "D", 6.0);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoElemException(){
        Dijkstra.findPath(g1, "A", "B");
    }

    @Test(expected = RuntimeException.class)
    public void testNoPathException(){
        Dijkstra.findPath(g2, "B", "A");
    }

    @Test
    public void testDijkstra(){
        assertEquals(2.0, Dijkstra.findPath(g2, "C", "D").getCost(), 0.001);
        assertEquals(4.0, Dijkstra.findPath(g2, "B", "D").getCost(), 0.001);
        assertEquals(5.0, Dijkstra.findPath(g2, "A", "D").getCost(), 0.001);
    }



}
