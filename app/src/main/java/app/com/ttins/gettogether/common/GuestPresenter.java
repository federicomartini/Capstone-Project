package app.com.ttins.gettogether.common;

import android.util.Log;

import java.lang.ref.WeakReference;

public class GuestPresenter implements GuestMVP.PresenterOps, GuestMVP.RequestedPresenterOps {

    private static final String LOG_TAG = GuestPresenter.class.getSimpleName();

    private static final int FAB_STATUS_ADD_GUEST = 0;
    private static final int FAB_STATUS_ADD_GUEST_CONFIRM = 1;


    WeakReference<GuestMVP.RequestedViewOps> view;
    GuestMVP.ModelOps model;
    boolean isChangingConfig;
    int fabStatus;

    public GuestPresenter(GuestMVP.RequestedViewOps view) {
        this.view = new WeakReference<>(view);
        this.model = new GuestModel(this);
    }

    /**
     * Sent from Activity after a configuration changes
     * @param view  View reference
     */
    @Override
    public void onConfigurationChanged(GuestMVP.RequestedViewOps view) {
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
    public void initFabStatus() {
        setFabStatus(this.fabStatus);
    }

    private void setFabStatus(int fabStatus) {

        this.fabStatus = fabStatus;
        switch (fabStatus) {
            case FAB_STATUS_ADD_GUEST:
                view.get().onSetFabToAddGuestStatus();
                break;
            case FAB_STATUS_ADD_GUEST_CONFIRM:
                view.get().onSetFabToAddGuestConfirmStatus();
                break;
            default:
                break;
        }
        Log.d(LOG_TAG, "fabStatus: " + fabStatus);
    }
}
