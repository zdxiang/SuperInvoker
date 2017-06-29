package cn.zdxiang.invoker.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import cn.zdxiang.invoker.InvokerEngine;
import cn.zdxiang.invoker.utils.PackageUtils;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;


/**
 * @author jm
 * @date 17-4-27.上午10:19
 * @description WatchDogService
 */

public class WatchDogService extends Service {

    protected static final int SERVICE_ID = 1001;

    protected static Subscription sSubscription;

    protected static PendingIntent sPendingIntent;

    public static void start(Context context) {
        try {
            Intent intent = new Intent(context, WatchDogService.class);
            context.startService(intent);
        } catch (Exception ignored) {

        }
    }

    /**
     * 用于在不需要服务运行的时候取消 Job / Alarm / Subscription.
     * <p>
     * 因 WatchDogService 运行在 :watch 子进程, 请勿在主进程中直接调用此方法.
     * 而是向 InvokerReceiver 发送一个 Action 为 InvokerReceiver.ACTION_CANCEL_JOB_ALARM_SUB 的广播.
     */
    public static void cancelJobAlarmSub(Context context) {
        if (!InvokerEngine.sInitialized) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.cancel(SERVICE_ID);
        } else {
            AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            if (sPendingIntent != null) am.cancel(sPendingIntent);
        }
        if (sSubscription != null) sSubscription.unsubscribe();
    }


    /**
     * 守护服务，运行在:watch子进程中
     */
    protected final int onStart(Intent intent, int flags, int startId) {
        if (!InvokerEngine.sInitialized) return START_STICKY;
        if (sSubscription != null && !sSubscription.isUnsubscribed()) return START_STICKY;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            startForeground(SERVICE_ID, new Notification());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                try {
                    startService(new Intent(this, WatchDogNotificationService.class));
                } catch (Exception ignored) {
                }
        }

        //定时检查 BaseBizService 是否在运行，如果不在运行就把它拉起来
        //Android 5.0+ 使用 JobScheduler，效果比 AlarmManager 好
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(SERVICE_ID, new ComponentName(this, JobSchedulerService.class));
            builder.setPeriodic(InvokerEngine.getWakeUpInterval());
            //Android 7.0+ 增加了一项针对 JobScheduler 的新限制，最小间隔只能是下面设定的数字
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                builder.setPeriodic(JobInfo.getMinPeriodMillis(), JobInfo.getMinFlexMillis());
            builder.setPersisted(true);
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());
        } else {
            //Android 4.4- 使用 AlarmManager
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent i = new Intent(this, InvokerEngine.sServiceClass);
            sPendingIntent = PendingIntent.getService(this, SERVICE_ID, i, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + InvokerEngine.getWakeUpInterval(), InvokerEngine.getWakeUpInterval(), sPendingIntent);
        }

        //使用定时 Observable，避免 Android 定制系统 JobScheduler / AlarmManager 唤醒间隔不稳定的情况
        sSubscription = Observable.interval(InvokerEngine.getWakeUpInterval(), TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    public void call(Long aLong) {
                        startService(new Intent(WatchDogService.this, InvokerEngine.sServiceClass));
                    }
                }, new Action1<Throwable>() {
                    public void call(Throwable t) {
                        t.printStackTrace();
                    }
                });
        //守护 Service 组件的启用状态, 使其不被 MAT 等工具禁用
        PackageUtils.setComponentDefault(this, InvokerEngine.sServiceClass.getName());
        return START_STICKY;
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        return onStart(intent, flags, startId);
    }

    @Override
    public final IBinder onBind(Intent intent) {
        onStart(intent, 0, 0);
        return null;
    }

    protected void onEnd(Intent rootIntent) {
        if (!InvokerEngine.sInitialized) return;
        try {
            startService(new Intent(this, InvokerEngine.sServiceClass));
        } catch (Exception ignored) {
        }
        try {
            startService(new Intent(this, WatchDogService.class));
        } catch (Exception ignored) {
        }
    }

    /**
     * 最近任务列表中划掉卡片时回调
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        onEnd(rootIntent);
    }

    /**
     * 设置-正在运行中停止服务时回调
     */
    @Override
    public void onDestroy() {
        onEnd(null);
    }


    public static class WatchDogNotificationService extends Service {
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(WatchDogService.SERVICE_ID, new Notification());
            stopSelf();
            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
