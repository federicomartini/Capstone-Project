package app.com.ttins.gettogether.eventedit;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.ui.ThreeTwoImageView;
import app.com.ttins.gettogether.common.utils.DateTimeFormat;
import app.com.ttins.gettogether.common.utils.FilePath;
import app.com.ttins.gettogether.common.utils.Permissions;
import app.com.ttins.gettogether.data.GetTogetherContract;
import app.com.ttins.gettogether.eventedit.loader.EventEditLoader;
import app.com.ttins.gettogether.eventsetplace.EventSetPlaceView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class EventEditView extends Fragment implements EventEditMVP.RequiredViewOps {

    private static final String LOG_TAG = EventEditView.class.getSimpleName();

    public static final String FRAG_EVENT_EDIT_DETAIL_VIEW_ID_ARG = "FRAG_EVENT_EDIT_DETAIL_VIEW_ID_ARG";
    private static final int SELECT_IMAGE_REQ_CODE = 1;

    @BindView(R.id.event_title_edit_text_event_edit_view) EditText eventTitle;
    @BindView(R.id.location_edit_text_event_edit_view) EditText location;
    @BindView(R.id.meeting_location_edit_text_event_edit_view) EditText meetingLocation;
    @BindView(R.id.phone_edit_text_event_edit_view) EditText phone;
    @BindView(R.id.start_time_text_view_event_detail_view) EditText startTime;
    @BindView(R.id.time_end_text_view_event_edit_view) EditText endTime;
    @BindView(R.id.date_start_text_view_event_edit_view) EditText startDate;
    @BindView(R.id.date_end_text_view_event_edit_view) EditText endDate;
    @BindView(R.id.note_text_view_event_edit_view) EditText note;
    @BindView(R.id.image_icon_image_view) ImageView eventPhoto;
    @BindView(R.id.square_image_view_event_view)
        ThreeTwoImageView toolBarImage;

    String photoSrc = null;
    String toolbarPhotoPending = null;

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
        Log.d(LOG_TAG, "onResume");
        presenter.onAttachView(getContext());
        getActivity().invalidateOptionsMenu();
        callback.onEventEditViewResumed();

        if (placeName != null) {
            Log.d(LOG_TAG, "Setting PlaceName onResumeView");
            location.setText(this.placeName);
            this.placeName = null;
        }

        if (toolbarPhotoPending != null) {
            Log.d(LOG_TAG, "onChangeEventPhoto call pending photo for Toolbar: " + toolbarPhotoPending);
            callback.onShowPictureEditViewToolbar(toolbarPhotoPending);
            toolbarPhotoPending = null;
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(LOG_TAG, "onPrepareOptionsMenu");
        menu.findItem(R.id.guest_item_menu).setVisible(false);
        menu.findItem(R.id.event_edit_item_menu).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
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
            presenter.onEditEventReceived(eventId);
            isNewEvent = false;
        } else {
            presenter.onNewEventReceived();
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
        eventPhoto = ButterKnife.findById(root, R.id.image_icon_image_view);
        toolBarImage = ButterKnife.findById(root, R.id.square_image_view_event_view);

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

        eventPhoto.setClickable(true);
        eventPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onEventPhotoIconClick();
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

        if (photoSrc == null || !photoSrc.isEmpty()) {
            dataMap.put(EventEditLoader.Query.PHOTO_PATH, photoSrc);
        }


        Log.d(LOG_TAG, "StartTimeHour: " + DateTimeFormat.getStringHoursFromTime(startTime.getText().toString()));
        Log.d(LOG_TAG, "Saving photo uri: " + photoSrc);

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
        isNewEvent = false;
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
            this.note.setText(notes);
        } else {
            this.note.setText(getResources().getString(R.string.null_field));
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
            this.startTime.setText(startTime);
        } else {
            this.startTime.setText(getResources().getString(R.string.null_field));
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

    @Override
    public void onShowGalleryForPicture() {
        //Intent intent = new Intent();
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE_REQ_CODE);
        Log.d(LOG_TAG, "onShowGalleryForPicture");
        if (Permissions.checkPermission(getContext())) {
            Log.d(LOG_TAG, "Permission granted!");
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECT_IMAGE_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(LOG_TAG, "onRequestPermissionsResult...");
        switch(requestCode) {
            case Permissions.MY_PERMISSIONS_REQUEST_MANAGE_DOCUMENTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                    Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_IMAGE_REQ_CODE);

            } else {
                Log.d(LOG_TAG, "onRequestPermissionsResult: Permission denied");
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "onActivityResult...");
        if (requestCode == SELECT_IMAGE_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getActivity().getContentResolver(), data.getData());
                        Log.d(LOG_TAG, "Bitmap received: " + data.getDataString());
                        //photoSrc = data.getData().toString();
                        photoSrc = FilePath.getPath(getContext(), data.getData());
                        Log.d(LOG_TAG, "onActivityResult.photoSrc = " + photoSrc);
                        toolbarPhotoPending = null;
                        callback.onShowPictureEditViewToolbar(photoSrc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(LOG_TAG, "Bitmap is null!");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onChangeEventPhoto(String photoUri) {
        Log.d(LOG_TAG, "onChangeEventPhoto");
        if (toolBarImage != null) {
            Log.d(LOG_TAG, "onChangeEventPhoto photo: " + photoUri);
            callback.onShowPictureEditViewToolbar(photoUri);
        } else {
            Log.d(LOG_TAG, "onChangeEventPhoto view is null. toolbarPhotoPending: " + photoUri);
            toolbarPhotoPending = photoUri;
        }

    }


    @Override
    public void onChangeStartDateText(String startDate) {
        if (startDate != null && !startDate.isEmpty())
            this.startDate.setText(startDate);
    }

    @Override
    public void onEventEdited(long id) {
        callback.onEventEdited(id);
    }

    public interface Callback {
        void onEventSaved();
        void onEventEdited(long id);
        void onEventEditViewResumed();
        void onShowPlaceView();
        void onShowTimePickerDialog(String dialogTag);
        void onShowDatePickerDialog(String dialogTag);
        void onShowPictureEditViewToolbar(String photoUri);
    }


}
