package supportlib;

/**
 * Created by Ilya on 17/11/16.
 */

/**
 Custom class to store information for one set of travel instructions.
 Stores:
 1. To which destination
 2. From which destination
 3. IDs of above
 4. Cost of transport
 5. Mode of transport
 **/

public class PathInfo {
    String to, from;
    int toId, fromId,duration;
    double cost;
    TRANSPORTATION mode;

    public PathInfo(String to, String from, int toId, int fromId, double cost, TRANSPORTATION mode) {
        this.to = to;
        this.from = from;
        this.toId = toId;
        this.fromId = fromId;
        this.cost = cost;
        this.mode = mode;
    }

    public PathInfo(String to, String from, int toId, int fromId, int duration, double cost, TRANSPORTATION mode) {
        this.to = to;
        this.from = from;
        this.toId = toId;
        this.fromId = fromId;
        this.duration = duration;
        this.cost = cost;
        this.mode = mode;
    }

    public PathInfo(String to, String from, int toId, int fromId, int duration, TRANSPORTATION mode) {
        this.to = to;
        this.from = from;
        this.toId = toId;
        this.fromId = fromId;
        this.duration = duration;
        this.mode = mode;
    }

    public PathInfo( String to, String from,int toId,int fromId) {
        this.fromId = fromId;
        this.to = to;
        this.from = from;
        this.toId = toId;
    }


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getToId() {
        return toId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public TRANSPORTATION getMode() {
        return mode;
    }

    public void setMode(TRANSPORTATION mode) {
        this.mode = mode;
    }


    //Enum containing modes of travel because modes of travel are fixed.
    public enum TRANSPORTATION{
        WALKING, BUS, TAXI;
    }

}




