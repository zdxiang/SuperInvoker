package com.marswin89.marsdaemon;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.marswin89.marsdaemon.receiver.EmptyReceiver;
import com.marswin89.marsdaemon.receiver.EmptyReceiver2;
import cn.zdxiang.invoker.service.CoreService;
import cn.zdxiang.invoker.service.InvokerService;

import cn.zdxiang.invoker.InvokerEngine;

/**
 * @author jm
 * @date 17-4-27.上午10:19
 * @description DaemonApplication
 */

public abstract class DaemonApplication extends Application {
    private DaemonClient mDaemonClient;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mDaemonClient = new DaemonClient(createDaemonConfigurations());
        if (mDaemonClient.isDaemonPermitting(base)) {
            boolean b = mDaemonClient.onAttachBaseContext(base);
            if (!b) {
//                Toast.makeText(base, "守护失败", Toast.LENGTH_SHORT).show();
                InvokerEngine.initialize(this,InvokerService.class,InvokerEngine.DEFAULT_WAKE_UP_INTERVAL);
                InvokerService.start(base);
            } else {
                Log.d("DaemonApplication", "守护成功");
                InvokerService.start(base);
            }
        } else {
            mDaemonClient.setDaemonPermiiting(base, false);
            Log.d("DaemonApplication", "这个设备不允许daemon");
            InvokerEngine.initialize(this,InvokerService.class,InvokerEngine.DEFAULT_WAKE_UP_INTERVAL);
            InvokerService.start(base);
        }
    }

    /**
     * e.g cn.zdxiang.superinvoker:invokerservice
     *
     * @return The business process name;
     */
    protected abstract String getWorkProcessName();

    /**
     * e.g cn.zdxiang.superinvoker:invokerservice
     *
     * @return The empty process name;
     */
    protected abstract String getCoreProcessName();


    /**
     * Daemon SDK needs the Daemon Configurations contains two process informations</br>
     * see {@link DaemonConfigurations} and {@link DaemonConfigurations.DaemonConfiguration}
     *
     * @return DaemonConfigurations
     */
    private DaemonConfigurations createDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                getWorkProcessName(),
                InvokerService.class.getCanonicalName(),
                EmptyReceiver.class.getCanonicalName());

        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                getCoreProcessName(),
                CoreService.class.getCanonicalName(),
                EmptyReceiver2.class.getCanonicalName());

        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }


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
            Log.d("fuck", "onWatchDaemonDaed");

        }
    }
}
