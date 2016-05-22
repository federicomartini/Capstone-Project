package app.com.ttins.gettogether.eventdetail;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.HashMap;

import app.com.ttins.gettogether.data.GetTogetherContract;
import app.com.ttins.gettogether.eventdetail.loader.EventDetailLoader;

public class EventDetailModel implements EventDetailMVP.ModelOps, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EventDetailModel.class.getSimpleName();

    private static final int LOADER_EVENT_ALL_DETAILS = 1;
    private static final int LOADER_EVENT_GUEST_LIST_DETAIL = 10;

    HashMap<Integer, String> dataMap;
    EventDetailMVP.RequiredPresenterOps presenter;
    long eventId;
    long guestListAddId;
    private Context context;
    boolean isSaveGuestPending;
    long pendingEventId;
    String pendingGuestList;

    public EventDetailModel(EventDetailMVP.RequiredPresenterOps presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getEventData(long id) {

        eventId = id;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case LOADER_EVENT_ALL_DETAILS:
                return EventDetailLoader.eventDetailFromId(presenter.onContextViewRequired(), eventId);
            case LOADER_EVENT_GUEST_LIST_DETAIL:
                return EventDetailLoader.eventDetailFromId(presenter.onContextViewRequired(), eventId);
            default:
                break;
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }

        if (cursor.moveToFirst()) {
            dataMap.put(EventDetailLoader.Query._ID, cursor.getString(EventDetailLoader.Query._ID));
            dataMap.put(EventDetailLoader.Query.TITLE, cursor.getString(EventDetailLoader.Query.TITLE));
            dataMap.put(EventDetailLoader.Query.CONFIRMATION_STATUS, cursor.getString(EventDetailLoader.Query.CONFIRMATION_STATUS));
            dataMap.put(EventDetailLoader.Query.END_TIME_HOUR, cursor.getString(EventDetailLoader.Query.END_TIME_HOUR));
            dataMap.put(EventDetailLoader.Query.END_TIME_MINUTE, cursor.getString(EventDetailLoader.Query.END_TIME_MINUTE));
            dataMap.put(EventDetailLoader.Query.EVENT_DAY, cursor.getString(EventDetailLoader.Query.EVENT_DAY));
            dataMap.put(EventDetailLoader.Query.EVENT_MONTH, cursor.getString(EventDetailLoader.Query.EVENT_MONTH));
            dataMap.put(EventDetailLoader.Query.EVENT_TYPE, cursor.getString(EventDetailLoader.Query.EVENT_TYPE));
            dataMap.put(EventDetailLoader.Query.EVENT_YEAR, cursor.getString(EventDetailLoader.Query.EVENT_YEAR));
            dataMap.put(EventDetailLoader.Query.GUEST_LIST, cursor.getString(EventDetailLoader.Query.GUEST_LIST));
            dataMap.put(EventDetailLoader.Query.LOCATION, cursor.getString(EventDetailLoader.Query.LOCATION));
            dataMap.put(EventDetailLoader.Query.MEETING_LOCATION, cursor.getString(EventDetailLoader.Query.MEETING_LOCATION));
            dataMap.put(EventDetailLoader.Query.NOTES, cursor.getString(EventDetailLoader.Query.NOTES));
            dataMap.put(EventDetailLoader.Query.PHOTO_PATH, cursor.getString(EventDetailLoader.Query.PHOTO_PATH));
            dataMap.put(EventDetailLoader.Query.PLACE_NAME, cursor.getString(EventDetailLoader.Query.PLACE_NAME));
            dataMap.put(EventDetailLoader.Query.PLACE_PHONE_NUMBER, cursor.getString(EventDetailLoader.Query.PLACE_PHONE_NUMBER));
            dataMap.put(EventDetailLoader.Query.START_TIME_HOUR, cursor.getString(EventDetailLoader.Query.START_TIME_HOUR));
            dataMap.put(EventDetailLoader.Query.START_TIME_MINUTE, cursor.getString(EventDetailLoader.Query.START_TIME_MINUTE));

            Log.d(LOG_TAG, "_ID = " + dataMap.get(EventDetailLoader.Query._ID) + ", " +
                            "CONFIRMATION_STATUS = " + dataMap.get(EventDetailLoader.Query.CONFIRMATION_STATUS) + ", " +
                            "TITLE = " + dataMap.get(EventDetailLoader.Query.TITLE) + ", " +
                            "END_TIME_HOUR = " + dataMap.get(EventDetailLoader.Query.END_TIME_HOUR) + ", " +
                            "END_TIME_MINUTE = " + dataMap.get(EventDetailLoader.Query.END_TIME_MINUTE) + ", " +
                            "EVENT_DAY = " + dataMap.get(EventDetailLoader.Query.EVENT_DAY) + ", " +
                            "EVENT_MONTH = " + dataMap.get(EventDetailLoader.Query.EVENT_MONTH) + ", " +
                            "EVENT_TYPE = " + dataMap.get(EventDetailLoader.Query.EVENT_TYPE) + ", " +
                            "EVENT_YEAR = " + dataMap.get(EventDetailLoader.Query.EVENT_YEAR) + ", " +
                            "GUEST_LIST = " + dataMap.get(EventDetailLoader.Query.GUEST_LIST) + ", " +
                            "LOCATION = " + dataMap.get(EventDetailLoader.Query.LOCATION) + ", " +
                            "MEETING_LOCATION = " + dataMap.get(EventDetailLoader.Query.MEETING_LOCATION) + ", " +
                            "NOTES = " + dataMap.get(EventDetailLoader.Query.NOTES) + ", " +
                            "PHOTO_PATH = " + dataMap.get(EventDetailLoader.Query.PHOTO_PATH) + ", " +
                            "PLACE_NAME = " + dataMap.get(EventDetailLoader.Query.PLACE_NAME) + ", " +
                            "PLACE_PHONE_NUMBER = " + dataMap.get(EventDetailLoader.Query.PLACE_PHONE_NUMBER) + ", " +
                            "START_TIME_HOUR = " + dataMap.get(EventDetailLoader.Query.START_TIME_HOUR) + ", " +
                            "START_TIME_MINUTE = " + dataMap.get(EventDetailLoader.Query.START_TIME_MINUTE)
            );
        }

        switch(loader.getId()) {
            case LOADER_EVENT_ALL_DETAILS:
                Log.d(LOG_TAG, "LOADER_EVENT_ALL_DETAILS");
                presenter.guestListHandler(cursor.getString(EventDetailLoader.Query.GUEST_LIST));
                break;
            case LOADER_EVENT_GUEST_LIST_DETAIL:
                Log.d(LOG_TAG, "LOADER_EVENT_GUEST_LIST_DETAIL: ID = " + dataMap.get(EventDetailLoader.Query._ID));
                presenter.guestListHandler(guestListAddId,
                        guestListAddId,
                        cursor.getString(EventDetailLoader.Query.GUEST_LIST));
                break;
            default:
                break;
        }

        presenter.onEventLoadFinished(dataMap);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void initLoader() {
        presenter.onLoaderInitCompleted(this);
    }

    @Override
    public void onEventAddGuestReceived(long id) {
        Log.d(LOG_TAG, "onEventAddGuestReceived");
        presenter.onRestartLoaderRequest(this, LOADER_EVENT_GUEST_LIST_DETAIL);
        guestListAddId = id;
    }

    @Override
    public void onSaveGuestList(long eventId, String guestList) {
        ContentValues values = new ContentValues();
        values.put(GetTogetherContract.Events.GUEST_LIST, guestList);
        int rows = 0;

        if (this.context != null) {
            Log.d(LOG_TAG, "onSaveGuestList: " + guestList);
            isSaveGuestPending = false;
            rows = context.getContentResolver().update(
                    GetTogetherContract.Events.buildEventsUri(eventId),
                    values,
                    GetTogetherContract.Events._ID + " = ? ",
                    new String[]{String.valueOf(eventId)}
            );
        } else {
            Log.d(LOG_TAG, "onSaveGuestList pending");
            isSaveGuestPending = true;
            pendingEventId = eventId;
            pendingGuestList = guestList;
        }

        Log.d(LOG_TAG, "Rows:" + rows + "Save guest list: " + guestList);
    }

    @Override
    public void onAttachContext(Context context) {
        this.context = context;

        if(isSaveGuestPending) {
            onSaveGuestList(pendingEventId, pendingGuestList);
        }
    }

    @Override
    public void onDetachContext() {
        this.context = null;
    }

    @Override
    public void onGetGuestList() {

    }
}
