package graph;

import java.util.List;

/**
 * Represents a mutable multi directed graph consisting of a finite set of vertices
 * and a set of ordered pairs(edges).
 * A graph cannot contain duplicate vertex name and no 2 edges with the same
 * parent and child nodes have the same edge label. However, multiple edges
 * may have the same label.
 */
public class Graph {

    /**
     * Creates an empty graph.
     * @spec.modifies this
     */
    public Graph(){
        throw new RuntimeException("not yet implemented");
    }

    /**
     * Add an additional Node to the graph.
     * @throws RuntimeException when there exist node with the same name
     * @param newNode a name of the new node
     * @spec.requires newNode != null, and no dups with the existing Node names
     * @spec.modifies this
     * @spec.effects add a new node with name newNode to this
     */
    public void addNode(String newNode){
        throw new RuntimeException("not yet implemented");
    }

    /**
     * Add a new edge with parent node srdNode, child node dstNode and label label
     * @throws java.util.NoSuchElementException when no node with a name srcNode or dstNode
     * @throws RuntimeException when there exist edge with the same srdNode, dstNode, and label
     * @param srcNode name of a node from which the edge to dstNode
     * @param dstNode name of a node to which the edge from srcNode
     * @param label label of the edge.
     * @spec.requires srcNode, dstNode, label != null, srcNode and dstNode are name of nodes present in the graph
     * there should be no edges consisting of the same srdNode, dstNode, and label in the graph
     * @spec.modifies this
     * @spec.effects add a new edge with a parent node name srdNode, a child node name dstNode and a label label
     */
    public void addEdge(String srcNode, String dstNode, String label){
        throw new RuntimeException("not yet implemented");
    }

    /**
     * Return a name of nodes in this Graph in lexicographical order.
     * @return a name of nodes in a lexicographical order.
     */
    public List<String> listNodes(){
        throw new RuntimeException("not yet implemented");
    }

    /**
     * Return a name of the parentNode's children nodes in lexicographical order by node name
     * and secondarily by edge label
     * @param parentNode a name of parent node
     * @return a name of children nodes in lexicographical order by node name
     * @spec.requires parentNode != null
     */
    public List<String> listChildren(String parentNode){
        throw new RuntimeException("not yet implemented");
    }

    /**
     * Represents a mutable vertex with a name and a list of edges to which from this
     */
    public class Node {

        /**
         * Creates a Node with the name name
         * @param name a name of the Node
         * @spec.modifies this
         * @spec.requires name != null
         */
        public Node(String name) {
            throw new RuntimeException("not yet implemented");
        }

        /**
         * Check if this and other are the same node
         * @param other a node to compare with
         * @return true if this and other are the same else false
         * @spec.requires other != null, other is an instance of Node
         */
        @Override
        public boolean equals(Object other) {
            throw new RuntimeException("not yet implemented");
        }
    }

    /**
     * Represents an immutable edge with a child node with a label.
     */
    public class Edge {

        /**
         * Creates an Edge with the label label and a child Node name dstNode
         * @param dstNode name of a node to which the edge
         * @param label label of the edge
         * @spec.modifies this
         * @spec.requires dstNode, label != null
         */
        public Edge(String dstNode, String label) {
            throw new RuntimeException("not yet implemented");
        }

    }

}
