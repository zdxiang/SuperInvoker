package com.marswin89.marsdaemon.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.zdxiang.invoker.utils.PackageUtils;

//import cn.zdxiang.invoker.utils.PackageUtils;


/**
 * @author jm
 * @date 17-4-24.下午4:06
 * @description
 */

public class ShutDownReceiver extends BroadcastReceiver {

    /**
     * Shut down
     */
    private static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "关机", Toast.LENGTH_SHORT).show();
//        PackageUtils.setComponentDefault(context, "cn.zdxiang.superinvoker.service.InvokerService");
        Log.d("shutDownReceiver","==<>"+InvokerReceiver.class.getCanonicalName());
        PackageUtils.setComponentDefault(context, InvokerReceiver.class.getCanonicalName());
//        AppUtils.isServiceExisted(context, "");
    }
}
