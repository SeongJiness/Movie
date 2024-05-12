package com.example.movie2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MovieProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.movie2.MovieProvider";
    private static final String BASE_PATH = "movie";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int MOVIES = 1;
    private static final int MOVIE_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, MOVIES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIE_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext(), 3);
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                cursor = database.query(DatabaseHelper.TABLE_NAME, DatabaseHelper.ALL_COLUMNS, s, null, null, null,
                        DatabaseHelper.MOVIE_TITLE + " ASC");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + uriMatcher.match(uri));
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // URI에 대한 MIME 타입을 반환
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                return "vnd.android.cursor.dir/vnd.com.example.movie2.movies";
            case MOVIE_ID:
                return "vnd.android.cursor.item/vnd.com.example.movie2.movies";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);

        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                count = database.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                count = database.update(DatabaseHelper.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
