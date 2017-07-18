package com.marswin89.marsdaemon.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.zdxiang.invoker.InvokerEngine;
import cn.zdxiang.invoker.manager.KeepLiveManager;


/**
 * @author jm
 * @date 17-4-24.上午10:59
 * @description InvokerReceiver
 */

public  class InvokerReceiver extends BroadcastReceiver {

    public static final String TAG = InvokerReceiver.class.getSimpleName();

    /**
     * WECHAT open refresh action.
     * Will receive after open WECHAT(Ps.Not any time when open the WeChat)
     */
    public static final String ACTION_TENCENT_REFRESH_WXAPP = "com.tencent.mm.plugin.openapi.Intent.ACTION_REFRESH_WXAPP";

    /**
     * 向 InvokerReceiver 发送带有此 Action 的广播, 即可在不需要服务运行的时候取消 Job / Alarm / Subscription.
     */
    public static final String ACTION_CANCEL_JOB_ALARM_SUB = "cn.zdxiang.invoker.CANCEL_JOB_ALARM_SUB";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: InvokerReceiver receive action =" + action);
        if (!InvokerEngine.sInitialized) return;
        Log.d(TAG, "1");

        if (!InvokerEngine.sInitialized) {
            try {
                context.startService(new Intent(context, InvokerEngine.sServiceClass));
                Log.d(TAG, "2");
            } catch (Exception ignored) {

            }
        }

        Log.d(TAG, "3");
        switch (action) {

            case Intent.ACTION_USER_PRESENT:
                KeepLiveManager.getInstance().finishKeepLiveActivity();
                break;

//            case ACTION_TENCENT_REFRESH_WXAPP:
//                InvokerService.start(context);
//                break;


        }
        Log.d(TAG, "4");
//        onMyReceiver(context, intent);
    }
}

