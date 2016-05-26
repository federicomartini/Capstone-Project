package app.com.ttins.gettogether.eventedit;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

import java.util.HashMap;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.utils.DateTimeFormat;
import app.com.ttins.gettogether.data.GetTogetherContract;
import app.com.ttins.gettogether.eventedit.loader.EventEditLoader;
import app.com.ttins.gettogether.eventsetplace.EventSetPlaceView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class EventEditView extends Fragment implements EventEditMVP.RequiredViewOps {

    private static final String LOG_TAG = EventEditView.class.getSimpleName();

    public static final String FRAG_EVENT_EDIT_DETAIL_VIEW_ID_ARG = "FRAG_EVENT_EDIT_DETAIL_VIEW_ID_ARG";

    @BindView(R.id.event_title_edit_text_event_edit_view) EditText eventTitle;
    @BindView(R.id.location_edit_text_event_edit_view) EditText location;
    @BindView(R.id.meeting_location_edit_text_event_edit_view) EditText meetingLocation;
    @BindView(R.id.phone_edit_text_event_edit_view) EditText phone;
    @BindView(R.id.start_time_text_view_event_detail_view) EditText startTime;
    @BindView(R.id.time_end_text_view_event_edit_view) EditText endTime;
    @BindView(R.id.date_start_text_view_event_edit_view) EditText startDate;
    @BindView(R.id.date_end_text_view_event_edit_view) EditText endDate;
    @BindView(R.id.note_text_view_event_edit_view) EditText note;
    String photoSrc = null;

    private EventEditMVP.PresenterOps presenter;
    private EventEditView.Callback callback;
    Unbinder unbinder;
    private boolean isNewEvent;
    private long eventId;
    private String placeName;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new EventEditPresenter(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            callback = (EventEditView.Callback) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onAttachView(getContext());
        getActivity().invalidateOptionsMenu();
        callback.onEventEditViewResumed();

        if (placeName != null) {
            Log.d(LOG_TAG, "Setting PlaceName onResumeView");
            location.setText(this.placeName);
            this.placeName = null;
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(LOG_TAG, "onPrepareOptionsMenu");
        menu.findItem(R.id.guest_item_menu).setVisible(false);
        menu.findItem(R.id.event_edit_item_menu).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_add_guest).setVisible(false);
        menu.findItem(R.id.action_remove_guest).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.event_edit_view, container, false);
        unbinder = ButterKnife.bind(this, root);

        Bundle args = getArguments();

        if (args != null && !args.isEmpty()) {
            Log.d(LOG_TAG, "onCreateView with args: Id = " + args.getLong(FRAG_EVENT_EDIT_DETAIL_VIEW_ID_ARG));
            presenter.initEventEditLoader(args.getLong(FRAG_EVENT_EDIT_DETAIL_VIEW_ID_ARG));
            eventId = args.getLong(FRAG_EVENT_EDIT_DETAIL_VIEW_ID_ARG);
            isNewEvent = false;
        } else {
            isNewEvent = true;
        }

        eventTitle = ButterKnife.findById(root, R.id.event_title_edit_text_event_edit_view);
        location = ButterKnife.findById(root, R.id.location_edit_text_event_edit_view);
        meetingLocation = ButterKnife.findById(root, R.id.meeting_location_edit_text_event_edit_view);
        phone = ButterKnife.findById(root, R.id.phone_edit_text_event_edit_view);
        startTime = ButterKnife.findById(root, R.id.time_start_text_view_event_edit_view);
        endTime = ButterKnife.findById(root, R.id.time_end_text_view_event_edit_view);
        startDate = ButterKnife.findById(root, R.id.date_start_text_view_event_edit_view);
        endDate = ButterKnife.findById(root, R.id.date_end_text_view_event_edit_view);
        note = ButterKnife.findById(root, R.id.note_text_view_event_edit_view);


        Log.d(LOG_TAG, "Location: " + location.getText().toString());

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLocationClick();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "startTime click");
                presenter.onStartTimeTextClick();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "endTime click");
                presenter.onEndTimeTextClick();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "startDate click");
                presenter.onStartDateTextClick();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "endDate click");
                presenter.onEndDateTextClick();
            }
        });


        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onDetachView();
    }

    public void addEvent() {
        //TODO: add data to DB
        HashMap<Integer, String> dataMap = new HashMap<>();

        dataMap.put(EventEditLoader.Query.TITLE, eventTitle.getText().toString());
        dataMap.put(EventEditLoader.Query.LOCATION, location.getText().toString());
        dataMap.put(EventEditLoader.Query.MEETING_LOCATION, meetingLocation.getText().toString());
        dataMap.put(EventEditLoader.Query.EVENT_DAY, DateTimeFormat.getStringDayFromDate(startDate.getText().toString()));
        dataMap.put(EventEditLoader.Query.EVENT_MONTH, DateTimeFormat.getStringMonthFromDate(startDate.getText().toString()));
        dataMap.put(EventEditLoader.Query.EVENT_YEAR, DateTimeFormat.getStringYearFromDate(startDate.getText().toString()));
        //dataMap.put(EventEditLoader.Query.LOCATION, endDate.getText().toString());
        dataMap.put(EventEditLoader.Query.START_TIME_HOUR, DateTimeFormat.getStringHoursFromTime(startTime.getText().toString()));
        dataMap.put(EventEditLoader.Query.START_TIME_MINUTE, DateTimeFormat.getStringMinutesFromTime(startTime.getText().toString()));
        dataMap.put(EventEditLoader.Query.END_TIME_HOUR, DateTimeFormat.getStringHoursFromTime(endTime.getText().toString()));
        dataMap.put(EventEditLoader.Query.END_TIME_MINUTE, DateTimeFormat.getStringMinutesFromTime(endTime.getText().toString()));
        dataMap.put(EventEditLoader.Query.PLACE_PHONE_NUMBER, phone.getText().toString());
        dataMap.put(EventEditLoader.Query.NOTES, note.getText().toString());
        dataMap.put(EventEditLoader.Query.PHOTO_PATH, photoSrc);

        Log.d(LOG_TAG, "StartTimeHour: " + DateTimeFormat.getStringHoursFromTime(startTime.getText().toString()));

        if (isNewEvent) {
            Log.d(LOG_TAG, "Creating New Event");
            presenter.saveEvent(dataMap);
        } else {
            Log.d(LOG_TAG, "Editing Event");
            presenter.saveEvent(eventId, dataMap);
        }

    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventSaved() {
        callback.onEventSaved();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }

    @Override
    public void onLoadInitReady(LoaderManager.LoaderCallbacks loaderClass) {
        getLoaderManager().initLoader(4, null, loaderClass);
    }

    @Override
    public Context onContextViewRequired() {
        return getContext();
    }


    @Override
    public void onChangeEventTitle(String eventTitle) {
        Log.d(LOG_TAG, "onChangeEventTitle: " + eventTitle);
        if(eventTitle != null && !eventTitle.isEmpty()) {
            this.eventTitle.setText(eventTitle);
        } else {
            this.eventTitle.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onChangeMeetLocation(String meetLocation) {
        if (meetLocation != null && !meetLocation.isEmpty()) {
            this.meetingLocation.setText(meetLocation);
        } else {
            this.meetingLocation.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onChangeNotes(String notes) {
        if (notes != null && !notes.isEmpty()) {
            //this.notes.setText(notes);
        } else {
            //this.notes.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onChangePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            this.phone.setText(phoneNumber);
        } else {
            this.phone.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onChangeStartTimeText(String startTime) {
        if (startTime != null && !startTime.isEmpty()) {
            //this.startTime.setText(startTime);
        } else {
            //this.startTime.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onChangeLocationText(String location) {
        if (location != null && !location.isEmpty()) {
            this.location.setText(location);
        } else {
            this.location.setText(getResources().getString(R.string.null_field));
        }
    }

    @Override
    public void onShowSetTimeDialog(String dialogTag) {
        callback.onShowTimePickerDialog(dialogTag);
    }

    @Override
    public void onShowSetDateDialog(String dialogTag) {
        callback.onShowDatePickerDialog(dialogTag);
    }

    @Override
    public void onSetStartTime(String time) {
        Log.d(LOG_TAG, "onSetStartTime - " + time);
        startTime.setText(time);
    }

    @Override
    public void onSetEndTime(String time) {
        Log.d(LOG_TAG, "onSetStartTime - " + time);
        endTime.setText(time);
    }

    @Override
    public void onSetStartDate(String date) {
        Log.d(LOG_TAG, "onSetStartDate - " + date);
        startDate.setText(date);
    }

    @Override
    public void onSetEndDate(String date) {
        Log.d(LOG_TAG, "onSetEndDate - " + date);
        endDate.setText(date);
    }

    public void onUpdateDateTimeFromDialog(String dialogTag, String message) {
        presenter.onUpdateDateTimeFromDialog(dialogTag, message);
    }

    @Override
    public void onShowPlaceView() {
        callback.onShowPlaceView();
    }

    public void setPlace(String placeName) {
        Log.d(LOG_TAG, "setPlace: " + placeName);
        presenter.onPlaceReceived(placeName);
    }

    @Override
    public void onShowLocation(String placeName) {
        Log.d(LOG_TAG, "onShowLocation: " + placeName);
        if (location != null && this.getView() != null) {
            Log.d(LOG_TAG, "Setting PlaceName onShowLocation");
            location.setText(placeName);
            this.placeName = null;
        } else {
            Log.d(LOG_TAG, "Fetching PlaceName...");
            this.placeName = placeName;
        }

    }

    public interface Callback {
        void onEventSaved();
        void onEventEditViewResumed();
        void onShowPlaceView();
        void onShowTimePickerDialog(String dialogTag);
        void onShowDatePickerDialog(String dialogTag);
    }


}
