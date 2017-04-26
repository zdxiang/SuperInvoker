package cn.zdxiang.superinvoker.application;

import android.content.Context;
import android.util.Log;


import com.marswin89.marsdaemon.DaemonApplication;
import com.marswin89.marsdaemon.DaemonConfigurations;

import cn.zdxiang.superinvoker.receiver.EmptyReceiver;
import cn.zdxiang.superinvoker.receiver.EmptyReceiver2;
import cn.zdxiang.superinvoker.service.InvokerService;
import cn.zdxiang.superinvoker.service.EmptyService;

/**
 * @author jm
 * @date 17-4-21.下午5:34
 * @description
 */

public abstract class InvokerApplication extends DaemonApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    /**
     * give the configuration to lib in this callback
     *
     * @return DaemonConfigurations
     */
    @Override
    protected DaemonConfigurations getDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                initProcessName(),
                InvokerService.class.getCanonicalName(),
                EmptyReceiver.class.getCanonicalName());

        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "cn.zdxiang.superinvoker:core",
                EmptyService.class.getCanonicalName(),
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
    protected abstract String initProcessName();

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
