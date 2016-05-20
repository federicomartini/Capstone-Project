package app.com.ttins.gettogether.common;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.persistence.EventStateMaintainer;
import app.com.ttins.gettogether.guestedit.GuestEditView;
import app.com.ttins.gettogether.guestlist.GuestListView;

public class GuestActivity extends AppCompatActivity implements GuestMVP.RequestedViewOps, GuestListView.Callback {

    private static final String LOG_TAG = GuestActivity.class.getSimpleName();
    private static final String FRAGMENT_GUEST_ADD_VIEW_TAG = "FRAGMENT_GUEST_ADD_VIEW_TAG";

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;
    GuestMVP.PresenterOps presenter;
    private final EventStateMaintainer stateMaintainer =
            new EventStateMaintainer( this.getSupportFragmentManager(), LOG_TAG );


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        toolbar = (Toolbar) findViewById(R.id.toolbar_guest_activity);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout_guest_activity);
        fab = (FloatingActionButton) findViewById(R.id.fab_guest_add_guest_activity);
        setSupportActionBar(toolbar);

        startMVPOps();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFabClick();
            }
        });
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
    private void initialize(GuestMVP.RequestedViewOps view)
            throws InstantiationException, IllegalAccessException{
        presenter = new GuestPresenter(view);
        GuestListView fragmentGuestListView = new GuestListView();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, fragmentGuestListView).commit();
        stateMaintainer.put(GuestMVP.PresenterOps.class.getSimpleName(), presenter);
    }

    /**
     * Recovers Presenter and informs Presenter that occurred a config change.
     * If Presenter has been lost, recreates a instance
     */
    private void reinitialize(GuestMVP.RequestedViewOps view)
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSetFabToAddGuestStatus() {
        if (fab != null) {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person_add_white_36dp));
        }
    }

    @Override
    public void onSetFabToAddGuestConfirmStatus() {
        if (fab != null) {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_white_36dp));
        }
    }

    @Override
    public void onShowGuestEditView() {
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        GuestEditView fragmentGuestAddView = new GuestEditView();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentGuestAddView , FRAGMENT_GUEST_ADD_VIEW_TAG).addToBackStack(null).commit();
    }

    @Override
    public void onGuestItemClick(long id, String titleText) {

    }

    @Override
    public void onGuestListViewResume() {
        presenter.guestListViewResume();
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.guest_edit_menu));
    }
}
