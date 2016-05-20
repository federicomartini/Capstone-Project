package app.com.ttins.gettogether.common;

public interface GuestMVP {
    interface PresenterOps {
        void onConfigurationChanged(GuestMVP.RequestedViewOps view);
        void onDestroy(boolean isChangingConfig);
        void initFabStatus();
        void onFabClick();
        void guestListViewResume();
        void onGuestDataSaved();
        void onGuestItemClick(long id);
    }

    interface ModelOps {
        void onDestroy();

    }

    interface RequestedPresenterOps {

    }

    interface RequestedViewOps {
        void onSetFabToAddGuestStatus();
        void onSetFabToAddGuestConfirmStatus();
        void onSetFabToGuestDetailStatus();
        void onShowGuestEditView();
        void onShowGuestListView();
        void onSaveGuestDataRequest();
        void onShowGuestDetailView(long id);
        void onShowGuestEditDetailView();
    }
}
