package cn.zdxiang.superinvoker;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

/**
 * @author jm
 * @date 17-4-26.下午2:15
 * @description
 */

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Button button = new Button(this);

        button.setText("test");
        button.setTextSize(22);

        setContentView(button);
    }


}
