package cn.zdxiang.superinvoker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.marswin89.marsdaemon.PackageUtils;

//import com.marswin89.marsdaemon.PackageUtils;


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
        PackageUtils.setComponentDefault(context, "cn.zdxiang.superinvoker.service.InvokerService");
//        AppUtils.isServiceExisted(context, "");
    }
}
