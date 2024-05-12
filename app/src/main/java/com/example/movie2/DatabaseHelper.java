package com.example.movie2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "movie";
    public static final String MOVIE_ID = "_id";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_DIRECTOR = "director";
    public static final String MOVIE_OPENDT = "openDt";

    public static final String MOVIE_NATIONALT = "nationAlt";

    public static final String MOVIE_GENREALT = "genreAlt";

    public static final String [] ALL_COLUMNS = {MOVIE_ID, MOVIE_TITLE, MOVIE_DIRECTOR, MOVIE_OPENDT, MOVIE_NATIONALT, MOVIE_GENREALT};

    public DatabaseHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 데이터베이스 테이블 생성 SQL문
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MOVIE_TITLE + " TEXT, " +
                MOVIE_DIRECTOR + " TEXT, " +
                MOVIE_OPENDT + " TEXT, " +
                MOVIE_GENREALT + " TEXT, " +
                MOVIE_NATIONALT + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 데이터베이스 버전이 변경될 때 호출되는 메서드
        if (oldVersion < 2 && newVersion >= 2) {
            // 예를 들어, 버전 2에서의 스키마 변경 SQL문
            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN new_column TEXT");
        }
    }
}
