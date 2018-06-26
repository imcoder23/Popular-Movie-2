package com.example.hunter.popularmovies.Utils;

import android.net.Uri;
import android.util.Log;
import com.example.hunter.popularmovies.BuildConfig;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieUrlUtils {
   public static final String API_KEY = BuildConfig.API_KEY;
    private static final String Movie_Query ="api_key";
    private static final String Movie_Base_Url = "https://api.themoviedb.org/3/movie/";

    public static URL buildUrl(String movieUrl) {
        Uri uri = Uri.parse(Movie_Base_Url)
                    .buildUpon()
                    .appendPath(movieUrl)
                    .appendQueryParameter(Movie_Query, API_KEY)
                    .build();
            URL url = null;
            try {
                url = new URL(uri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("URL ERROR", "Problems With Creating Url", e);
            }
            return url;
    }

    public static URL buildUrlTrailers(String idTrailer, String trailer){
        Uri uri = Uri.parse(Movie_Base_Url.concat(idTrailer.concat(trailer)))
                .buildUpon()
                .appendQueryParameter(Movie_Query, API_KEY)
                .build();
        URL url= null;
        try {
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            Log.e("URL PROBLEM", "Problems With Creating Url", e);
        }
        return url;
    }
    public static URL buildUrlReview( String idMovie, String reviews){
        Uri uri = Uri.parse(Movie_Base_Url.concat(idMovie).concat(reviews))
                .buildUpon()
                .appendQueryParameter(Movie_Query, API_KEY)
                .build();
        URL url= null;
        try {
            url= new URL(uri.toString());

        } catch (MalformedURLException e) {
            Log.e("URL PROBLEM", "Problems With Creating Url", e);
        }
        return url;
    }
}
