package cn.zdxiang.invoker.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author jm
 * @date 17-4-21.下午5:39
 * @description DO NOT do anything in this Service!<br/>
 */

public class CoreService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }
}
