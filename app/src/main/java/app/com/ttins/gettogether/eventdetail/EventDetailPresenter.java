package app.com.ttins.gettogether.eventdetail;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

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

    public EventDetailPresenter(EventDetailMVP.RequiredViewOps view) {
        model = new EventDetailModel(this);
    }

    public void onAttachView(EventDetailMVP.RequiredViewOps view) {
            this.view = new WeakReference<>(view);

        if (addGuestRequestPending) {
            Log.d(LOG_TAG, "onAttachView pendingRequest");
            this.view.get().onRestartLoaderRequest(this.loader);
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
    public void onRestartLoaderRequest(LoaderManager.LoaderCallbacks<Cursor> loaderClass) {
        if (view != null) {
            view.get().onRestartLoaderRequest(loaderClass);
        } else {
            Log.d(LOG_TAG, "onRestartLoaderRequest: view is null");
            addGuestRequestPending = true;
            this.loader = loaderClass;
        }

    }
}
