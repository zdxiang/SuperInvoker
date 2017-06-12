package cn.zdxiang.superinvoker;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
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

        if (!IntentWrapperUtils.isIntentWrapperSet(this)) {
            IntentWrapper.whiteListMatters(this, this, "为保证功能稳定运行");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
        super.onBackPressed();
    }
}
