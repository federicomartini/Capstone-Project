package app.com.ttins.gettogether.common;

import android.util.Log;

import java.lang.ref.WeakReference;

public class GuestPresenter implements GuestMVP.PresenterOps, GuestMVP.RequestedPresenterOps {

    private static final String LOG_TAG = GuestPresenter.class.getSimpleName();

    private static final int FAB_STATUS_ADD_GUEST = 0;
    private static final int FAB_STATUS_ADD_GUEST_CONFIRM = 1;
    private static final int FAB_GUEST_DETAIL = 2;
    private static final int FAB_GUEST_EDIT_DETAIL = 3;



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
            case FAB_GUEST_DETAIL:
                view.get().onSetFabToGuestDetailStatus();
            default:
                break;
        }
        Log.d(LOG_TAG, "fabStatus: " + fabStatus);
    }

    @Override
    public void onFabClick() {
        switch (fabStatus) {
            case FAB_STATUS_ADD_GUEST:
                onAddGuestFabClick();
                break;
            case FAB_STATUS_ADD_GUEST_CONFIRM:
                onAddGuestConfirmFabClick();
                break;
            case FAB_GUEST_DETAIL:
                onEditGuestFabClick();
                break;
            default:
                break;
        }
    }

    void onEditGuestFabClick() {
        Log.d(LOG_TAG, "onEditGuestFabClick");
        view.get().onShowGuestEditDetailView();
    }

    void onAddGuestFabClick() {
        Log.d(LOG_TAG, "onAddGuestFabClick");
        view.get().onShowGuestEditView();
        setFabStatus(FAB_STATUS_ADD_GUEST_CONFIRM);
    }

    void onAddGuestConfirmFabClick() {
        view.get().onSaveGuestDataRequest();
        //setFabStatus(FAB_STATUS_ADD_GUEST);
    }

    @Override
    public void guestListViewResume() {
        Log.d(LOG_TAG, "GuestListView resumed");
        setFabStatus(FAB_STATUS_ADD_GUEST);
    }

    @Override
    public void onGuestDataSaved() {
        view.get().onShowGuestListView();
        setFabStatus(FAB_STATUS_ADD_GUEST);
    }

    @Override
    public void onGuestItemClick(long id) {
        Log.d(LOG_TAG, "onGuestItemClick");
        view.get().onShowGuestDetailView(id);
        setFabStatus(FAB_GUEST_DETAIL);
    }

    @Override
    public void guestEditViewResume() {
        Log.d(LOG_TAG, "GuestEditView resumed");
        setFabStatus(FAB_STATUS_ADD_GUEST_CONFIRM);
    }

    @Override
    public void guestDetailViewResume() {
        Log.d(LOG_TAG, "GuestDetailView resumed");
        setFabStatus(FAB_GUEST_DETAIL);
    }
}
