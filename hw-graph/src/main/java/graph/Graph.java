package graph;

import java.util.*;

/**
 * Represents a mutable multi directed graph consisting of a finite set of vertices
 * and a set of ordered pairs(edges).
 * A graph cannot contain duplicate vertex name and no 2 edges with the same
 * parent and child nodes have the same edge label. However, multiple edges
 * may have the same label.
 */
public class Graph {
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

    private HashMap<String, Node> nodes;

    /**
     * Add a node to the graph.
     * @throws RuntimeException when there exist node with the same name
     * @param newNode a name of the new node
     * @spec.requires newNode != null, and no dups with the existing Node names
     * @spec.modifies this
     * @spec.effects add a new node with name newNode to this
     */
    public void addNode(String newNode) {
        if (DEBUG) checkRep();

        if (nodes.get(newNode) != null)
            throw new RuntimeException("Duplicate node");
        this.nodes.put(newNode, new Node(newNode));

        if (DEBUG) checkRep();
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
        if (DEBUG) checkRep();

        if (nodes.get(srcNode) == null || nodes.get(dstNode) == null)
            throw new java.util.NoSuchElementException("No such node");

        Node parent = this.nodes.get(srcNode);
        boolean isAdded = parent.addEdge(dstNode, label);

        if (!isAdded){
            throw new RuntimeException("Duplicate edge with same srcNode, dstNode and label");
        }

        if (DEBUG) checkRep();
    }

    /**
     * Return a name of nodes in this Graph in lexicographical order.
     * @return a name of nodes in a lexicographical order.
     */
    public List<String> listNodes(){
        if (DEBUG) checkRep();

        List<String> ret = new ArrayList<>(nodes.keySet());

        if (DEBUG) checkRep();
        return ret;
    }

    /**
     * Return a name and a label of the parentNode's children nodes
     * @param parentNode a name of parent node
     * @throws java.util.NoSuchElementException when no node with a name parentNode
     * @return a name of children nodes and label.
     * @spec.requires parentNode != null, parentNode is a name of the node present in the graph
     */
    public List<String> listChildren(String parentNode){
        if (DEBUG) checkRep();

        if (nodes.get(parentNode) == null){
            throw new java.util.NoSuchElementException("No matching node");
        }

        HashSet<Edge> edges = this.nodes.get(parentNode).getEdges();
        Iterator<Edge> i = edges.iterator();
        List<String> ret = new ArrayList<>();

        while(i.hasNext()){
            ret.add(i.next().toString());
        }

        if (DEBUG) checkRep();
        return ret;
    }


    private void checkRep() {
        // Assert this.nodes is not a null
        assert this.nodes != null : "this.nodes is null!";

        // Iterating HashMap through for loop
        for (Map.Entry<String, Node> set : this.nodes.entrySet()) {
            assert set != null : "this.nodes has a null element!";
        }
    }

    /**
     * Represents a mutable vertex with a name and a set of edges to which from this
     */
    public class Node {

        /**
         * Creates a Node with the name name
         * @param name a name of the Node
         * @spec.modifies this
         * @spec.requires name != null
         */
        public Node(String name) {
            this.name = name;
            this.edges = new HashSet<>();
        }

        // The name of this Node is a name. Edges are stored in a HashSet edges,
        //
        // RI: name != null, edges != null
        // AF(this) = A node named name with outgoing edges in edges
        private String name;
        private HashSet<Edge> edges;

        /**
         * return a set of outgoing edges from this.
         * @return a set of outgoing edges from this.
         */
        public HashSet<Edge> getEdges(){
            if (DEBUG) checkRep();

            HashSet<Edge> ret = new HashSet<>(edges);

            if (DEBUG) checkRep();
            return ret;
        }

        /**
         * Add a new edge from this to a node name dstNode with a label label
         * @param dstName name of a node to which the edge from srcNode
         * @param label label of the edge.
         * @return true if no duplicate edge, else false
         * @spec.modifies this
         * @spec.requires dstName, label != null
         * @spec.effects add a new edge from this to a node name dstNode with a label label.
         */
        public boolean addEdge(String dstName, String label){
            if (DEBUG) checkRep();

            Edge newEdge = new Edge(dstName, label);
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
    public class Edge {

        /**
         * Creates an Edge with the label label and a child Node name dstNode
         * @param dstNode name of a node to which the edge
         * @param label label of the edge
         * @spec.modifies this
         * @spec.requires dstNode, label != null
         */
        public Edge(String dstNode, String label) {
            this.dstName = dstNode;
            this.label = label;
        }

        // The label of this edge is a label.
        // The childnode's name of this edge is a dstName
        //
        // RI: label != null, dstName != null
        // AF(this) = An edge with label and childnode name dstName.
        private String label;
        private String dstName;

        /**
         * return the label of this edge.
         * @return the label of this edge.
         */
        public String getLabel() {
            if (DEBUG) checkRep();
            return label;
        }

        /**
         * return the name of the destination node of this edge.
         * @return the name of the destination node of this edge.
         */
        public String getdstName() {
            if (DEBUG) checkRep();
            return dstName;
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
            if (! (o instanceof Edge)){
                return false;
            }
            Edge e = (Edge) o;

            if (DEBUG) checkRep();
            return Objects.equals(this.label, e.label) && Objects.equals(this.dstName, e.dstName);
        }

        /**
         * Returns a hash code value for the object.
         * @return a hash code value for the object.
         */
        @Override
        public int hashCode(){
            if (DEBUG) checkRep();
            return this.label.hashCode()*this.dstName.hashCode();
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
            assert this.dstName != null : "this.dstName is null!";
        }

    }

}
