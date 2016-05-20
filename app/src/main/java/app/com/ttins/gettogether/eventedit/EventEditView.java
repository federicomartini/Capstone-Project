package app.com.ttins.gettogether.eventedit;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.EventActivity;
import app.com.ttins.gettogether.common.EventMVP;
import app.com.ttins.gettogether.eventlist.EventListMVP;
import app.com.ttins.gettogether.eventlist.EventListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class EventEditView extends Fragment implements EventEditMVP.RequiredViewOps {

    private static final String LOG_TAG = EventEditView.class.getSimpleName();

    @BindView(R.id.event_title_edit_text_event_edit_view) EditText eventTitle;
    @BindView(R.id.location_edit_text_event_edit_view) EditText location;
    @BindView(R.id.meeting_location_edit_text_event_edit_view) EditText meetingLocation;
    @BindView(R.id.phone_edit_text_event_edit_view) EditText phone;
    EventEditMVP.PresenterOps presenter;
    EventEditView.Callback callback;
    Unbinder unbinder;

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
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(LOG_TAG, "onPrepareOptionsMenu");
        menu.findItem(R.id.guest_item_menu).setVisible(false);
        menu.findItem(R.id.event_edit_item_menu).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.event_edit_view, container, false);
        unbinder = ButterKnife.bind(this, root);

        eventTitle = ButterKnife.findById(root, R.id.event_title_edit_text_event_edit_view);
        location = ButterKnife.findById(root, R.id.location_edit_text_event_edit_view);
        meetingLocation = ButterKnife.findById(root, R.id.meeting_location_edit_text_event_edit_view);
        phone = ButterKnife.findById(root, R.id.phone_edit_text_event_edit_view);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onDetachView();
    }

    public void addEvent() {
        //TODO: add data to DB
        String titleText;
        String locationText;
        String meetingLocationText;
        String phoneNumber;

        titleText = eventTitle.getText().toString();
        locationText = location.getText().toString();
        meetingLocationText = meetingLocation.getText().toString();
        phoneNumber = phone.getText().toString();

        presenter.saveEvent(titleText, locationText, meetingLocationText, phoneNumber);
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

    public interface Callback {
        void onEventSaved();
    }
}
