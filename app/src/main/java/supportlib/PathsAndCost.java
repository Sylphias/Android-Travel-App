package supportlib;

import java.util.ArrayList;

/**
 * Created by Ilya on 17/11/16.
 */

public class PathsAndCost {
    ArrayList<PathInfo> path;
    double cost;
    String name;
    public PathsAndCost(){
        super();
    }

    public PathsAndCost(ArrayList<PathInfo> path, double cost, String name) {
        this.path = path;
        this.cost = cost;
        this.name = name;
    }


    public PathsAndCost(ArrayList<PathInfo> path, double cost) {
        this.path = path;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PathInfo> getPath() {
        return path;
    }

    public void setPath(ArrayList<PathInfo> path) {
        this.path = path;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
