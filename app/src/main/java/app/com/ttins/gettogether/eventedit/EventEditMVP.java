package app.com.ttins.gettogether.eventedit;


import android.content.Context;
import android.support.v4.app.LoaderManager;

import java.util.HashMap;

public interface EventEditMVP {

    interface PresenterOps {
        void saveEvent(HashMap<Integer, String> dataMap);
        void saveEvent(Long eventId, HashMap<Integer, String> dataMap);
        void onAttachView(Context context);
        void onDetachView();
        void initEventEditLoader(long id);
        void onStartTimeTextClick();
        void onEndTimeTextClick();
        void onStartDateTextClick();
        void onEndDateTextClick();
        void onUpdateDateTimeFromDialog(String dialogTag, String message);
        void onLocationClick();
        void onPlaceReceived(String placeName);
        void onEventPhotoIconClick();
        void onNewEventReceived();
        void onEditEventReceived(long id);
    }

    interface ModelOps {
        void onAttachView(Context context);
        void onDetachView();
        void saveEventData(HashMap<Integer, String> dataMap);
        void saveEventData(Long eventId, HashMap<Integer, String> dataMap);
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
        void onEventEdited(long id);
        void onLoadInitReady(LoaderManager.LoaderCallbacks loaderClass);
        Context onContextViewRequired();
        void onChangeEventTitle(String eventTitle);
        void onChangeMeetLocation(String meetLocation);
        void onChangeNotes(String notes);
        void onChangePhoneNumber(String phoneNumber);
        void onChangeStartTimeText(String startTime);
        void onChangeLocationText(String location);
        void onChangeStartDateText(String startDate);
        void onChangeEventPhoto(String photoUri);
        void onShowSetTimeDialog(String dialogTag);
        void onShowSetDateDialog(String dialogTag);
        void onSetStartTime(String message);
        void onSetStartDate(String message);
        void onShowPlaceView();
        void onShowLocation(String placeName);
        void onShowGalleryForPicture();
    }

}
