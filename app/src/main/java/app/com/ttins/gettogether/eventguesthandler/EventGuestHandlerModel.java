package app.com.ttins.gettogether.eventguesthandler;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import app.com.ttins.gettogether.eventguesthandler.loader.EventGuestHandlerLoader;

public class EventGuestHandlerModel implements EventGuestHandlerMVP.ModelOps, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EventGuestHandlerModel.class.getSimpleName();
    EventGuestHandlerMVP.RequestedPresenterOps presenter;

    public EventGuestHandlerModel(EventGuestHandlerMVP.RequestedPresenterOps presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onInitLoader() {
        presenter.onLoaderInitCompleted(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return EventGuestHandlerLoader.allGuests(presenter.onContextViewRequired());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        presenter.onLoadResults(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
