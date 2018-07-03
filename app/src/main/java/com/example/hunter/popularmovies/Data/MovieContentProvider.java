package com.example.hunter.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.hunter.popularmovies.MainActivity;
import com.example.hunter.popularmovies.R;

public class MovieContentProvider extends ContentProvider {
    public static final int Movies = 300;
    public static final int Movies_With_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavouriteDbHelper mfFavouriteDbHelper;

    private static UriMatcher buildUriMatcher() {
    UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    uriMatcher.addURI(FavouriteContract.Authority, FavouriteContract.Path_Movies,Movies);
    uriMatcher.addURI(FavouriteContract.Authority, FavouriteContract.Path_Movies + "/#",Movies_With_ID);

        return uriMatcher;
    }



    @Override
    public boolean onCreate() {
        Context context = getContext();
        mfFavouriteDbHelper = new FavouriteDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mfFavouriteDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor courserReturned;

        switch (match){
            case Movies:
                courserReturned = db.query(FavouriteContract.FavouriteEntry.TABLE_Name,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + uri);
        }
        courserReturned.setNotificationUri(getContext().getContentResolver(), uri);


        return courserReturned;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mfFavouriteDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){

            case Movies:
                long id = db.insert(FavouriteContract.FavouriteEntry.TABLE_Name,null,values);

                if(id > 0){
                    returnUri = ContentUris.withAppendedId(FavouriteContract.FavouriteEntry.CONTENT_URI , id);

                }else throw new SQLException(getContext().getString(R.string.row_failed) + uri);
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mfFavouriteDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int movieDeleted;
        switch (match){
            case Movies_With_ID:
                String id = uri.getPathSegments().get(1);
                movieDeleted = db.delete(FavouriteContract.FavouriteEntry.TABLE_Name,
                        FavouriteContract.FavouriteEntry.COLUMN_MovieID +"=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + uri);
        }
        if(movieDeleted != 0)
        getContext().getContentResolver().notifyChange(uri,null);

        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
