package app.com.ttins.gettogether.common;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.eventedit.EventEditView;
import app.com.ttins.gettogether.eventlist.EventListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventActivity extends AppCompatActivity implements EventMVP.RequestedViewOps, EventListView.Callback {

    private final String LOG_TAG = getClass().getSimpleName();

    private static final String FRAGMENT_EDIT_VIEW_TAG = "FRAG_EDIT_VIEW";

    private EventMVP.PresenterOps presenter;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_event_activity);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        presenter = new EventPresenter(this);

        /* Starts with the Event List View */
        EventListView fragmentEventListView = new EventListView();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, fragmentEventListView).commit();
        fab = (FloatingActionButton) findViewById(R.id.fab_event_event_activity);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onAddEventFabClick();
                }
            });
        } else {
            Log.d(LOG_TAG, "FAB setOnClickListener failed: FAB is null");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.initFabStatus();
    }

    @Override
    public void onEventListViewResume() {
        presenter.eventListViewResume();
    }

    @Override
    public void onSetFabToAddEventStatus() {
        if (fab != null) {
            fab.setImageDrawable(ContextCompat.getDrawable(this.getBaseContext(), R.drawable.ic_add_white_36dp));
        } else {
            Log.d(LOG_TAG, "Error setting FAB drawables: FAB is null");
        }
    }

    @Override
    public void onSetFabToAddEventConfirmStatus() {
        if (fab != null) {
            fab.setImageDrawable(ContextCompat.getDrawable(this.getBaseContext(), R.drawable.ic_check_white_36dp));
        } else {
            Log.d(LOG_TAG, "Error setting FAB drawables: FAB is null");
        }
    }

    @Override
    public void onShowEventEditView() {
        EventEditView fragmentEventEditView = new EventEditView();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragmentEventEditView, FRAGMENT_EDIT_VIEW_TAG).addToBackStack(null).commit();
    }

}
