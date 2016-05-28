package app.com.ttins.gettogether.eventdetail;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.gson.Guest;
import app.com.ttins.gettogether.common.gson.Guests;
import app.com.ttins.gettogether.data.GetTogetherContract;
import app.com.ttins.gettogether.eventdetail.adapter.EventDetailAdapter;

public class EventDetailView extends Fragment implements EventDetailMVP.RequiredViewOps {

    private static final String LOG_TAG = EventDetailView.class.getSimpleName();

    public static final String FRAG_EVENT_DETAIL_EVENT_ID = "FRAG_EVENT_DETAIL_EVENT_ID";
    public static final String FRAG_GUEST_ADD_LIST_ID_ARG = "FRAG_GUEST_ADD_LIST_ID_ARG";

    EventDetailMVP.PresenterOps presenter;
    long eventId;
    Callback callback;

    TextView eventTitle;
    TextView startTime;
    TextView location;
    TextView meetLocation;
    TextView eventDate;
    TextView phoneNumber;
    TextView confirmStatus;
    TextView emptyGuestList;
    TextView notes;
    ListView guestsList;
    ImageButton confirmButton;
    RecyclerView recyclerView;
    EventDetailAdapter eventDetailAdapter;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout itemLayout;
        public String photoUri;
        public TextView guestName;
        public TextView note;

        public ViewHolder(View view) {
            super(view);

            itemLayout = (LinearLayout) view.findViewById(R.id.guest_list_item_event_detail_view);
            guestName = (TextView) view.findViewById(R.id.guest_list_name_text_view_event_detail_view);
            note = (TextView) view.findViewById(R.id.guest_note_list_text_view_event_detail_view);
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (presenter == null) {
            presenter = new EventDetailPresenter(this);
        }

        Bundle args = getArguments();

        if(args.containsKey(FRAG_EVENT_DETAIL_EVENT_ID)) {
            eventId = args.getLong(FRAG_EVENT_DETAIL_EVENT_ID);
        }

        if(args.containsKey(FRAG_GUEST_ADD_LIST_ID_ARG)) {
            //presenter.onEventAddGuestReceived(args.getLong(FRAG_GUEST_ADD_LIST_ID_ARG));
        }


        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Activity) {
            callback = (EventDetailView.Callback)context;
        }
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "EventDetailViewResume");
        super.onResume();
        presenter.onAttachView(this);
        presenter.onAttachContext(getContext());
        presenter.initLoader();
        callback.onEventDetailViewResumed();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.event_edit_item_menu).setVisible(true);
        menu.findItem(R.id.guest_item_menu).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.event_edit_item_menu:
                presenter.onEditItemClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_detail_view, container, false);
        presenter.onPopulateDetailView(eventId);

        startTime = (TextView) root.findViewById(R.id.start_time_text_view_event_detail_view);
        location = (TextView) root.findViewById(R.id.location_text_view_event_detail_view);
        meetLocation = (TextView) root.findViewById(R.id.meet_loc_text_view_event_detail_view);
        eventDate = (TextView) root.findViewById(R.id.event_date_text_view_event_detail_view);
        phoneNumber = (TextView) root.findViewById(R.id.phone_text_view_event_detail_view);
        confirmStatus = (TextView) root.findViewById(R.id.confirm_status_text_view_event_detail_view);
        emptyGuestList = (TextView) root.findViewById(R.id.empty_guest_list_text_view_event_detail_view);
        notes = (TextView) root.findViewById(R.id.note_text_view_event_detail_view);
        confirmButton = (ImageButton) root.findViewById(R.id.confirm_image_button_event_detail_view);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_guests_event_detail_view);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onConfirmButtonClick();
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onDetachView();
        presenter.onDetachContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onChangeConfirmStatus(String confirmStatus) {
        if (confirmStatus != null && !confirmStatus.isEmpty()) {
            this.confirmStatus.setText(confirmStatus);
        } else {
            this.confirmStatus.setText(getResources().getString(R.string.null_field));
        }


    }

    @Override
    public void onChangeEmptyGuestList() {
        this.emptyGuestList.setVisibility(View.VISIBLE);
        this.emptyGuestList.setText(getResources().getString(R.string.no_guests));
    }

    @Override
    public void onChangeEventDate(String eventDate) {
        if (eventDate != null && !eventDate.isEmpty()) {
            this.eventDate.setText(eventDate);
        } else {
            this.eventDate.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onChangeEventTitle(String eventTitle) {
        if(eventTitle != null && !eventTitle.isEmpty()) {
            callback.onChangeToolbarToEventTitle(eventTitle);
        }
    }

    @Override
    public void onChangeMeetLocation(String meetLocation) {
        if (meetLocation != null && !meetLocation.isEmpty()) {
            this.meetLocation.setText(meetLocation);
        } else {
            this.meetLocation.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onChangeNotes(String notes) {
        if (notes != null && !notes.isEmpty()) {
            this.notes.setText(notes);
        } else {
            this.notes.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onChangePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            this.phoneNumber.setText(phoneNumber);
        } else {
            this.phoneNumber.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onChangeStartTimeText(String startTime) {
        if (startTime != null && !startTime.isEmpty()) {
            this.startTime.setText(startTime);
        } else {
            this.startTime.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass) {
        getLoaderManager().restartLoader(1, null, loaderClass);
    }

    @Override
    public void onRestartLoaderRequest(LoaderManager.LoaderCallbacks<Cursor> loaderClass, int loaderId) {
        Log.d(LOG_TAG, "onRestartLoaderRequest");
        getLoaderManager().restartLoader(loaderId, null, loaderClass);
    }

    @Override
    public Context onContextViewRequired() {
        return getContext();
    }

    public void setGuestIdToAddOnList(long id) {
        Log.d(LOG_TAG, "setGuestIdToAddOnList");
        presenter.onEventAddGuestReceived(id);
    }

    @Override
    public void onSendDataForEditDetailsView() {
        Log.d(LOG_TAG, "onSendDataForEditDetailsView: Id = " + eventId);
        callback.onReceiveIdEditDetailView(eventId);
    }

    @Override
    public void onDestroyLoader(int loaderId) {
        getLoaderManager().destroyLoader(loaderId);
    }


    @Override
    public void onShowEmptyRecyclerView() {
        Log.d(LOG_TAG, "onShowEmptyRecyclerView");
        recyclerView.setVisibility(View.GONE);
        emptyGuestList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowRecyclerView() {
        Log.d(LOG_TAG, "onShowRecyclerView");
        recyclerView.setVisibility(View.VISIBLE);
        emptyGuestList.setVisibility(View.GONE);
    }

    @Override
    public void onSetRecyclerViewAdapter(Guests guests) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        if(recyclerView.hasPendingAdapterUpdates()) {
            Log.d(LOG_TAG, "recyclerView hasPendingAdapterUpdates");
            eventDetailAdapter.swapGuests(guests);
            eventDetailAdapter.notifyDataSetChanged();
        }

        if (recyclerView.getAdapter() == null) {
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(eventDetailAdapter);
        }

    }

    @Override
    public void onLoadFinished(Guests guests) {
        if (eventDetailAdapter == null) {
            eventDetailAdapter = new EventDetailAdapter(guests, new EventDetailAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(long id) {
                    Log.d(LOG_TAG, "onItemClick Received by View");
                }

                @Override
                public void onLongItemClick(final long id) {
                    Log.d(LOG_TAG, "onLongItemClick Received by View");
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete Event")
                            .setMessage("Want to delete guest \"" + id + "\" from the event ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    presenter.onEventRemoveGuestReceived(id);
                                }
                            });
                    builder.create().show();
                }
            });

        }
        eventDetailAdapter.swapGuests(guests);
    }

    @Override
    public void onResetViewAdapter() {
        recyclerView.setAdapter(null);
        eventDetailAdapter = null;
        /*recyclerView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Item Count : " + eventDetailAdapter.getItemCount());
                eventDetailAdapter.notifyDataSetChanged();
                Log.d(LOG_TAG, "notifyDataSetChanged -> Item Count : " + eventDetailAdapter.getItemCount());
            }
        });*/
    }

    @Override
    public void onNotifySetDataChanged() {
        if (eventDetailAdapter != null)
            eventDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChangeLocation(String location) {
        if (this.location != null) {
            this.location.setText(location);
        }
    }

    @Override
    public void onGuestAlreadyInList(String guestName) {
        callback.onShowEventDetailViewToast(guestName + " already in list");
    }

    @Override
    public void onChangeEventPhoto(String photoUri) {
        Log.d(LOG_TAG, "onChangeEventPhoto: " + photoUri);
        callback.onSetToolbarPhotoBackground(photoUri);
    }

    public interface Callback {
        void onChangeToolbarToEventTitle(String eventTitle);
        void onReceiveIdEditDetailView(long id);
        void onEventDetailViewResumed();
        void onShowEventDetailViewToast(String message);
        void onSetToolbarPhotoBackground(String photoUri);
    }

}
