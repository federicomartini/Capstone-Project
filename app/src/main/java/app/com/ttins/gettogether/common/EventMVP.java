package app.com.ttins.gettogether.common;

public interface EventMVP {

    interface PresenterOps {
        void onAddEventFabClick();
        void eventListViewResume();
        void initFabStatus();
    }

    interface ModelOps {

    }

    interface RequestedPresenterOps {

    }

    interface RequestedViewOps {
        void onShowEventEditView();
        void onSetFabToAddEventStatus();
        void onSetFabToAddEventConfirmStatus();
    }

}
