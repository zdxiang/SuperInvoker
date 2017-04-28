package cn.zdxiang.invoker.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.marswin89.marsdaemon.utils.PackageUtils;

import java.util.concurrent.TimeUnit;

import cn.zdxiang.invoker.InvokerEngine;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class WatchDogService extends Service {

    protected static final int HASH_CODE = 2;

    protected static Subscription sSubscription;

    protected static PendingIntent sPendingIntent;

    /**
     * 守护服务，运行在:watch子进程中
     */
    protected final int onStart(Intent intent, int flags, int startId) {

        if (!InvokerEngine.sInitialized) return START_STICKY;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            startForeground(HASH_CODE, new Notification());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                try {
                    startService(new Intent(InvokerEngine.sApp, WatchDogNotificationService.class));
                } catch (Exception ignored) {
                }
        }

        //定时检查 BaseBizService 是否在运行，如果不在运行就把它拉起来
        //Android 5.0+ 使用 JobScheduler，效果比 AlarmManager 好
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(HASH_CODE, new ComponentName(InvokerEngine.sApp, JobSchedulerService.class));
            builder.setPeriodic(InvokerEngine.getWakeUpInterval());
            //Android 7.0+ 增加了一项针对 JobScheduler 的新限制，最小间隔只能是下面设定的数字
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) builder.setPeriodic(JobInfo.getMinPeriodMillis(), JobInfo.getMinFlexMillis());
            builder.setPersisted(true);
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());
        } else {
            //Android 4.4- 使用 AlarmManager
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent i = new Intent(InvokerEngine.sApp, InvokerEngine.sServiceClass);
            sPendingIntent = PendingIntent.getService(InvokerEngine.sApp, HASH_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + InvokerEngine.getWakeUpInterval(), InvokerEngine.getWakeUpInterval(), sPendingIntent);
        }

        //使用定时 Observable，避免 Android 定制系统 JobScheduler / AlarmManager 唤醒间隔不稳定的情况
        sSubscription = Observable.interval(InvokerEngine.getWakeUpInterval(), TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    public void call(Long aLong) {startService(new Intent(InvokerEngine.sApp, InvokerEngine.sServiceClass));}
                }, new Action1<Throwable>() {
                    public void call(Throwable t) {t.printStackTrace();}
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
            startService(new Intent(InvokerEngine.sApp, InvokerEngine.sServiceClass));
        } catch (Exception ignored) {
        }
        try {
            startService(new Intent(InvokerEngine.sApp, WatchDogService.class));
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

        /**
         * 利用漏洞在 API Level 18 及以上的 Android 系统中，启动前台服务而不显示通知
         * 运行在:watch子进程中
         */
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(WatchDogService.HASH_CODE, new Notification());
            stopSelf();
            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
