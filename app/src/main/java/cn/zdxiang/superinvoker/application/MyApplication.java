package cn.zdxiang.superinvoker.application;

import com.marswin89.marsdaemon.DaemonApplication;

import cn.zdxiang.invoker.InvokerEngine;
import cn.zdxiang.superinvoker.service.WorkingService;


/**
 * @author jm
 * @date 17-4-21.下午5:34
 * @description
 */

public class MyApplication extends DaemonApplication {

    @Override
    protected void startYourService() {
        InvokerEngine.initialize(WorkingService.class, InvokerEngine.DEFAULT_WAKE_UP_INTERVAL);
        WorkingService.start(this);
    }

    @Override
    protected String getYourProcessName() {
        return "cn.zdxiang.superinvoker:secure";
    }

    @Override
    protected String getCoreProcessName() {
        return "cn.zdxiang.superinvoker:core";
    }

    @Override
    protected String getYourServiceCanonicalName() {
        return WorkingService.class.getCanonicalName();
    }
}
