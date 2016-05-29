package app.com.ttins.gettogether.gettogetherwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

import java.util.ArrayList;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.gson.Event;
import app.com.ttins.gettogether.common.utils.DateTimeFormat;
import app.com.ttins.gettogether.data.GetTogetherContract;

public class GetTogetherWidgetService extends RemoteViewsService{

    private static final String LOG_TAG = GetTogetherRemoveViewsFactory.class.getSimpleName();

    private ArrayList<Event> events;
    int widgetId;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(LOG_TAG, "onGetViewFactory");
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new GetTogetherRemoveViewsFactory(this.getApplicationContext(), intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        events = new ArrayList<>();
    }


    public class GetTogetherRemoveViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context context;

        public GetTogetherRemoveViewsFactory(Context context, Intent intent) {
            Log.d(LOG_TAG, "GetTogetherRemoveViewsFactory constructor");
            this.context = context;
        }

        @Override
        public void onCreate() {
            //AsyncTaskQuery asyncTaskQuery = new AsyncTaskQuery();
            //asyncTaskQuery.execute();
            Log.d(LOG_TAG, "GetTogetherRemoveViewsFactory onCreate");

            /*Cursor cursor = getContentResolver().query(GetTogetherContract.Events.buildEventsUri(),
                    null,
                    null,
                    null,
                    null);

            if (cursor != null) {
                if(cursor.moveToFirst()) {
                    while(!cursor.isAfterLast()) {
                        Event event = new Event();

                        event.setEventId((long) cursor.getInt(cursor.getColumnIndex(GetTogetherContract.Events._ID)));

                        String date = DateTimeFormat.convertDate(cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.EVENT_DAY)),
                                        cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.EVENT_MONTH)),
                                        cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.EVENT_YEAR)));

                        event.setEventDate(date);

                        String time = DateTimeFormat.convertTime(cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.START_TIME_HOUR)),
                                cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.START_TIME_MINUTE)));

                        event.setEventTime(time);

                        event.setEventTitle(cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.TITLE)));

                        event.setEventPhotoPath(cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.PHOTO_PATH)));


                        Log.d(LOG_TAG, "ID = " + event.getEventId() + "Title = " + event.getEventTitle() + " - Date = " + event.getEventDate() + " - Time = " + event.getEventTime());
                        events.add(event);

                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }*/

            Log.d(LOG_TAG, "Widget data updated");
        }

        public class AsyncTaskQuery extends AsyncTask<Void, Void, Cursor> {
            @Override
            protected Cursor doInBackground(Void... params) {
                return getContentResolver().query(GetTogetherContract.Events.buildEventsUri(),
                        null,
                        null,
                        null,
                        null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        while(!cursor.isAfterLast()) {
                            Event event = new Event();

                            event.setEventId((long) cursor.getInt(cursor.getColumnIndex(GetTogetherContract.Events._ID)));

                            String date = DateTimeFormat.convertDate(cursor.getColumnIndex(GetTogetherContract.Events.EVENT_DAY),
                                    cursor.getColumnIndex(GetTogetherContract.Events.EVENT_MONTH),
                                    cursor.getColumnIndex(GetTogetherContract.Events.EVENT_YEAR));

                            event.setEventTime(date);

                            String time = DateTimeFormat.convertTime(cursor.getColumnIndex(GetTogetherContract.Events.START_TIME_HOUR),
                                    cursor.getColumnIndex(GetTogetherContract.Events.START_TIME_MINUTE));

                            event.setEventTime(time);

                            event.setEventTitle(cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.TITLE)));


                            Log.d(LOG_TAG, "ID = " + event.getEventId() + "Title = " + event.getEventTitle() + " - Date = " + event.getEventDate() + " - Time = " + event.getEventTime());
                            events.add(event);

                            cursor.moveToNext();
                        }
                    }
                    cursor.close();
                }
            }
        }

        @Override
        public int getCount() {
            Log.d(LOG_TAG, "getCount = " + events.size());
            return events.size();
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(final int position) {
            Log.d(LOG_TAG, "updateWidget getViewAt: " + position);
            RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.gettogether_widget_item_layout);
            remote.setTextViewText(R.id.event_title_text_view_widget_item_layout, events.get(position).getEventTitle());
            remote.setTextViewText(R.id.date_text_view_widget_item_layout, events.get(position).getEventDate());
            remote.setTextViewText(R.id.time_title_text_view_widget_item_layout, events.get(position).getEventTime());

            /*final AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, remote, R.id.event_image_view_widget_view, widgetId);
            Handler mainHandler = new Handler(context.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    Glide.with(context.getApplicationContext())
                            .load(events.get(position).getEventPhotoPath())
                            .asBitmap()
                            .into(appWidgetTarget);
                }
            };
            mainHandler.post(myRunnable);*/



            Bundle extras = new Bundle();
            extras.putLong(GetTogetherWidgetProvider.EXTRA_ITEM, events.get(position).getEventId());
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            // Make it possible to distinguish the individual on-click
            // action of a given item
            remote.setOnClickFillInIntent(R.id.item_widget_layout, fillInIntent);

            return remote;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDataSetChanged() {
            Log.d(LOG_TAG, "onDataSetChanged");

            final long token = Binder.clearCallingIdentity();
            try {
                Cursor cursor = getContentResolver().query(GetTogetherContract.Events.buildEventsUri(),
                        null,
                        null,
                        null,
                        null);

                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        events.clear();
                        while(!cursor.isAfterLast()) {
                            Event event = new Event();

                            event.setEventId((long) cursor.getInt(cursor.getColumnIndex(GetTogetherContract.Events._ID)));

                            String date = DateTimeFormat.convertDate(cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.EVENT_DAY)),
                                    cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.EVENT_MONTH)),
                                    cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.EVENT_YEAR)));

                            event.setEventDate(date);

                            String time = DateTimeFormat.convertTime(cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.START_TIME_HOUR)),
                                    cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.START_TIME_MINUTE)));

                            event.setEventTime(time);

                            event.setEventTitle(cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.TITLE)));

                            event.setEventPhotoPath(cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.PHOTO_PATH)));


                            Log.d(LOG_TAG, "ID = " + event.getEventId() + "Title = " + event.getEventTitle() + " - Date = " + event.getEventDate() + " - Time = " + event.getEventTime());
                            events.add(event);

                            cursor.moveToNext();
                        }
                    }
                    cursor.close();
                }

            } finally {
                Binder.restoreCallingIdentity(token);
            }

        }

        @Override
        public void onDestroy() {

        }
    }

}
