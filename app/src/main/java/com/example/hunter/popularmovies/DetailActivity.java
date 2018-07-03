package com.example.hunter.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hunter.popularmovies.Adapter.ReviewAdapter;
import com.example.hunter.popularmovies.Adapter.TrailerAdapter;
import com.example.hunter.popularmovies.Adapter.movieAdapter;
import com.example.hunter.popularmovies.Data.FavouriteContract;
import com.example.hunter.popularmovies.Data.FavouriteDbHelper;
import com.example.hunter.popularmovies.Model.Movie;
import com.example.hunter.popularmovies.Model.Reviews;
import com.example.hunter.popularmovies.Model.Trailer;
import com.example.hunter.popularmovies.Utils.MovieUrlUtils;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private static final String URL_IMAGE_PATH = "http://image.tmdb.org/t/p/w185";
    private FavouriteDbHelper favouriteDbHelper;
    private RecyclerView rv_trailers;
    private RecyclerView rv_review;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private List<Trailer> trailerList;
    private List<Reviews> reviewList;
    private RequestQueue mRequestQueue;
    private RequestQueue mRequestQueueReview;
    private SQLiteDatabase mdb;
    private final AppCompatActivity activity = DetailActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView detail_moviePic = findViewById(R.id.imageView_moviePic);
        TextView movieName = findViewById(R.id.et_moviename);
        TextView movieDis = findViewById(R.id.textView_movieDescription);
        TextView movieRelease = findViewById(R.id.textView_year);
        TextView movieRating = findViewById(R.id.textView_rating);
        List<Movie> moviesList = new ArrayList<>();
        rv_trailers = findViewById(R.id.rv_trailers);
        rv_review = findViewById(R.id.rv_reviews);
        final movieAdapter adapter = new movieAdapter(this, moviesList);

        favouriteDbHelper = new FavouriteDbHelper(this);
        mdb = favouriteDbHelper.getWritableDatabase();

        MaterialFavoriteButton fb_button = findViewById(R.id.favorite_button);


        Intent intent = getIntent();
        final String Title = intent.getStringExtra("title");
        final String Poster = intent.getStringExtra("poster");
        final String plot = intent.getStringExtra("plot");
        final String rating = intent.getStringExtra("rating");
        final String release = intent.getStringExtra("releaseDate");
        final int movie_id = intent.getIntExtra("id", 0);

//        String releaseFinal = release.substring(0,4);


        movieName.setText(Title);
        movieDis.setText(plot);
        movieRelease.setText(release);
        movieRating.setText(rating);

        Picasso.with(this)
                .load(URL_IMAGE_PATH.concat(Poster))
                .fit()
                .into(detail_moviePic);

        if(checkExists(movie_id)){
            fb_button.setFavorite(true);
            fb_button.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                    if(favorite){
                        saveFavourite();
                        Snackbar.make(buttonView, "Added to Favorite", Snackbar.LENGTH_SHORT).show();
                    }else{
                          delFavourite(movie_id);
                        Snackbar.make(buttonView, "Removed from Favorite", Snackbar.LENGTH_SHORT).show();

                    }
                }

            });
        }else{
            fb_button.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                    if(favorite){
                        saveFavourite();
                        Snackbar.make(buttonView, "Added to Favorite", Snackbar.LENGTH_SHORT).show();
                    }else{
                        int movie_id = getIntent().getExtras().getInt("id");
                        delFavourite(movie_id);
                        Snackbar.make(buttonView, "Removed from Favorite", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
            init_reviews();
            init_trailer();
    }

    private void delFavourite(int movie_id) {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = FavouriteContract.FavouriteEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(movie_id)).build();

        contentResolver.delete(uri,null,null);
    }


    @Override
    public void onBackPressed() {
        this.startActivity(new Intent(DetailActivity.this,MainActivity.class));
    }

    private void saveFavourite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MovieID,getIntent().getIntExtra("id", 0));
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_Title,getIntent().getStringExtra("title"));
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_Poster_Path,getIntent().getStringExtra("poster"));
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_Plot_Sypnosis,getIntent().getStringExtra("plot"));
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_Userrating,getIntent().getStringExtra("rating"));
        contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_Release,getIntent().getStringExtra("releaseDate"));

        getContentResolver().insert(FavouriteContract.FavouriteEntry.CONTENT_URI,contentValues);
    }


    private boolean checkExists(int movieID){
        String[] Projection = {
                FavouriteContract.FavouriteEntry._ID,
                FavouriteContract.FavouriteEntry.COLUMN_MovieID,
                FavouriteContract.FavouriteEntry.COLUMN_Title,
                FavouriteContract.FavouriteEntry.COLUMN_Poster_Path,
                FavouriteContract.FavouriteEntry.COLUMN_Plot_Sypnosis,
                FavouriteContract.FavouriteEntry.COLUMN_Release,
                FavouriteContract.FavouriteEntry.COLUMN_Userrating,
        };
        String Selection = FavouriteContract.FavouriteEntry.COLUMN_MovieID + " =?";
        String[] SelectionArgs = {String.valueOf(movieID)};
        String limit = "1";
        Cursor cursor = mdb.query(FavouriteContract.FavouriteEntry.TABLE_Name,Projection,Selection,SelectionArgs,null,null,null,limit);
        boolean exists = (cursor.getCount()>0);
        cursor.close();
        return exists;
    }




    private void init_reviews() {
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(DetailActivity.this, reviewList);
        mRequestQueueReview = Volley.newRequestQueue(this);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_review.setLayoutManager(mlayoutManager);
        rv_review.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();

        load_reviewJson();

    }

    private void load_reviewJson() {

        int movie_id = getIntent().getExtras().getInt("id");

        URL Url_reviews = MovieUrlUtils.buildUrlReview(String.valueOf(movie_id),"/reviews");


        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, Url_reviews.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                String AUTHOR = result.getString("author");
                                String CONTENT = result.getString("content");

                                reviewList.add(new Reviews(AUTHOR, CONTENT));
                            }
                            reviewAdapter = new ReviewAdapter(DetailActivity.this, reviewList);
                            rv_review.setAdapter(reviewAdapter);
//                            trailerAdapter.setOnItemClickListener(DetailActivity.this);
                            reviewAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }

        );
        mRequestQueueReview.add(request);

    }

    private void init_trailer(){
        trailerList = new ArrayList<>();
        trailerAdapter = new TrailerAdapter(DetailActivity.this, trailerList);
        mRequestQueue = Volley.newRequestQueue(this);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_trailers.setLayoutManager(mlayoutManager);
        rv_trailers.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();

        load_trailerJson();
}

    private void load_trailerJson() {
        int movie_id = getIntent().getExtras().getInt("id");


        URL Url_trailer = MovieUrlUtils.buildUrlTrailers(String.valueOf(movie_id),"/videos");


        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, Url_trailer.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                String KEY = result.getString("key");
                                String NAME = result.getString("name");
                                trailerList.add(new Trailer(KEY, NAME));

                                Log.d("TrailerList", String.valueOf(trailerList));
                            }
                            trailerAdapter = new TrailerAdapter(DetailActivity.this, trailerList);
                            rv_trailers.setAdapter(trailerAdapter);
//                            trailerAdapter.setOnItemClickListener(DetailActivity.this);
                            trailerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", String.valueOf(error));
                error.printStackTrace();
            }
        }

        );
        mRequestQueue.add(request);

    }


}
