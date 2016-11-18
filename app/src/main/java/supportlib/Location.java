package supportlib;

import com.example.hermes.travelapp.R;

import java.util.Arrays;

/**
 * Created by Regan Andela on 16-Nov-16.
 */
public class Location {
    public int id;
    private String location;
    private float[] publiccost;
    private int[] publictime;
    private float[] privatecost;
    private int[] privatetime;
    private int[] foottime;
    private int image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float[] getPubliccost() {
        return publiccost;
    }

    public void setPubliccost(float[] publiccost) {
        this.publiccost = publiccost;
    }

    public int[] getPublictime() {
        return publictime;
    }

    public void setPublictime(int[] publictime) {
        this.publictime = publictime;
    }

    public float[] getPrivatecost() {
        return privatecost;
    }

    public void setPrivatecost(float[] privatecost) {
        this.privatecost = privatecost;
    }

    public int[] getPrivatetime() {
        return privatetime;
    }

    public void setPrivatetime(int[] privatetime) {
        this.privatetime = privatetime;
    }

    public int[] getFoottime() {
        return foottime;
    }

    public void setFoottime(int[] foottime) {
        this.foottime = foottime;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Location(String id, String location, String pc, String pt, String pvc, String pvt, String ft, int image){
        this.id = Integer.parseInt(id)-1; //id starts from 1 in sqlite
        this.location = location;
        this.image = image;

        //publiccost = Arrays.stream(pc.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray(); //API 24 :(
        String[] pca = pc.split(",");
        this.publiccost = new float[pca.length];
        for(int i = 0;i<pca.length;i++){
            try{
                publiccost[i] = Float.parseFloat(pca[i]);
            }catch(Exception e){
                System.err.println(e);
            }
        }
        pca = pt.split(","); //can do this b/c everything is length 6
        this.publictime = new int[pca.length];
        for(int i = 0;i<pca.length;i++){
            try{
                publictime[i] = Integer.parseInt(pca[i]);
            }catch(Exception e){
                System.err.println(e);
            }
        }

        pca = pvc.split(",");
        this.privatecost = new float[pca.length];
        for(int i = 0;i<pca.length;i++){
            try{
                privatecost[i] = Float.parseFloat(pca[i]);
            }catch(Exception e){
                System.err.println(e);
            }
        }
        pca = pvt.split(",");
        this.privatetime = new int[pca.length];
        for(int i = 0;i<pca.length;i++){
            try{
                privatetime[i] = Integer.parseInt(pca[i]);
            }catch(Exception e){
                System.err.println(e);
            }
        }

        pca = ft.split(",");
        this.foottime = new int[pca.length];
        for(int i = 0;i<pca.length;i++){
            try{
                foottime[i] = Integer.parseInt(pca[i]);
            }catch(Exception e){
                System.err.println(e);
            }
        }
    }
}
