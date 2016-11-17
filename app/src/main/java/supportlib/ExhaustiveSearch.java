package supportlib;

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

public class ExhaustiveSearch {

    /**
     * This method takes in a list of locations and returns a lists of all possible permutations of order of location visit.
     **/
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
                List<Integer> temp = new ArrayList<>(smallerPermutated);
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
    public static List<Integer> getBestPath(ArrayList<ArrayList<Integer>> paths, double budget,ArrayList<Location> rawdata) {
        ArrayList<Integer> bestPath = new ArrayList<>();
        double bestCost = 0;


        for (int i = 0; i < paths.size(); i++) {
            double currentTimeCost = getPathTimeCost(paths.get(i),rawdata);
            double currentCost = getPathCost(paths.get(i),rawdata);
            if (bestCost < currentTimeCost && budget > currentCost ) {
                bestCost = currentTimeCost;
                bestPath = paths.get(i);
            }
        }
        return bestPath;
    }


    public static double getPathTimeCost(ArrayList<Integer> inp, ArrayList<Location> rawdata){

        double time = 0;
        for (int i = 0; i < inp.size()-1;i++){
            int toLoc = i+1;
            for(int p = 0; p < rawdata.size();p++)
            {
                if (rawdata.get(p).id == i){
                    ArrayList<Double> timescores = new ArrayList<>();
                    double timePub = rawdata.get(p).publictime[toLoc];
                    double timeFoot = rawdata.get(p).foottime[toLoc];
                    double timePriv = rawdata.get(p).privatetime[toLoc];
                    double costPub = rawdata.get(p).publiccost[toLoc];
                    double costFoot = 0;
                    double costPriv = rawdata.get(p).privatecost[toLoc];
                    timescores.add(map(timePub,0,300,0,10) + map(costPub,0,25,0,10));
                    timescores.add(map(timePriv,0,300,0,10) + map(costPriv,0,25,0,10));
                    timescores.add(map(timeFoot,0,300,0,10) + map(costFoot,0,25,0,10));
                    time = time + Collections.min(timescores);
                }
            }
        }
        return time;
    }

    public static double getPathCost(ArrayList<Integer> inp, ArrayList<Location> rawdata){

        double time = 0;
        double cost = 0;
        for (int i = 0; i < inp.size()-1;i++){
            int toLoc = i+1;
            for(int p = 0; p < rawdata.size();p++)
            {
                if (rawdata.get(p).id == i){
                    ArrayList<Double> timescores = new ArrayList<>();
                    double timePub = rawdata.get(p).publictime[toLoc];
                    double timeFoot = rawdata.get(p).foottime[toLoc];
                    double timePriv = rawdata.get(p).privatetime[toLoc];
                    double costPub = rawdata.get(p).publiccost[toLoc];
                    double costFoot = 0;
                    double costPriv = rawdata.get(p).privatecost[toLoc];
                    timescores.add(map(timePub,0,300,0,10) + map(costPub,0,25,0,10));
                    timescores.add(map(timePriv,0,300,0,10) + map(costPriv,0,25,0,10));
                    timescores.add(map(timeFoot,0,300,0,10) + map(costFoot,0,25,0,10));
                    time = time + Collections.min(timescores);
                    int index = timescores.indexOf(Collections.min(timescores));

                    switch (index) {
                        case 0: cost = cost + rawdata.get(p).publiccost[toLoc];
                                break;
                        case 1: cost = cost + rawdata.get(p).privatecost[toLoc];
                            break;
                        case 2:
                            break;
                    }
                }
            }
        }
        return cost;
    }

    public static HashMap<Integer,Double> getConnected(int locaID, ArrayList<Location> Locations){
        HashMap<Integer,Double> res = new HashMap<>();
        for (int i = 0; i < Locations.size();i++){
            if (Locations.get(i).id == locaID)
                continue;
            else
            {
                double timePub = Locations.get(locaID).publictime[i];
                double timeFoot = Locations.get(locaID).foottime[i];
                double timePriv = Locations.get(locaID).privatetime[i];
                double costPub = Locations.get(locaID).publiccost[i];
                double costFoot = 0;
                double costPriv = Locations.get(locaID).privatecost[i];
                double secondArg = (map(timePub,0,300,0,10) + map(costPub,0,25,0,10) + map(timePriv,0,300,0,10) + map(costPriv,0,25,0,10) + map(timeFoot,0,300,0,10) + map(costFoot,0,25,0,10))/3;
                res.put(Locations.get(i).id,secondArg);
            }
        }
        return res;
    }

    public static double map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

}


