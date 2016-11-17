package supportlib;

import java.util.Arrays;

/**
 * Created by Regan Andela on 16-Nov-16.
 */
public class Location {
    public int id;
    public String location;
    public float[] publiccost;
    public int[] publictime;
    public float[] privatecost;
    public int[] privatetime;
    public int[] foottime;

    public Location(String id,String location, String pc,String pt,String pvc,String pvt,String ft){
        this.id = Integer.parseInt(id)-1; //id starts from 1 in sqlite
        this.location = location;

        //publiccost = Arrays.stream(pc.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray(); //API 24 :(
        String[] pca = pc.split(",");
        publiccost = new float[pca.length];
        for(int i = 0;i<pca.length;i++){
            try{
                publiccost[i] = Float.parseFloat(pca[i]);
            }catch(Exception e){
                System.err.println(e);
            }
        }
        pca = pt.split(","); //can do this b/c everything is length 6
        publictime = new int[pca.length];
        for(int i = 0;i<pca.length;i++){
            try{
                publictime[i] = Integer.parseInt(pca[i]);
            }catch(Exception e){
                System.err.println(e);
            }
        }

        pca = pvc.split(",");
        privatecost = new float[pca.length];
        for(int i = 0;i<pca.length;i++){
            try{
                privatecost[i] = Float.parseFloat(pca[i]);
            }catch(Exception e){
                System.err.println(e);
            }
        }
        pca = pvt.split(",");
        privatetime = new int[pca.length];
        for(int i = 0;i<pca.length;i++){
            try{
                privatetime[i] = Integer.parseInt(pca[i]);
            }catch(Exception e){
                System.err.println(e);
            }
        }

        pca = ft.split(",");
        foottime = new int[pca.length];
        for(int i = 0;i<pca.length;i++){
            try{
                foottime[i] = Integer.parseInt(pca[i]);
            }catch(Exception e){
                System.err.println(e);
            }
        }
    }
}
