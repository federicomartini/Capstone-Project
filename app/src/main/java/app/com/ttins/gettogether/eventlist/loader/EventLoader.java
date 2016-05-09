package app.com.ttins.gettogether.eventlist.loader;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import app.com.ttins.gettogether.data.GetTogetherContract;

public class EventLoader extends CursorLoader {

    private EventLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, null);
    }

    public static EventLoader allEvents(Context context) {
        return new EventLoader(context, GetTogetherContract.Events.buildEventsUri());
    }

    public static EventLoader eventFromId(Context context, long itemId) {
        return new EventLoader(context, GetTogetherContract.Events.buildEventsUri(itemId));
    }

    public interface Query {
        String[] PROJECTION = {
                GetTogetherContract.Events._ID,
                GetTogetherContract.Events.TITLE,
                GetTogetherContract.Events.PHOTO_PATH,
        };

        int _ID = 0;
        int TITLE = 1;
        int PHOTO_PATH = 2;
    }
}
