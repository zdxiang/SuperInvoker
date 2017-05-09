package cn.zdxiang.superinvoker.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import cn.zdxiang.invoker.service.BaseBizService;
import cn.zdxiang.invoker.utils.AppUtils;
import cn.zdxiang.superinvoker.manager.MyNotificationManager;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;


/**
 * @author jm
 * @date 17-4-24.上午9:55
 * @description
 */

public class WorkingService extends BaseBizService {

    private static final String Tag = WorkingService.class.getSimpleName();

    public static Subscription sSubscription;

    public static void start(Context context) {
        if (!AppUtils.isServiceExisted(context, WorkingService.class.getName())) {
            Intent intent = new Intent(context, WorkingService.class);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            context.startService(intent);
        }
    }


    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return null;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        Log.d("workingservice", "==>startWork");
        sSubscription = Observable
                .interval(1, TimeUnit.HOURS)
                //取消任务时取消定时唤醒
                .doOnUnsubscribe(new Action0() {
                    public void call() {

                    }
                }).subscribe(new Action1<Long>() {
                    public void call(Long count) {
                        MyNotificationManager.notify(WorkingService.this, "Notification", "https://github.com/zdxiang/SuperInvoker/", null);
                    }
                });
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        if (sSubscription != null && !sSubscription.isUnsubscribed()) {
            sSubscription.unsubscribe();
        }
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

    @Override
    public void onDestroy() {
        onEnd(null);
        super.onDestroy();
    }
}
