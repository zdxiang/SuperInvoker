package cn.zdxiang.invoker.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import cn.zdxiang.invoker.KeepLiveActivity;


/**
 * @author jm
 * @date 17-4-17.上午9:25
 * @description KeepLiveManager
 */

public class KeepLiveManager {
    private static final String TAG = KeepLiveManager.class.getSimpleName();

    private static KeepLiveManager sInstance = new KeepLiveManager();

    private WeakReference<KeepLiveActivity> mWeakActivityRef = null;

    private KeepLiveManager() {

    }

    public static KeepLiveManager getInstance() {
        return sInstance;
    }

    @SuppressLint("RtlHardcoded")
    public void initKeepLiveActivity(KeepLiveActivity mainActivity) {
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
     * Start the keep live activity
     *
     * @param context context
     */
    public void startKeepLive(Context context) {
        Intent intent = new Intent(context, KeepLiveActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Finish the keep live activity
     */
    public void finishKeepLiveActivity() {
        if (mWeakActivityRef != null) {
            Activity activity = mWeakActivityRef.get();
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}