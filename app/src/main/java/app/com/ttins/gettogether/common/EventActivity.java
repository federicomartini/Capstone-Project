package app.com.ttins.gettogether.common;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.persistence.EventStateMaintainer;
import app.com.ttins.gettogether.eventedit.EventEditView;
import app.com.ttins.gettogether.eventlist.EventListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventActivity extends AppCompatActivity implements EventMVP.RequestedViewOps,
        EventListView.Callback, EventEditView.Callback {

    private final String LOG_TAG = getClass().getSimpleName();

    private static final String FRAGMENT_LIST_VIEW_TAG = "FRAG_LIST_VIEW";
    private static final String FRAGMENT_EDIT_VIEW_TAG = "FRAG_EDIT_VIEW";

    private EventMVP.PresenterOps presenter;
    private FloatingActionButton fab;
    private final EventStateMaintainer stateMaintainer =
            new EventStateMaintainer( this.getSupportFragmentManager(), LOG_TAG );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_event_activity);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        startMVPOps();

        /* Starts with the Event List View */
        fab = (FloatingActionButton) findViewById(R.id.fab_event_event_activity);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onFabClick();
                }
            });
        } else {
            Log.d(LOG_TAG, "FAB setOnClickListener failed: FAB is null");
        }
    }

    /**
     * Initialize and restart the Presenter.
     * This method should be called after {@link AppCompatActivity#onCreate(Bundle)}
     */
    public void startMVPOps() {
        try {
            if (stateMaintainer.firstTimeIn()) {
                Log.d(LOG_TAG, "onCreate() called for the first time");
                initialize(this);
            } else {
                Log.d(LOG_TAG, "onCreate() called more than once");
                reinitialize(this);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            Log.d(LOG_TAG, "onCreate() " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize relevant MVP Objects.
     * Creates a Presenter instance, saves the presenter in {@link EventStateMaintainer}
     */
    private void initialize(EventMVP.RequestedViewOps view)
            throws InstantiationException, IllegalAccessException{
        presenter = new EventPresenter(view);
        EventListView fragmentEventListView = new EventListView();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, fragmentEventListView).commit();
        stateMaintainer.put(EventMVP.PresenterOps.class.getSimpleName(), presenter);
    }

    /**
     * Recovers Presenter and informs Presenter that occurred a config change.
     * If Presenter has been lost, recreates a instance
     */
    private void reinitialize(EventMVP.RequestedViewOps view)
            throws InstantiationException, IllegalAccessException {
        presenter = stateMaintainer.get(EventMVP.PresenterOps.class.getSimpleName());

        if (presenter == null) {
            Log.w(LOG_TAG, "recreating Presenter");
            initialize(view);
        } else {
            presenter.onConfigurationChanged(view);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.initFabStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentEventEditView, FRAGMENT_EDIT_VIEW_TAG).addToBackStack(null).commit();
    }

    @Override
    public void onShowEventListView() {
        EventListView fragmentEventListView = new EventListView();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentEventListView, FRAGMENT_LIST_VIEW_TAG).commit();
    }

    @Override
    public void onSaveEventDataRequest() {
        EventEditView fragmentEventEditView = (EventEditView) getSupportFragmentManager().
                                                        findFragmentByTag(FRAGMENT_EDIT_VIEW_TAG);
        fragmentEventEditView.addEvent();
    }


    @Override
    public void onDestroy() {
        presenter.onDestroy(isChangingConfigurations());
        super.onDestroy();
    }

    @Override
    public void onEventSaved() {
        presenter.onEventDataSaved();
    }
}
