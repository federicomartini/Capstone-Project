package app.com.ttins.gettogether.eventguesthandler;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import java.lang.ref.WeakReference;

public class EventGuestHandlerPresenter implements EventGuestHandlerMVP.PresenterOps,
                                                    EventGuestHandlerMVP.RequestedPresenterOps {

    private static final String LOG_TAG = EventGuestHandlerPresenter.class.getSimpleName();

    WeakReference<EventGuestHandlerMVP.RequestedViewOps> view;
    EventGuestHandlerMVP.ModelOps model;

    public EventGuestHandlerPresenter(EventGuestHandlerMVP.RequestedViewOps view) {

        this.view = new WeakReference<>(view);
        this.model = new EventGuestHandlerModel(this);
    }

    @Override
    public void onInitLoader() {
        model.onInitLoader();
    }

    @Override
    public void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass) {
        view.get().onLoaderInitCompleted(loaderClass);
    }

    @Override
    public Context onContextViewRequired() {
        return view.get().onContextViewRequired();
    }

    @Override
    public void onLoadResults(Cursor cursor) {
        view.get().onLoadResults(cursor);

        Log.d(LOG_TAG, "onLoadFinished");

        if (!cursor.moveToFirst()) {
            Log.d(LOG_TAG, "RecyclerView is empty. Showing empty view");
            view.get().onShowEmptyRecyclerView();
        } else {
            Log.d(LOG_TAG, "Total Guests number: " + cursor.getCount());
            view.get().onShowRecyclerView();
        }

        view.get().onSetRecyclerViewAdapter();
    }
}
