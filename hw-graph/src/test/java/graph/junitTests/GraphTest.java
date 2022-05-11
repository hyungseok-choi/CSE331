package graph.junitTests;
import graph.Graph;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.Arrays;
import static org.junit.Assert.*;

public class GraphTest {

    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    @Test
    public void testCreation(){
        Graph g1 = new Graph();
        assertEquals(Arrays.asList(),
                g1.listNodes());        // empty
    }

    @Test
    public void testAddNode(){
        Graph g1 = new Graph();

        g1.addNode("n1");
        assertEquals(Arrays.asList("n1"),
                g1.listNodes());        // a single node

        g1.addNode("n2");
        assertEquals(Arrays.asList("n1", "n2"),
                g1.listNodes());        // multiple nodes

    }

    @Test(expected = RuntimeException.class)
    public void testDupAddNode(){
        Graph g1 = new Graph();
        g1.addNode("n1");
        g1.addNode("n1");
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testNonexistentNodeAddEdge1(){
        Graph g1 = new Graph();
        g1.addNode("n1");

        g1.addEdge("n1", "n2", "e1");
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testNonexistentNodeAddEdge2(){
        Graph g1 = new Graph();
        g1.addNode("n1");

        g1.addEdge("n2", "n1", "e1");
    }


    @Test(expected = RuntimeException.class)
    public void testDupAddEdge(){
        Graph g1 = new Graph();
        g1.addNode("n1");
        g1.addNode("n2");

        g1.addEdge("n1", "n2", "e1");
        g1.addEdge("n1", "n2", "e1");
    }
}
