package com.example.hunter.popularmovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavouriteContract {
        static final String Authority = "com.example.hunter.popularmovies";
        static final Uri Base_Content_Uri = Uri.parse("content://" + Authority);
        static final String Path_Movies = "movies";

        public static final class FavouriteEntry implements BaseColumns {
            public static final Uri CONTENT_URI =
                    Base_Content_Uri.buildUpon()
                    .appendPath(Path_Movies)
                    .build();


            public static final String TABLE_Name = "favourite";
            public static final String COLUMN_MovieID = "id";
            public static final String COLUMN_Title = "title";
            public static final String COLUMN_Release = "release";
            public static final String COLUMN_Userrating = "userrating";
            public static final String COLUMN_Poster_Path = "posterpath";
            public static final String COLUMN_Plot_Sypnosis = "plotsypnosis";

        }

}
