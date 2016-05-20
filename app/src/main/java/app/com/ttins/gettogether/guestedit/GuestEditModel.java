package app.com.ttins.gettogether.guestedit;


import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import app.com.ttins.gettogether.data.GetTogetherContract;

public class GuestEditModel implements GuestEditMVP.ModelOps {

    private static final String LOG_TAG = GuestEditModel.class.getSimpleName();

    GuestEditMVP.RequestedPresenterOps presenter;
    Context viewContext;

    public GuestEditModel(GuestEditMVP.RequestedPresenterOps presenter) {
        this.presenter = presenter;
    }

    @Override
    public void saveGuestData(String guestName, String phoneNumber, String address) {

        Uri retUri;
        ContentValues values = new ContentValues();

        if (viewContext != null) {
            values.put(GetTogetherContract.Guests.NAME, guestName);
            values.put(GetTogetherContract.Guests.PHONE_NUMBER, phoneNumber);
            values.put(GetTogetherContract.Guests.ADDRESS, address);

            retUri = viewContext.getContentResolver().insert(GetTogetherContract.Guests.CONTENT_URI, values);
            if (retUri != null) {
                Log.d(LOG_TAG, "Uri created: " + retUri.toString());
                presenter.onGuestSaved();
            } else {
                Log.d(LOG_TAG, "Uri returned after insert is null! ");
            }

        }
    }

    @Override
    public void onAttachView(Context context) {
        this.viewContext = context;
    }

    @Override
    public void onDetachView() {
        this.viewContext = null;
    }
}
