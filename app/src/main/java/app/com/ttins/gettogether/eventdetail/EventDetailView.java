package app.com.ttins.gettogether.eventdetail;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

import app.com.ttins.gettogether.R;

public class EventDetailView extends Fragment implements EventDetailMVP.RequiredViewOps {

    private static final String LOG_TAG = EventDetailView.class.getSimpleName();

    EventDetailMVP.PresenterOps presenter;
    long eventId;
    Callback callback;

    TextView eventTitle;
    TextView startTime;
    TextView meetLocation;
    TextView eventDuration;
    TextView phoneNumber;
    TextView confirmStatus;
    TextView emptyGuestList;
    TextView notes;
    ListView guestsList;
    ImageButton confirmButton;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (presenter == null) {
            presenter = new EventDetailPresenter(this);
        }

        Bundle args = getArguments();
        eventId = args.getLong("FRAG_EVENT_DETAIL_EVENT_ID");
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
        super.onResume();
        presenter.onAttachView(this);
        presenter.initLoader();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.event_edit_item_menu).setVisible(true);
        menu.findItem(R.id.guest_item_menu).setVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_detail_view, container, false);
        presenter.onPopulateDetailView(eventId);

        eventTitle = (TextView) root.findViewById(R.id.event_title_text_view_event_detail_view);
        startTime = (TextView) root.findViewById(R.id.start_time_text_view_event_detail_view);
        meetLocation = (TextView) root.findViewById(R.id.meet_loc_text_view_event_detail_view);
        eventDuration = (TextView) root.findViewById(R.id.elapse_time_text_view_event_detail_view);
        phoneNumber = (TextView) root.findViewById(R.id.phone_text_view_event_detail_view);
        confirmStatus = (TextView) root.findViewById(R.id.confirm_status_text_view_event_detail_view);
        emptyGuestList = (TextView) root.findViewById(R.id.empty_guest_list_text_view_event_detail_view);
        notes = (TextView) root.findViewById(R.id.note_text_view_event_detail_view);
        guestsList = (ListView) root.findViewById(R.id.guest_list_event_detail_view);
        confirmButton = (ImageButton) root.findViewById(R.id.confirm_image_button_event_detail_view);

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
    public void onChangeEventDuration(String eventDuration) {
        if (eventDuration != null && !eventDuration.isEmpty()) {
            this.eventDuration.setText(eventDuration);
        } else {
            this.eventDuration.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onChangeEventTitle(String eventTitle) {
        Log.d(LOG_TAG, "onChangeEventTitle: " + eventTitle);
        if(eventTitle != null && !eventTitle.isEmpty()) {
            this.eventTitle.setText(eventTitle);
            callback.onChangeToolbarToEventTitle(eventTitle);
        } else {
            this.eventTitle.setText(getResources().getString(R.string.null_field));
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
        getLoaderManager().initLoader(1, null, loaderClass);
    }

    @Override
    public Context onContextViewRequired() {
        return getContext();
    }

    public interface Callback {
        void onChangeToolbarToEventTitle(String eventTitle);
    }
}
