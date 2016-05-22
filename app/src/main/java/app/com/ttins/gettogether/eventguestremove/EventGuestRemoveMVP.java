package app.com.ttins.gettogether.eventguestremove;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;

public class EventGuestRemoveMVP {
    interface PresenterOps {
        void onInitLoader();
    }

    interface ModelOps {
        void onInitLoader();
    }

    interface RequestedPresenterOps {
        void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass, int loaderId);
        Context onContextViewRequired();
        void onLoadResults(Cursor cursor);
    }

    interface RequestedViewOps {
        void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass, int loaderId);
        Context onContextViewRequired();
        void onLoadResults(Cursor cursor);
        void onShowEmptyRecyclerView();
        void onShowRecyclerView();
        void onSetRecyclerViewAdapter();
    }
}
