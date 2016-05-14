package app.com.ttins.gettogether.eventedit;


import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import app.com.ttins.gettogether.data.GetTogetherContract;

public class EventEditModel implements EventEditMVP.ModelOps {

    private static final String LOG_TAG =EventEditModel.class.getSimpleName();

    EventEditMVP.RequiredPresenterOps presenter;
    Context viewContext;
    Uri uri;

    public EventEditModel(EventEditMVP.RequiredPresenterOps presenter) {
        this.presenter = presenter;
    }

    @Override
    public void saveEventData(String title, String location, String meetingLocation, String phone) {

        Uri retUri;
        ContentValues values = new ContentValues();

        if (viewContext != null) {
            values.put(GetTogetherContract.Events.TITLE, title);
            values.put(GetTogetherContract.Events.LOCATION, location);
            values.put(GetTogetherContract.Events.MEETING_LOCATION, meetingLocation);
            values.put(GetTogetherContract.Events.PLACE_PHONE_NUMBER, phone);

            retUri = viewContext.getContentResolver().insert(GetTogetherContract.Events.CONTENT_URI, values);
            if (retUri != null) {
                Log.d(LOG_TAG, "Uri created: " + retUri.toString());
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
