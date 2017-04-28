package cn.zdxiang.invoker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import cn.zdxiang.invoker.manager.KeepLiveManager;

import com.marswin89.marsdaemon.observer.ScreenObserver;
import com.marswin89.marsdaemon.utils.AppUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;


/**
 * @author jm
 * @date 17-4-24.上午9:55
 * @description
 */

public class InvokerService extends BaseBizService {

    private static final String Tag = InvokerService.class.getSimpleName();

    public static final int SERVICE_ID = 1000;

    static InvokerService sKeepLiveService;

    private ScreenObserver mScreenObserver;

    //是否 任务完成, 不再需要服务运行?
    public static boolean sShouldStopService;

    public static Subscription sSubscription;


    public static void start(Context context) {
        if (!AppUtils.isServiceExisted(context, InvokerService.class.getName())) {
            Intent intent = new Intent(context, InvokerService.class);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            context.startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sKeepLiveService = this;
    }


    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return null;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        initScreenObserver();
        System.out.println("检查磁盘中是否有上次销毁时保存的数据");
        Log.d("wocaonim", "startWork");
        sSubscription = Observable
                .interval(3, TimeUnit.SECONDS)
                //取消任务时取消定时唤醒
                .doOnUnsubscribe(new Action0() {
                    public void call() {
                        System.out.println("保存数据到磁盘。");
                    }
                }).subscribe(new Action1<Long>() {
                    public void call(Long count) {
                        System.out.println("每 3 秒采集一次数据... count = " + count);
                        if (count > 0 && count % 18 == 0) System.out.println("保存数据到磁盘。 saveCount = " + (count / 18 - 1));
                    }
                });
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {

    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        //若还没有取消订阅, 就说明任务仍在运行.
        return sSubscription != null && !sSubscription.isUnsubscribed();
    }

    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
        System.out.println("保存数据");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void initScreenObserver() {
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.d("fuckscreen", "onScreenOn");
                KeepLiveManager.getInstance().finishOnePxAct();
            }

            @Override
            public void onScreenOff() {
                Log.d("fuckscreen", "onScreenOff");
                if (!AppUtils.isForeground(InvokerService.this, getPackageName())) {
                    KeepLiveManager.getInstance().startOnePxAct(InvokerService.this);
                }
            }
        });
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class InnerService extends Service {

        public static void start(Context context) {
            Intent intent = new Intent(context, InnerService.class);
            context.startService(intent);
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            KeepLiveManager.getInstance().setForegroundService(sKeepLiveService, this);
            return super.onStartCommand(intent, flags, startId);
        }


        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    @Override
    public void onDestroy() {
        InvokerService.start(this);
        if (mScreenObserver != null) {
            mScreenObserver.stopScreenStateUpdate();
        }
        super.onDestroy();
    }
}
