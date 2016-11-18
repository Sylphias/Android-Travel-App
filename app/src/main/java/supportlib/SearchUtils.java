package supportlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Ilya on 16/11/16.
 * Given a graph of locations
 * 0 - MBS
 * 1 - Singapore Flyer
 * 2 - Vivo City
 * 3 - Resorts World Sentosa
 * 4 - Buddha Tooth Relic Temple
 * 5 - Zoo
 *
 * Given a list of location IDs, we will return an array list of all possible permutations of that array list
 */

public class SearchUtils {

    /**
     * This method takes in a list of locations and returns a lists of all possible permutations of order of location visit.
     **/
    public static HashMap<Integer,Location> getRawData(ArrayList<Integer> locations, Context context){
        HashMap<Integer,Location> rawdata = new HashMap<Integer,Location>();
        for(int i = 0; i < locations.size();i++){
            rawdata.put(locations.get(i),new TravelSQL(context).getEntryFrom(locations.get(i)));
        }
        rawdata.put(0,new TravelSQL(context).getEntryFrom(0));
        return rawdata;
    }

    public static List<List<Integer>> generateAllPaths(List<Integer> locations) {
        if (locations.size() == 0) {
            List<List<Integer>> result = new ArrayList<List<Integer>>();
            result.add(new ArrayList<Integer>());
            return result;
        }
        Integer firstElement = locations.remove(0);
        List<List<Integer>> returnValue = new ArrayList<List<Integer>>();
        List<List<Integer>> permutations = generateAllPaths(locations);
        for (List<Integer> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<Integer> temp = new ArrayList<Integer>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

    /**
     * getBestPath iterates through the input list of paths, then compares the cost ratio of each path
     * and returns the path with the best cost ratio if the path is within the budget
     *
     * Complexity of searching for the best path from the exhaustive method is O(n)
     */
    public static PathsAndCost getBestPath(ArrayList<ArrayList<Integer>> paths, double budget,HashMap<Integer,Location> rawdata) {
        ArrayList<PathInfo> bestPath = new ArrayList<>();
        double bestCost = 0;
        for (int i = 0; i < paths.size(); i++) {
            paths.get(i).add(0,0);
            paths.get(i).add(paths.get(i).size(),0);
            double currentTimeCost = getPathTimeCost(paths.get(i),rawdata);
            PathsAndCost pnc = getPathCost(paths.get(i),rawdata);
            double currentCost = pnc.getCost();
            if (bestCost < currentTimeCost && budget > currentCost ) {
                bestCost = currentTimeCost;
                bestPath = pnc.getPath();
            }
        }
        return new PathsAndCost(bestPath,bestCost);
    }
    /**
     * This method takes in a path and calculates the weighted cost of the path by taking the minimum of a scaled timecost (which is a variable based on time and cost)
     **/
    public static double getPathTimeCost(ArrayList<Integer> path, HashMap<Integer,Location> rawdata){

        double timecost = 0;
        for (int i = 0; i < path.size()-1;i++){
            int toLoc = path.get(i+1);
            int fromLoc = path.get(i);
                ArrayList<Double> timescores = new ArrayList<>();
                double timePub = rawdata.get(fromLoc).getPublictime()[toLoc];
                double timeFoot = rawdata.get(fromLoc).getFoottime()[toLoc];
                double timePriv = rawdata.get(fromLoc).getPrivatetime()[toLoc];
                double costPub = rawdata.get(fromLoc).getPubliccost()[toLoc];
                double costFoot = 0;
                double costPriv = rawdata.get(fromLoc).getPrivatecost()[toLoc];
                timescores.add(map(timePub,0,300,0,10) + map(costPub,0,25,0,10));
                timescores.add(map(timePriv,0,300,0,10) + map(costPriv,0,25,0,10));
                timescores.add(map(timeFoot,0,300,0,10) + map(costFoot,0,25,0,10));
                timecost = timecost + Collections.min(timescores);
                int index = timescores.indexOf(Collections.min(timescores));
        }
        return timecost;
    }

    public static PathsAndCost getPathCost(ArrayList<Integer> path, HashMap<Integer,Location> rawdata){

        double time = 0;
        double cost = 0;
        ArrayList<PathInfo> pathList = new ArrayList<PathInfo>();
        for (int i = 0; i < path.size()-1;i++){
            int toLoc = path.get(i+1);
            int fromLoc = path.get(i);
            ArrayList<Double> timescores = new ArrayList<>();
            double timePub = rawdata.get(fromLoc).getPublictime()[toLoc];
            double timeFoot = rawdata.get(fromLoc).getFoottime()[toLoc];
            double timePriv = rawdata.get(fromLoc).getPrivatetime()[toLoc];
            double costPub = rawdata.get(fromLoc).getPubliccost()[toLoc];
            double costFoot = 0;
            double costPriv = rawdata.get(fromLoc).getPrivatecost()[toLoc];
            timescores.add(map(timePub,0,300,0,10) + map(costPub,0,25,0,10));
            timescores.add(map(timePriv,0,300,0,10) + map(costPriv,0,25,0,10));
            timescores.add(map(timeFoot,0,300,0,10) + map(costFoot,0,25,0,10));
            time = time + Collections.min(timescores);
            int index = timescores.indexOf(Collections.min(timescores));
            PathInfo pi = new PathInfo( rawdata.get(toLoc).getLocation(),rawdata.get(fromLoc).getLocation(),toLoc,fromLoc );

            switch (index) {
                case 0:
                    cost = cost + rawdata.get(fromLoc).getPubliccost()[toLoc];
                    pi.setCost(rawdata.get(fromLoc).getPubliccost()[toLoc]);
                    pi.setDuration(rawdata.get(fromLoc).getPublictime()[toLoc]);
                    pi.setMode(TRANSPORTATION.BUS);
                    break;
                case 1:
                    cost = cost + rawdata.get(fromLoc).getPrivatecost()[toLoc];
                    pi.setCost(rawdata.get(fromLoc).getPrivatecost()[toLoc]);
                    pi.setDuration(rawdata.get(fromLoc).getPrivatetime()[toLoc]);
                    pi.setMode(TRANSPORTATION.TAXI);
                    break;
                case 2:
                    pi.setCost(0);
                    pi.setDuration(rawdata.get(fromLoc).getFoottime()[toLoc]);
                    pi.setMode(TRANSPORTATION.WALKING);
                    break;
            }

            pathList.add(pi);
        }
        PathsAndCost pnc = new PathsAndCost(pathList,cost);
        return pnc;
    }

    /**
     * This method returns an array list of Paths for comparison to be done for nearest neighbour
     **/

    public static ArrayList<PathInfo> getConnected(int locationId, HashMap<Integer,Location> locations){
        ArrayList<PathInfo> res = new ArrayList<>();

        for (int toLocation : locations.keySet()) {
            if (toLocation != locationId) {
                double privateCost , publicCost, privateDuration, publicDuration, publicRatio, privateRatio;
                publicCost = locations.get(locationId).getPubliccost()[toLocation];
                privateCost = locations.get(locationId).getPubliccost()[toLocation];
                publicCost = locations.get(locationId).getPubliccost()[toLocation];
                publicCost = locations.get(locationId).getPubliccost()[toLocation];
                res.add(new PathInfo(locations.get(toLocation).getLocation(), locations.get(locationId).getLocation(),
                        toLocation, locationId, locations.get(locationId).getPrivatetime()[toLocation],
                        locations.get(locationId).getPrivatecost()[toLocation], TRANSPORTATION.TAXI));
            }
        }
        return res;
    }

    public static PathInfo getVehicleConnection(PathInfo pi , HashMap<Integer,Location> locations){
        switch(pi.getMode()) {
            case TAXI:
                pi.setDuration(locations.get(pi.getFromId()).getPublictime()[pi.getToId()]);
                pi.setCost(locations.get(pi.getFromId()).getPubliccost()[pi.getToId()]);
                pi.setMode(TRANSPORTATION.BUS);
                break;
            case BUS:
                pi.setDuration(locations.get(pi.getFromId()).getFoottime()[pi.getToId()]);
                pi.setCost(0);
                pi.setMode(TRANSPORTATION.WALKING);
                break;
        }
    return pi;

    }

    /**
    * This method is used to calculate the timecost of each edge,
    ***/
    public static double map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}


