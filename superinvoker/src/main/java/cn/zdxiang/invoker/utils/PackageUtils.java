package cn.zdxiang.invoker.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @author jm
 * @date 17-4-17.上午9:35
 * @description Utils to prevent component from third-party app forbidding
 */

public class PackageUtils {
    /**
     * set the component in our package default
     *
     * @param context            context
     * @param componentClassName componentClassName
     */
    public static void setComponentDefault(Context context, String componentClassName) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context.getPackageName(), componentClassName);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * get the component in our package default
     *
     * @param context            context
     * @param componentClassName componentClassName
     */
    public static boolean isComponentDefault(Context context, String componentClassName) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context.getPackageName(), componentClassName);
        return pm.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
    }
}
