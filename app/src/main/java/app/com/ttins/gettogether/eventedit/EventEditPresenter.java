package app.com.ttins.gettogether.eventedit;


import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;

import app.com.ttins.gettogether.common.utils.DateTimeFormat;
import app.com.ttins.gettogether.eventedit.loader.EventEditLoader;

public class EventEditPresenter implements EventEditMVP.PresenterOps, EventEditMVP.RequiredPresenterOps {

    private static final String LOG_TAG = EventEditPresenter.class.getSimpleName();

    private static final int INSERT_DATA_NO_ERROR = 0;
    private static final int INSERT_DATA_TITLE_EMPTY_ERROR = 1;
    private static final int INSERT_DATA_LOCATION_EMPTY_ERROR = 2;
    private static final int INSERT_DATA_START_DATE_EMPTY_ERROR = 3;

    public static final String START_DATE_DIALOG_TAG = "START_DATE_DIALOG_TAG";
    public static final String END_DATE_DIALOG_TAG = "END_DATE_DIALOG_TAG";
    public static final String START_TIME_DIALOG_TAG = "START_TIME_PICKER";
    public static final String END_TIME_DIALOG_TAG = "END_TIME_DIALOG_TAG";

    private WeakReference<EventEditMVP.RequiredViewOps> view;
    private EventEditMVP.ModelOps model;
    private Context viewContext;

    private String startTimePending = null;
    private String endTimePending;
    private String toolbarPhotoPending;

    public EventEditPresenter(EventEditMVP.RequiredViewOps view) {
        this.view = new WeakReference<>(view);
        this.model = new EventEditModel(this);

    }

    @Override
    public void saveEvent(HashMap<Integer, String> dataMap) {
        int retCheck;

        retCheck = areAllEventDataOk(dataMap);
        showToastOnCheckError(retCheck);

        if (retCheck == INSERT_DATA_NO_ERROR) {
            model.saveEventData(dataMap);
        }
    }

    @Override
    public void saveEvent(Long eventId, HashMap<Integer, String> dataMap) {
        int retCheck;

        retCheck = areAllEventDataOk(dataMap);
        showToastOnCheckError(retCheck);

        if (retCheck == INSERT_DATA_NO_ERROR) {
            model.saveEventData(eventId, dataMap);
        }
    }


    @Override
    public void onAttachView(Context context) {
        this.viewContext = context;
        model.onAttachView(context);

        if (startTimePending != null) {
            view.get().onChangeStartTimeText(startTimePending);
            startTimePending = null;
        }


    }

    @Override
    public void onDetachView() {
        this.viewContext = null;
        model.onDetachView();
    }

    private void showToastOnCheckError(int retCheck) {
        if (retCheck > 0) {
            switch(retCheck) {
                case INSERT_DATA_TITLE_EMPTY_ERROR:
                    view.get().onShowToast("Event Title field can't be empty");
                    break;
                case INSERT_DATA_LOCATION_EMPTY_ERROR:
                    view.get().onShowToast("Event Location field can't be empty");
                    break;
                case INSERT_DATA_START_DATE_EMPTY_ERROR:
                    view.get().onShowToast("Event Start Date field can't be empty");
                    break;
                default:
                    view.get().onShowToast("Error while inserting a new event");
                    break;
            }
        }
    }


    private int areAllEventDataOk(HashMap<Integer, String> dataMap) {

        if (dataMap.get(EventEditLoader.Query.TITLE).isEmpty()) {
            return INSERT_DATA_TITLE_EMPTY_ERROR;
        } else if (dataMap.get(EventEditLoader.Query.LOCATION).isEmpty()) {
            return INSERT_DATA_LOCATION_EMPTY_ERROR;
        } else if (dataMap.get(EventEditLoader.Query.EVENT_DAY).isEmpty()) {
            return INSERT_DATA_START_DATE_EMPTY_ERROR;
        }

        return 0;
    }

    @Override
    public void onEventSaved() {
        view.get().onEventSaved();
    }

    @Override
    public void initEventEditLoader(long id) {
        model.initEventEditLoader(id);
    }

    @Override
    public void onLoadInitReady(LoaderManager.LoaderCallbacks loaderClass) {
        view.get().onLoadInitReady(loaderClass);
    }

    @Override
    public Context onContextViewRequired() {
        if (view != null) {
            return view.get().onContextViewRequired();
        }
        return null;
    }

    @Override
    public void onEventLoaderFinished(HashMap<Integer, String> dataMap) {
        Log.d(LOG_TAG, "onEventLoadFinished");

        if (view != null) {
            view.get().onChangeEventTitle(dataMap.get(EventEditLoader.Query.TITLE));
            view.get().onChangeLocationText(dataMap.get(EventEditLoader.Query.LOCATION));
            view.get().onChangeMeetLocation(dataMap.get(EventEditLoader.Query.MEETING_LOCATION));
            view.get().onChangeNotes(dataMap.get(EventEditLoader.Query.NOTES));
            view.get().onChangePhoneNumber(dataMap.get(EventEditLoader.Query.PLACE_PHONE_NUMBER));

            String startTime = String.format(Locale.getDefault(), "%s:%s",
                    dataMap.get(EventEditLoader.Query.START_TIME_HOUR),
                    dataMap.get(EventEditLoader.Query.START_TIME_MINUTE));

            Log.d(LOG_TAG, "StartTime: " + startTime);

            String endTime = String.format(Locale.getDefault(), "%s:%s",
                    dataMap.get(EventEditLoader.Query.END_TIME_HOUR),
                    dataMap.get(EventEditLoader.Query.END_TIME_MINUTE));

            if (startTime.length() > 0) {
                if (view != null) {
                    view.get().onChangeStartTimeText(startTime);
                    startTimePending = null;
                } else {
                    startTimePending = startTime;
                }

            }


            String startDate = DateTimeFormat.convertDate(dataMap.get(EventEditLoader.Query.EVENT_DAY),
                    dataMap.get(EventEditLoader.Query.EVENT_MONTH),
                    dataMap.get(EventEditLoader.Query.EVENT_YEAR));

            if (startDate.length() > 0) {
                view.get().onChangeStartDateText(startDate);
            }

            String photoUri = dataMap.get(EventEditLoader.Query.PHOTO_PATH);
            if ( photoUri != null && photoUri.length() > 0) {
                view.get().onChangeEventPhoto(photoUri);
            }

        }
    }

    @Override
    public void onStartTimeTextClick() {
        view.get().onShowSetTimeDialog(START_TIME_DIALOG_TAG);
    }

    @Override
    public void onEndTimeTextClick() {
        view.get().onShowSetTimeDialog(END_TIME_DIALOG_TAG);
    }

    @Override
    public void onStartDateTextClick() {
        view.get().onShowSetDateDialog(START_DATE_DIALOG_TAG);
    }

    @Override
    public void onEndDateTextClick() {
        view.get().onShowSetDateDialog(END_DATE_DIALOG_TAG);
    }

    @Override
    public void onUpdateDateTimeFromDialog(String dialogTag, String message) {
        if (dialogTag.compareTo(START_TIME_DIALOG_TAG) == 0) {
            view.get().onSetStartTime(message);
        } else if (dialogTag.compareTo(END_TIME_DIALOG_TAG) == 0) {
            view.get().onSetEndTime(message);
        } else if (dialogTag.compareTo(START_DATE_DIALOG_TAG) == 0) {
            view.get().onSetStartDate(message);
        } else if (dialogTag.compareTo(END_DATE_DIALOG_TAG) == 0) {
            view.get().onSetEndDate(message);
        } else {
            Log.d(LOG_TAG, "Update Date Time - Unknown tag");
        }
    }

    @Override
    public void onLocationClick() {
        view.get().onShowPlaceView();
    }

    @Override
    public void onPlaceReceived(String placeName) {
        view.get().onShowLocation(placeName);
    }

    @Override
    public void onEventPhotoIconClick() {
        if (view != null) {
            view.get().onShowGalleryForPicture();
        }
    }
}
