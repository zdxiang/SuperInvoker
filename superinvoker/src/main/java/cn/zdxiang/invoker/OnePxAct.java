package cn.zdxiang.invoker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.zdxiang.invoker.manager.KeepLiveManager;


/**
 * @author jm
 * @date 17-4-18.下午6:01
 * @description
 */

public class OnePxAct extends Activity {

    public static void start(Context context) {
        Intent intent = new Intent(context, OnePxAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KeepLiveManager.getInstance().initOnePx(this);
        Log.d("OnePxAct", "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeepLiveManager.getInstance().finishOnePxAct();
        Log.d("OnePxAct", "onDestroy");
    }
}