package supportlib;

import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    public static List<Integer> getBestPath(List<List<Integer>> paths, double budget) {
        List<Integer> bestPath = new ArrayList<Integer>();
        double bestCost = 0;

        for (int i = 0; i < paths.size(); i++) {
            double currentTimeCost = getPathTimeCost(paths.get(i));
            double currentCost = getPathCost(paths.get(i));
            if (bestCost < currentTimeCost && budget > currentCost ) {
                bestCost = currentTimeCost;
                bestPath = paths.get(i);
            }
        }
        return bestPath;
    }
}


