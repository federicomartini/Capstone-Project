package app.com.ttins.gettogether.eventguestremove;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import app.com.ttins.gettogether.eventguestremove.loader.EventGuestRemoveLoader;

public class EventGuestRemoveModel implements EventGuestRemoveMVP.ModelOps, LoaderManager.LoaderCallbacks<Cursor> {

    EventGuestRemoveMVP.RequestedPresenterOps presenter;

    private static final int LOADER_EVENT_GUEST_REMOVE_ID = 20;

    public EventGuestRemoveModel(EventGuestRemoveMVP.RequestedPresenterOps presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onInitLoader() {
        presenter.onLoaderInitCompleted(this, LOADER_EVENT_GUEST_REMOVE_ID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return EventGuestRemoveLoader.allGuests(presenter.onContextViewRequired());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        presenter.onLoadResults(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
