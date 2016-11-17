package supportlib;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ilya on 17/11/16.
 *
 * This class is our implementation of Nearest Neighbour algorithm,
 * It is a greedy algo that will allow us to traverse through the graph visiting all points.
 */

public class NearestNeighbour {
    /**
     * This is the approximated path
    * */
    public static PathsAndCost getApproximatedPath(HashMap<Integer,Location> locations){
        ArrayList<PathInfo> visited = new ArrayList<PathInfo>();
        int current_location_id = 0;
        // First step is to find and iterate through all points in the first node (hotel)
        Location hotel = locations.get(1);
        current_location_id = locations.get(1).getId();
        PathInfo nextPath = getNextLocation(SearchUtils.getConnected(current_location_id,locations));
        double cost = nextPath.getCost();
        visited.add(nextPath);

        while(visited.size() != locations.size()){
            locations.remove(current_location_id);
            current_location_id = nextPath.getToId();
            nextPath = getNextLocation(SearchUtils.getConnected(current_location_id,locations));
            cost += nextPath.getCost();
            visited.add(nextPath);
        }

        // Finally route back to the hotel.
        locations.put(1,hotel);
        current_location_id = nextPath.getToId();
        nextPath = getNextLocation(SearchUtils.getConnected(current_location_id,locations));
        cost += nextPath.getCost();
        visited.add(nextPath);

        return new PathsAndCost(visited,cost);
    }

    /**
    *  Following the principle of dijkstra, this method w
    **/
    public ArrayList<PathInfo> relaxPathInfo(ArrayList<PathInfo> paths,double cost, double budget, HashMap<Integer,Location> locations){
        ArrayList<PathInfo> relaxedPath = paths;

        for(int i = 0; i < 2 ; i ++) {
            ArrayList<PathInfo> tempPath = new ArrayList<PathInfo>();
            for (PathInfo path : relaxedPath) {
                cost -= path.getCost();
                PathInfo newPath = SearchUtils.getVehicleConnection(path, locations);
                cost += path.getCost();
                tempPath.add(newPath);
            }
            if(cost<budget) break;
            relaxedPath =tempPath;
        }
        return relaxedPath;
    }

    /**
     * This method loops through the number of edges and chooses the edge with the shortest duration.
     */
    public static PathInfo getNextLocation(ArrayList<PathInfo> edges){
        int shortest_duration = 0;
        PathInfo shortest_path = edges.get(0);
        for (PathInfo pi : edges){
           if (shortest_duration> pi.getDuration()){
               shortest_duration = pi.getDuration();
               shortest_path = pi;
           }
        }
        return shortest_path;
    }
}
