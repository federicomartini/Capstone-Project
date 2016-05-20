package app.com.ttins.gettogether.guestedit;


import android.content.Context;

public interface GuestEditMVP {
    interface PresenterOps {
        void saveGuest(String guestName, String phoneNumber, String address);
        void onAttachView(Context context);
        void onDetachView();
    }

    interface ModelOps {
        void saveGuestData(String guestName, String phoneNumber, String address);
        void onAttachView(Context context);
        void onDetachView();
    }

    interface RequestedPresenterOps {
        void onGuestSaved();
    }

    interface RequestedViewOps {
        void onShowToast(String message);
        void onGuestSaved();
    }
}
