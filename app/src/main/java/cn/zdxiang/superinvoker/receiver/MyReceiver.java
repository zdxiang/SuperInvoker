package cn.zdxiang.superinvoker.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.marswin89.marsdaemon.receiver.InvokerReceiver;

/**
 * @author Jm
 * @date 17-7-13上午9:07
 */
public class MyReceiver extends InvokerReceiver {

//    @Override
//    public void onMyReceiver(Context context, Intent intent) {
//
//    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(InvokerReceiver.TAG, "6==>" + intent.getAction());
    }
}
