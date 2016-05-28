package app.com.ttins.gettogether.guestedit;


import android.content.Context;

public interface GuestEditMVP {
    interface PresenterOps {
        void saveGuest(Long id, String guestName, String phoneNumber, String address, String photoSrc);
        void saveGuest(String guestName, String phoneNumber, String address, String photoSrc);
        void onAttachView(Context context);
        void onDetachView();
        void onPhotoClick();
    }

    interface ModelOps {
        void saveGuestData(Long id, String guestName, String phoneNumber, String address, String photoSrc);
        void saveGuestData(String guestName, String phoneNumber, String address, String photoSrc);
        void onAttachView(Context context);
        void onDetachView();
    }

    interface RequestedPresenterOps {
        void onGuestSaved();
    }

    interface RequestedViewOps {
        void onShowToast(String message);
        void onGuestSaved();
        void onShowGalleryForPicture();
    }
}
