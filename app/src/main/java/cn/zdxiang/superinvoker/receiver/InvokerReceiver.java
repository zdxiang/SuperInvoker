package cn.zdxiang.superinvoker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.zdxiang.superinvoker.manager.KeepLiveManager;
import cn.zdxiang.superinvoker.service.InvokerService;
import cn.zdxiang.superinvoker.utils.AppUtils;

/**
 * @author jm
 * @date 17-4-24.上午10:59
 * @description
 */

public class InvokerReceiver extends BroadcastReceiver {
    public static final String TAG = InvokerReceiver.class.getSimpleName();

    /**
     * WECHAT open action.
     * Will receive after open WECHAT(Ps.the WeChat status must be stopped)
     */
    public static final String ACTION_TENCENT_REFRESH_WXAPP = "com.tencent.mm.plugin.openapi.Intent.ACTION_REFRESH_WXAPP";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: InvokerReceiver receive action =" + action);
        Toast.makeText(context, "收到广播=>" + action, Toast.LENGTH_LONG).show();
        startInvokerService(context);
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


    private void startInvokerService(Context context) {
        Log.d(TAG, "isServiceExit==>" + AppUtils.isServiceExisted(context, InvokerService.class.getName()));
        if (!AppUtils.isServiceExisted(context, InvokerService.class.getName())) {
            Log.d(TAG, "服务不存在,启动");
            InvokerService.start(context);
        }

    }
}
