package app.com.ttins.gettogether.eventedit;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.eventlist.EventListMVP;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EventEditView extends Fragment implements EventEditMVP.RequiredViewOps {

    @BindView(R.id.event_title_edit_text_event_edit_view) EditText eventTitle;
    @BindView(R.id.location_edit_text_event_edit_view) EditText location;
    @BindView(R.id.meeting_location_edit_text_event_edit_view) EditText meetingLocation;
    @BindView(R.id.phone_edit_text_event_edit_view) EditText phone;
    EventEditMVP.PresenterOps presenter;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new EventEditPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onAttachView(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.event_edit_view, container, false);
        ButterKnife.bind(this, root);

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

}
