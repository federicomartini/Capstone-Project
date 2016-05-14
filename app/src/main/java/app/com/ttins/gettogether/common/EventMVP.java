package app.com.ttins.gettogether.common;

public interface EventMVP {

    interface PresenterOps {
        void onFabClick();
        void eventListViewResume();
        void initFabStatus();
        void onConfigurationChanged(EventMVP.RequestedViewOps view);
        void onDestroy(boolean isChangingConfig);
        void onEventDataSaved();
    }

    interface ModelOps {
        void onDestroy();
    }

    interface RequestedPresenterOps {

    }

    interface RequestedViewOps {
        void onShowEventEditView();
        void onShowEventListView();
        void onSetFabToAddEventStatus();
        void onSetFabToAddEventConfirmStatus();
        void onSaveEventDataRequest();
    }

}
