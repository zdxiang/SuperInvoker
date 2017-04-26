package cn.zdxiang.superinvoker.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import cn.zdxiang.superinvoker.R;


/**
 * @author jm
 * @date 17-4-17.下午3:45
 * @description
 */

public class MyNotificationManager {


    /**
     * Notify user to submit order
     *
     * @param context context
     */
    public static void notify(Context context, String title, String url, Bitmap bitmap) {
        Intent LaunchIntent = new Intent();
        LaunchIntent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        LaunchIntent.setData(content_url);
        LaunchIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent[] intents = {LaunchIntent};
        PendingIntent pit = PendingIntent.getActivities(context, (int) System.currentTimeMillis(), intents, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap LargeBitmap = bitmap;
        if (bitmap == null) {
            LargeBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title)
                .setContentText("点击查看")
                .setTicker(title)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(LargeBitmap)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pit);

        builder.build();
        Notification notify = builder.build();
        mNManager.notify((int) System.currentTimeMillis(), notify);
    }
}
