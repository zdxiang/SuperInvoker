package com.marswin89.marsdaemon;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.marswin89.marsdaemon.receiver.EmptyReceiver;
import com.marswin89.marsdaemon.receiver.EmptyReceiver2;

import cn.zdxiang.invoker.service.CoreService;

/**
 * Make sure that your Application extends DaemonApplication and override the abstract methods
 *
 * @author jm
 * @date 17-4-27.上午10:19
 * @description DaemonApplication
 */

public abstract class DaemonApplication extends Application {
    protected String TAG = DaemonApplication.class.getName();

    private DaemonClient mDaemonClient;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mDaemonClient = new DaemonClient(createDaemonConfigurations());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP||Build.CPU_ABI.equals("arm64-v8a")) {
                mDaemonClient.setDaemonPermiiting(base, false);
            }
        if (mDaemonClient.isDaemonPermitting(base)) {
            boolean b = mDaemonClient.onAttachBaseContext(base);
            if (!b) {
//                Toast.makeText(base, "守护失败", Toast.LENGTH_SHORT).show();
                startYourService();
            } else {
                Log.d(TAG, "native daemon success");
                startYourService();
            }
        } else {
            mDaemonClient.setDaemonPermiiting(base, false);
            Log.d(TAG, "native daemon failed");
            startYourService();
        }
    }

    /**
     * Start your service that extends {@link cn.zdxiang.invoker.service.BaseBizService}
     */
    protected abstract void startYourService();

    /**
     * e.g cn.zdxiang.superinvoker:invokerservice
     * P.S,can not uses getPackageName();
     *
     * @return The business process name;
     */
    protected abstract String getYourProcessName();

    /**
     * P.S,can not uses getPackageName();
     *
     * @return The empty process name;
     */
    protected abstract String getCoreProcessName();

    /**
     * @return Your service that extends {@link cn.zdxiang.invoker.service.BaseBizService}
     */
    protected abstract String getYourServiceCanonicalName();

    /**
     * Daemon SDK needs the Daemon Configurations contains two process informations</br>
     * see {@link DaemonConfigurations} and {@link DaemonConfigurations.DaemonConfiguration}
     *
     * @return DaemonConfigurations
     */
    private DaemonConfigurations createDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                getYourProcessName(),
                getYourServiceCanonicalName(),
                EmptyReceiver.class.getCanonicalName());

        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                getCoreProcessName(),
                CoreService.class.getCanonicalName(),
                EmptyReceiver2.class.getCanonicalName());

        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }

    public void onWatchDaemonDead() {

    }

    public void onPersistentStart(Context context) {

    }

    public void onDaemonAssistantStart(Context context) {

    }


    private class MyDaemonListener implements DaemonConfigurations.DaemonListener {
        @Override
        public void onPersistentStart(Context context) {
            Log.d(TAG, "onPersistentStart");
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
            Log.d(TAG, "onDaemonAssistantStart");
        }

        @Override
        public void onWatchDaemonDaed() {
            Log.d(TAG, "onWatchDaemonDead");
        }
    }
}
