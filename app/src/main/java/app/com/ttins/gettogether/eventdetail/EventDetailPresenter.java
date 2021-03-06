package app.com.ttins.gettogether.eventdetail;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import app.com.ttins.gettogether.common.gson.Guest;
import app.com.ttins.gettogether.common.gson.Guests;
import app.com.ttins.gettogether.common.utils.DateTimeFormat;
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
    Guests localGuestList;
    boolean loaderPendingRequest = false;
    boolean confirmButtonStatus = false;
    boolean addGuestRequestPending = false;
    LoaderManager.LoaderCallbacks<Cursor> loader;
    int loaderId;
    long addGuestId;
    String addGuestname, addGuestPhotoPath;
    boolean pendingShowView = false;
    HashMap<Integer, String> pendingEventDetailMap;
    Guests pendingGuests;
    String guestNameToastPending = null;

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

        if(guestNameToastPending != null && !guestNameToastPending.isEmpty() ) {
            Log.d(LOG_TAG, "onAttachView guestNameToastPending");
            sendToastToActivity(guestNameToastPending);
            guestNameToastPending = null;
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

            String startTime = DateTimeFormat.convertTime(this.eventDataMap.get(EventDetailLoader.Query.START_TIME_HOUR),
                    this.eventDataMap.get(EventDetailLoader.Query.START_TIME_MINUTE));

            String eventDate = DateTimeFormat.convertDate(this.eventDataMap.get(EventDetailLoader.Query.EVENT_DAY),
                                        this.eventDataMap.get(EventDetailLoader.Query.EVENT_MONTH),
                                        this.eventDataMap.get(EventDetailLoader.Query.EVENT_YEAR));

            view.get().onChangeEventDate(eventDate);

            view.get().onChangeEventTitle(this.eventDataMap.get(EventDetailLoader.Query.TITLE));

            if(startTime.length() >= 4) {
                view.get().onChangeStartTimeText(startTime);
            }

            view.get().onChangeLocation(this.eventDataMap.get(EventDetailLoader.Query.LOCATION));
            view.get().onChangeMeetLocation(this.eventDataMap.get(EventDetailLoader.Query.MEETING_LOCATION));
            view.get().onChangeNotes(this.eventDataMap.get(EventDetailLoader.Query.NOTES));
            view.get().onChangePhoneNumber(this.eventDataMap.get(EventDetailLoader.Query.PLACE_PHONE_NUMBER));

            String photoPath = this.eventDataMap.get(EventDetailLoader.Query.PHOTO_PATH);
            if ( photoPath != null && !photoPath.isEmpty()) {
                Log.d(LOG_TAG, "onEventLoadFinishedphoto: Path: " + photoPath);
                view.get().onChangeEventPhoto(photoPath);
            } else {
                Log.d(LOG_TAG, "onEventLoadFinished: photoPath is empty or null");
            }

            String notes = eventDataMap.get((EventDetailLoader.Query.NOTES));

            if (notes != null && notes.length() > 0) {
                view.get().onChangeNotes(notes);
            }

            view.get().onResetViewAdapter();
            view.get().onLoadFinished(guests);
            if (guests != null && guests.getGuests() != null && guests.getGuests().size() > 0) {
                Log.d(LOG_TAG, "onEventLoadFinished: guest list OK");
                view.get().onShowRecyclerView();
                view.get().onSetRecyclerViewAdapter(guests);
            } else {
                Log.d(LOG_TAG, "onEventLoadFinished: guest list fail!");
                view.get().onShowEmptyRecyclerView();
            }

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
    public void onEditItemClick() {
        view.get().onSendDataForEditDetailsView();
    }

    @Override
    public void onEventAddGuestReceived(long id, String name, String photoPath) {
        Log.d(LOG_TAG, "onEventAddGuestReceived: ID = " + id + " - name = " + name + " - photoPath = " + photoPath);
        model.onEventAddGuestReceived(id);
        addGuestId = id;
        addGuestname = name;
        addGuestPhotoPath = photoPath;
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

    /*@Override
    public void guestListHandler(String guestList) {
        Log.d(LOG_TAG, "onGuestListReceived");
        Gson gson = new Gson();
        List<Guest> guests = gson.fromJson(guestList,
                new TypeToken<List<Guest>>(){}.getType());
        for (Guest guest: guests) {
            Log.d(LOG_TAG, "id = " + guest.getId() + " - note: " + guest.getNote());
        }
        if (guests != null && guests.size() >= 0) {
            Log.d(LOG_TAG, "Guest List - Last Id = " + guests.get(guests.size() - 1).getId()
                    + " lenght = " + guests.size());
        }


        //view.get().onNotifySetDataChanged();

    }*/

    /*@Override
    public void guestListHandler(long guestId, long eventId, String guestList) {
        Log.d(LOG_TAG, "guestListHandler");
        Gson gson = new Gson();

        if (guestList == null) {
            //Log.d(LOG_TAG, "Guest List is NULL");
        }

        //if (guestList == null || guestList.isEmpty()) {
            //List<Guest> listGuest = new ArrayList<>();
            //listGuest.add(new Guest(guestId, ""));
            //listGuest.add(new Guest(Long.getLong("8"), "pizza"));
            //listGuest.add(new Guest(Long.getLong("10"), "pasta"));
            //listGuest.add(new Guest(Long.getLong("12"), "panino"));
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
                sendToastToActivity(String.valueOf(guestId));
            }
        }
    }*/

    private void sendToastToActivity(String guestName) {
        Log.d(LOG_TAG, "sendToastToActivity");
        if (view != null) {
            view.get().onGuestAlreadyInList(guestName);
        } else {
            Log.d(LOG_TAG, "View is null");
            guestNameToastPending = guestName;
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
        Log.d(LOG_TAG, "onLoadFinished: guestId = " + addGuestId + "to list: " + guestList);
        boolean guestFound = false;
        String guestFoundName = "";
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
                        guestFoundName = String.valueOf(guest.getId());
                        break;
                    }
                }
            }

            if (!guestFound) {
                Log.d(LOG_TAG, "Guest " + addGuestId + " added to list...");
                if (listOfGuest == null) {
                    listOfGuest = new ArrayList<>();
                }

                Guest guest = new Guest();
                guest.setId(addGuestId);
                guest.setName(addGuestname);
                guest.setPhotoPath(addGuestPhotoPath);

                listOfGuest.add(guest);
                String idJsonList = gson.toJson(listOfGuest);

                model.onUpdateGuestList(idJsonList);
            } else {
                Log.d(LOG_TAG, "Guest ID Found");
                sendToastToActivity(String.valueOf(guestFoundName));
                model.onGetDataForView(guests);
                localGuestList = guests;
            }

        } else {
            Log.d(LOG_TAG, "No guest to add - addGuestId = " + addGuestId);
            model.onGetDataForView(guests);
            localGuestList = guests;
        }

        addGuestId = GUEST_ID_NULL;
    }

    @Override
    public void onGuestListUpdated(Guests guests) {
        Log.d(LOG_TAG, "onGuestListUpdated");
        model.onGetDataForView(guests);
        localGuestList = guests;


        /*for (Guest guest:guests.getGuests()) {
            Log.d(LOG_TAG, "Guest Id = " + guest.getId());
        }*/
    }

    @Override
    public void onEventRemoveGuestReceived(long id) {
        List<Guest> listOfGuest = localGuestList.getGuests();
        Gson gson = new Gson();
        int posToRemove = 0;

        for (Guest guest:listOfGuest) {
            if (guest.getId() == id) {
                Log.d(LOG_TAG, "Item to delete found (" + id + ")");
                break;
            }
            Log.d(LOG_TAG, "I am here");
            posToRemove++;
        }

        listOfGuest.remove(posToRemove);
        String idJsonList = gson.toJson(listOfGuest);
        Log.d(LOG_TAG, "JsonList after remove: " + idJsonList);
        model.onUpdateGuestList(idJsonList);
    }
}
