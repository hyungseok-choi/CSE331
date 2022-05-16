package graph;

import java.util.*;

/**
 * Represents a mutable multi directed graph consisting of a finite set of vertices
 * and a set of ordered pairs(edges).
 * A graph cannot contain duplicate vertex name and no 2 edges with the same
 * parent and child nodes have the same edge label. However, multiple edges
 * may have the same label.
 */
public class Graph<T, E> {
    public static final boolean DEBUG = false;

    /**
     * Creates an empty graph.
     * @spec.modifies this
     */
    public Graph(){
        nodes = new HashMap<>();
    }

    // Nodes are stored in a HashMap nodes, and each outgoing Edges are stored in the parent Node.
    //
    // RI: nodes != null, nodes' elements != null
    // AF(this) = Vertices (this.nodes.keySet()), Edges (for all value n in this.nodes, n.edges)

    private HashMap<T, Node<T>> nodes;

    /**
     * Add a node to the graph.
     * @throws RuntimeException when there exist node with the same name
     * @param newNode a label of the new node
     * @spec.requires newNode != null, and no dups with the existing Node label
     * @spec.modifies this
     * @spec.effects add a new node with label newNode to this
     */
    public void addNode(T newNode) {
        if (DEBUG) checkRep();

        if (nodes.get(newNode) != null)
            throw new RuntimeException("Duplicate node");
        this.nodes.put(newNode, new Node<T>(newNode));

        if (DEBUG) checkRep();
    }

    /**
     * Add a new edge with parent node srdNode, child node dstNode and label label
     * @throws java.util.NoSuchElementException when no node with label srcNode or dstNode
     * @throws RuntimeException when there exist edge with the same srdNode, dstNode, and label
     * @param srcNode label of a node from which the edge to dstNode
     * @param dstNode label of a node to which the edge from srcNode
     * @param label label of the edge.
     * @spec.requires srcNode, dstNode, label != null, srcNode and dstNode are label of nodes present in the graph
     * there should be no edges consisting of the same srdNode, dstNode, and label in the graph
     * @spec.modifies this
     * @spec.effects add a new edge with a parent node label srdNode, a child node label dstNode and a label label
     */
    public void addEdge(T srcNode, T dstNode, E label){
        if (DEBUG) checkRep();

        if (nodes.get(srcNode) == null || nodes.get(dstNode) == null)
            throw new java.util.NoSuchElementException("No such node");

        Node<T> parent = this.nodes.get(srcNode);
        boolean isAdded = parent.addEdge(srcNode, dstNode, label);

        if (!isAdded){
            throw new RuntimeException("Duplicate edge with same srcNode, dstNode and label");
        }

        if (DEBUG) checkRep();
    }

    /**
     * Return a list of label of nodes in this Graph.
     * @return a list of label of nodes.
     */
    public List<T> listNodes(){
        if (DEBUG) checkRep();

        List<T> ret = new ArrayList<>(nodes.keySet());

        if (DEBUG) checkRep();
        return ret;
    }

    /**
     * Return a set of edge which consists of each node's label and edge label
     * @param parentNode a label of parent node
     * @throws java.util.NoSuchElementException when no node with a label parentNode
     * @return a set of edge which consists of each node's label and edge label
     * @spec.requires parentNode != null, parentNode is a label of the node present in the graph
     */
    public Set<Edge<T, E>> listChildren(T parentNode){
        if (DEBUG) checkRep();

        if (nodes.get(parentNode) == null){
            throw new java.util.NoSuchElementException("No matching node");
        }

        Set<Edge<T, E>> ret = this.nodes.get(parentNode).getEdges();

        if (DEBUG) checkRep();
        return ret;
    }


    private void checkRep() {
        // Assert this.nodes is not a null
        assert this.nodes != null : "this.nodes is null!";

        // Iterating HashMap through for loop
        for (Map.Entry<T, Node<T>> set : this.nodes.entrySet()) {
            assert set != null : "this.nodes has a null element!";
        }
    }

    /**
     * Represents a mutable vertex with a label and a set of edges to which from this
     */
    public class Node<T> {

        /**
         * Creates a node with the label name
         * @param name a label of the Node
         * @spec.modifies this
         * @spec.requires name != null
         */
        public Node(T name) {
            this.name = name;
            this.edges = new HashSet<>();
        }

        // The label of this Node is a name. Edges are stored in a HashSet edges,
        //
        // RI: name != null, edges != null
        // AF(this) = A node labeled name with outgoing edges in edges
        private T name;
        private HashSet<Edge<T, E>> edges;

        /**
         * return a set of outgoing edges from this.
         * @return a set of outgoing edges from this.
         */
        public HashSet<Edge<T,E>> getEdges(){
            if (DEBUG) checkRep();

            HashSet<Edge<T, E>> ret = new HashSet<Edge<T, E>>(edges);

            if (DEBUG) checkRep();
            return ret;
        }

        /**
         * Add a new edge from this to a node label dstNode with a label label
         * @param srcName label of a node from which the edge to dstNode
         * @param dstName label of a node to which the edge from srcNode
         * @param label label of the edge.
         * @return true if no duplicate edge, else false
         * @spec.modifies this
         * @spec.requires dstName, label != null
         * @spec.effects add a new edge from this to a node label dstNode with a label label.
         */
        public boolean addEdge(T srcName, T dstName, E label) {
            if (DEBUG) checkRep();

            Edge<T, E> newEdge = new Edge<T, E>(srcName, dstName, label);
            boolean isAdded = edges.add(newEdge);

            if (DEBUG) checkRep();
            return isAdded;
        }

        private void checkRep(){
            // Assert this.name is not a null
            assert this.name != null : "this.name is null!";

            // Assert this.edges is not a null
            assert this.edges != null : "this.edges is null!";
        }
    }

    /**
     * Represents an immutable edge with a child node with a label.
     */
    public static class Edge<T, E> {

        /**
         * Creates an Edge with the label label, a child Node label dstNode
         * @param srcName label of a node from which the edge
         * @param dstNode label of a node to which the edge
         * @param label label of the edge
         * @spec.modifies this
         * @spec.requires dstNode, label != null
         */
        public Edge(T srcName, T dstNode, E label) {
            this.srcName = srcName;
            this.dstName = dstNode;
            this.label = label;
        }

        // The label of this edge is a label.
        // The childnode's label of this edge is a dstName
        //
        // RI: label != null, dstName != null, srcName != null
        // AF(this) = An edge with label this.label which parentnode name is srcName and
        // childnode name is dstName.
        private T srcName;
        private T dstName;
        private E label;

        /**
         * return the label of this edge.
         * @return the label of this edge.
         */
        public E getLabel() {
            if (DEBUG) checkRep();
            return label;
        }

        /**
         * return the label of the destination node of this edge.
         * @return the label of the destination node of this edge.
         */
        public T getdstName() {
            if (DEBUG) checkRep();
            return dstName;
        }

        /**
         * return the label of the source node of this edge.
         * @return the label of the source node of this edge.
         */
        public T getsrcName() {
            if (DEBUG) checkRep();
            return srcName;
        }


        /**
         * Indicates whether some other object is "equal to" this one.
         * @param o the reference object with which to compare.
         * @return true if this object is the same as the obj argument; false otherwise.
         * @spec.requires o != null
         */
        @Override
        public boolean equals(Object o){
            if (DEBUG) checkRep();
            if (! (o instanceof Graph.Edge<?, ?>)){
                return false;
            }
            Graph.Edge<?, ?> e = (Graph.Edge<?, ?>) o;

            if (DEBUG) checkRep();
            return this.getsrcName().equals(e.getsrcName()) && this.getdstName().equals(e.getdstName()) && this.getLabel().equals(e.getLabel());
        }

        /**
         * Returns a hash code value for the object.
         * @return a hash code value for the object.
         */
        @Override
        public int hashCode(){
            if (DEBUG) checkRep();
            return 31*this.label.hashCode()+this.dstName.hashCode();
        }

        /**
         * Returns a string representation of the object.
         * @return a string representation of the object.
         */
        @Override
        public String toString() {
            if (DEBUG) checkRep();
            return getdstName() + "(" + getLabel() + ")";
        }

        private void checkRep(){
            // Assert this.label is not a null
            assert this.label != null : "this.label is null!";

            // Assert this.dstName is not a null
            assert this.srcName != null : "this.dstName is null!";

            // Assert this.dstName is not a null
            assert this.dstName != null : "this.dstName is null!";
        }

    }

}
