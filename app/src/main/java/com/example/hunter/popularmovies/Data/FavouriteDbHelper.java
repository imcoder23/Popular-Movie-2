package com.example.hunter.popularmovies.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hunter.popularmovies.Model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourite.db";
    private static final int DATABASE_VERSION = 1;
    public static final String LOGTAG = "FAVOURITE";

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase db;

    public FavouriteDbHelper(Context context ){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void open(){
        Log.i(LOGTAG,"DataBase Opened");
        db = dbhelper.getWritableDatabase();
    }

    public  void close(){
        Log.i(LOGTAG,"DataBase Closed");
        dbhelper.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + FavouriteContract.FavouriteEntry.TABLE_Name + " (" +
                FavouriteContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouriteContract.FavouriteEntry.COLUMN_MovieID + " INTEGER NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_Title + " TEXT NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_Poster_Path + " TEXT NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_Plot_Sypnosis + " TEXT NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_Userrating + " TEXT NOT NULL, " +
                FavouriteContract.FavouriteEntry.COLUMN_Release + " TEXT NOT NULL" +
                "); ";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ FavouriteContract.FavouriteEntry.TABLE_Name);
        onCreate(db);

    }

    public void addFavMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavouriteContract.FavouriteEntry.COLUMN_MovieID,movie.getId());
        values.put(FavouriteContract.FavouriteEntry.COLUMN_Title,movie.getMovie_title());
        values.put(FavouriteContract.FavouriteEntry.COLUMN_Poster_Path,movie.getMovie_poster());
        values.put(FavouriteContract.FavouriteEntry.COLUMN_Plot_Sypnosis,movie.getMovie_plot());
        values.put(FavouriteContract.FavouriteEntry.COLUMN_Release,movie.getMovie_releasedate());
        values.put(FavouriteContract.FavouriteEntry.COLUMN_Userrating,movie.getMovie_rating());

//        Log.d("Inserted valies", String.valueOf(values));

//        db.insert(FavouriteContract.FavouriteEntry.TABLE_Name,null,values);
//        try {
            db.insertOrThrow(FavouriteContract.FavouriteEntry.TABLE_Name, null, values);
//        }catch (SQLiteConstraintException e){
//            Log.d("SQL thorwn", String.valueOf(e));
//        }
//        finally {
            db.close();
//        }

//        getallFavorite();
    }

    public void DelFavMovie(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavouriteContract.FavouriteEntry.TABLE_Name,FavouriteContract.FavouriteEntry.COLUMN_MovieID+ "=" + id,null);
    }

    public List<Movie> getallFavorite(){
        String[] columns = {
                FavouriteContract.FavouriteEntry._ID,
                FavouriteContract.FavouriteEntry.COLUMN_MovieID,
                FavouriteContract.FavouriteEntry.COLUMN_Title,
                FavouriteContract.FavouriteEntry.COLUMN_Poster_Path,
                FavouriteContract.FavouriteEntry.COLUMN_Plot_Sypnosis,
                FavouriteContract.FavouriteEntry.COLUMN_Release,
                FavouriteContract.FavouriteEntry.COLUMN_Userrating,
        };
        String sortOrder = FavouriteContract.FavouriteEntry._ID + " ASC";
        List<Movie> favouritelist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FavouriteContract.FavouriteEntry.TABLE_Name,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);


        if(cursor.moveToFirst()){
    do{
        Movie movie = new Movie();
//        Log.i("1", String.valueOf(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_MovieID)))));
//        Log.i("2",cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Title)));
//        Log.i("3",cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Userrating)));
//        Log.i("4",cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Poster_Path)));
//        Log.i("5",cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Userrating)));
//        Log.i("6",cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Release)));
//        Log.i("7",cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Plot_Sypnosis)));

        movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_MovieID))));
        movie.setMovie_title(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Title)));
        movie.setMovie_rating(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Userrating)));
        movie.setMovie_poster(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Poster_Path)));
        movie.setMovie_plot(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Plot_Sypnosis)));
        movie.setMovie_rating(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Userrating)));
        movie.setMovie_releasedate(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_Release)));

        favouritelist.add(movie);
    }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favouritelist;
    }
}
