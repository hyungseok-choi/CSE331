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

            // add node
            try{
                campusGraph.addNode(new Point(src_x, src_y));
            } catch (RuntimeException e){

            }

            try{
                campusGraph.addNode(new Point(dst_x, dst_y));
            } catch (RuntimeException e) {

            }

            // add to campusGraph Edge
            campusGraph.addEdge(new Point(src_x, src_y), new Point(dst_x, dst_y), cost);
        }
    }

    @Override
    public boolean shortNameExists(String shortName) {
        return buildingName.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        if (!shortNameExists(shortName))
            throw new IllegalArgumentException("shortName does not exist");
        return buildingName.get(shortName);
    }

    @Override
    public Map<String, String> buildingNames() {
        return Map.copyOf(buildingName);
    }

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
