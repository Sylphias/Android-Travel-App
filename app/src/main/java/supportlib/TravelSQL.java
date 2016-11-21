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
        db.execSQL("create table if not exists LocationsTable (id integer primary key, location text, publictime text,privatetime text,foottime text, publiccost text, privatecost text, image integer)");
        ContentValues cv = new ContentValues();
        cv.put("location","Marina Bay Sands");
        cv.put("publiccost","0.0,0.83,1.18,4.03,0.88,1.96");
        cv.put("publictime","0,17,26,35,19,84");
        cv.put("privatecost","0.0,3.22,6.96,8.50,4.98,18.40");
        cv.put("privatetime","0,3,14,19,8,30");
        cv.put("foottime","0,14,69,76,28,269");
        cv.put("type","hotel");
        cv.put("image", R.mipmap.marinabaysands);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Singapore Flyer");
        cv.put("publiccost","0.83,0.0,1.26,4.03,0.98,1.89");
        cv.put("publictime","17,0,31,38,24,85");
        cv.put("privatecost","4.32,0.0,7.84,9.38,4.76,18.18");
        cv.put("privatetime","6,0,13,18,8,29");
        cv.put("foottime","14,0,81,88,39,264");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.singaporeflyer);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Vivocity");
        cv.put("publiccost","1.18,1.26,0.0,2.00,0.98,1.99");
        cv.put("publictime","24,29,0,10,18,85");
        cv.put("privatecost","8.30,7.96,0.0,4.54,6.42,22.58");
        cv.put("privatetime","12,14,0,9,11,31");
        cv.put("foottime","69,81,0,12,47,270");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.vivocity);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Resorts World Sentosa");
        cv.put("publiccost","1.18,1.26,0.0,0.0,0.98,1.99");
        cv.put("publictime","33,38,10,0,27,92");
        cv.put("privatecost","8.74,8.40,3.22,0.0,6.64,22.80");
        cv.put("privatetime","13,14,4,0,12,32");
        cv.put("foottime","76,88,12,0,55,285");
        cv.put("type","hotel");
        cv.put("image", R.mipmap.resortsworldsentosa);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Buddha Tooth Relic Temple");
        cv.put("publiccost","0.88,0.98,0.98,3.98,0.0,1.91");
        cv.put("publictime","18,23,19,28,0,83");
        cv.put("privatecost","5.32,4.76,4.98,6.52,0.0,18.40");
        cv.put("privatetime","7,8,9,14,0,30");
        cv.put("foottime","28,39,47,55,0,264");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.buddhatemple);
        db.insert("LocationsTable",null,cv);

        cv = new ContentValues();
        cv.put("location","Singapore Zoo");
        cv.put("publiccost","1.88,1.96,2.11,4.99,1.91,0.0");
        cv.put("publictime","86,87,86,96,84,0");
        cv.put("privatecost","22.48,19.40,21.48,23.68,21.60,0.0");
        cv.put("privatetime","32,29,32,36,30,0");
        cv.put("foottime","269,264,270,285,264,0");
        cv.put("type","attraction");
        cv.put("image", R.mipmap.singaporezoo);
        db.insert("LocationsTable",null,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //versions unused
        db.execSQL("DROP TABLE IF EXISTS LocationsTable");
        onCreate(db);
    }

    public ArrayList<Location> getAllEntries() {
        ArrayList<Location> array_list = new ArrayList<Location>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from LocationsTable", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new Location(
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
        return array_list;
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

    public boolean deleteTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from LocationsTable");
        return true;
    }
}

