package app.com.ttins.gettogether.eventedit;


import android.content.Context;

import java.lang.ref.WeakReference;

public class EventEditPresenter implements EventEditMVP.PresenterOps, EventEditMVP.RequiredPresenterOps {

    private static final int INSERT_DATA_NO_ERROR = 0;
    private static final int INSERT_DATA_TITLE_EMPTY_ERROR = 1;
    private static final int INSERT_DATA_LOCATION_EMPTY_ERROR = 2;

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
        int retVal = 0;

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
}
