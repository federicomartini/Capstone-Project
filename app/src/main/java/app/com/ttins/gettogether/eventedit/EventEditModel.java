package app.com.ttins.gettogether.eventedit;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.HashMap;

import app.com.ttins.gettogether.data.GetTogetherContract;
import app.com.ttins.gettogether.eventedit.loader.EventEditLoader;

public class EventEditModel implements EventEditMVP.ModelOps, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG =EventEditModel.class.getSimpleName();

    EventEditMVP.RequiredPresenterOps presenter;
    Context viewContext;
    long eventEditDetailId;

    public EventEditModel(EventEditMVP.RequiredPresenterOps presenter) {
        this.presenter = presenter;
    }

    @Override
    public void saveEventData(HashMap<Integer, String> dataMap) {

        Uri retUri;
        ContentValues values = new ContentValues();

        if (viewContext != null) {
            values.put(GetTogetherContract.Events.TITLE, dataMap.get(EventEditLoader.Query.TITLE));
            values.put(GetTogetherContract.Events.LOCATION, dataMap.get(EventEditLoader.Query.LOCATION));
            Log.d(LOG_TAG, "saveEventData Location: " + dataMap.get(EventEditLoader.Query.LOCATION));
            values.put(GetTogetherContract.Events.MEETING_LOCATION, dataMap.get(EventEditLoader.Query.MEETING_LOCATION));
            values.put(GetTogetherContract.Events.PLACE_PHONE_NUMBER, dataMap.get(EventEditLoader.Query.PLACE_PHONE_NUMBER));
            values.put(GetTogetherContract.Events.START_TIME_HOUR, dataMap.get(EventEditLoader.Query.START_TIME_HOUR));
            values.put(GetTogetherContract.Events.START_TIME_MINUTE, dataMap.get(EventEditLoader.Query.START_TIME_MINUTE));
            values.put(GetTogetherContract.Events.END_TIME_HOUR, dataMap.get(EventEditLoader.Query.END_TIME_HOUR));
            values.put(GetTogetherContract.Events.END_TIME_MINUTE, dataMap.get(EventEditLoader.Query.END_TIME_MINUTE));
            values.put(GetTogetherContract.Events.EVENT_DAY, dataMap.get(EventEditLoader.Query.EVENT_DAY));
            values.put(GetTogetherContract.Events.EVENT_MONTH, dataMap.get(EventEditLoader.Query.EVENT_MONTH));
            values.put(GetTogetherContract.Events.EVENT_YEAR, dataMap.get(EventEditLoader.Query.EVENT_YEAR));
            values.put(GetTogetherContract.Events.NOTES, dataMap.get(EventEditLoader.Query.NOTES));
            values.put(GetTogetherContract.Events.PHOTO_PATH, dataMap.get(EventEditLoader.Query.PHOTO_PATH));
            Log.d(LOG_TAG, "saveEventData Photo Path: " + dataMap.get(EventEditLoader.Query.PHOTO_PATH));
            values.put(GetTogetherContract.Events.PLACE_NAME, dataMap.get(EventEditLoader.Query.PLACE_NAME));

            retUri = viewContext.getContentResolver().insert(GetTogetherContract.Events.CONTENT_URI, values);
            if (retUri != null) {
                Log.d(LOG_TAG, "Uri created: " + retUri.toString());
                presenter.onEventSaved();
            } else {
                Log.d(LOG_TAG, "Uri returned after insert is null! ");
            }

        }
    }


    @Override
    public void saveEventData(Long eventId, HashMap<Integer, String> dataMap) {

        int rows;
        ContentValues values = new ContentValues();

        if (viewContext != null) {
            values.put(GetTogetherContract.Events.TITLE, dataMap.get(EventEditLoader.Query.TITLE));
            values.put(GetTogetherContract.Events.LOCATION, dataMap.get(EventEditLoader.Query.LOCATION));
            Log.d(LOG_TAG, "saveEventData Location " + EventEditLoader.Query.LOCATION + ": " + dataMap.get(EventEditLoader.Query.LOCATION));
            values.put(GetTogetherContract.Events.MEETING_LOCATION, dataMap.get(EventEditLoader.Query.MEETING_LOCATION));
            values.put(GetTogetherContract.Events.PLACE_PHONE_NUMBER, dataMap.get(EventEditLoader.Query.PLACE_PHONE_NUMBER));
            values.put(GetTogetherContract.Events.START_TIME_HOUR, dataMap.get(EventEditLoader.Query.START_TIME_HOUR));
            values.put(GetTogetherContract.Events.START_TIME_MINUTE, dataMap.get(EventEditLoader.Query.START_TIME_MINUTE));
            values.put(GetTogetherContract.Events.END_TIME_HOUR, dataMap.get(EventEditLoader.Query.END_TIME_HOUR));
            values.put(GetTogetherContract.Events.END_TIME_MINUTE, dataMap.get(EventEditLoader.Query.END_TIME_MINUTE));
            values.put(GetTogetherContract.Events.EVENT_DAY, dataMap.get(EventEditLoader.Query.EVENT_DAY));
            values.put(GetTogetherContract.Events.EVENT_MONTH, dataMap.get(EventEditLoader.Query.EVENT_MONTH));
            values.put(GetTogetherContract.Events.EVENT_YEAR, dataMap.get(EventEditLoader.Query.EVENT_YEAR));
            values.put(GetTogetherContract.Events.NOTES, dataMap.get(EventEditLoader.Query.NOTES));

            String photoPath = dataMap.get(EventEditLoader.Query.PHOTO_PATH);
            if (photoPath != null && !photoPath.isEmpty()) {
                values.put(GetTogetherContract.Events.PHOTO_PATH, dataMap.get(EventEditLoader.Query.PHOTO_PATH));
            }
            Log.d(LOG_TAG, "saveEventData Photo Path: " + dataMap.get(EventEditLoader.Query.PHOTO_PATH));
            values.put(GetTogetherContract.Events.PLACE_NAME, dataMap.get(EventEditLoader.Query.PLACE_NAME));

            rows = viewContext.getContentResolver().update(GetTogetherContract.Events.CONTENT_URI,
                                                            values,
                                                            GetTogetherContract.Guests._ID + " = ?",
                                                            new String[]{String.valueOf(eventId)});

            if (rows != 0) {
                Log.d(LOG_TAG, "Rows created: " + rows);
                presenter.onEventSaved();
            } else {
                Log.d(LOG_TAG, "Uri returned after update is null! ");
            }
        }
    }

    @Override
    public void onAttachView(Context context) {
        this.viewContext = context;
    }

    @Override
    public void onDetachView() {
        this.viewContext = null;
    }

    @Override
    public void initEventEditLoader(long id) {
        eventEditDetailId = id;
        presenter.onLoadInitReady(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (presenter.onContextViewRequired() != null) {
            return EventEditLoader.eventEditItemFromId(presenter.onContextViewRequired(), eventEditDetailId);
        } else {
            Log.d(LOG_TAG, "onCreateLoader failed because of View is null!");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToFirst()) {
            HashMap<Integer, String> dataMap = new HashMap<>();

            dataMap.put(EventEditLoader.Query._ID, cursor.getString(EventEditLoader.Query._ID));
            dataMap.put(EventEditLoader.Query.TITLE, cursor.getString(EventEditLoader.Query.TITLE));
            dataMap.put(EventEditLoader.Query.CONFIRMATION_STATUS, cursor.getString(EventEditLoader.Query.CONFIRMATION_STATUS));
            dataMap.put(EventEditLoader.Query.END_TIME_HOUR, cursor.getString(EventEditLoader.Query.END_TIME_HOUR));
            dataMap.put(EventEditLoader.Query.END_TIME_MINUTE, cursor.getString(EventEditLoader.Query.END_TIME_MINUTE));
            dataMap.put(EventEditLoader.Query.EVENT_DAY, cursor.getString(EventEditLoader.Query.EVENT_DAY));
            dataMap.put(EventEditLoader.Query.EVENT_MONTH, cursor.getString(EventEditLoader.Query.EVENT_MONTH));
            dataMap.put(EventEditLoader.Query.EVENT_TYPE, cursor.getString(EventEditLoader.Query.EVENT_TYPE));
            dataMap.put(EventEditLoader.Query.EVENT_YEAR, cursor.getString(EventEditLoader.Query.EVENT_YEAR));
            dataMap.put(EventEditLoader.Query.GUEST_LIST, cursor.getString(EventEditLoader.Query.GUEST_LIST));
            dataMap.put(EventEditLoader.Query.LOCATION, cursor.getString(EventEditLoader.Query.LOCATION));
            Log.d(LOG_TAG, "onLoadFinished Location " + EventEditLoader.Query.LOCATION + ": " + cursor.getString(EventEditLoader.Query.LOCATION));
            dataMap.put(EventEditLoader.Query.MEETING_LOCATION, cursor.getString(EventEditLoader.Query.MEETING_LOCATION));
            dataMap.put(EventEditLoader.Query.NOTES, cursor.getString(EventEditLoader.Query.NOTES));
            dataMap.put(EventEditLoader.Query.PHOTO_PATH, cursor.getString(EventEditLoader.Query.PHOTO_PATH));
            Log.d(LOG_TAG, "onLoadFinished Photo Path: " + cursor.getString(EventEditLoader.Query.PHOTO_PATH));
            dataMap.put(EventEditLoader.Query.PLACE_NAME, cursor.getString(EventEditLoader.Query.PLACE_NAME));
            dataMap.put(EventEditLoader.Query.PLACE_PHONE_NUMBER, cursor.getString(EventEditLoader.Query.PLACE_PHONE_NUMBER));
            dataMap.put(EventEditLoader.Query.START_TIME_HOUR, cursor.getString(EventEditLoader.Query.START_TIME_HOUR));
            dataMap.put(EventEditLoader.Query.START_TIME_MINUTE, cursor.getString(EventEditLoader.Query.START_TIME_MINUTE));

            presenter.onEventLoaderFinished(dataMap);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
