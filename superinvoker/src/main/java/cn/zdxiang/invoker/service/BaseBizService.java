package cn.zdxiang.invoker.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.marswin89.marsdaemon.receiver.InvokerReceiver;

import cn.zdxiang.invoker.InvokerEngine;
import cn.zdxiang.invoker.manager.KeepLiveManager;
import cn.zdxiang.invoker.observer.ScreenObserver;
import cn.zdxiang.invoker.utils.PackageUtils;

/**
 * @author jm
 * @date 17-4-24.上午9:55
 * @description The BaseBizService
 */
public abstract class BaseBizService extends Service {

    protected String TAG = BaseBizService.class.getName();

    protected static final int SERVICE_ID = 1000;

    protected boolean mFirstStarted = true;

    private ScreenObserver mScreenObserver;

    /**
     * 是否 任务完成, 不再需要服务运行?
     *
     * @return 应当停止服务, true; 应当启动服务, false; 无法判断, 什么也不做, null.
     */
    public abstract Boolean shouldStopService(Intent intent, int flags, int startId);

    public abstract void startWork(Intent intent, int flags, int startId);

    public abstract void stopWork(Intent intent, int flags, int startId);

    /**
     * 任务是否正在运行?
     *
     * @return 任务正在运行, true; 任务当前不在运行, false; 无法判断, 什么也不做, null.
     */
    public abstract Boolean isWorkRunning(Intent intent, int flags, int startId);

    public abstract IBinder onBind(Intent intent, Void alwaysNull);

    public abstract void onServiceKilled(Intent rootIntent);


    /**
     * 用于在不需要服务运行的时候取消 Job / Alarm / Subscription.
     */
    public static void cancelJobAlarmSub(Context context) {
        if (!InvokerEngine.sInitialized) return;
        context.sendBroadcast(new Intent(InvokerReceiver.ACTION_CANCEL_JOB_ALARM_SUB));
    }

    /**
     * 1.防止重复启动，可以任意调用startService(Intent i);
     * 2.利用漏洞启动前台服务而不显示通知;
     * 3.在子线程中运行定时任务，处理了运行前检查和销毁时保存的问题;
     * 4.启动守护服务;
     * 5.守护 Service 组件的启用状态, 使其不被 MAT 等工具禁用.
     */
    protected int onStart(Intent intent, int flags, int startId) {

        //Register the Screen observer
        initScreenObserver();
        WatchDogService.start(getApplication());

        //业务逻辑: 实际使用时，根据需求，将这里更改为自定义的条件，判定服务应当启动还是停止 (任务是否需要运行)
        Boolean shouldStopService = shouldStopService(intent, flags, startId);
        if (shouldStopService != null && shouldStopService) {
            stopService(intent, flags, startId);
        } else {
            startService(intent, flags, startId);
        }

        if (mFirstStarted) {
            mFirstStarted = false;
            //启动前台服务而不显示通知的漏洞已在 API Level 25 修复
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                //利用漏洞在 API Level 17 及以下的 Android 系统中，启动前台服务而不显示通知
                startForeground(SERVICE_ID, new Notification());
                //利用漏洞在 API Level 18 及以上的 Android 系统中，启动前台服务而不显示通知
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                    try {
                        startService(new Intent(this, WorkNotificationService.class));
                    } catch (Exception ignored) {
                    }
            }
            PackageUtils.setComponentDefault(this, WatchDogService.class.getName());
        }
        return START_STICKY;
    }

    void startService(Intent intent, int flags, int startId) {
        //检查服务是否不需要运行
        Boolean shouldStopService = shouldStopService(intent, flags, startId);
        if (shouldStopService != null && shouldStopService) return;
        //若还没有取消订阅，说明任务仍在运行，为防止重复启动，直接 return
        Boolean workRunning = isWorkRunning(intent, flags, startId);
        if (workRunning != null && workRunning) return;
        //业务逻辑
        startWork(intent, flags, startId);
    }

    /**
     * 停止服务并取消定时唤醒
     * <p>
     * 停止服务使用取消订阅的方式实现，而不是调用 Context.stopService(Intent name)。因为：
     * 1.stopService 会调用 Service.onDestroy()，而 BaseBizService 做了保活处理，会把 Service 再拉起来；
     * 2.我们希望 BaseBizService 起到一个类似于控制台的角色，即 BaseBizService 始终运行 (无论任务是否需要运行)，
     * 而是通过 onStart() 里自定义的条件，来决定服务是否应当启动或停止。
     */
    void stopService(Intent intent, int flags, int startId) {
        //取消对任务的订阅
        stopWork(intent, flags, startId);
        //取消 Job / Alarm / Subscription
        cancelJobAlarmSub(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return onStart(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        onStart(intent, 0, 0);
        return onBind(intent, null);
    }

    protected void onEnd(Intent rootIntent) {
        onServiceKilled(rootIntent);
        if (!InvokerEngine.sInitialized) return;
        try {
            startService(new Intent(this, InvokerEngine.sServiceClass));
        } catch (Exception ignored) {
        }
        try {
            WatchDogService.start(this);
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
        if (mScreenObserver != null) {
            mScreenObserver.stopScreenStateUpdate();
        }
    }

    private void initScreenObserver() {
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.d(TAG, "onScreenOn");
                KeepLiveManager.getInstance().finishKeepLiveActivity();
            }

            @Override
            public void onScreenOff() {
                Log.i(TAG, "onScreenOff");
//                if (!AppUtils.isForeground(BaseBizService.this, getPackageName())) {
//                    KeepLiveManager.getInstance().startKeepLive(BaseBizService.this);
//                }
                KeepLiveManager.getInstance().startKeepLive(BaseBizService.this);
            }
        });
    }

    public static class WorkNotificationService extends Service {

        /**
         * 利用漏洞在 API Level 18 及以上的 Android 系统中，启动前台服务而不显示通知
         */
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(BaseBizService.SERVICE_ID, new Notification());
            stopSelf();
            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
