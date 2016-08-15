package com.example.almaz.gdgforecast.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

public class CitiesContentProvider extends ContentProvider {

    final String LOG_TAG = "CitiesProvider LOG";
    //Database
    static final String DB_NAME = "gdg_forecast_db";
    static final int DB_VERSION = 1;

    //Tables
    static final String CITIES_TABLE = "cities";

    //Fields
    public static final String CITY_ID = "_id";
    public static final String CITY_NAME = "name";
    public static final String CITY_LAST_FORECAST = "last_forecast";

    //Columns
    public static final int ID_COLUMN = 0;
    public static final int NAME_COLUMN = 1;
    public static final int LAST_FORECAST_COLUMN = 2;

    //Database creating script
    static final String DB_CREATE = "create table " + CITIES_TABLE + "("
            + CITY_ID + " integer primary key autoincrement, "
            + CITY_NAME + " text unique, "
            + CITY_LAST_FORECAST + " text" + ");";

    // // Uri
    //authority
    final static String AUTHORITY = "almaz.example.com.gdgforecast";

    //path
    final static String CITIES_PATH = "cities";

    //united Uri
    public static final Uri CITIES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CITIES_PATH);

    //data types
    //set of rows
    static final String CITIES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CITIES_PATH;

    //one row
    static final String CITIES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CITIES_PATH;

    // // UriMatcher
    //united Uri
    static final int URI_CITIES = 1;

    //Uri with id
    static final int URI_CITIES_ID = 2;

    //describing and creating UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CITIES_PATH, URI_CITIES);
        uriMatcher.addURI(AUTHORITY, CITIES_PATH + "/#", URI_CITIES_ID);
    }

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public CitiesContentProvider() {
    }

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CITIES_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                CITIES_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CITIES:
                return CITIES_CONTENT_TYPE;
            case URI_CITIES_ID:
                return CITIES_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_CITIES){
            throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(CITIES_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(CITIES_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CITIES:
                Log.d(LOG_TAG, "URI_CONTACTS");
                break;
            case URI_CITIES_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = CITY_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CITY_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(CITIES_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CITIES:
                Log.d(LOG_TAG, "URI_CONTACTS");

                break;
            case URI_CITIES_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = CITY_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CITY_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(CITIES_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);

            //fill db with start cities list
            db.insert(CITIES_TABLE, null, newRow("Kazan"));
            db.insert(CITIES_TABLE, null, newRow("Moscow"));
            db.insert(CITIES_TABLE, null, newRow("London"));
            db.insert(CITIES_TABLE, null, newRow("Tokyo"));
            db.insert(CITIES_TABLE, null, newRow("New York"));
            db.insert(CITIES_TABLE, null, newRow("Paris"));
            db.insert(CITIES_TABLE, null, newRow("Berlin"));
            db.insert(CITIES_TABLE, null, newRow("Rome"));
            db.insert(CITIES_TABLE, null, newRow("Nizhnekamsk"));
            db.insert(CITIES_TABLE, null, newRow("Peking"));
        }

        private ContentValues newRow(String name){
            ContentValues cv = new ContentValues();
            cv.put(CITY_NAME, name);
            return cv;
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
