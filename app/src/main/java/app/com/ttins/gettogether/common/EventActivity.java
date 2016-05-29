package app.com.ttins.gettogether.common;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.gson.Event;
import app.com.ttins.gettogether.common.persistence.EventStateMaintainer;
import app.com.ttins.gettogether.common.ui.ThreeTwoImageView;
import app.com.ttins.gettogether.common.utils.Permissions;
import app.com.ttins.gettogether.datepickerdialog.DatePickerDialogView;
import app.com.ttins.gettogether.eventdetail.EventDetailView;
import app.com.ttins.gettogether.eventedit.EventEditView;
import app.com.ttins.gettogether.eventguesthandler.EventGuestHandlerView;
import app.com.ttins.gettogether.eventlist.EventListView;
import app.com.ttins.gettogether.eventsetplace.EventSetPlaceView;
import app.com.ttins.gettogether.gettogetherwidget.GetTogetherWidgetProvider;
import app.com.ttins.gettogether.timepickerdialog.TimePickerDialogView;
import butterknife.ButterKnife;


public class EventActivity extends AppCompatActivity implements EventMVP.RequestedViewOps,
        EventListView.Callback, EventEditView.Callback, EventDetailView.Callback,
        EventGuestHandlerView.Callback, TimePickerDialogView.Callback, DatePickerDialogView.Callback {

    private final String LOG_TAG = getClass().getSimpleName();

    private static final String FRAGMENT_LIST_VIEW_TAG = "FRAG_LIST_VIEW";
    private static final String FRAGMENT_EDIT_VIEW_TAG = "FRAG_EDIT_VIEW";
    private static final String FRAGMENT_DETAIL_VIEW_TAG = "FRAG_DETAIL_VIEW";
    private static final String FRAGMENT_GUEST_HANDLER_VIEW_TAG = "FRAGMENT_GUEST_HANDLER_VIEW_TAG";
    private static final String FRAGMENT_PLACE_VIEW_TAG = "FRAGMENT_PLACE_VIEW_TAG";

    public static final String EVENT_DETAIL = "app.com.ttins.gettogether.common.EventActivity.MATCH_DETAIL";

    private static final int MY_MANAGE_DOCUMENTS = 1;

    private EventMVP.PresenterOps presenter;
    private ThreeTwoImageView toolbarEventPhoto;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Animation fab_guest_open, fab_guest_close;
    private AppBarLayout appBarLayout;
    private final EventStateMaintainer stateMaintainer =
            new EventStateMaintainer( this.getSupportFragmentManager(), LOG_TAG );
    private String photoUriPermission = null;
    private String toolbarPhotoUriPending = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar_event_activity);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout_event_activity);
        fab_guest_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_guest_open);
        fab_guest_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_guest_close);
        setSupportActionBar(toolbar);
        toolbarEventPhoto = (ThreeTwoImageView) findViewById(R.id.square_image_view_event_view);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout_event_activity);
        ButterKnife.bind(this);



        startMVPOps();


        fab = (FloatingActionButton) findViewById(R.id.fab_event_event_activity);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "fab onClick");
                    presenter.onFabClick();

                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                    for (Fragment fragment:fragments) {
                        if (fragment != null)
                            Log.d(LOG_TAG, "Fragment Tag: " + fragment.getTag());
                        else
                            Log.d(LOG_TAG, "Fragment Tag: null");
                    }

                }
            });


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED ) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.MANAGE_DOCUMENTS)) {
                    Toast.makeText(this,"should show explanation", Toast.LENGTH_LONG).show();
                }
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.MANAGE_DOCUMENTS},
                        MY_MANAGE_DOCUMENTS);
            }

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
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        presenter.initFabStatus();

        if (toolbarPhotoUriPending != null) {
            Log.d(LOG_TAG, "setting pending Toolbar Photo " + toolbarPhotoUriPending);
            Glide
            toolbarPhotoUriPending = null;
        }

        Intent intent = getIntent();

        if (intent.hasExtra(EVENT_DETAIL)) {
            long eventId = intent.getLongExtra(EVENT_DETAIL, 0);
            intent.removeExtra(EVENT_DETAIL);

            onShowEventDetailView(eventId);
        }

        //broadcastForWidget();
        GetTogetherWidgetProvider.updateWidget(this);
    }

    private void broadcastForWidget() {
        Intent updateIntent = new Intent(this, GetTogetherWidgetProvider.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), GetTogetherWidgetProvider.class));
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(updateIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
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
        Log.d(LOG_TAG, "onEventListViewResume");
        toolbarEventPhoto.setImageBitmap(null);
        removeAnyFragmentAndTransactions();
        presenter.eventListViewResume();
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
    }

    @Override
    public void onSetFabToAddEventStatus() {
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(ContextCompat.getDrawable(this.getBaseContext(),
                    R.drawable.ic_add_white_36dp));
        } else {
            Log.d(LOG_TAG, "Error setting FAB drawables: FAB is null");
        }
    }

    @Override
    public void onSetFabToAddEventConfirmStatus() {
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(ContextCompat.getDrawable(this.getBaseContext(),
                    R.drawable.ic_check_white_36dp));
        } else {
            Log.d(LOG_TAG, "Error setting FAB drawables: FAB is null");
        }
    }

    @Override
    public void onShowEventEditView() {
        Log.d(LOG_TAG, "onShowEventEditView");
        collapsingToolbarLayout.setTitle("Edit Event");
        EventEditView fragmentEventEditView = new EventEditView();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentEventEditView, FRAGMENT_EDIT_VIEW_TAG)
                //.addToBackStack(null)
                .commit();

    }

    @Override
    public void onShowEventListView() {
        Log.d(LOG_TAG, "onShowEventListView");

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment:fragments) {
            if (fragment != null) {
                Log.d(LOG_TAG, "Fragment " + fragment.getTag() + " removed");
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();

        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        EventListView fragmentEventListView = new EventListView();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentEventListView, FRAGMENT_LIST_VIEW_TAG)
                .commit();

        Log.d(LOG_TAG, "Setting toolbar Image to null");
        toolbarEventPhoto.setImageBitmap(null);

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
        GetTogetherWidgetProvider.updateWidget(getApplicationContext());
        presenter.onEventDataSaved();
    }

    @Override
    public void onEventEdited(long id) {
        GetTogetherWidgetProvider.updateWidget(getApplicationContext());
        presenter.onEventDataEdited(id);
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
    public void onShowEventEditedDetailView(long id) {
        Log.d(LOG_TAG, "onShowEventDetailView");

        getSupportFragmentManager().popBackStack();

        Bundle args = new Bundle();
        EventDetailView fragmentEventDetailView = new EventDetailView();

        args.putLong("FRAG_EVENT_DETAIL_EVENT_ID", id);

        fragmentEventDetailView.setArguments(args);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_content, fragmentEventDetailView, FRAGMENT_DETAIL_VIEW_TAG)
                .commit();
    }

    @Override
    public void onSetFabToGuestStatus() {
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(ContextCompat.getDrawable(this.getBaseContext(),
                    R.drawable.ic_person_add_white_36dp));
        } else {
            Log.d(LOG_TAG, "Error setting FAB drawables: FAB is null");
        }
    }

    @Override
    public void onChangeToolbarToEventTitle(String eventTitle) {
        collapsingToolbarLayout.setTitle(eventTitle);
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
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.event_edit_menu));
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


    @Override
    public void onShowEventGuestRemoveView() {
        Log.d(LOG_TAG, "onShowEventGuestRemoveView");

    }

    @Override
    public void onShowTimePickerDialog(String dialogTag) {
        Log.d(LOG_TAG, "onShowTimePickerDialog");
        TimePickerDialogView timePickerDialogView = new TimePickerDialogView();
        timePickerDialogView.show(getSupportFragmentManager(), dialogTag);
    }

    @Override
    public void onShowDatePickerDialog(String dialogTag) {
        Log.d(LOG_TAG, "onShowDatePickerDialog");
        DatePickerDialogView datePickerDialogView = new DatePickerDialogView();
                datePickerDialogView.show(getSupportFragmentManager(), dialogTag);
    }

    @Override
    public void onTimeSet(String tag, String time) {
        EventEditView fragmentEventEditView = (EventEditView)
                getSupportFragmentManager().findFragmentByTag(FRAGMENT_EDIT_VIEW_TAG);

        if(fragmentEventEditView != null)
            fragmentEventEditView.onUpdateDateTimeFromDialog(tag, time);

    }

    @Override
    public void onDateSet(String tag, String date) {
        Log.d(LOG_TAG, "Date = " + date);
        EventEditView fragmentEventEditView = (EventEditView)
                getSupportFragmentManager().findFragmentByTag(FRAGMENT_EDIT_VIEW_TAG);

        if(fragmentEventEditView != null)
            fragmentEventEditView.onUpdateDateTimeFromDialog(tag, date);
    }

    @Override
    public void onShowPlaceView() {
        EventSetPlaceView eventSetPlaceView = (EventSetPlaceView) getSupportFragmentManager()
                            .findFragmentByTag(FRAGMENT_PLACE_VIEW_TAG);

        if(eventSetPlaceView == null) {
            eventSetPlaceView = new EventSetPlaceView();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, eventSetPlaceView, FRAGMENT_PLACE_VIEW_TAG)
                .addToBackStack(null)
                .commit();

        eventSetPlaceView.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d(LOG_TAG, "onPlaceSelected");
                Log.d(LOG_TAG, "Name= " + place.getName());
                Log.d(LOG_TAG, "Address= " + place.getAddress());
                Log.d(LOG_TAG, "Phone= " + place.getPhoneNumber());
                Log.d(LOG_TAG, "Rating= " + place.getRating());
                Log.d(LOG_TAG, "Price level= " + place.getPriceLevel());
                Log.d(LOG_TAG, "WebSite= " + place.getWebsiteUri());

                EventEditView fragmentEventEditView = (EventEditView) getSupportFragmentManager()
                        .findFragmentByTag(FRAGMENT_EDIT_VIEW_TAG);
                /*
                if (fragmentEventEditView == null) {
                    fragmentEventEditView = new EventEditView();
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, fragmentEventEditView)
                        .commit();
                 */
                getSupportFragmentManager().popBackStack();
                fragmentEventEditView.setPlace(place.getName().toString());

            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*  List<Fragment> listFragment = getSupportFragmentManager().getFragments();
            for (Fragment fragment:listFragment) {
            if (fragment != null)
                Log.d(LOG_TAG, "Fragment: " + fragment.getTag());
            else
                Log.d(LOG_TAG, "Fragment: null");
        }*/

        EventSetPlaceView eventSetPlaceView = (EventSetPlaceView) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_PLACE_VIEW_TAG);

        if (eventSetPlaceView != null) {
            getSupportFragmentManager().beginTransaction().remove(eventSetPlaceView).commit();
        }

    }

    void removeAnyFragmentAndTransactions() {
        EventEditView fragmentEditView = (EventEditView) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_EDIT_VIEW_TAG);
        EventDetailView fragmentDetailView = (EventDetailView) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_DETAIL_VIEW_TAG);
        EventSetPlaceView eventSetPlaceView = (EventSetPlaceView) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_PLACE_VIEW_TAG);

        if (fragmentEditView != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentEditView).commit();
        }

        if (fragmentDetailView != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentDetailView).commit();
        }

        if (eventSetPlaceView != null) {
            getSupportFragmentManager().beginTransaction().remove(eventSetPlaceView).commit();
        }

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @Override
    public void onShowEventDetailViewToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetToolbarPhotoBackground(final String photoUri) {
        Log.d(LOG_TAG, "onSetToolbarPhotoBackground photoUri: " + photoUri);

        if(Permissions.checkPermission(this)) {
            if (toolbarEventPhoto != null) {
                Log.d(LOG_TAG, "onSetToolbarPhotoBackground: Permission OK!");
                Glide.with(this).load(photoUri).into(toolbarEventPhoto);
            } else {
                Log.d(LOG_TAG, "toolbarEventPhoto is null");

            }
            photoUriPermission = null;
        } else {
            photoUriPermission = photoUri;
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
                    Log.d(LOG_TAG, "Picasso loading photo: " + photoUriPermission);
                    Glide.with(this).load(photoUriPermission).into(toolbarEventPhoto);
                } else {
                    Log.d(LOG_TAG, "onRequestPermissionsResult: Permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    photoUriPermission = null;
                }
                break;
            default:
                Log.d(LOG_TAG, "RequestCode unknown");
                break;
        }
    }

    @Override
    public void onShowPictureEditViewToolbar(String photoUri) {
        Log.d(LOG_TAG, "onShowPictureEditViewToolbar photoUri: " + photoUri);

        if(Permissions.checkPermission(this)) {
            Log.d(LOG_TAG, "onShowPictureEditViewToolbar: Permissions OK!");
            Glide.with(this)
                    .load(photoUri)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.i(LOG_TAG, "onException: " + e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(toolbarEventPhoto);

            photoUriPermission = null;
        } else {
            photoUriPermission = photoUri;
        }


    }


}
