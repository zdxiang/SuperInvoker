package cn.zdxiang.superinvoker.application;

import com.marswin89.marsdaemon.DaemonApplication;


/**
 * @author jm
 * @date 17-4-21.下午5:34
 * @description
 */

public class MyApplication extends DaemonApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected String getWorkProcessName() {
        return "cn.zdxiang.superinvoker:invokerservice";
    }

    @Override
    protected String getCoreProcessName() {
        return "cn.zdxiang.superinvoker:core";
    }
}
