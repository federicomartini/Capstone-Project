package app.com.ttins.gettogether.eventguestremove;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;

import java.lang.ref.WeakReference;

public class EventGuestRemovePresenter implements EventGuestRemoveMVP.PresenterOps,
                                                    EventGuestRemoveMVP.RequestedPresenterOps {

    WeakReference<EventGuestRemoveMVP.RequestedViewOps> view;
    EventGuestRemoveMVP.ModelOps model;

    public EventGuestRemovePresenter(EventGuestRemoveMVP.RequestedViewOps view) {
        this.view = new WeakReference<>(view);
        this.model = new EventGuestRemoveModel(this);
    }

    @Override
    public void onInitLoader() {
        model.onInitLoader();
    }

    @Override
    public void onLoaderInitCompleted(LoaderManager.LoaderCallbacks loaderClass, int loaderId) {
        view.get().onLoaderInitCompleted(loaderClass, loaderId);
    }

    @Override
    public Context onContextViewRequired() {
        return view.get().onContextViewRequired();
    }

    @Override
    public void onLoadResults(Cursor cursor) {
        view.get().onLoadResults(cursor);

        if (!cursor.moveToFirst()) {
            view.get().onShowEmptyRecyclerView();
        } else {
            view.get().onShowRecyclerView();
        }

        view.get().onSetRecyclerViewAdapter();
    }

}
