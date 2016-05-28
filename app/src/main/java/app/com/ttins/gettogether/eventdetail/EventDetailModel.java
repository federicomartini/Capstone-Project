package app.com.ttins.gettogether.eventdetail;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

import app.com.ttins.gettogether.common.gson.Guest;
import app.com.ttins.gettogether.common.gson.Guests;
import app.com.ttins.gettogether.data.GetTogetherContract;
import app.com.ttins.gettogether.eventdetail.loader.EventDetailLoader;

public class EventDetailModel implements EventDetailMVP.ModelOps, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EventDetailModel.class.getSimpleName();

    private static final int LOADER_EVENT_ALL_DETAILS = 1;
    private static final int LOADER_EVENT_GUEST_LIST_DETAIL = 10;

    HashMap<Integer, String> dataMap;
    EventDetailMVP.RequiredPresenterOps presenter;
    long eventId;
    private Context context;
    boolean isSaveGuestPending;
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
            dataMap.clear();
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

            Log.d(LOG_TAG, "onLoadFinished photoPath: " + cursor.getString(EventDetailLoader.Query.PHOTO_PATH));

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
        } else {
            Log.d(LOG_TAG, "onLoadFinished no first item");
        }

        switch(loader.getId()) {
            case LOADER_EVENT_ALL_DETAILS:
                /*Log.d(LOG_TAG, "LOADER_EVENT_ALL_DETAILS : ID = " +
                        cursor.getString(EventDetailLoader.Query._ID) +
                        " - GuestList: " + cursor.getString(EventDetailLoader.Query.GUEST_LIST));*/
                //presenter.guestListHandler(cursor.getString(EventDetailLoader.Query.GUEST_LIST));
                break;
            case LOADER_EVENT_GUEST_LIST_DETAIL:
                /*Log.d(LOG_TAG, "LOADER_EVENT_GUEST_LIST_DETAIL: ID = " +
                        cursor.getString(EventDetailLoader.Query._ID) +
                        " - GuestList: " + cursor.getString(EventDetailLoader.Query.GUEST_LIST));*/
                /*presenter.guestListHandler(guestListAddId,
                        eventId,
                        dataMap.get(EventDetailLoader.Query.GUEST_LIST));*/
                break;
            default:
                break;
        }

        Gson gson = new Gson();
        List<Guest> guestList = gson.fromJson(dataMap.get(EventDetailLoader.Query.GUEST_LIST),
                new TypeToken<List<Guest>>(){}.getType());
        Guests guests = new Guests();
        guests.setGuests(guestList);

        Log.d(LOG_TAG, "onLoadFinished - Guest List: " + dataMap.get(EventDetailLoader.Query.GUEST_LIST));
        presenter.onLoadFinished(dataMap.get(EventDetailLoader.Query.GUEST_LIST));
        //presenter.onEventLoadFinished(dataMap, guests);
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
        Log.d(LOG_TAG, "onEventAddGuestReceived - ID = " + id);
        presenter.onLoaderInitCompleted(this);
        //presenter.onRestartLoaderRequest(this, LOADER_EVENT_GUEST_LIST_DETAIL);
        //guestListAddId = id;
    }

    /*@Override
    public void onSaveGuestList(long eventId, String guestList) {
        Log.d(LOG_TAG, "Save new Guest on EVENT ID = " + eventId + " - GuestList: " + guestList);
        ContentValues values = new ContentValues();
        values.put(GetTogetherContract.Events.GUEST_LIST, guestList);
        int rows = 0;

        if (this.context != null) {
            isSaveGuestPending = false;
            //pendingGuestList = null;
            rows = context.getContentResolver().update(
                    GetTogetherContract.Events.buildEventsUri(eventId),
                    values,
                    GetTogetherContract.Events._ID + " = ? ",
                    new String[]{String.valueOf(eventId)}
            );
        } else {
            Log.d(LOG_TAG, "onSaveGuestList pending");
            Log.d(LOG_TAG, "onSaveGuestList PENDING: " + guestList);
            isSaveGuestPending = true;
            pendingEventId = eventId;
            pendingGuestList = guestList;
        }

        if (rows > 0) {
            Gson gson = new Gson();
            List<Guest> listOfGuest = gson.fromJson(guestList,
                    new TypeToken<List<Guest>>(){}.getType());
            Guests guests = new Guests();
            guests.setGuests(listOfGuest);

            presenter.onGuestListUpdated(guests);
        }

        //Log.d(LOG_TAG, "Rows: " + rows + " - Save guest list: " + guestList);
    }*/

    @Override
    public void onAttachContext(Context context) {
        this.context = context;

        if(pendingGuestList != null) {
            //onSaveGuestList(pendingEventId, pendingGuestList);
            onUpdateGuestList(pendingGuestList);
        }
    }

    @Override
    public void onDetachContext() {
        this.context = null;
    }

    @Override
    public void onGetGuestList() {

    }

    @Override
    public void onUpdateGuestList(String guestList) {
        Log.d(LOG_TAG, "onAddGuestToList Updating DB Event guest list: " + guestList);
        Log.d(LOG_TAG, "onAddGuestToList pendingGuestList = " + pendingGuestList);
        ContentValues values = new ContentValues();
        values.put(GetTogetherContract.Events.GUEST_LIST, guestList);

        if (this.context != null) {
            isSaveGuestPending = false;
            pendingGuestList = null;

            AsyncTaskUpdate asyncTaskUpdate = new AsyncTaskUpdate();
            asyncTaskUpdate.execute(guestList);

        } else {
            Log.d(LOG_TAG, "onAddGuestToList pending");
            //Log.d(LOG_TAG, "onSaveGuestList PENDING: " + guestList);
            isSaveGuestPending = true;
            pendingGuestList = guestList;
        }

        //presenter.onEventLoadFinished(dataMap, guests);
        //Log.d(LOG_TAG, "Rows: " + rows + " - Save guest list: " + guestList);

    }

    public class AsyncTaskUpdate extends AsyncTask<String, Void, Integer> {

        ContentValues values;

        @Override
        protected Integer doInBackground(String... params) {
            values = new ContentValues();
            values.put(GetTogetherContract.Events.GUEST_LIST, params[0]);

            return context.getContentResolver().update(
                    GetTogetherContract.Events.buildEventsUri(eventId),
                    values,
                    GetTogetherContract.Events._ID + " = ? ",
                    new String[]{String.valueOf(eventId)});
        }

        @Override
        protected void onPostExecute(Integer rows) {
            super.onPostExecute(rows);
            Log.d(LOG_TAG, "onAddGuestToList updated - rows updated: " + rows);

            Gson gson = new Gson();
            List<Guest> listOfGuest = gson
                    .fromJson(values.get(GetTogetherContract.Events.GUEST_LIST).toString(),
                    new TypeToken<List<Guest>>(){}.getType());
                    Guests guests = new Guests();
                    guests.setGuests(listOfGuest);

            Log.d(LOG_TAG, "onAddGuestToList: onPostExecute: Guest listupdated: " + values.get(GetTogetherContract.Events.GUEST_LIST).toString());
            initLoader();
            presenter.onGuestListUpdated(guests);

        }
    }

    @Override
    public void onGetDataForView(Guests guests) {
        Log.d(LOG_TAG, "onGetDataForView");
        presenter.onEventLoadFinished(dataMap, guests);
    }

}
