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
    }

    interface ModelOps {
        void getEventData(long id);
        void initLoader();
    }

    interface RequiredPresenterOps {
        void onEventLoadFinished(HashMap<Integer, String> eventDetailMap);
        Context onContextViewRequired();
        void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass);
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

    }
}