package app.com.ttins.gettogether.common;

public interface EventMVP {

    interface PresenterOps {
        void onFabClick();
        void onEventLongClick();
        void eventListViewResume();
        void initFabStatus();
        void onConfigurationChanged(EventMVP.RequestedViewOps view);
        void onDestroy(boolean isChangingConfig);
        void onEventDataSaved();
        void onEventItemClick(long id);
        void guestMenuItemClick();
        void eventEditViewResume();
        void eventDetailViewResume();
        void eventGuestHandlerResume();
        void onFabAddGuestClick();
        void onFabGuestRemoveClick();
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
        void onSetFabToGuestStatus();
        void onsetFabToAddGuestToListStatus();
        void onSaveEventDataRequest();
        void onShowEventDetailView(long id);
        void onOpenFabGuestAnimation();
        void onCloseFabGuestAnimation();
        void onOpenFabGuestRemoveAnimation();
        void onCloseFabGuestRemoveAnimation();
        void onOpenGuestActivity();
        void onShowGuestHandlerView();
        void onShowEventGuestRemoveView();
    }

}
