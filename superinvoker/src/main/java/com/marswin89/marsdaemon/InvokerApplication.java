package com.marswin89.marsdaemon;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.marswin89.marsdaemon.receiver.EmptyReceiver;
import com.marswin89.marsdaemon.receiver.EmptyReceiver2;
import com.marswin89.marsdaemon.service.CoreService;
import com.marswin89.marsdaemon.service.InvokerService;

/**
 * @author jm
 * @date 17-4-27.上午10:19
 * @description
 */

public abstract class InvokerApplication extends Application {
    private DaemonClient mDaemonClient;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d("InvokerApplication", "Build.MODEL==>" + Build.MODEL);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mDaemonClient = new DaemonClient(createDaemonConfigurations());
            mDaemonClient.onAttachBaseContext(base);
        } else {
            InvokerService.start(base);
        }
    }


    protected abstract String getWorkProcessName();

    protected abstract String getCoreProcessName();


    private DaemonConfigurations createDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
//                "cn.zdxiang.superinvoker:invokerservice",
                getWorkProcessName(),
                InvokerService.class.getCanonicalName(),
                EmptyReceiver.class.getCanonicalName());

        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
//                "cn.zdxiang.superinvoker:core",
                getCoreProcessName(),
                CoreService.class.getCanonicalName(),
                EmptyReceiver2.class.getCanonicalName());

        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        //return new DaemonConfigurations(configuration1, configuration2);//listener can be null
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }

    /**
     * e.g cn.zdxiang.superinvoker:invokerservice
     *
     * @return The hole process name;
     */

    private class MyDaemonListener implements DaemonConfigurations.DaemonListener {
        @Override
        public void onPersistentStart(Context context) {
            Log.d("fuck", "onPersistentStart");
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
            Log.d("fuck", "onDaemonAssistantStart");
        }

        @Override
        public void onWatchDaemonDaed() {
            InvokerService.start(getApplicationContext());
            Log.d("fuck", "onWatchDaemonDaed");
        }
    }
}
