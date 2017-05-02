package cn.zdxiang.invoker.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.zdxiang.invoker.InvokerEngine;
import cn.zdxiang.invoker.utils.IntentWrapperUtils;

public class IntentWrapper {

    //Android 6.0+ Doze 模式
    protected static final int DOZE = 98;
    //华为 自启管理
    protected static final int HUAWEI = 99;
    //华为 锁屏清理
    protected static final int HUAWEI_GOD = 100;
    //小米 自启动管理
    protected static final int XIAOMI = 101;
    //小米 神隐模式 (建议只在 App 的核心功能需要后台连接网络/后台定位的情况下使用)
    protected static final int XIAOMI_GOD = 102;
    //三星 5.0/5.1 自启动应用程序管理
    protected static final int SAMSUNG_L = 103;
    //魅族 自启动管理
    protected static final int MEIZU = 104;
    //魅族 待机耗电管理
    protected static final int MEIZU_GOD = 105;
    //Oppo 自启动管理
    protected static final int OPPO = 106;
    //三星 6.0+ 未监视的应用程序管理
    protected static final int SAMSUNG_M = 107;
    //Oppo 自启动管理(旧版本系统)
    protected static final int OPPO_OLD = 108;
    //Vivo 后台高耗电
    protected static final int VIVO_GOD = 109;
    //金立 应用自启
    protected static final int GIONEE = 110;
    //乐视 自启动管理
    protected static final int LETV = 111;
    //乐视 应用保护
    protected static final int LETV_GOD = 112;
    //酷派 自启动管理
    protected static final int COOLPAD = 113;
    //联想 后台管理
    protected static final int LENOVO = 114;
    //联想 后台耗电优化
    protected static final int LENOVO_GOD = 115;
    //中兴 自启管理
    protected static final int ZTE = 116;
    //中兴 锁屏加速受保护应用
    protected static final int ZTE_GOD = 117;

    protected static List<IntentWrapper> sIntentWrapperList;

    public static List<IntentWrapper> getIntentWrapperList(Context context) {
        if (sIntentWrapperList == null) {

            if (!InvokerEngine.sInitialized) return new ArrayList<>();

            sIntentWrapperList = new ArrayList<>();

            //Android 6.0+ Doze 模式
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                boolean ignoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(context.getPackageName());
                if (!ignoringBatteryOptimizations) {
                    Intent dozeIntent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    dozeIntent.setData(Uri.parse("package:" + context.getPackageName()));
                    sIntentWrapperList.add(new IntentWrapper(dozeIntent, DOZE));
                }
            }

            //华为 自启管理
            Intent huaweiIntent = new Intent();
            huaweiIntent.setAction("huawei.intent.action.HSM_BOOTAPP_MANAGER");
            sIntentWrapperList.add(new IntentWrapper(huaweiIntent, HUAWEI));

            //华为 锁屏清理
            Intent huaweiGodIntent = new Intent();
            huaweiGodIntent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            sIntentWrapperList.add(new IntentWrapper(huaweiGodIntent, HUAWEI_GOD));

            //小米 自启动管理
            Intent xiaomiIntent = new Intent();
            xiaomiIntent.setAction("miui.intent.action.OP_AUTO_START");
            xiaomiIntent.addCategory(Intent.CATEGORY_DEFAULT);
            sIntentWrapperList.add(new IntentWrapper(xiaomiIntent, XIAOMI));

            //小米 神隐模式 (建议只在 App 的核心功能需要后台连接网络/后台定位的情况下使用)
            Intent xiaomiGodIntent = new Intent();
            xiaomiGodIntent.setComponent(new ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity"));
            sIntentWrapperList.add(new IntentWrapper(xiaomiGodIntent, XIAOMI_GOD));

            //三星 5.0/5.1 自启动应用程序管理
            Intent samsungLIntent = context.getPackageManager().getLaunchIntentForPackage("com.samsung.android.sm");
            if (samsungLIntent != null) sIntentWrapperList.add(new IntentWrapper(samsungLIntent, SAMSUNG_L));

            //三星 6.0+ 未监视的应用程序管理
            Intent samsungMIntent = new Intent();
            samsungMIntent.setComponent(new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.battery.BatteryActivity"));
            sIntentWrapperList.add(new IntentWrapper(samsungMIntent, SAMSUNG_M));

            //魅族 自启动管理
            Intent meizuIntent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            meizuIntent.addCategory(Intent.CATEGORY_DEFAULT);
            meizuIntent.putExtra("packageName", context.getPackageName());
            sIntentWrapperList.add(new IntentWrapper(meizuIntent, MEIZU));

            //魅族 待机耗电管理
            Intent meizuGodIntent = new Intent();
            meizuGodIntent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.powerui.PowerAppPermissionActivity"));
            sIntentWrapperList.add(new IntentWrapper(meizuGodIntent, MEIZU_GOD));

            //Oppo 自启动管理
            Intent oppoIntent = new Intent();
            oppoIntent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            sIntentWrapperList.add(new IntentWrapper(oppoIntent, OPPO));

            //Oppo 自启动管理(旧版本系统)
            Intent oppoOldIntent = new Intent();
            oppoOldIntent.setComponent(new ComponentName("com.color.safecenter", "com.color.safecenter.permission.startup.StartupAppListActivity"));
            sIntentWrapperList.add(new IntentWrapper(oppoOldIntent, OPPO_OLD));

            //Vivo 后台高耗电
            Intent vivoGodIntent = new Intent();
            vivoGodIntent.setComponent(new ComponentName("com.vivo.abe", "com.vivo.applicationbehaviorengine.ui.ExcessivePowerManagerActivity"));
            sIntentWrapperList.add(new IntentWrapper(vivoGodIntent, VIVO_GOD));

            //金立 应用自启
            Intent gioneeIntent = new Intent();
            gioneeIntent.setComponent(new ComponentName("com.gionee.softmanager", "com.gionee.softmanager.MainActivity"));
            sIntentWrapperList.add(new IntentWrapper(gioneeIntent, GIONEE));

            //乐视 自启动管理
            Intent letvIntent = new Intent();
            letvIntent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            sIntentWrapperList.add(new IntentWrapper(letvIntent, LETV));

            //乐视 应用保护
            Intent letvGodIntent = new Intent();
            letvGodIntent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.BackgroundAppManageActivity"));
            sIntentWrapperList.add(new IntentWrapper(letvGodIntent, LETV_GOD));

            //酷派 自启动管理
            Intent coolpadIntent = new Intent();
            coolpadIntent.setComponent(new ComponentName("com.yulong.android.security", "com.yulong.android.seccenter.tabbarmain"));
            sIntentWrapperList.add(new IntentWrapper(coolpadIntent, COOLPAD));

            //联想 后台管理
            Intent lenovoIntent = new Intent();
            lenovoIntent.setComponent(new ComponentName("com.lenovo.security", "com.lenovo.security.purebackground.PureBackgroundActivity"));
            sIntentWrapperList.add(new IntentWrapper(lenovoIntent, LENOVO));

            //联想 后台耗电优化
            Intent lenovoGodIntent = new Intent();
            lenovoGodIntent.setComponent(new ComponentName("com.lenovo.powersetting", "com.lenovo.powersetting.ui.Settings$HighPowerApplicationsActivity"));
            sIntentWrapperList.add(new IntentWrapper(lenovoGodIntent, LENOVO_GOD));

            //中兴 自启管理
            Intent zteIntent = new Intent();
            zteIntent.setComponent(new ComponentName("com.zte.heartyservice", "com.zte.heartyservice.autorun.AppAutoRunManager"));
            sIntentWrapperList.add(new IntentWrapper(zteIntent, ZTE));

            //中兴 锁屏加速受保护应用
            Intent zteGodIntent = new Intent();
            zteGodIntent.setComponent(new ComponentName("com.zte.heartyservice", "com.zte.heartyservice.setting.ClearAppSettingsActivity"));
            sIntentWrapperList.add(new IntentWrapper(zteGodIntent, ZTE_GOD));
        }
        return sIntentWrapperList;
    }

    protected static String sApplicationName;

    protected static String getApplicationName(Context context) {
        if (sApplicationName == null) {
            if (!InvokerEngine.sInitialized) return "";
            PackageManager pm;
            ApplicationInfo ai;
            try {
                pm = context.getPackageManager();
                ai = pm.getApplicationInfo(context.getPackageName(), 0);
                sApplicationName = pm.getApplicationLabel(ai).toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                sApplicationName = context.getPackageName();
            }
        }
        return sApplicationName;
    }

    /**
     * 处理白名单
     */
    public static void whiteListMatters(final Context context, final Activity a, String reason) {
        if (reason == null) reason = "核心服务的持续运行";
        List<IntentWrapper> intentWrapperList = getIntentWrapperList(a.getApplicationContext());
        for (final IntentWrapper iw : intentWrapperList) {
            //如果本机上没有能处理这个Intent的Activity，说明不是对应的机型，直接忽略进入下一次循环。
            if (!iw.doesActivityExists(context)) continue;
            switch (iw.type) {
                case DOZE:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                        if (pm.isIgnoringBatteryOptimizations(a.getPackageName())) break;
                        new AlertDialog.Builder(a)
                                .setCancelable(false)
                                .setTitle("需要忽略 " + getApplicationName(context) + " 的电池优化")
                                .setMessage(reason + "需要 " + getApplicationName(context) + " 加入到电池优化的忽略名单。\n\n" +
                                        "请点击『确定』，在弹出的『忽略电池优化』对话框中，选择『是』。")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                                }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                IntentWrapperUtils.saveIntentWrapperSet(context, true);
                            }
                        }).show();
                    }
                    break;
                case HUAWEI:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle("需要允许 " + getApplicationName(context) + " 自动启动")
                            .setMessage(reason + "需要允许 " + getApplicationName(context) + " 的自动启动。\n\n" +
                                    "请点击『确定』，在弹出的『自启管理』中，将 " + getApplicationName(context) + " 对应的开关打开。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case ZTE_GOD:
                case HUAWEI_GOD:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle(getApplicationName(context) + " 需要加入锁屏清理白名单")
                            .setMessage(reason + "需要 " + getApplicationName(context) + " 加入到锁屏清理白名单。\n\n" +
                                    "请点击『确定』，在弹出的『锁屏清理』列表中，将 " + getApplicationName(context) + " 对应的开关打开。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
//                case XIAOMI_GOD:
//                    new AlertDialog.Builder(a)
//                            .setCancelable(false)
//                            .setTitle("需要关闭 " + getApplicationName(context) + " 的神隐模式")
//                            .setMessage(reason + "需要 " + getApplicationName(context) + " 的神隐模式关闭。\n\n" +
//                                    "请点击『确定』，在弹出的神隐模式应用列表中，点击 " + getApplicationName(context) + " ，然后选择『无限制』和『允许定位』。")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
//                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
//                        }
//                    })
//                            .show();
//                    break;
                case SAMSUNG_L:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle("需要允许 " + getApplicationName(context) + " 的自启动")
                            .setMessage(reason + "需要 " + getApplicationName(context) + " 在屏幕关闭时继续运行。\n\n" +
                                    "请点击『确定』，在弹出的『智能管理器』中，点击『内存』，选择『自启动应用程序』选项卡，将 " + getApplicationName(context) + " 对应的开关打开。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case SAMSUNG_M:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle("需要允许 " + getApplicationName(context) + " 的自启动")
                            .setMessage(reason + "需要 " + getApplicationName(context) + " 在屏幕关闭时继续运行。\n\n" +
                                    "请点击『确定』，在弹出的『电池』页面中，点击『未监视的应用程序』->『添加应用程序』，勾选 " + getApplicationName(context) + "，然后点击『完成』。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case MEIZU:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle("需要允许 " + getApplicationName(context) + " 的自启动")
                            .setMessage(reason + "需要允许 " + getApplicationName(context) + " 的自启动。\n\n" +
                                    "请点击『确定』，在弹出的应用信息界面中，将『自启动』开关打开。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case MEIZU_GOD:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle(getApplicationName(context) + " 需要在待机时保持运行")
                            .setMessage(reason + "需要 " + getApplicationName(context) + " 在待机时保持运行。\n\n" +
                                    "请点击『确定』，在弹出的『待机耗电管理』中，将 " + getApplicationName(context) + " 对应的开关打开。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case ZTE:
                case LETV:
                case XIAOMI:
                case OPPO:
                case OPPO_OLD:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle("需要允许 " + getApplicationName(context) + " 的自启动")
                            .setMessage(reason + "需要 " + getApplicationName(context) + " 加入到自启动白名单。\n\n" +
                                    "请点击『确定』，在弹出的『自启动管理』中，将 " + getApplicationName(context) + " 对应的开关打开。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case COOLPAD:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle("需要允许 " + getApplicationName(context) + " 的自启动")
                            .setMessage(reason + "需要允许 " + getApplicationName(context) + " 的自启动。\n\n" +
                                    "请点击『确定』，在弹出的『酷管家』中，找到『软件管理』->『自启动管理』，取消勾选 " + getApplicationName(context) + "，将 " + getApplicationName(context) + " 的状态改为『已允许』。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case VIVO_GOD:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle(getApplicationName(context) + " 需要在后台高耗电时允许运行")
                            .setMessage(reason + "需要允许 " + getApplicationName(context) + " 在后台高耗电时运行。\n\n" +
                                    "请点击『确定』，在弹出的『后台高耗电』中，将 " + getApplicationName(context) + " 对应的开关打开。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case GIONEE:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle(getApplicationName(context) + " 需要加入应用自启和绿色后台白名单")
                            .setMessage(reason + "需要允许 " + getApplicationName(context) + " 的自启动和后台运行。\n\n" +
                                    "请点击『确定』，在弹出的『系统管家』中，分别找到『应用管理』->『应用自启』和『绿色后台』->『清理白名单』，将 " + getApplicationName(context) + " 添加到白名单。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case LETV_GOD:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle("需要禁止 " + getApplicationName(context) + " 被自动清理")
                            .setMessage(reason + "需要禁止 " + getApplicationName(context) + " 被自动清理。\n\n" +
                                    "请点击『确定』，在弹出的『应用保护』中，将 " + getApplicationName(context) + " 对应的开关关闭。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case LENOVO:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle("需要允许 " + getApplicationName(context) + " 的后台 GPS 和后台运行")
                            .setMessage(reason + "需要允许 " + getApplicationName(context) + " 的后台自启、后台 GPS 和后台运行。\n\n" +
                                    "请点击『确定』，在弹出的『后台管理』中，分别找到『后台自启』、『后台 GPS』和『后台运行』，将 " + getApplicationName(context) + " 对应的开关打开。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
                case LENOVO_GOD:
                    new AlertDialog.Builder(a)
                            .setCancelable(false)
                            .setTitle("需要关闭 " + getApplicationName(context) + " 的后台耗电优化")
                            .setMessage(reason + "需要关闭 " + getApplicationName(context) + " 的后台耗电优化。\n\n" +
                                    "请点击『确定』，在弹出的『后台耗电优化』中，将 " + getApplicationName(context) + " 对应的开关关闭。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int w) {iw.startActivity(a);}
                            }).setNegativeButton("已经设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentWrapperUtils.saveIntentWrapperSet(context, true);
                        }
                    })
                            .show();
                    break;
            }
        }
    }

    /**
     * 防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
     */
    public static void onBackPressed(Activity a) {
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        a.startActivity(launcherIntent);
    }

    protected Intent intent;
    protected int type;

    protected IntentWrapper(Intent intent, int type) {
        this.intent = intent;
        this.type = type;
    }

    /**
     * 判断本机上是否有能处理当前Intent的Activity
     */
    protected boolean doesActivityExists(Context context) {
        if (!InvokerEngine.sInitialized) return false;
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list != null && list.size() > 0;
    }

    /**
     * 安全地启动一个Activity
     */
    protected void startActivity(Activity a) {
        try {
            a.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
