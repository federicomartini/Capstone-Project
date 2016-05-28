package app.com.ttins.gettogether.guestdetail;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;

import java.util.HashMap;

public interface GuestDetailMVP {
    interface PresenterOps {
        void onPopulateView(long id);
        void onAttachView(GuestDetailMVP.RequestedViewOps view);
        void onDetachView();
        void initLoader();
        void onMapClick(String address);
    }

    interface ModelOps {
        void getGuestData(long id);
        void initLoader();
    }

    interface RequestedPresenterOps {
        void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass);
        Context onContextViewRequired();
        void onGuestLoadFinished(HashMap<Integer, String> dataMap);
    }

    interface RequestedViewOps {
        void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass);
        Context onContextViewRequired();
        void onChangeGuestName(String guestName);
        void onChangePhoneNumber(String phoneNumber);
        void onChangeAddress(String address);
        void onChangeGuestPhoto(String photoSrc);
        void onSendAddressToActivity(String address);
    }
}
