package supportlib;

import java.util.ArrayList;

/**
 * Created by Ilya on 17/11/16.
 *
 * This class is our implementation of Nearest Neighbour algorithm,
 * It is a greedy algo that will allow us to traverse through the graph visiting all points.
 */

public class NearestNeighbour {
    public static void getApproximatedPath(ArrayList<Location> locations){
        ArrayList<Location> visited = new ArrayList<Location>();
        int current_location_id = 0 ;
        boolean has_visited_all = false;
        while(locations.size() != 0){
            // Find lowest cost then set that as the next
            current_location_id = locations.get(0).getId();

        }
    }
}
