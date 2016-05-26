package app.com.ttins.gettogether.common;


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
import android.view.View;

import java.util.HashMap;
import java.util.List;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.persistence.GuestStateMaintainer;
import app.com.ttins.gettogether.guestdetail.GuestDetailView;
import app.com.ttins.gettogether.guestedit.GuestEditView;
import app.com.ttins.gettogether.guestlist.GuestListView;
import app.com.ttins.gettogether.guestsetmaplocation.GuestSetMapLocationMVP;
import app.com.ttins.gettogether.guestsetmaplocation.GuestSetMapLocationView;

public class GuestActivity extends AppCompatActivity implements GuestMVP.RequestedViewOps,
                        GuestListView.Callback, GuestEditView.Callback, GuestDetailView.Callback,
                        GuestSetMapLocationView.Callback {

    private static final String LOG_TAG = GuestActivity.class.getSimpleName();
    private static final String FRAGMENT_GUEST_ADD_VIEW_TAG = "FRAGMENT_GUEST_ADD_VIEW_TAG";
    private static final String FRAGMENT_GUEST_LIST_VIEW_TAG = "FRAGMENT_GUEST_LIST_VIEW_TAG";
    private static final String FRAGMENT_DETAIL_VIEW_TAG = "FRAGMENT_DETAIL_VIEW_TAG";
    private static final String FRAGMENT_MAP_VIEW_TAG = "FRAGMENT_MAP_VIEW_TAG";

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;
    GuestMVP.PresenterOps presenter;
    private final GuestStateMaintainer stateMaintainer =
            new GuestStateMaintainer( this.getSupportFragmentManager(), LOG_TAG );


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.initFabStatus();
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
     * Creates a Presenter instance, saves the presenter in {@link GuestStateMaintainer}
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
        presenter = stateMaintainer.get(GuestMVP.PresenterOps.class.getSimpleName());

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
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.guest_edit_menu));
        GuestEditView fragmentGuestAddView = new GuestEditView();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentGuestAddView , FRAGMENT_GUEST_ADD_VIEW_TAG).addToBackStack(null).commit();
    }

    @Override
    public void onGuestItemClick(long id, String guestName) {
        presenter.onGuestItemClick(id);
    }

    @Override
    public void onGuestListViewResume() {
        presenter.guestListViewResume();
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.guest_activity));
    }

    @Override
    public void onSaveGuestDataRequest() {
        GuestEditView fragmentGuestEditView = (GuestEditView) getSupportFragmentManager().
                findFragmentByTag(FRAGMENT_GUEST_ADD_VIEW_TAG);
        if (fragmentGuestEditView != null) {
            fragmentGuestEditView.addGuest();
        }
    }

    @Override
    public void onGuestSaved() {
        presenter.onGuestDataSaved();
    }

    @Override
    public void onShowGuestListView() {
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.guest_activity));
        GuestListView fragmentGuestListView = new GuestListView();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentGuestListView, FRAGMENT_GUEST_ADD_VIEW_TAG).commit();
    }

    @Override
    public void onShowGuestDetailView(long id) {
        Log.d(LOG_TAG, "onShowGuestDetailView");

        Bundle args = new Bundle();
        GuestDetailView fragmentGuestDetailView = new GuestDetailView();

        args.putLong("FRAG_GUEST_DETAIL_EVENT_ID", id);

        fragmentGuestDetailView.setArguments(args);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentGuestDetailView, FRAGMENT_DETAIL_VIEW_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSetFabToGuestDetailStatus() {
        if (fab != null) {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_mode_edit_white_24dp));
        }
    }

    @Override
    public void onChangeToolbarTitleToGuestName(String guestName) {
        collapsingToolbarLayout.setTitle(guestName);
    }

    @Override
    public void onShowGuestEditDetailView() {
        GuestDetailView fragmentGuestDetailView = (GuestDetailView ) getSupportFragmentManager().
                findFragmentByTag(FRAGMENT_DETAIL_VIEW_TAG);
        if (fragmentGuestDetailView != null) {
            Bundle args = fragmentGuestDetailView.getDetails();
            collapsingToolbarLayout.setTitle(getResources().getString(R.string.guest_edit_menu));
            GuestEditView fragmentGuestAddView = new GuestEditView();
            fragmentGuestAddView.setArguments(args);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_content, fragmentGuestAddView , FRAGMENT_GUEST_ADD_VIEW_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onGuestEditViewResumed() {
        presenter.guestEditViewResume();
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.guest_edit_menu));
    }

    @Override
    public void onGuestDetailViewResumed() {
        presenter.guestDetailViewResume();
    }

    @Override
    public void onAddressForMap(String address) {
        Log.d(LOG_TAG, "onAddressForMap");
        GuestSetMapLocationView fragmentMapView = (GuestSetMapLocationView) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_MAP_VIEW_TAG);

        if (fragmentMapView == null) {
            fragmentMapView = new GuestSetMapLocationView();
            Bundle args = new Bundle();
            args.putString(GuestSetMapLocationView.FRAG_MAP_ADDRESS_ARG, address);
            fragmentMapView.setArguments(args);

        } else {
            fragmentMapView.setAddress(address);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, fragmentMapView, FRAGMENT_MAP_VIEW_TAG)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onMapViewGone() {
        Log.d(LOG_TAG, "onMapViewGone");
        //getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed");
        super.onBackPressed();

        List<Fragment> listFragment = getSupportFragmentManager().getFragments();
        for (Fragment fragment:listFragment) {
            if (fragment != null)
                Log.d(LOG_TAG, "Fragment: " + fragment.getTag());
            else
                Log.d(LOG_TAG, "Fragment: null");
        }

        GuestSetMapLocationView fragmentMapView = (GuestSetMapLocationView) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_MAP_VIEW_TAG);

        if (fragmentMapView != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentMapView).commit();
        }
    }
}
