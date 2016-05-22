package app.com.ttins.gettogether.eventdetail;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;

import java.util.HashMap;

import app.com.ttins.gettogether.common.gson.Guests;


public interface EventDetailMVP {
    interface PresenterOps {
        void onAttachView(EventDetailMVP.RequiredViewOps view);
        void onDetachView();
        void onPopulateDetailView(long id);
        void initLoader();
        void onConfirmButtonClick();
        void onEditItemClick();
        void onEventAddGuestReceived(long id);
        void onAttachContext(Context context);
        void onDetachContext();
        void populateGuestListView();
    }

    interface ModelOps {
        void onAttachContext(Context context);
        void onDetachContext();
        void getEventData(long id);
        void initLoader();
        void onEventAddGuestReceived(long id);
        void onSaveGuestList(long eventId, String guestList);
        void onGetGuestList();
    }

    interface RequiredPresenterOps {
        void onEventLoadFinished(HashMap<Integer, String> eventDetailMap, Guests guests);
        Context onContextViewRequired();
        void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass);
        void onRestartLoaderRequest(LoaderManager.LoaderCallbacks<Cursor> loaderClass, int loaderId);
        void guestListHandler(long guestId, long eventId, String guestList);
        void guestListHandler(String guestList);
        void onDestroyLoader(int loaderId);
    }

    interface RequiredViewOps {
        void onChangeEventTitle(String eventTitle);
        void onChangeStartTimeText(String startTime);
        void onChangeMeetLocation(String meetLocation);
        void onChangeEventDuration(String eventDuration);
        void onChangePhoneNumber(String phoneNumber);
        void onChangeConfirmStatus(String confirmStatus);
        void onChangeEmptyGuestList();
        void onChangeNotes(String notes);
        void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass);
        Context onContextViewRequired();
        void onSendDataForEditDetailsView();
        void onRestartLoaderRequest(LoaderManager.LoaderCallbacks<Cursor> loaderClass, int loaderId);
        void onDestroyLoader(int loaderId);
        void onShowEmptyRecyclerView();
        void onShowRecyclerView();
        void onSetRecyclerViewAdapter();
        void onLoadFinished(Guests guests);
        void onResetViewAdapter();
    }
}