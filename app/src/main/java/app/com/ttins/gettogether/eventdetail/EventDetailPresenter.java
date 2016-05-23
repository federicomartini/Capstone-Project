package app.com.ttins.gettogether.eventdetail;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.com.ttins.gettogether.common.gson.Guest;
import app.com.ttins.gettogether.common.gson.Guests;
import app.com.ttins.gettogether.eventdetail.loader.EventDetailLoader;


public class EventDetailPresenter implements EventDetailMVP.PresenterOps,
                                                EventDetailMVP.RequiredPresenterOps {

    private static final String LOG_TAG = EventDetailPresenter.class.getSimpleName();

    private static final String EVENT_STATUS_CONFIRMED = "YES";
    private static final String EVENT_STATUS_NOT_CONFIRMED = "NO";

    private static final long GUEST_ID_NULL = 0;

    EventDetailMVP.ModelOps model;
    WeakReference<EventDetailMVP.RequiredViewOps> view;
    HashMap<Integer, String> eventDataMap;
    boolean loaderPendingRequest = false;
    boolean confirmButtonStatus = false;
    boolean addGuestRequestPending = false;
    LoaderManager.LoaderCallbacks<Cursor> loader;
    int loaderId;
    long addGuestId;
    boolean pendingShowView = false;
    HashMap<Integer, String> pendingEventDetailMap;
    Guests pendingGuests;

    public EventDetailPresenter(EventDetailMVP.RequiredViewOps view) {
        if (this.model == null) {
            model = new EventDetailModel(this);
        }
    }

    public void onAttachView(EventDetailMVP.RequiredViewOps view) {
        Log.d(LOG_TAG, "Presenter onAttachView");
            this.view = new WeakReference<>(view);

        if (addGuestRequestPending) {
            Log.d(LOG_TAG, "onAttachView pendingRequest");
            this.view.get().onRestartLoaderRequest(this.loader, this.loaderId);
            addGuestRequestPending = false;
            this.loader = null;
        }

        if (loaderPendingRequest) {
            Log.d(LOG_TAG, "onAttachView loaderPendingRequest");
            this.view.get().onLoaderInitCompleted(loader);
            loaderPendingRequest = false;
        }

        if (pendingShowView) {
            onEventLoadFinished(pendingEventDetailMap, pendingGuests);
            loaderPendingRequest = false;
        }

        //this.view.get().onShowEmptyRecyclerView();
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
    public void onEventLoadFinished(HashMap<Integer, String> eventDetailMap, Guests guests) {
        Log.d(LOG_TAG, "onEventLoadFinished");
        eventDataMap = eventDetailMap;

        if (view != null) {
            view.get().onChangeEventTitle(this.eventDataMap.get(EventDetailLoader.Query.TITLE));
            view.get().onChangeEventDuration(this.eventDataMap.get(EventDetailLoader.Query.TITLE));
            view.get().onChangeMeetLocation(this.eventDataMap.get(EventDetailLoader.Query.MEETING_LOCATION));
            view.get().onChangeNotes(this.eventDataMap.get(EventDetailLoader.Query.NOTES));
            view.get().onChangePhoneNumber(this.eventDataMap.get(EventDetailLoader.Query.PLACE_PHONE_NUMBER));
            view.get().onChangeStartTimeText(this.eventDataMap.get(EventDetailLoader.Query.START_TIME_HOUR));

            view.get().onResetViewAdapter();
            view.get().onLoadFinished(guests);
            if (guests != null && guests.getGuests() != null && guests.getGuests().size() > 0) {
                Log.d(LOG_TAG, "onEventLoadFinished: guest list OK");
                view.get().onShowRecyclerView();
                view.get().onSetRecyclerViewAdapter();
            } else {
                Log.d(LOG_TAG, "onEventLoadFinished: guest list fail!");
                view.get().onShowEmptyRecyclerView();
            }
            /*if (this.eventDataMap.get(EventDetailLoader.Query.CONFIRMATION_STATUS).compareTo(EVENT_STATUS_CONFIRMED) == 0) {
                confirmButtonStatus = true;
            } else {
                confirmButtonStatus = false;
            }*/
            pendingShowView = false;
            pendingGuests = null;
            pendingEventDetailMap = null;
        } else {
            Log.d(LOG_TAG, "onEventLoadFinished: view is null!");
            pendingShowView = true;
            pendingGuests = guests;
            pendingEventDetailMap = eventDetailMap;
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
        if(view != null) {
            view.get().onLoaderInitCompleted(loaderClass);
        } else {
            this.loader = loaderClass;
            loaderPendingRequest = true;
        }

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
        Log.d(LOG_TAG, "onEventAddGuestReceived: ID = " + id);
        model.onEventAddGuestReceived(id);
        addGuestId = id;
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
    public void guestListHandler(String guestList) {
        Log.d(LOG_TAG, "onGuestListReceived");
        Gson gson = new Gson();
        List<Guest> guests = gson.fromJson(guestList,
                new TypeToken<List<Guest>>(){}.getType());
        /*for (Guest guest: guests) {
            Log.d(LOG_TAG, "id = " + guest.getId() + " - note: " + guest.getNote());
        }*/
        if (guests != null && guests.size() >= 0) {
            Log.d(LOG_TAG, "Guest List - Last Id = " + guests.get(guests.size() - 1).getId()
                    + " lenght = " + guests.size());
        }


        //view.get().onNotifySetDataChanged();

    }

    @Override
    public void guestListHandler(long guestId, long eventId, String guestList) {
        Log.d(LOG_TAG, "guestListHandler");
        Gson gson = new Gson();

        if (guestList == null) {
            //Log.d(LOG_TAG, "Guest List is NULL");
        }

        if (guestList == null || guestList.isEmpty()) {
            List<Guest> listGuest = new ArrayList<>();
            listGuest.add(new Guest(guestId, ""));
            /*listGuest.add(new Guest(Long.getLong("8"), "pizza"));
            listGuest.add(new Guest(Long.getLong("10"), "pasta"));
            listGuest.add(new Guest(Long.getLong("12"), "panino"));*/
            String idJsonList = gson.toJson(listGuest);
            //Log.d(LOG_TAG, "Json: " + idJsonList);

            model.onSaveGuestList(eventId, idJsonList);

        } else {
            boolean guestAlreadyInList = false;
            List<Guest> guests = gson.fromJson(guestList,
                    new TypeToken<List<Guest>>(){}.getType());

            for (Guest guest:guests) {
                if(guest.getId() == guestId) {
                    guestAlreadyInList = true;
                }
            }

            if (!guestAlreadyInList) {
                guests.add(new Guest(guestId, ""));
                String idJsonList = gson.toJson(guests);

                Log.d(LOG_TAG, "guestListHandler Guest List: ID: " + guestId + " to add to list: " + guestList);
                Log.d(LOG_TAG, "guestListHandler JSON list: " + idJsonList);
                model.onSaveGuestList(eventId, idJsonList);
            } else {
                Log.d(LOG_TAG, "Guest " + guestId + " already in List");
            }
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

    @Override
    public void onDestroyLoader(int loaderId) {
        view.get().onDestroyLoader(loaderId);
    }

    @Override
    public void populateGuestListView() {
        model.onGetGuestList();
    }

    @Override
    public void onNotifySetDataChanged() {

    }

    @Override
    public void onLoadFinished(String guestList) {
        Log.d(LOG_TAG, "onLoadFinished: guestId = " + addGuestId);
        boolean guestFound = false;
        Gson gson = new Gson();
        Guests guests = new Guests();
        List<Guest> listOfGuest = gson.fromJson(guestList,
                new TypeToken<List<Guest>>(){}.getType());
        guests.setGuests(listOfGuest);

        if (addGuestId > GUEST_ID_NULL) {
            Log.d(LOG_TAG, "Guest ID to Add... checking");
            if (listOfGuest != null) {
                for (Guest guest:listOfGuest) {
                    if (addGuestId == guest.getId()) {
                        Log.d(LOG_TAG, "Guest ID Found");
                        guestFound = true;
                        addGuestId = GUEST_ID_NULL;
                    }
                }
            }

            if (!guestFound) {
                Log.d(LOG_TAG, "Guest " + addGuestId + " added to list...");
                if (listOfGuest == null) {
                    listOfGuest = new ArrayList<>();
                }
                listOfGuest.add(new Guest(addGuestId, ""));
                String idJsonList = gson.toJson(listOfGuest);

                model.onAddGuestToList(idJsonList);
            } else {
                Log.d(LOG_TAG, "Guest ID Found");
                model.onGetDataForView(guests);
            }

        } else {
            Log.d(LOG_TAG, "No guest to add - addGuestId = " + addGuestId);
            model.onGetDataForView(guests);
        }



        addGuestId = GUEST_ID_NULL;
    }

    @Override
    public void onGuestListUpdated(Guests guests) {
        Log.d(LOG_TAG, "onGuestListUpdated");
        model.onGetDataForView(guests);

        for (Guest guest:guests.getGuests()) {
            Log.d(LOG_TAG, "Guest Id = " + guest.getId());
        }
    }
}
