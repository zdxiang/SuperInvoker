package cn.zdxiang.invoker.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import cn.zdxiang.invoker.OnePxAct;


/**
 * @author jm
 * @date 17-4-17.上午9:25
 * @description KeepLiveManager
 */

public class KeepLiveManager {
    private static final String TAG = KeepLiveManager.class.getSimpleName();

    private static KeepLiveManager sInstance = new KeepLiveManager();

    private WeakReference<OnePxAct> mWeakActivityRef = null;

    private KeepLiveManager() {

    }

    public static KeepLiveManager getInstance() {
        return sInstance;
    }

    public void initOnePx(OnePxAct mainActivity) {
        this.mWeakActivityRef = new WeakReference<>(mainActivity);
        Window window = mainActivity.getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = 1;
        params.height = 1;
        window.setAttributes(params);
    }


    /**
     * 启动一像素Activity
     *
     * @param context context
     */
    public void startOnePxAct(Context context) {
        Intent intent = new Intent(context, OnePxAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Log.d(TAG, "onScreenOn=打开activity");
    }

    /**
     * 结束一像素Activity
     */
    public void finishOnePxAct() {
        if (mWeakActivityRef != null) {
            Activity activity = mWeakActivityRef.get();
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
                Log.d(TAG, "关闭Activity");
            }
        }
    }
}