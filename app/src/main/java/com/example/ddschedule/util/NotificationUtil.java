package com.example.ddschedule.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.ddschedule.R;
import com.example.ddschedule.model.ScheduleModel;

import java.util.List;

public class NotificationUtil {

    public static final String VERBOSE_NOTIFICATION_CHANNEL_NAME = "Live Notification";
    public static final String CHANNEL_ID = "LIVE_NOTIFICATION";
    public static final String GROUP = "LIVE_NOTIFICATION_GROUP";

    private static int NOTIFICATION_ID = 0;

    private Context context;

    public NotificationUtil(Context context) {
        this.context = context;
    }

    public void setupChannel() {
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = VERBOSE_NOTIFICATION_CHANNEL_NAME;
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);

            // Add the channel
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void showNotification(List<ScheduleModel> schedules){
        for (ScheduleModel schedule:schedules) {

            Uri uri = Uri.parse("https://www.youtube.com/watch?v="+schedule.getVideo_id());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Create the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_live_tv_24)
                    .setContentTitle(schedule.getStreamer_name()+"将于"+DateUtil.getDateToString(schedule.getScheduled_start_time(), "HH:mm")+"开播！")
                    .setContentText(schedule.getTitle())
                    .setPriority(NotificationCompat.PRIORITY_LOW)
//                    .setGroup(GROUP)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);;

            // Show the notification
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID++, builder.build());
        }
    }
}
