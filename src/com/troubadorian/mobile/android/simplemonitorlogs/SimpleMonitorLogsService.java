package com.troubadorian.mobile.android.simplemonitorlogs;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.troubadorian.mobile.android.simplemonitorlogs.R;
public class SimpleMonitorLogsService extends Service {
    private Timer timer = new Timer();

    private static final long UPDATE_INTERVAL = 5000;

    private final IBinder mBinder = new MyBinder();

    private ArrayList<String> list = new ArrayList<String>();

    private String[] fixedList = {
            "Simple Monitor v1.0 is running", "Thanks for using Simple Monitor v1.0",
            "Simple Monitor v1.0 is running", "Thanks for using Simple Monitor v1.0"
    };
    
    private PowerManager.WakeLock wl;

    private int index = 0;
    public void onCreate() {
        super.onCreate();
        createNotification("Simple Monitor v1.0 started");
        doKeepRunning();

    }
    private void doKeepRunning() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // populate list
                if (list.size() >= 6) {
                    list.remove(0);
                }

                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int minute = Calendar.getInstance().get(Calendar.MINUTE);
                int second = Calendar.getInstance().get(Calendar.SECOND);
                
                Log.i(getClass().getSimpleName(), "Simple Monitor v1.0 is running at" + " " + hour + ":" + minute + ":" + second);
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
                wl.acquire();
                list.add(fixedList[index++]);
                
                if (index >= fixedList.length) {
                    index = 0;
                }
            }
        }, 0, UPDATE_INTERVAL);
        Log.i(getClass().getSimpleName(), "Simple Monitor v1.0 started.");
    }
    public void createNotification(String notificationText) {
        /* start of displaying app in notification area */
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        int icon = R.drawable.icon;
        CharSequence tickerText = notificationText;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        Context context = getApplicationContext();
        CharSequence contentTitle = "Simple Monitor";
        CharSequence contentText = notificationText;
        Intent notificationIntent = new Intent(this, SimpleMonitorLogsService.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        final int mSimpleMonitorLogs = 2;
        mNotificationManager.notify(mSimpleMonitorLogs, notification);
        /* end of displaying app in notification area */
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        wl.release();
//        if (timer != null) {
//            timer.cancel();
//        }
        Toast.makeText(this, "Simple Monitor v1.0 is still running", Toast.LENGTH_SHORT).show();
        Log.i(getClass().getSimpleName(), "Simple Monitor v1.0 is still running.");
    }
    // We return the binder class upon a call of bindService
    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }
    public class MyBinder extends Binder {
        SimpleMonitorLogsService getService() {
            return SimpleMonitorLogsService.this;
        }
    }
    public List<String> getWordList() {
        return list;
    }
}// end class MyService
