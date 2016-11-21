package supportlib;

import android.content.res.Resources;

import com.example.hermes.travelapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Ilya on 21/11/16.
 */

public class HawkerUtils {
    public static ArrayList<ArrayList<String>> getHawkerLocations(Resources resources) {
        String line = null;
        InputStream is = null;
        ArrayList<ArrayList<String>> big = new ArrayList<ArrayList<String>>();
        try {
            is = resources.openRawResource(R.raw.hawkers);
            //is = new FileInputStream("C:\\Users\\tycli_000\\AndroidStudioProjects\\TestAndr\\app\\src\\main\\res\\raw\\hawkers
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            boolean nameflag = false;
            ArrayList<String> smol = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                if(line.contains("<name>") && !line.contains("<name>HAWKERCENTRE")){
                    smol.add(line.trim().replace("<name>", "").replace("</name>", ""));
                    nameflag = true;
                }else if(line.contains("<coordinates>") && nameflag){
                    String[] tmp = line.trim().split(",");
                    smol.add(0,tmp[1]);
                    smol.add(0, tmp[0].split(" ")[1]);
                    big.add(smol);
                    smol = new ArrayList<String>();
                    nameflag = false;
                }
            }
            br.close();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        catch(Exception er){
            er.printStackTrace();
        }

        return big;
    }

    public static ArrayList<ArrayList<String>> getNearbyHawkers(ArrayList<ArrayList<String>> inpt,double lat, double lon){
        //data is long lat
        double modval = 0.025;
        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
        for(int i =0;i<inpt.size();i++){
            //if within bounding box
            if((!(lon+modval < Double.parseDouble(inpt.get(i).get(0)) ||
                    lon-modval > Double.parseDouble(inpt.get(i).get(0))) &&
                    !(lat+modval < Double.parseDouble(inpt.get(i).get(1)) ||
                            lat-modval > Double.parseDouble(inpt.get(i).get(1))))){
                ret.add(inpt.get(i));
            }
        }
        return ret;
    }
}
