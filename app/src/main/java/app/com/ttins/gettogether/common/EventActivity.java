package app.com.ttins.gettogether.common;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.persistence.EventStateMaintainer;
import app.com.ttins.gettogether.eventdetail.EventDetailView;
import app.com.ttins.gettogether.eventedit.EventEditView;
import app.com.ttins.gettogether.eventguesthandler.EventGuestHandlerView;
import app.com.ttins.gettogether.eventlist.EventListView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity implements EventMVP.RequestedViewOps,
        EventListView.Callback, EventEditView.Callback, EventDetailView.Callback,
        EventGuestHandlerView.Callback {

    private final String LOG_TAG = getClass().getSimpleName();

    private static final String FRAGMENT_LIST_VIEW_TAG = "FRAG_LIST_VIEW";
    private static final String FRAGMENT_EDIT_VIEW_TAG = "FRAG_EDIT_VIEW";
    private static final String FRAGMENT_DETAIL_VIEW_TAG = "FRAG_DETAIL_VIEW";
    private static final String FRAGMENT_GUEST_HANDLER_VIEW_TAG = "FRAGMENT_GUEST_HANDLER_VIEW_TAG";

    private EventMVP.PresenterOps presenter;
    private FloatingActionButton fab, fabGuestAdd;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Animation fab_guest_open, fab_guest_close;
    private final EventStateMaintainer stateMaintainer =
            new EventStateMaintainer( this.getSupportFragmentManager(), LOG_TAG );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar_event_activity);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout_event_activity);
        fab_guest_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_guest_open);
        fab_guest_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_guest_close);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        startMVPOps();

        fabGuestAdd = (FloatingActionButton) findViewById(R.id.fab_guest_add_event_activity);
        fab = (FloatingActionButton) findViewById(R.id.fab_event_event_activity);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "fab onClick");
                    presenter.onFabClick();
                }
            });

        } else {
            Log.d(LOG_TAG, "FAB setOnClickListener failed: FAB is null");
        }

        if (fabGuestAdd != null) {
            fabGuestAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "fabGuestAdd onClick");
                    presenter.onFabAddGuestClick();
                }
            });
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
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, fragmentEventListView, FRAGMENT_LIST_VIEW_TAG).commit();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onOpenGuestActivity() {
        Intent guestActivityIntent = new Intent(EventActivity.this, GuestActivity.class);
        //guestActivityIntent.setAction(getResources().getString(R.string.guest_list_open_action_intent));
        startActivity(guestActivityIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add_guest:
                break;
            case R.id.action_remove_guest:
                break;
            case R.id.action_settings:
                break;
            case R.id.guest_item_menu:
                presenter.guestMenuItemClick();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEventListViewResume() {
        Log.d(LOG_TAG, "onEventListViewresume");
        presenter.eventListViewResume();
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));

    }

    @Override
    public void onSetFabToAddEventStatus() {
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(ContextCompat.getDrawable(this.getBaseContext(), R.drawable.ic_add_white_36dp));
        } else {
            Log.d(LOG_TAG, "Error setting FAB drawables: FAB is null");
        }
    }

    @Override
    public void onSetFabToAddEventConfirmStatus() {
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(ContextCompat.getDrawable(this.getBaseContext(), R.drawable.ic_check_white_36dp));
        } else {
            Log.d(LOG_TAG, "Error setting FAB drawables: FAB is null");
        }
    }

    @Override
    public void onShowEventEditView() {
        collapsingToolbarLayout.setTitle("Edit Event");
        EventEditView fragmentEventEditView = new EventEditView();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentEventEditView, FRAGMENT_EDIT_VIEW_TAG)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onShowEventListView() {
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        EventListView fragmentEventListView = new EventListView();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentEventListView, FRAGMENT_LIST_VIEW_TAG)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onSaveEventDataRequest() {
        EventEditView fragmentEventEditView = (EventEditView) getSupportFragmentManager().
                                                        findFragmentByTag(FRAGMENT_EDIT_VIEW_TAG);
        if (fragmentEventEditView != null) {
            fragmentEventEditView.addEvent();
        }
    }

    @Override
    public void onEventItemClick(long id, String eventTitle) {
        presenter.onEventItemClick(id);
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

    @Override
    public void onShowEventDetailView(long id) {
        Log.d(LOG_TAG, "onShowEventDetailView");

        Bundle args = new Bundle();
        EventDetailView fragmentEventDetailView = new EventDetailView();

        args.putLong("FRAG_EVENT_DETAIL_EVENT_ID", id);

        fragmentEventDetailView.setArguments(args);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentEventDetailView, FRAGMENT_DETAIL_VIEW_TAG)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onSetFabToGuestStatus() {
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(ContextCompat.getDrawable(this.getBaseContext(),
                    R.drawable.ic_person_white_36dp));
        } else {
            Log.d(LOG_TAG, "Error setting FAB drawables: FAB is null");
        }
    }

    @Override
    public void onChangeToolbarToEventTitle(String eventTitle) {
        collapsingToolbarLayout.setTitle(eventTitle);
    }

    @Override
    public void onOpenFabGuestAnimation() {
        Log.d(LOG_TAG, "onOpenFabGuestAnimation");
        fabGuestAdd.setVisibility(View.VISIBLE);
        fabGuestAdd.setClickable(true);
        fabGuestAdd.startAnimation(fab_guest_open);

    }

    @Override
    public void onCloseFabGuestAnimation() {
        Log.d(LOG_TAG, "onCloseFabGuestAnimation");
        fabGuestAdd.setVisibility(View.GONE);
        fabGuestAdd.startAnimation(fab_guest_close);
        fabGuestAdd.setClickable(false);
    }

    @Override
    public void onReceiveIdEditDetailView(long id) {
        Log.d(LOG_TAG, "onReceiveIdEditDetailView: Id = " + id);
        Bundle args = new Bundle();
        args.putLong(EventEditView.FRAG_EVENT_EDIT_DETAIL_VIEW_ID_ARG, id);
        EventEditView fragmentEventEditView = new EventEditView ();
        fragmentEventEditView.setArguments(args);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentEventEditView , FRAGMENT_EDIT_VIEW_TAG)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onEventEditViewResumed() {
        presenter.eventEditViewResume();
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.guest_edit_menu));
    }

    @Override
        public void onEventDetailViewResumed() {
            presenter.eventDetailViewResume();
        }

        @Override
        public void onShowGuestHandlerView() {
            Log.d(LOG_TAG, "onShowGuestHandlerView");
            EventGuestHandlerView fragmentEventGuestHandlerView = new EventGuestHandlerView();

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_content, fragmentEventGuestHandlerView,
                            FRAGMENT_GUEST_HANDLER_VIEW_TAG)
                    .addToBackStack(null)
                    .commit();

        }

        @Override
        public void onEventGuestHandlerResumed() {
            collapsingToolbarLayout.setTitle(getResources().getString(R.string.event_guest_list_title));
            presenter.eventGuestHandlerResume();
        }

        @Override
        public void onsetFabToAddGuestToListStatus() {
            if (fab != null) {
                fab.setVisibility(View.GONE);
            } else {
                Log.d(LOG_TAG, "Error setting FAB drawables: FAB is null");
            }
    }

    @Override
    public void onEventGuestHandlerAddRequest(long id) {
        Log.d(LOG_TAG, "onEventGuestHandlerAddRequest");
        EventDetailView fragmentEventDetailView = (EventDetailView) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_DETAIL_VIEW_TAG);

        getSupportFragmentManager().popBackStack();

        fragmentEventDetailView.setGuestIdToAddOnList(id);
    }
}
