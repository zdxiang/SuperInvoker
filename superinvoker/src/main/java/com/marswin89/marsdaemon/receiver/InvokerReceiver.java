package com.marswin89.marsdaemon.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.zdxiang.invoker.InvokerEngine;
import cn.zdxiang.invoker.manager.KeepLiveManager;


/**
 * @author jm
 * @date 17-4-24.上午10:59
 * @description InvokerReceiver
 */

public class InvokerReceiver extends BroadcastReceiver {
    public static final String TAG = InvokerReceiver.class.getSimpleName();

    /**
     * WECHAT open action.
     * Will receive after open WECHAT(Ps.the WeChat status must be stopped)
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
        Toast.makeText(context, "收到广播=>" + action, Toast.LENGTH_LONG).show();
        if (!InvokerEngine.sInitialized) return;
        try {
            context.startService(new Intent(context, InvokerEngine.sServiceClass));
        } catch (Exception ignored) {
        }

        switch (action) {

            case Intent.ACTION_USER_PRESENT:
                Log.d(TAG, "screen unlocked,finish onePxAct");
                KeepLiveManager.getInstance().finishOnePxAct();
                break;

//            case ACTION_TENCENT_REFRESH_WXAPP:
//                InvokerService.start(context);
//                break;


        }
    }
}

