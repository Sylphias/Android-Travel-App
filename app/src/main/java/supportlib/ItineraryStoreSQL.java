package supportlib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Path;

import com.example.hermes.travelapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Ilya on 22/11/16.
 */

public class ItineraryStoreSQL extends SQLiteOpenHelper{

        public static String sqlitetable = "sqlitetable.db";

        public ItineraryStoreSQL(Context c){
        super(c,sqlitetable,null,1);
    }

        @Override
        public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists PathsTable (id integer primary key, name text, cost real, path text");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //versions unused
        db.execSQL("DROP TABLE IF EXISTS PathsTable");
        onCreate(db);
    }

    public ArrayList<PathsAndCost> getAllItineraries() {
        ArrayList<PathsAndCost> locationsList = new ArrayList<PathsAndCost>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from PathsTable", null );
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PathInfo>>(){}.getType();
        res.moveToFirst();
        while(res.isAfterLast() == false){
            locationsList.add(new PathsAndCost(
                    (ArrayList<PathInfo>) gson.fromJson(res.getString(res.getColumnIndex("path")),type),
                    res.getDouble(res.getColumnIndex("cost")),
                    res.getString(res.getColumnIndex("name"))
            )
            );

            res.moveToNext();
        }
        res.close();
        return locationsList;
    }

    public void insertItinerary(PathsAndCost pnc,SQLiteDatabase db,String name){
        ContentValues cv = new ContentValues();
        Gson gson = new Gson();
        cv.put("path", gson.toJson(pnc.getPath()));
        cv.put("cost", pnc.getCost());
        cv.put("name", name);
        db.insert("PathsTable",null,cv);
    }


    public boolean deleteTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from PathsTable");
        return true;
    }
}

