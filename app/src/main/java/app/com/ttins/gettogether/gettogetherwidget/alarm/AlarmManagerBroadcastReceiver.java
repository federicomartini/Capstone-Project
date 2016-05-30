package app.com.ttins.gettogether.gettogetherwidget.alarm;


import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.RemoteViews;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.gettogetherwidget.GetTogetherWidgetProvider;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");

        wl.acquire();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.gettogether_widget_layout);

        ComponentName thiswidget = new ComponentName(context, GetTogetherWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thiswidget, remoteViews);

        wl.release();
    }
}
