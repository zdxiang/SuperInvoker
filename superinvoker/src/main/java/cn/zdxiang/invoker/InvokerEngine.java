package cn.zdxiang.invoker;

import cn.zdxiang.invoker.service.BaseBizService;

/**
 * @author jm
 * @date 17-4-28.下午2:32
 * @description
 */

public class InvokerEngine {
    private InvokerEngine() {}

    public static final int DEFAULT_WAKE_UP_INTERVAL = 6 * 60 * 1000;

    private static final int MINIMAL_WAKE_UP_INTERVAL = 3 * 60 * 1000;

    public static Class<? extends BaseBizService> sServiceClass;

    private static int sWakeUpInterval = DEFAULT_WAKE_UP_INTERVAL;

    public static boolean sInitialized;

    /**
     * @param wakeUpInterval 定时唤醒的时间间隔(ms).
     */
    public static void initialize(Class<? extends BaseBizService> serviceClass, Integer wakeUpInterval) {
        sServiceClass = serviceClass;
        if (wakeUpInterval != null) sWakeUpInterval = wakeUpInterval;
        sInitialized = true;
    }

    public static int getWakeUpInterval() {
        return Math.max(sWakeUpInterval, MINIMAL_WAKE_UP_INTERVAL);
    }
}
