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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Represent buildings and path data.
 *  Each building data consist of shortName, longName, and coordinate on map.
 *  Each path data consist of start, end coordinate on map, and cost.
 */
public class CampusMap implements ModelAPI {

    static final Map<String, String> buildingName;
    static final Map<String, Point> buildingCor;
    static final Graph<Point, Double> campusGraph;

    static {
        List<CampusBuilding> buildings = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        List<CampusPath> paths = CampusPathsParser.parseCampusPaths("campus_paths.csv");

        buildingName = new HashMap<>();
        buildingCor = new HashMap<>();
        campusGraph = new Graph<>();

        for (CampusBuilding campusBuilding : buildings) {
            String sName = campusBuilding.getShortName();
            String lName = campusBuilding.getLongName();
            double bld_x = campusBuilding.getX();
            double bld_y = campusBuilding.getY();

            // add to buildingName hashMap
            buildingName.put(sName, lName);

            // add to buildingCor haspMap
            buildingCor.put(sName, new Point(bld_x, bld_y));

            // add to campusGraph Node
            campusGraph.addNode(new Point(bld_x, bld_y));
        }

        for (CampusPath campusPath : paths) {
            double src_x = campusPath.getX1();
            double src_y = campusPath.getY1();
            double dst_x = campusPath.getX2();
            double dst_y = campusPath.getY2();
            double cost = campusPath.getDistance();

            // add nodes to campusGraph
            Point src = new Point(src_x, src_y);
            Point dst = new Point(dst_x, dst_y);
            if (!campusGraph.listNodes().contains(src))
                campusGraph.addNode(src);

            if (!campusGraph.listNodes().contains(dst))
                campusGraph.addNode(dst);

            // add Edges to campusGraph
            campusGraph.addEdge(new Point(src_x, src_y), new Point(dst_x, dst_y), cost);
        }
    }

    // shortName and longName for buildings are stored in hashMap buildingName.
    // shortName and coordinate for buildings are stored in hashMap buildingCor.
    // every path's start and end points with their cost are stored in Graph campusGraph
    //
    // RI: buildingName, buildingCor, campusGraph != null
    // AF(this) = short name for buildings = {buildingName.keySet()}
    //            long name for buildings = {buildingName.values()}
    //            possible paths in campus = {all Edges in campusGraph}

    /**
     * @param shortName The short name of a building to query.
     * @return {@literal true} iff the short name provided exists in this campus map.
     */
    @Override
    public boolean shortNameExists(String shortName) {
        return buildingName.containsKey(shortName);
    }

    /**
     * @param shortName The short name of a building to look up.
     * @return The long name of the building corresponding to the provided short name.
     * @throws IllegalArgumentException if the short name provided does not exist.
     */
    @Override
    public String longNameForShort(String shortName) {
        if (!shortNameExists(shortName))
            throw new IllegalArgumentException("shortName does not exist");
        return buildingName.get(shortName);
    }

    /**
     * @return A mapping from all the buildings' short names to their long names in this campus map.
     */
    @Override
    public Map<String, String> buildingNames() {
        return Map.copyOf(buildingName);
    }

    /**
     * Finds the shortest path, by distance, between the two provided buildings.
     *
     * @param startShortName The short name of the building at the beginning of this path.
     * @param endShortName   The short name of the building at the end of this path.
     * @return A path between {@code startBuilding} and {@code endBuilding}, or {@literal null}
     * if none exists.
     * @throws IllegalArgumentException if {@code startBuilding} or {@code endBuilding} are
     *                                  {@literal null}, or not valid short names of buildings in
     *                                  this campus map.
     */
    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        try{
            Point srcPoint = buildingCor.get(startShortName);
            Point dstPoint = buildingCor.get(endShortName);

            return Dijkstra.findPath(campusGraph, srcPoint, dstPoint);
        } catch (Exception e){
            throw new IllegalArgumentException("Invalid arguments");
        }
    }

}
