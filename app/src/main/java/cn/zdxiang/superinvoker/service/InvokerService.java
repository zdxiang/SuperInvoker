package cn.zdxiang.superinvoker.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import cn.zdxiang.invoker.service.BaseBizService;
import cn.zdxiang.invoker.utils.AppUtils;
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
    }


    @Override
    public boolean shouldStopService(Intent intent, int flags, int startId) {
        return false;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        System.out.println("检查磁盘中是否有上次销毁时保存的数据");
        Log.d(Tag, "startWork");
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

    @Override
    public void onDestroy() {
        onEnd(null);
        super.onDestroy();
    }
}
