package cn.zdxiang.superinvoker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cn.zdxiang.superinvoker.service.InvokerService;

/**
 * @author jm
 * @date 17-4-24.下午5:20
 * @description
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "启动成功==>" + intent.getAction(), Toast.LENGTH_SHORT).show();
        InvokerService.start(context);
    }
}
