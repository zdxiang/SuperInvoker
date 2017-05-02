package cn.zdxiang.superinvoker;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;

import cn.zdxiang.invoker.manager.IntentWrapper;
import cn.zdxiang.invoker.utils.IntentWrapperUtils;

/**
 * @author jm
 * @date 17-4-26.下午2:15
 * @description
 */

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d("fuckyou", "whiteListMatters");
        if (!IntentWrapperUtils.isIntentWrapperSet(this)) {
            IntentWrapper.whiteListMatters(this, this, "为保证功能稳定运行");
        }
    }
}
