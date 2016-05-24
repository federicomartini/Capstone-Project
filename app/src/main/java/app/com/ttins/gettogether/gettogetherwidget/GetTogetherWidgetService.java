package app.com.ttins.gettogether.gettogetherwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.gson.Event;
import app.com.ttins.gettogether.data.GetTogetherContract;

public class GetTogetherWidgetService extends RemoteViewsService{

    private static final String LOG_TAG = GetTogetherRemoveViewsFactory.class.getSimpleName();

    private ArrayList<Event> events;
    int widgetId;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
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
            this.context = context;
        }

        @Override
        public void onCreate() {
            Cursor cursor = getContentResolver().query(GetTogetherContract.Events.buildEventsUri(),
                    null,
                    null,
                    null,
                    null);

            if (cursor != null) {
                if(cursor.moveToFirst()) {
                    while(!cursor.isAfterLast()) {
                        Event event = new Event();

                        event.setEventId((long) cursor.getInt(cursor.getColumnIndex(GetTogetherContract.Events._ID)));

                        String date = cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.EVENT_YEAR)) + "/" +
                                cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.EVENT_MONTH)) + "/" +
                                cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.EVENT_DAY));
                        event.setEventTime(date);

                        String time = cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.START_TIME_HOUR)) + ":" +
                                cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.START_TIME_MINUTE));
                        event.setEventTime(time);

                        event.setEventTitle(cursor.getString(cursor.getColumnIndex(GetTogetherContract.Events.TITLE)));

                        events.add(event);

                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }

            Log.d(LOG_TAG, "Widget data updated");
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.d(LOG_TAG, "getViewAt: " + position);
            RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.gettogether_widget_item_layout);
            remote.setTextViewText(R.id.event_title_text_view_widget_item_layout, events.get(position).getEventTitle());
            remote.setTextViewText(R.id.date_text_view_widget_item_layout, events.get(position).getEventDate());
            remote.setTextViewText(R.id.time_title_text_view_widget_item_layout, events.get(position).getEventTime());

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

        }

        @Override
        public void onDestroy() {

        }
    }
}
