package app.com.ttins.gettogether.common;

import android.util.Log;

import java.lang.ref.WeakReference;

public class EventPresenter implements EventMVP.PresenterOps, EventMVP.RequestedPresenterOps {

    private static final String LOG_TAG = EventPresenter.class.getSimpleName();

    private static final int FAB_STATUS_ADD_EVENT           = 0;
    private static final int FAB_STATUS_ADD_EVENT_CONFIRM   = 1;
    private static final int FAB_STATUS_GUEST               = 2;
    private static final int FAB_STATUS_GUEST_OPENED        = 3;
    private static final int FAB_STATUS_GUEST_CLOSED        = 4;

    private WeakReference<EventMVP.RequestedViewOps> view;
    private EventMVP.ModelOps model;
    private int fabStatus;
    // Configuration change state
    private boolean isChangingConfig;

    public EventPresenter(EventMVP.RequestedViewOps view){
        this.view = new WeakReference<>(view);
        this.model = new EventModel(this);
    }

    @Override
    public void initFabStatus() {
        setFabStatus(this.fabStatus);
    }

    @Override
    public void onFabClick() {
        switch (fabStatus) {
            case FAB_STATUS_ADD_EVENT:
                onAddEventFabClick();
                break;
            case FAB_STATUS_ADD_EVENT_CONFIRM:
                onAddEventConfirmFabClick();
                break;
            case FAB_STATUS_GUEST:
            case FAB_STATUS_GUEST_OPENED:
            case FAB_STATUS_GUEST_CLOSED:
                onGuestFabClick();
                break;
            default:
                break;
        }
    }

    public void onAddEventFabClick() {
        view.get().onShowEventEditView();
        setFabStatus(FAB_STATUS_ADD_EVENT_CONFIRM);
    }

    private void onAddEventConfirmFabClick() {
        /*TODO: Shouldn't be done here because if therea re some problems with insert data
        we don't want to show back the eventListView */
        view.get().onSaveEventDataRequest();
    }

    private void onGuestFabClick() {
        switch(fabStatus) {
            case FAB_STATUS_GUEST:
            case FAB_STATUS_GUEST_CLOSED:
                setFabStatus(FAB_STATUS_GUEST_OPENED);
                break;
            case FAB_STATUS_GUEST_OPENED:
                setFabStatus(FAB_STATUS_GUEST_CLOSED);
                break;
            default:
                Log.d(LOG_TAG, "Unknown click event");
                break;
        }
    }

    private void setFabStatus(int fabStatus) {
        forceCloseStatus(fabStatus);

        this.fabStatus = fabStatus;
        switch (fabStatus) {
            case FAB_STATUS_ADD_EVENT:
                view.get().onSetFabToAddEventStatus();
                break;
            case FAB_STATUS_ADD_EVENT_CONFIRM:
                view.get().onSetFabToAddEventConfirmStatus();
                break;
            case FAB_STATUS_GUEST:
                view.get().onSetFabToGuestStatus();
                break;
            case FAB_STATUS_GUEST_OPENED:
                view.get().onSetFabToGuestStatus();
                view.get().onOpenFabGuestAnimation();
                break;
            case FAB_STATUS_GUEST_CLOSED:
                view.get().onSetFabToGuestStatus();
                view.get().onCloseFabGuestAnimation();
                break;
            default:
                break;
        }
        Log.d(LOG_TAG, "fabStatus: " + fabStatus);
    }

    private void forceCloseStatus(int fabStatus) {
        if(this.fabStatus == FAB_STATUS_GUEST_OPENED) {
            if (fabStatus != this.fabStatus ) {
                view.get().onCloseFabGuestAnimation();
            }
        }
    }


    /**
     * Sent from Activity after a configuration changes
     * @param view  View reference
     */
    @Override
    public void onConfigurationChanged(EventMVP.RequestedViewOps view) {
        Log.d(LOG_TAG, "onConfigurationChanged");
        this.view = new WeakReference<>(view);
    }

    /**
     * Receives {@link MainActivity#onDestroy()} event
     * @param isChangingConfig  Config change state
     */
    @Override
    public void onDestroy(boolean isChangingConfig) {
        this.view = null;
        this.isChangingConfig = isChangingConfig;
        if (!this.isChangingConfig) {
            this.model.onDestroy();
        }
    }

    @Override
    public void eventListViewResume() {
        Log.d(LOG_TAG, "eventListView resumed");
        setFabStatus(FAB_STATUS_ADD_EVENT);
    }

    @Override
    public void eventEditViewResume() {
        Log.d(LOG_TAG, "eventEditView resumed");
        setFabStatus(FAB_STATUS_ADD_EVENT_CONFIRM);
    }

    @Override
    public void onEventDataSaved() {
        view.get().onShowEventListView();
        setFabStatus(FAB_STATUS_ADD_EVENT);
    }

    @Override
    public void onEventLongClick() {

    }

    @Override
    public void onEventItemClick(long id) {
        Log.d(LOG_TAG, "onEventItemClick");
        view.get().onShowEventDetailView(id);
        setFabStatus(FAB_STATUS_GUEST);

    }

    @Override
    public void guestMenuItemClick() {
        view.get().onOpenGuestActivity();
    }

    @Override
    public void eventDetailViewResume() {
        setFabStatus(FAB_STATUS_GUEST);
    }
}
