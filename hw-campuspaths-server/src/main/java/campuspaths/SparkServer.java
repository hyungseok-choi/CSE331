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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.Locale;
import java.util.Map;

public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().
        CampusMap map = new CampusMap();

        Spark.get("/bldnames", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Map<String, String> blds = map.buildingNames();
                Gson gson = new Gson();
                return gson.toJson(blds.keySet().toArray());
            }
        });

        Spark.get("/minpath", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String startBld = request.queryParams("s");
                String endBld = request.queryParams("e");
                if(startBld == null || endBld == null || !map.shortNameExists(startBld) || !map.shortNameExists(endBld)) {
                    Spark.halt(400);
                }
                Gson gson = new Gson();
                return gson.toJson(map.findShortestPath(startBld, endBld));
            }
        });

    }

}
