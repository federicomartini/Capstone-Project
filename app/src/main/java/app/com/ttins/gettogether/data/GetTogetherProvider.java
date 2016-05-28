package app.com.ttins.gettogether.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class GetTogetherProvider extends ContentProvider {

    public static final String LOG_TAG = GetTogetherProvider.class.getSimpleName();

    private static final int EVENTS = 100;
    private static final int EVENTS_ID = 101;
    private static final int GUESTS = 102;
    private static final int GUESTS_ID = 103;

    private GetTogetherDBHelper getTogetherDBHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        String content = GetTogetherContract.CONTENT_AUTHORITY;
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, GetTogetherContract.Events.PATH_EVENTS, EVENTS);
        matcher.addURI(content, GetTogetherContract.Events.PATH_EVENTS + "/#", EVENTS_ID);
        matcher.addURI(content, GetTogetherContract.Guests.PATH_GUESTS, GUESTS);
        matcher.addURI(content, GetTogetherContract.Guests.PATH_GUESTS + "/#", GUESTS_ID);

        return matcher;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)) {
            case EVENTS:
                return GetTogetherContract.Events.CONTENT_TYPE;
            case EVENTS_ID:
                return GetTogetherContract.Events.CONTENT_ITEM_TYPE;
            case GUESTS:
                return GetTogetherContract.Guests.CONTENT_TYPE;
            case GUESTS_ID:
                return GetTogetherContract.Guests.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = getTogetherDBHelper.getWritableDatabase();
        int rows;

        switch(uriMatcher.match(uri)) {
            case EVENTS:
            case EVENTS_ID:
                rows = db.update(GetTogetherContract.Events.TABLE_EVENTS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case GUESTS:
            case GUESTS_ID:
                rows = db.update(GetTogetherContract.Guests.TABLE_GUESTS,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }

        return rows;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        SQLiteDatabase db = getTogetherDBHelper.getWritableDatabase();
        long id;
        Uri retUri;

        Log.d(LOG_TAG, "UriMatcher = " + uriMatcher.match(uri));

        switch (uriMatcher.match(uri)) {
            case EVENTS:
               id = db.insert(GetTogetherContract.Events.TABLE_EVENTS,
                       null,
                       values);

                if (id > 0) {
                    retUri = GetTogetherContract.Events.buildEventsUri(id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }

                break;
            case GUESTS:
                id = db.insert(GetTogetherContract.Guests.TABLE_GUESTS,
                        null,
                        values);

                if (id > 0) {
                    retUri = GetTogetherContract.Guests.buildGuestsUri(id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        return uri;
    }

    @Override
    public boolean onCreate() {
        getTogetherDBHelper = GetTogetherDBHelper.getInstance(getContext());
        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = getTogetherDBHelper.getWritableDatabase();
        int rows;

        switch (uriMatcher.match(uri)) {
            case EVENTS:
                rows = db.delete(GetTogetherContract.Events.TABLE_EVENTS,
                        selection,
                        selectionArgs);

                break;
            case GUESTS:
                rows = db.delete(GetTogetherContract.Guests.TABLE_GUESTS,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        /*if (selection == null || rows == 0) {*/
            try {
                getContext().getContentResolver().notifyChange(uri, null);
                Log.d(LOG_TAG, "Delete command received");
            }
            catch (NullPointerException e) {
                Log.d(LOG_TAG, "Content Resolver is null");
            }
        /*}*/

        return rows;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = getTogetherDBHelper.getWritableDatabase();
        Cursor cursor;
        long id;

        switch (uriMatcher.match(uri)) {
            case EVENTS:
                cursor = db.query(GetTogetherContract.Events.TABLE_EVENTS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case EVENTS_ID:
                id = ContentUris.parseId(uri);
                cursor = db.query(GetTogetherContract.Events.TABLE_EVENTS,
                        projection,
                        GetTogetherContract.Events._ID + " = ?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        sortOrder);
                break;

            case GUESTS:
                cursor = db.query(GetTogetherContract.Guests.TABLE_GUESTS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case GUESTS_ID:
                id = ContentUris.parseId(uri);
                cursor = db.query(GetTogetherContract.Guests.TABLE_GUESTS,
                        projection,
                        GetTogetherContract.Guests._ID + " = ?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        try {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (NullPointerException e) {
            Log.d(LOG_TAG, "Content Resolver is null");
        }


        return cursor;
    }
}
