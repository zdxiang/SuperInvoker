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
 * @description Start this when screen off and destroy when screen on
 */

public class KeepLiveActivity extends Activity {

    public static void start(Context context) {
        Intent intent = new Intent(context, KeepLiveActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KeepLiveManager.getInstance().initKeepLiveActivity(this);
    }
}