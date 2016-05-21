package app.com.ttins.gettogether.eventguesthandler.loader;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import app.com.ttins.gettogether.data.GetTogetherContract;

public class EventGuestHandlerLoader extends CursorLoader {

    public EventGuestHandlerLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, null);
    }

    public static EventGuestHandlerLoader allGuests(Context context) {
        return new EventGuestHandlerLoader(context, GetTogetherContract.Guests.buildGuestsUri());
    }

    public static EventGuestHandlerLoader guestFromId(Context context, long id) {
        return new EventGuestHandlerLoader(context, GetTogetherContract.Guests.buildGuestsUri(id));
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
