package app.com.ttins.gettogether.gettogetherwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.EventActivity;
import app.com.ttins.gettogether.gettogetherwidget.alarm.AlarmManagerBroadcastReceiver;

public class GetTogetherWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = GetTogetherWidgetProvider.class.getSimpleName();

    public static final String EXTRA_ITEM = "app.com.ttins.gettogether.EXTRA_ITEM";
    public static final String CLICK_ACTION = "app.com.ttins.gettogether.gettogetherwidget.CLICK_ACTION";
    public static final String EXTRA_REFRESH = "app.com.ttins.gettogether.gettogetherwidget.EXTRA_REFRESH";

    static AlarmManager alarmManager;
    static PendingIntent alarmPendingIntent;

    public static void saveAlarmManager(AlarmManager am, PendingIntent pi) {
        alarmManager = am;
        alarmPendingIntent = pi;
    }

    public static void updateWidget(Context context) {
        Log.d(LOG_TAG, "updateWidget request");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, GetTogetherWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.events_list_view_widget);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Toast.makeText(context, "TimeWidgetRemoved id(s):" + appWidgetIds, Toast.LENGTH_SHORT).show();
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000 * 3600, 1000 , pi);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals(GetTogetherWidgetProvider.CLICK_ACTION)) {
            long eventId = intent.getLongExtra(EXTRA_ITEM, 0);
            Intent eventDetailsIntent = new Intent(context, EventActivity.class);
            eventDetailsIntent.putExtra(EventActivity.EVENT_DETAIL, eventId);
            eventDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(eventDetailsIntent);
            Log.d(LOG_TAG, "Widget CLICK_ACTION");
        } else if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE) ||
                intent.getAction().equals(EXTRA_REFRESH)) {
            Log.d(LOG_TAG, "Widget ACTION_APPWIDGET_UPDATE");
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, GetTogetherWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(cn),
                    R.id.events_list_view_widget);
        }

        Log.d(LOG_TAG, "onReceive");
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        Log.d(LOG_TAG, "onUpdate");

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, GetTogetherWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.gettogether_widget_layout);
            Intent clickIntent = new Intent(context, GetTogetherWidgetProvider.class);
            clickIntent.setAction(GetTogetherWidgetProvider.CLICK_ACTION);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.events_list_view_widget, clickPendingIntent);
            //views.setOnClickPendingIntent(R.id.button, pendingIntent);
            views.setRemoteAdapter(R.id.events_list_view_widget, intent);
            views.setEmptyView(R.id.events_list_view_widget, R.id.empty_events_list_view_widget);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            Log.d(LOG_TAG, "updateAppWidget: " + appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled():last widget instance removed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }


}
