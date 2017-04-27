package com.marswin89.marsdaemon.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.marswin89.marsdaemon.manager.KeepLiveManager;
import com.marswin89.marsdaemon.observer.ScreenObserver;
import com.marswin89.marsdaemon.utils.AppUtils;


/**
 * @author jm
 * @date 17-4-24.上午9:55
 * @description
 */

public class InvokerService extends Service {

    private static final String Tag = InvokerService.class.getSimpleName();

    public static final int SERVICE_ID = 1000;

    static InvokerService sKeepLiveService;

    private ScreenObserver mScreenObserver;

    public static void start(Context context) {
        if (!AppUtils.isServiceExisted(context, InvokerService.class.getName())) {
            Intent intent = new Intent(context, InvokerService.class);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            context.startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sKeepLiveService = this;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Tag, "onStartCommand");
        initScreenObserver();
        InnerService.start(this);
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void initScreenObserver() {
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.d(Tag, "onScreenOn==>" + AppUtils.isForeground(InvokerService.this, getPackageName()));
                Log.d(Tag, "getPackageName()==>" + getPackageName());
                KeepLiveManager.getInstance().finishOnePxAct();
            }

            @Override
            public void onScreenOff() {
                Log.d(Tag, "onScreenOff==>" + AppUtils.isForeground(InvokerService.this, getPackageName()));
                Log.d(Tag, "getPackageName()==>" + getPackageName());
                if (!AppUtils.isForeground(InvokerService.this, getPackageName())) {
                    KeepLiveManager.getInstance().startOnePxAct(InvokerService.this);
                }
            }
        });
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class InnerService extends Service {

        public static void start(Context context) {
            Intent intent = new Intent(context, InnerService.class);
            context.startService(intent);
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            KeepLiveManager.getInstance().setForegroundService(sKeepLiveService, this);
            return super.onStartCommand(intent, flags, startId);
        }


        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    @Override
    public void onDestroy() {
        InvokerService.start(this);
        mScreenObserver.stopScreenStateUpdate();
        super.onDestroy();
    }
}
