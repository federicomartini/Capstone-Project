package app.com.ttins.gettogether.guestdetail;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.HashMap;

import app.com.ttins.gettogether.guestdetail.loader.GuestDetailLoader;

public class GuestDetailModel implements GuestDetailMVP.ModelOps, LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = GuestDetailModel.class.getSimpleName();

    GuestDetailMVP.RequestedPresenterOps presenter;
    HashMap<Integer, String> dataMap;
    long guestId;

    public GuestDetailModel(GuestDetailMVP.RequestedPresenterOps presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getGuestData(long id) {
        guestId = id;
    }

    @Override
    public void initLoader() {
        presenter.onLoaderInitCompleted(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return GuestDetailLoader.guestFromId(presenter.onContextViewRequired(), guestId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }

        if (cursor.moveToFirst()) {
            Log.d(LOG_TAG, "Columns: " + cursor.getColumnCount());
            dataMap.put(GuestDetailLoader.Query._ID, cursor.getString(GuestDetailLoader.Query._ID));
            dataMap.put(GuestDetailLoader.Query.NAME, cursor.getString(GuestDetailLoader.Query.NAME));
            dataMap.put(GuestDetailLoader.Query.PHONE_NUMBER, cursor.getString(GuestDetailLoader.Query.PHONE_NUMBER));
            dataMap.put(GuestDetailLoader.Query.ADDRESS, cursor.getString(GuestDetailLoader.Query.ADDRESS));
            //dataMap.put(GuestDetailLoader.Query.PHOTO_PATH, cursor.getString(GuestDetailLoader.Query.PHOTO_PATH));
        }

        presenter.onGuestLoadFinished(dataMap);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
