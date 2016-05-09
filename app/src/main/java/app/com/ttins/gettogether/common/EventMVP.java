package app.com.ttins.gettogether.common;

public interface EventMVP {

    interface PresenterOps {
        void onAddEventFabClick();
        void eventListViewResume();
        void initFabStatus();
        void onConfigurationChanged(EventMVP.RequestedViewOps view);
        void onDestroy(boolean isChangingConfig);
    }

    interface ModelOps {
        void onDestroy();
    }

    interface RequestedPresenterOps {

    }

    interface RequestedViewOps {
        void onShowEventEditView();
        void onSetFabToAddEventStatus();
        void onSetFabToAddEventConfirmStatus();
    }

}
