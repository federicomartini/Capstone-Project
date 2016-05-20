package app.com.ttins.gettogether.guestlist.loader;

import android.content.Context;
import android.support.v4.content.CursorLoader;
import android.net.Uri;

import app.com.ttins.gettogether.data.GetTogetherContract;

public class GuestLoader extends CursorLoader{

    public GuestLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, null);
    }

    public static GuestLoader allGuests(Context context) {
        return new GuestLoader(context, GetTogetherContract.Guests.buildGuestsUri());
    }

    public static GuestLoader guestFromId(Context context, long id) {
        return new GuestLoader(context, GetTogetherContract.Guests.buildGuestsUri(id));
    }

    public interface Query {
        String[] PROJECTION = {
                GetTogetherContract.Guests._ID,
                GetTogetherContract.Guests.NAME,
                GetTogetherContract.Guests.PHOTO_PATH
        };

        int _ID = 0;
        int NAME = 1;
        int PHOTO_PATH = 2;
    }
}
