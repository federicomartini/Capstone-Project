package app.com.ttins.gettogether.eventguesthandler;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;

public class EventGuestHandlerMVP {
    interface PresenterOps {
        void onInitLoader();
    }

    interface ModelOps {
        void onInitLoader();
    }

    interface RequestedPresenterOps {
        void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass);
        Context onContextViewRequired();
        void onLoadResults(Cursor cursor);
    }

    interface RequestedViewOps {
        void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass);
        Context onContextViewRequired();
        void onLoadResults(Cursor cursor);
        void onShowRecyclerView();
        void onShowEmptyRecyclerView();
        void onSetRecyclerViewAdapter();
    }

}
