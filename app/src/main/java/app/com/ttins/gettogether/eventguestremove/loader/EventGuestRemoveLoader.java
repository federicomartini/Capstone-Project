package app.com.ttins.gettogether.eventguestremove.loader;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import app.com.ttins.gettogether.data.GetTogetherContract;
import app.com.ttins.gettogether.eventlist.loader.EventLoader;

public class EventGuestRemoveLoader extends CursorLoader {

    public EventGuestRemoveLoader(Context context, Uri uri) {
        super(context, uri, EventLoader.Query.PROJECTION, null, null, null);
    }

    public static EventGuestRemoveLoader allGuests(Context context) {
        return new EventGuestRemoveLoader(context, GetTogetherContract.Events.buildEventsUri());
    }

    public static EventGuestRemoveLoader guestFromId(Context context, long id) {
        return new EventGuestRemoveLoader(context, GetTogetherContract.Events.buildEventsUri(id));
    }

    interface Query {
        String[] PROJECTION = {
                GetTogetherContract.Events._ID,
                GetTogetherContract.Events.GUEST_LIST
        };

        int _ID = 0;
        int GUEST_LIST = 1;
    }


}
