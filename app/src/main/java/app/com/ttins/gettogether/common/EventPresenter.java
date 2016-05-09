package app.com.ttins.gettogether.common;

import android.util.Log;

import java.lang.ref.WeakReference;

public class EventPresenter implements EventMVP.PresenterOps, EventMVP.RequestedPresenterOps {

    private static final String LOG_TAG = EventPresenter.class.getSimpleName();

    private static final int FAB_STATUS_ADD_EVENT           = 0;
    private static final int FAB_STATUS_ADD_EVENT_CONFIRM   = 1;

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
        setFabStatus(fabStatus);
    }

    @Override
    public void onAddEventFabClick() {
        view.get().onShowEventEditView();
        fabStatus = FAB_STATUS_ADD_EVENT_CONFIRM;
        setFabStatus(fabStatus);
    }

    private void setFabStatus(int fabStatus) {
        switch (fabStatus) {
            case FAB_STATUS_ADD_EVENT:
                view.get().onSetFabToAddEventStatus();
                break;
            case FAB_STATUS_ADD_EVENT_CONFIRM:
                view.get().onSetFabToAddEventConfirmStatus();
                break;
            default:
                break;
        }
        Log.d(LOG_TAG, "fatStatus: " + fabStatus);
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
        fabStatus = FAB_STATUS_ADD_EVENT;
        setFabStatus(fabStatus);
    }

}
