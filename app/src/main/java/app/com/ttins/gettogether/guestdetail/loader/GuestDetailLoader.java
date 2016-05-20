package app.com.ttins.gettogether.guestdetail.loader;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import app.com.ttins.gettogether.data.GetTogetherContract;

public class GuestDetailLoader extends CursorLoader {

    public GuestDetailLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, null);
    }

    static public GuestDetailLoader allGuestDetails(Context context) {
        return new GuestDetailLoader(context, GetTogetherContract.Guests.buildGuestsUri());
    }

    static public GuestDetailLoader guestFromId(Context context, long id) {
        return new GuestDetailLoader(context, GetTogetherContract.Guests.buildGuestsUri(id));
    }


    public interface Query {
        String[] PROJECTION = {
                GetTogetherContract.Guests._ID,
                GetTogetherContract.Guests.NAME,
                GetTogetherContract.Guests.PHONE_NUMBER,
                GetTogetherContract.Guests.ADDRESS,
                GetTogetherContract.Guests.PHOTO_PATH
        };

        int _ID = 0;
        int NAME = 1;
        int PHONE_NUMBER = 2;
        int ADDRESS = 3;
        int PHOTO_PATH = 4;
    }

}
