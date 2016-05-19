package app.com.ttins.gettogether.eventdetail.loader;


import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import app.com.ttins.gettogether.data.GetTogetherContract;

public class EventDetailLoader extends CursorLoader {

    public EventDetailLoader(Context context, Uri uri) {
        super(context, uri, Query.projection, null, null, null);
    }

    public static EventDetailLoader allEventDetails(Context context) {
        return new EventDetailLoader(context, GetTogetherContract.Events.buildEventsUri());
    }

    public static EventDetailLoader eventDetailFromId(Context context, long id) {
        return new EventDetailLoader(context, GetTogetherContract.Events.buildEventsUri(id));
    }


    public interface Query {
        String[] projection = {
                GetTogetherContract.Events._ID,
                GetTogetherContract.Events.TITLE,
                GetTogetherContract.Events.PHOTO_PATH,
                GetTogetherContract.Events.CONFIRMATION_STATUS,
                GetTogetherContract.Events.EVENT_DAY,
                GetTogetherContract.Events.EVENT_MONTH,
                GetTogetherContract.Events.EVENT_YEAR,
                GetTogetherContract.Events.START_TIME_HOUR,
                GetTogetherContract.Events.START_TIME_MINUTE,
                GetTogetherContract.Events.END_TIME_HOUR,
                GetTogetherContract.Events.END_TIME_MINUTE,
                GetTogetherContract.Events.LOCATION,
                GetTogetherContract.Events.MEETING_LOCATION,
                GetTogetherContract.Events.GUEST_LIST,
                GetTogetherContract.Events.NOTES,
                GetTogetherContract.Events.EVENT_TYPE,
                GetTogetherContract.Events.PLACE_NAME,
                GetTogetherContract.Events.PLACE_PHONE_NUMBER
        };

        int _ID = 0;
        int TITLE = 1;
        int PHOTO_PATH = 2;
        int CONFIRMATION_STATUS = 3;
        int EVENT_DAY = 4;
        int EVENT_MONTH = 5;
        int EVENT_YEAR = 6;
        int START_TIME_HOUR = 7;
        int START_TIME_MINUTE = 8;
        int END_TIME_HOUR = 9;
        int END_TIME_MINUTE = 10;
        int LOCATION = 11;
        int MEETING_LOCATION = 12;
        int GUEST_LIST = 13;
        int NOTES = 14;
        int EVENT_TYPE = 15;
        int PLACE_NAME = 16;
        int PLACE_PHONE_NUMBER = 17;
    }
}
