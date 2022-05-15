/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.Graph;
import pathfinder.Dijkstra;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    // ***************************
    // ***  JUnit Test Driver  ***
    // ***************************

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/

    private final Map<String, graph.Graph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        Graph<String, Double> g = new Graph<>();

        graphs.put(graphName, g);
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, Double> g = graphs.get(graphName);
        g.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        Double edgeLabel = Double.valueOf(arguments.get(3));

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {
        Graph<String, Double> g = graphs.get(graphName);
        g.addEdge(parentName, childName, edgeLabel);
        output.println("added edge " + String.format("%.3f", edgeLabel) + " from " + parentName + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String, Double> g = graphs.get(graphName);
        List<String> nodes = g.listNodes();

        Collections.sort(nodes);

        StringBuilder sb = new StringBuilder(graphName + " contains:");
        for (int i = 0; i < nodes.size(); i++){
            sb.append(" " + nodes.get(i));
        }
        output.println(sb);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String, Double> g = graphs.get(graphName);
        List<Graph.Edge<String, Double>> edges = new ArrayList<>(g.listChildren(parentName));

        edges.sort(new Comparator<Graph.Edge<String, Double>>() {
            @Override
            public int compare(Graph.Edge<String, Double> o1, Graph.Edge<String, Double> o2) {
                if (o1.getdstName().equals(o2.getdstName())) {
                    return o1.getLabel().compareTo(o2.getLabel());
                }
                return o1.getdstName().compareTo(o2.getdstName());
            }
        });

        StringBuilder sb = new StringBuilder("the children of " + parentName + " in " + graphName + " are:");
        for (Graph.Edge<String, Double> edge : edges) {
            sb.append(" ").append(edge.toString());
        }
        output.println(sb);
    }

    private void findPath(List<String> arguments) {
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to findPath: " + arguments);
        }

        String graphName = arguments.get(0);
        String srcName = arguments.get(1);
        String dstName = arguments.get(2);

        findPath(graphName, srcName, dstName);
    }

    private void findPath(String graphName, String srcName, String dstName) {
        Graph<String, Double> g = graphs.get(graphName);

        boolean isError = false;
        StringBuilder sb = new StringBuilder();

        if (!g.listNodes().contains(srcName)){
            sb.append("unknown: ").append(srcName);
            isError = true;
        }
        if (!g.listNodes().contains(dstName)){
            if (isError) sb.append("\n");
            sb.append("unknown: ").append(dstName);
            isError = true;
        }
        if (isError){
            output.println(sb);
            return;
        }

        sb.append("path from " + srcName + " to " + dstName + ":").append("\n");
        try{
            Path<String> path = Dijkstra.findPath(g, srcName, dstName);

            for (Path<String>.Segment<String> ss : path) {
                sb.append(ss.getStart() + " to " + ss.getEnd() + " with weight " + String.format("%.3f", ss.getCost())).append("\n");

//                sb.append(stringSegment).append("\n");
//                A to B with weight 1.000
            }
            sb.append(String.format("total cost: %.3f", path.getCost()));

        } catch(RuntimeException e){
            sb.append("no path found");
        } finally {
            output.println(sb);
        }
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }

}
