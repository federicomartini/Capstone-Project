package app.com.ttins.gettogether.eventedit.loader;


import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import app.com.ttins.gettogether.data.GetTogetherContract;

public class EventEditLoader extends CursorLoader {

    public EventEditLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, null);
    }

    public static EventEditLoader allEventEditItems(Context context) {
        return new EventEditLoader(context, GetTogetherContract.Events.buildEventsUri());
    }

    public static EventEditLoader eventEditItemFromId(Context context, long id) {
        return new EventEditLoader(context, GetTogetherContract.Events.buildEventsUri(id));
    }

    public interface Query {
        String[] PROJECTION = {
                GetTogetherContract.Events._ID,
                GetTogetherContract.Events.TITLE,
                GetTogetherContract.Events.LOCATION,
                GetTogetherContract.Events.MEETING_LOCATION,
                GetTogetherContract.Events.PLACE_NAME,
                GetTogetherContract.Events.PLACE_PHONE_NUMBER,
                GetTogetherContract.Events.EVENT_DAY,
                GetTogetherContract.Events.EVENT_MONTH,
                GetTogetherContract.Events.EVENT_YEAR,
                GetTogetherContract.Events.START_TIME_HOUR,
                GetTogetherContract.Events.START_TIME_MINUTE,
                GetTogetherContract.Events.END_TIME_HOUR,
                GetTogetherContract.Events.END_TIME_MINUTE,
                GetTogetherContract.Events.EVENT_TYPE,
                GetTogetherContract.Events.PHOTO_PATH,
                GetTogetherContract.Events.CONFIRMATION_STATUS,
                GetTogetherContract.Events.GUEST_LIST,
                GetTogetherContract.Events.NOTES,
                GetTogetherContract.Events.ATTENDEE_NUMBER
        };

        int _ID = 0;
        int TITLE = 1;
        int LOCATION = 2;
        int MEETING_LOCATION = 3;
        int PLACE_NAME = 4;
        int PLACE_PHONE_NUMBER = 5;
        int EVENT_DAY = 6;
        int EVENT_MONTH = 7;
        int EVENT_YEAR = 8;
        int START_TIME_HOUR = 9;
        int START_TIME_MINUTE = 10;
        int END_TIME_HOUR = 11;
        int END_TIME_MINUTE = 12;
        int EVENT_TYPE = 13;
        int PHOTO_PATH = 14;
        int CONFIRMATION_STATUS = 15;
        int GUEST_LIST = 16;
        int NOTES = 17;
        int PATH_EVENTS = 18;
        int ATTENDEE_NUMBER = 19;
    }
}
