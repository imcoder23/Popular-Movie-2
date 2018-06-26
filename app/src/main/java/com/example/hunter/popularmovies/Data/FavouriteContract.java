package com.example.hunter.popularmovies.Data;

import android.provider.BaseColumns;

public class FavouriteContract {
        public static final class FavouriteEntry implements BaseColumns {

            public static final String TABLE_Name = "favourite";
            public static final String COLUMN_MovieID = "id";
            public static final String COLUMN_Title = "title";
            public static final String COLUMN_Release = "release";
            public static final String COLUMN_Userrating = "userrating";
            public static final String COLUMN_Poster_Path = "posterpath";
            public static final String COLUMN_Plot_Sypnosis = "plotsypnosis";

        }

}
