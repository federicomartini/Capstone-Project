package app.com.ttins.gettogether.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class GetTogetherContract {

    public static final String CONTENT_AUTHORITY = "app.com.ttins.gettogether.gettogetherdatabase";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String DATABASE_NAME = "gettogether.db";
    public static final int DATABASE_VERSIONE = 1;

    public static class Events implements BaseColumns {

        public static final String PATH_EVENTS = "events";
        public static final String TABLE_EVENTS = "events";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENTS).build();

        public static final String TITLE = "title";
        public static final String EVENT_YEAR = "event_year";
        public static final String EVENT_MONTH = "event_month";
        public static final String EVENT_DAY = "event_day";
        public static final String LOCATION = "location";
        public static final String MEETING_LOCATION = "meeting_location";
        public static final String PLACE_NAME = "place_name";
        public static final String PLACE_PHONE_NUMBER = "place_phone_number";
        public static final String START_TIME_HOUR = "start_time_hour";
        public static final String START_TIME_MINUTE = "start_time_minute";
        public static final String END_TIME_HOUR = "end_time_hour";
        public static final String END_TIME_MINUTE = "end_time_minute";
        public static final String EVENT_TYPE = "event_type";
        public static final String PHOTO_PATH = "photo_path";
        public static final String NOTES = "notes";
        public static final String GUEST_LIST = "guest_list";
        public static final String ATTENDEE_NUMBER = "attendee_number";
        public static final String CONFIRMATION_STATUS = "confirmation_status";

        public static final String TABLE_CREATE = "CREATE TABLE " +
                Events.TABLE_EVENTS + "(" +
                _ID + " integer primary key autoincrement, " +
                TITLE + " text non null, " +
                EVENT_YEAR + " integer non null, " +
                EVENT_MONTH + " integer non null, " +
                EVENT_DAY + " integer non null, " +
                LOCATION + " text, " +
                MEETING_LOCATION + " text, " +
                PLACE_NAME + " text non null, " +
                PLACE_PHONE_NUMBER + " text, " +
                START_TIME_HOUR + " integer, " +
                START_TIME_MINUTE + " integer, " +
                END_TIME_HOUR + " integer, " +
                END_TIME_MINUTE + " integer, " +
                EVENT_TYPE + " integer, " +
                PHOTO_PATH + " text, " +
                NOTES + " text, " +
                GUEST_LIST + " text, " +
                ATTENDEE_NUMBER + " integer, " +
                CONFIRMATION_STATUS + " integer);";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_URI + "/" + PATH_EVENTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + CONTENT_URI + "/" + PATH_EVENTS;

        public static Uri buildEventsUri() {
            return CONTENT_URI;
        }

        public static Uri buildEventsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static class Guests implements BaseColumns {

        public static final String PATH_GUESTS = "guests";
        public static final String TABLE_GUESTS = "guests";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GUESTS).build();

        public static final String PHOTO_PATH = "photo_path";
        public static final String NAME = "name";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String ADDRESS = "address";
        public static final String GENDER = "gender";


        public static final String TABLE_CREATE = "CREATE TABLE " +
                Guests.TABLE_GUESTS + "(" +
                _ID + " integer primary key autoincrement, " +
                PHOTO_PATH + " text, " +
                NAME + " text non null, " +
                PHONE_NUMBER + " text, " +
                ADDRESS + " text, " +
                GENDER + " text);";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_URI + "/" + PATH_GUESTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + CONTENT_URI + "/" + PATH_GUESTS;

        public static Uri buildGuestsUri() {
            return CONTENT_URI;
        }

        public static Uri buildGuestsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

}