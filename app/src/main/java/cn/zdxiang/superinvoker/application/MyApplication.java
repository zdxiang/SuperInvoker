package cn.zdxiang.superinvoker.application;

import android.util.Log;

import com.marswin89.marsdaemon.DaemonApplication;

import cn.zdxiang.invoker.InvokerEngine;
import cn.zdxiang.superinvoker.service.InvokerService;


/**
 * @author jm
 * @date 17-4-21.下午5:34
 * @description
 */

public class MyApplication extends DaemonApplication {

    @Override
    public void onCreate() {
        Log.d("MyApplication", "MyApplication onCreate");
        super.onCreate();
    }

    @Override
    protected void startYourService() {
        InvokerEngine.initialize(InvokerService.class, InvokerEngine.DEFAULT_WAKE_UP_INTERVAL);
        InvokerService.start(this);
    }

    @Override
    protected String getYourProcessName() {
        return "cn.zdxiang.superinvoker:invokerservice";
    }

    @Override
    protected String getCoreProcessName() {
        return "cn.zdxiang.superinvoker:core";
    }

    @Override
    protected String getYourServiceCanonicalName() {
        return InvokerService.class.getCanonicalName();
    }
}
