package app.com.ttins.gettogether.eventedit;


import android.content.Context;
import android.support.v4.app.LoaderManager;

import java.util.HashMap;

public interface EventEditMVP {

    interface PresenterOps {
        void saveEvent(String title, String location, String meetingLocation, String phone);
        void saveEvent(Long eventId, String title, String location, String meetingLocation, String phone);
        void onAttachView(Context context);
        void onDetachView();
        void initEventEditLoader(long id);
        void onStartTimeTextClick();
        void onEndTimeTextClick();
        void onStartDateTextClick();
        void onEndDateTextClick();
        void onUpdateDateTimeFromDialog(String dialogTag, String message);
    }

    interface ModelOps {
        void onAttachView(Context context);
        void onDetachView();
        void saveEventData(String title, String location, String meetingLocation, String phone);
        void saveEventData(Long eventId, String title, String location, String meetingLocation, String phone);
        void initEventEditLoader(long id);
    }

    interface RequiredPresenterOps {
        void onEventSaved();
        void onLoadInitReady(LoaderManager.LoaderCallbacks loaderClass);
        Context onContextViewRequired();
        void onEventLoaderFinished(HashMap<Integer, String> dataMap);
    }

    interface RequiredViewOps {
        void onShowToast(String message);
        void onEventSaved();
        void onLoadInitReady(LoaderManager.LoaderCallbacks loaderClass);
        Context onContextViewRequired();
        void onChangeEventTitle(String eventTitle);
        void onChangeMeetLocation(String meetLocation);
        void onChangeNotes(String notes);
        void onChangePhoneNumber(String phoneNumber);
        void onChangeStartTimeText(String startTime);
        void onChangeLocationText(String location);
        void onShowSetTimeDialog(String dialogTag);
        void onShowSetDateDialog(String dialogTag);
        void onSetStartTime(String message);
        void onSetEndTime(String message);
        void onSetStartDate(String message);
        void onSetEndDate(String message);
    }

}
