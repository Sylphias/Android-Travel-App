package supportlib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hermes.travelapp.R;

import java.util.ArrayList;

public class TravelSQL extends SQLiteOpenHelper {
    public static String sqlitetable = "sqlitetable.db";

    public TravelSQL(Context c){
        super(c,sqlitetable,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists LocationsTable (id integer primary key, location text, publictime text,privatetime text,foottime text, publiccost text, privatecost text, type text, image integer)");
        ContentValues cv = new ContentValues();
        cv.put("location","Marina Bay Sands");
        cv.put("publiccost","0.0,0.83,1.18,4.03,0.88,1.96,0.78,1.39,1.24,1.43");
        cv.put("publictime","0,17,26,35,19,84,28,43,37,48");
        cv.put("privatecost","0.0,3.22,6.96,8.5,4.98,18.4,5.14,14.38,8.0,12.84");
        cv.put("privatetime","0,3,14,19,8,30,7,20,12,21");
        cv.put("foottime","0,14,69,76,28,269,20,129,29,232");
        cv.put("type","hotel");
        cv.put("image", R.mipmap.marinabaysands);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Singapore Flyer");
        cv.put("publiccost","0.83,0.0,1.26,4.03,0.98,1.89,0.78,1.43,1.17,1.39");
        cv.put("publictime","17,0,31,38,24,85,22,47,41,47");
        cv.put("privatecost","4.32,0.0,7.84,9.38,4.76,18.18,4.92,14.16,7.56,12.62");
        cv.put("privatetime","6,0,13,18,8,29,6,19,11,20");
        cv.put("foottime","14,0,81,88,39,264,23,142,38,221");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.singaporeflyer);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Vivocity");
        cv.put("publiccost","1.18,1.26,0.0,2.0,0.98,1.99,0.99,2.99,0.88,1.86");
        cv.put("publictime","24,29,0,10,18,85,23,25,25,90");
        cv.put("privatecost","8.3,7.96,0.0,7.84,9.38,4.76,7.46,6.68,6.02,21.2");
        cv.put("privatetime","12,14,0,9,11,31,11,12,9,27");
        cv.put("foottime","69,81,0,12,47,270,63,65,51,282");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.vivocity);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Resorts World Sentosa");
        cv.put("publiccost","1.18,1.26,0.0,0.0,0.98,4.99,2.99,2.99,2.88,3.54");
        cv.put("publictime","33,38,10,0,27,92,40,41,44,74");
        cv.put("privatecost","8.74,8.4,3.22,0.0,6.64,22.8,12.34,11.88,11.44,20.46");
        cv.put("privatetime","13,14,4,0,12,32,17,19,21,29");
        cv.put("foottime","76,88,12,0,55,285,75,83,63,294");
        cv.put("type","hotel");
        cv.put("image", R.mipmap.resortsworldsentosa);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Buddha Tooth Relic Temple");
        cv.put("publiccost","0.88,0.98,0.98,3.98,0.0,1.91,0.78,1.3,0.88,1.43");
        cv.put("publictime","18,23,19,28,0,83,24,35,22,45");
        cv.put("privatecost","5.32,4.76,4.98,6.52,0.0,18.4,4.26,9.32,5.58,13.06");
        cv.put("privatetime","7,8,9,14,0,30,5,15,8,17");
        cv.put("foottime","28,39,47,55,0,264,17,107,5,234");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.buddhatemple);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Singapore Zoo");
        cv.put("publiccost","1.88,1.96,2.11,4.99,1.91,0.0,1.86,1.88,1.5,1.62");
        cv.put("publictime","86,87,86,96,84,0,82,90,51,65");
        cv.put("privatecost","22.48,19.4,21.48,23.68,21.6,0.0,25.7,20.56,15.04,17.68");
        cv.put("privatetime","32,29,32,36,30,0,30,26,22,25");
        cv.put("foottime","269,264,270,285,264,0,280,121,281,247");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.singaporezoo);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Fullerton Hotel");
        cv.put("publiccost","0.78,0.78,0.99,2.99,0.78,1.84,0.0,1.35,1.24,1.5");
        cv.put("publictime","24,21,23,43,28,82,0,38,34,45");
        cv.put("privatecost","9.7,9.43,11.07,13.28,6.06,29.23,0.0,19.59,6.39,13.28");
        cv.put("privatetime","8,7,12,17,5,31,0,17,5,18");
        cv.put("foottime","20,23,63,75,17,280,0,122,17,227");
        cv.put("type","hotel");
        cv.put("image", R.mipmap.fullertonhotel);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Haw Par Villa");
        cv.put("publiccost","1.39,1.39,0.99,2.99,1.73,1.82,1.35,0.0,1.43,1.95");
        cv.put("publictime","45,48,31,41,96,85,34,0,57,101");
        cv.put("privatecost","12.3,13.18,6.46,8.22,10.1,19.66,10.54,0.0,8.44,19.66");
        cv.put("privatetime","16,19,11,16,16,28,16,0,14,32");
        cv.put("foottime","129,142,65,83,107,262,121,0,141,320");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.hawparvilla);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Chinatown");
        cv.put("publiccost","0.99,0.88,0.88,2.88,0.78,1.82,0.78,1.3,0.0,1.39");
        cv.put("publictime","27,29,21,41,10,72,20,35,0,38");
        cv.put("privatecost","4.7,5.36,9.03,10.68,3.6,20.1,4.04,12.62,0.0,12.4");
        cv.put("privatetime","6,7,9,16,4,27,4,15,0,15");
        cv.put("foottime","28,38,50,62,5,282,16,110,0,230");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.chinatown);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Punggol Waterway Park");
        cv.put("publiccost","2.54,2.53,1.86,3.86,1.76,1.84,1.7,1.96,1.54,0.0");
        cv.put("publictime","93,94,92,113,82,105,85,107,73,0");
        cv.put("privatecost","18.46,26.38,22.52,24.28,19.78,17.24,19.56,27.36,12.84,0.0");
        cv.put("privatetime","22,29,31,36,24,29,28,33,21,0");
        cv.put("foottime","232,220,281,293,234,249,225,324,230,0");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.punggolwaterway);
        db.insert("LocationsTable",null,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //versions unused
        db.execSQL("DROP TABLE IF EXISTS LocationsTable");
        onCreate(db);
    }

    public ArrayList<Location> getAllEntries() {
        ArrayList<Location> locationsList = new ArrayList<Location>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from LocationsTable", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            locationsList.add(new Location(
                    res.getString(res.getColumnIndex("id")), //id starts from 1, handled in Location constructor
                    res.getString(res.getColumnIndex("location")),
                    res.getString(res.getColumnIndex("publiccost")),
                    res.getString(res.getColumnIndex("publictime")),
                    res.getString(res.getColumnIndex("privatecost")),
                    res.getString(res.getColumnIndex("privatetime")),
                    res.getString(res.getColumnIndex("foottime")),
                    res.getString(res.getColumnIndex("type")),
                    res.getInt(res.getColumnIndex("image"))
            ));
            res.moveToNext();
        }
        res.close();
        return locationsList;
    }

    /*
        Return a Location object.
        Takes a String detailing location.
        PS: not doing any checks - Assumes First Letter Caps, and direct match to location.
     */
    public Location getEntryFrom(String s){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from LocationsTable where location="+s,null);
        res.moveToFirst();

        Location ret = new Location(
                res.getString(res.getColumnIndex("id")), //id starts from 1, handled in Location constructor
                res.getString(res.getColumnIndex("location")),
                res.getString(res.getColumnIndex("publiccost")),
                res.getString(res.getColumnIndex("publictime")),
                res.getString(res.getColumnIndex("privatecost")),
                res.getString(res.getColumnIndex("privatetime")),
                res.getString(res.getColumnIndex("foottime")),
                res.getString(res.getColumnIndex("type")),
                res.getInt(res.getColumnIndex("image"))
        );
        return ret;
    }

    /*
        Return a Location object.
        Takes an integer detailing id.
        (MBS = 0, Flyer = 1...) the method does the +1.
     */
    public Location getEntryFrom(int i){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from LocationsTable WHERE id = "+String.valueOf(i+1),null);
        res.moveToFirst();
//        Cursor res = db.rawQuery("select * from LocationsTable where id=1",null);
        Location ret = new Location(
                res.getString(res.getColumnIndex("id")), //id starts from 1, handled in Location constructor
                res.getString(res.getColumnIndex("location")),
                res.getString(res.getColumnIndex("publiccost")),
                res.getString(res.getColumnIndex("publictime")),
                res.getString(res.getColumnIndex("privatecost")),
                res.getString(res.getColumnIndex("privatetime")),
                res.getString(res.getColumnIndex("foottime")),
                res.getString(res.getColumnIndex("type")),
                res.getInt(res.getColumnIndex("image"))
        );
        return ret;
    }

    public ArrayList<Location> getHotels(){
        ArrayList<Location> hotelsList= new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from LocationsTable WHERE type = \"hotel\"",null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            hotelsList.add(new Location(
                    res.getString(res.getColumnIndex("id")), //id starts from 1, handled in Location constructor
                    res.getString(res.getColumnIndex("location")),
                    res.getString(res.getColumnIndex("publiccost")),
                    res.getString(res.getColumnIndex("publictime")),
                    res.getString(res.getColumnIndex("privatecost")),
                    res.getString(res.getColumnIndex("privatetime")),
                    res.getString(res.getColumnIndex("foottime")),
                    res.getString(res.getColumnIndex("type")),
                    res.getInt(res.getColumnIndex("image"))
            ));
            res.moveToNext();
        }
        res.close();
        return hotelsList;
    }

    public ArrayList<Location> getAllExceptHotel(Integer loc){
        ArrayList<Location> hotelsList= new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        loc = loc + 1;
        Cursor res = db.rawQuery("select * from LocationsTable EXCEPT select * from LocationsTable WHERE id = "+ loc,null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            hotelsList.add(new Location(
                    res.getString(res.getColumnIndex("id")), //id starts from 1, handled in Location constructor
                    res.getString(res.getColumnIndex("location")),
                    res.getString(res.getColumnIndex("publiccost")),
                    res.getString(res.getColumnIndex("publictime")),
                    res.getString(res.getColumnIndex("privatecost")),
                    res.getString(res.getColumnIndex("privatetime")),
                    res.getString(res.getColumnIndex("foottime")),
                    res.getString(res.getColumnIndex("type")),
                    res.getInt(res.getColumnIndex("image"))
            ));
            res.moveToNext();
        }
        res.close();
        return hotelsList;
    }

    public boolean deleteTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from LocationsTable");
        return true;
    }
}

