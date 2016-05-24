package app.com.ttins.gettogether.eventedit;


import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import app.com.ttins.gettogether.eventedit.loader.EventEditLoader;

public class EventEditPresenter implements EventEditMVP.PresenterOps, EventEditMVP.RequiredPresenterOps {

    private static final String LOG_TAG = EventEditView.class.getSimpleName();

    private static final int INSERT_DATA_NO_ERROR = 0;
    private static final int INSERT_DATA_TITLE_EMPTY_ERROR = 1;
    private static final int INSERT_DATA_LOCATION_EMPTY_ERROR = 2;

    public static final String START_DATE_DIALOG_TAG = "START_DATE_DIALOG_TAG";
    public static final String END_DATE_DIALOG_TAG = "END_DATE_DIALOG_TAG";
    public static final String START_TIME_DIALOG_TAG = "START_TIME_PICKER";
    public static final String END_TIME_DIALOG_TAG = "END_TIME_DIALOG_TAG";

    private WeakReference<EventEditMVP.RequiredViewOps> view;
    private EventEditMVP.ModelOps model;
    private Context viewContext;

    public EventEditPresenter(EventEditMVP.RequiredViewOps view) {
        this.view = new WeakReference<>(view);
        this.model = new EventEditModel(this);

    }

    @Override
    public void saveEvent(String title, String location, String meetingLocation, String phone) {
        int retCheck;

        retCheck = areAllEventDataOk(title, location, meetingLocation, phone);

        switch(retCheck) {
            case INSERT_DATA_TITLE_EMPTY_ERROR:
                view.get().onShowToast("Event Title field can't be empty");
                break;
            case INSERT_DATA_LOCATION_EMPTY_ERROR:
                view.get().onShowToast("Event Location field can't be empty");
                break;
            default:
                break;
        }

        if (retCheck == INSERT_DATA_NO_ERROR) {
            model.saveEventData(title, location, meetingLocation, phone);
        }
    }

    @Override
    public void saveEvent(Long eventId, String title, String location, String meetingLocation, String phone) {
        int retCheck;

        retCheck = areAllEventDataOk(title, location, meetingLocation, phone);

        switch(retCheck) {
            case INSERT_DATA_TITLE_EMPTY_ERROR:
                view.get().onShowToast("Event Title field can't be empty");
                break;
            case INSERT_DATA_LOCATION_EMPTY_ERROR:
                view.get().onShowToast("Event Location field can't be empty");
                break;
            default:
                break;
        }

        if (retCheck == INSERT_DATA_NO_ERROR) {
            model.saveEventData(eventId, title, location, meetingLocation, phone);
        }
    }

    @Override
    public void onAttachView(Context context) {
        this.viewContext = context;
        model.onAttachView(context);
    }

    @Override
    public void onDetachView() {
        this.viewContext = null;
        model.onDetachView();
    }

    private int areAllEventDataOk(String title, String location, String meetingLocation, String phone) {

        if (title.isEmpty()) {
            return INSERT_DATA_TITLE_EMPTY_ERROR;
        } else if (location.isEmpty()) {
            return INSERT_DATA_LOCATION_EMPTY_ERROR;
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

        HashMap<Integer, String> eventDataMap = dataMap;

        if (view != null) {
            view.get().onChangeEventTitle(eventDataMap.get(EventEditLoader.Query.TITLE));
            view.get().onChangeLocationText(eventDataMap.get(EventEditLoader.Query.LOCATION));
            view.get().onChangeMeetLocation(eventDataMap.get(EventEditLoader.Query.MEETING_LOCATION));
            view.get().onChangeNotes(eventDataMap.get(EventEditLoader.Query.NOTES));
            view.get().onChangePhoneNumber(eventDataMap.get(EventEditLoader.Query.PLACE_PHONE_NUMBER));
            view.get().onChangeStartTimeText(eventDataMap.get(EventEditLoader.Query.START_TIME_HOUR));
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
}
