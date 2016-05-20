package app.com.ttins.gettogether.common;

public interface GuestMVP {
    interface PresenterOps {
        void onConfigurationChanged(GuestMVP.RequestedViewOps view);
        void onDestroy(boolean isChangingConfig);
        void initFabStatus();
    }

    interface ModelOps {
        void onDestroy();

    }

    interface RequestedPresenterOps {

    }

    interface RequestedViewOps {
        void onSetFabToAddGuestStatus();
        void onSetFabToAddGuestConfirmStatus();
    }
}
