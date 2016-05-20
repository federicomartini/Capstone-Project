package app.com.ttins.gettogether.common;

public interface GuestMVP {
    interface PresenterOps {
        void onConfigurationChanged(GuestMVP.RequestedViewOps view);
        void onDestroy(boolean isChangingConfig);
        void initFabStatus();
        void onFabClick();
        void guestListViewResume();
        void onGuestDataSaved();
    }

    interface ModelOps {
        void onDestroy();

    }

    interface RequestedPresenterOps {

    }

    interface RequestedViewOps {
        void onSetFabToAddGuestStatus();
        void onSetFabToAddGuestConfirmStatus();
        void onShowGuestEditView();
        void onShowGuestListView();
        void onSaveGuestDataRequest();
    }
}
