package app.com.ttins.gettogether.eventdetail;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.com.ttins.gettogether.eventdetail.gson.Guest;
import app.com.ttins.gettogether.eventdetail.gson.Guests;
import app.com.ttins.gettogether.eventdetail.loader.EventDetailLoader;


public class EventDetailPresenter implements EventDetailMVP.PresenterOps,
                                                EventDetailMVP.RequiredPresenterOps {

    private static final String LOG_TAG = EventDetailPresenter.class.getSimpleName();

    private static final String EVENT_STATUS_CONFIRMED = "YES";
    private static final String EVENT_STATUS_NOT_CONFIRMED = "NO";

    EventDetailMVP.ModelOps model;
    WeakReference<EventDetailMVP.RequiredViewOps> view;
    HashMap<Integer, String> eventDataMap;
    boolean confirmButtonStatus = false;
    boolean addGuestRequestPending = false;
    LoaderManager.LoaderCallbacks<Cursor> loader;
    int loaderId;

    public EventDetailPresenter(EventDetailMVP.RequiredViewOps view) {
        model = new EventDetailModel(this);
    }

    public void onAttachView(EventDetailMVP.RequiredViewOps view) {
            this.view = new WeakReference<>(view);

        if (addGuestRequestPending) {
            Log.d(LOG_TAG, "onAttachView pendingRequest");
            this.view.get().onRestartLoaderRequest(this.loader, this.loaderId);
            addGuestRequestPending = false;
            this.loader = null;
        }
    }

    @Override
    public void onDetachView() {
        this.view = null;
    }

    @Override
    public void onPopulateDetailView(long id) {
        model.getEventData(id);
    }


    @Override
    public void onEventLoadFinished(HashMap<Integer, String> eventDetailMap) {

        eventDataMap = eventDetailMap;

        if (view != null) {
            view.get().onChangeEventTitle(this.eventDataMap.get(EventDetailLoader.Query.TITLE));
            view.get().onChangeEventDuration(this.eventDataMap.get(EventDetailLoader.Query.TITLE));
            view.get().onChangeMeetLocation(this.eventDataMap.get(EventDetailLoader.Query.MEETING_LOCATION));
            view.get().onChangeNotes(this.eventDataMap.get(EventDetailLoader.Query.NOTES));
            view.get().onChangePhoneNumber(this.eventDataMap.get(EventDetailLoader.Query.PLACE_PHONE_NUMBER));
            view.get().onChangeStartTimeText(this.eventDataMap.get(EventDetailLoader.Query.START_TIME_HOUR));


            /*if (this.eventDataMap.get(EventDetailLoader.Query.CONFIRMATION_STATUS).compareTo(EVENT_STATUS_CONFIRMED) == 0) {
                confirmButtonStatus = true;
            } else {
                confirmButtonStatus = false;
            }*/
        }
    }

    @Override
    public Context onContextViewRequired() {
        if (view != null) {
            return view.get().onContextViewRequired();
        }
        return null;
    }

    @Override
    public void initLoader() {
        model.initLoader();
    }

    @Override
    public void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass) {
        view.get().onLoaderInitCompleted(loaderClass);
    }

    @Override
    public void onConfirmButtonClick() {

    }

    @Override
    public void onEditItemClick() {
        view.get().onSendDataForEditDetailsView();
    }

    @Override
    public void onEventAddGuestReceived(long id) {
        model.onEventAddGuestReceived(id);
    }

    @Override
    public void onRestartLoaderRequest(LoaderManager.LoaderCallbacks<Cursor> loaderClass, int loaderId) {
        if (view != null) {
            view.get().onRestartLoaderRequest(loaderClass, loaderId);
        } else {
            Log.d(LOG_TAG, "onRestartLoaderRequest: view is null");
            addGuestRequestPending = true;
            this.loader = loaderClass;
            this.loaderId = loaderId;
        }

    }

    @Override
    public void guestListHandler(long guestId, long eventId, String guestList) {
        Log.d(LOG_TAG, "guestListHandler");
        Gson gson = new Gson();

        if (guestList == null || guestList.isEmpty()) {
            List<Guest> listGuest = new ArrayList<>();
            listGuest.add(new Guest(guestId, ""));
            /*listGuest.add(new Guest(Long.getLong("8"), "pizza"));
            listGuest.add(new Guest(Long.getLong("10"), "pasta"));
            listGuest.add(new Guest(Long.getLong("12"), "panino"));*/
            String idJsonList = gson.toJson(listGuest);
            Log.d(LOG_TAG, "Json: " + idJsonList);

            model.onSaveGuestList(eventId, idJsonList);

        } else {
            //Guests listGuest = gson.fromJson(guestList, Guests.class);
            Log.d(LOG_TAG, "Guest List: " + guestList);
        }
    }

    @Override
    public void onAttachContext(Context context) {
        model.onAttachContext(context);
    }

    @Override
    public void onDetachContext() {
        model.onDetachContext();
    }
}
