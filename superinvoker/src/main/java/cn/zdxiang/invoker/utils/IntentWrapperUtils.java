package cn.zdxiang.invoker.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author jm
 * @date 17-5-2.下午3:58
 * @description IntentWrapperUtils
 */

public class IntentWrapperUtils {

    private static final String INTENT_WRAPPER_SETTING_SP_FILENAME = "iw_file_name";

    private static final String INTENT_WRAPPER_SETTING_SP_KEY = "had_set";


    /**
     * 是否已经设置了自启动等相关参数.实际有没设置,暂时无法知晓
     *
     * @param context context
     * @return boolean default is false;
     */
    public static boolean isIntentWrapperSet(Context context) {
        SharedPreferences sp = context.getSharedPreferences(INTENT_WRAPPER_SETTING_SP_FILENAME, Context.MODE_PRIVATE);
        return sp.getBoolean(INTENT_WRAPPER_SETTING_SP_KEY, false);
    }


    /**
     * Set true or false
     *
     * @param context context
     * @param isSet   isSet
     * @return boolean default is false;
     */
    public static boolean saveIntentWrapperSet(Context context, boolean isSet) {
        SharedPreferences sp = context.getSharedPreferences(INTENT_WRAPPER_SETTING_SP_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(INTENT_WRAPPER_SETTING_SP_KEY, isSet);
        return editor.commit();
    }
}
