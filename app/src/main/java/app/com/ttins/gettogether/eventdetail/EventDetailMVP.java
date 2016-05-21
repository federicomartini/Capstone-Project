package app.com.ttins.gettogether.eventdetail;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;

import java.util.HashMap;


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
    }

    interface ModelOps {
        void onAttachContext(Context context);
        void onDetachContext();
        void getEventData(long id);
        void initLoader();
        void onEventAddGuestReceived(long id);
        void onSaveGuestList(long eventId, String guestList);
    }

    interface RequiredPresenterOps {
        void onEventLoadFinished(HashMap<Integer, String> eventDetailMap);
        Context onContextViewRequired();
        void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass);
        void onRestartLoaderRequest(LoaderManager.LoaderCallbacks<Cursor> loaderClass, int loaderId);
        void guestListHandler(long guestId, long eventId, String guestList);
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

    }
}