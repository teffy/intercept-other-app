
package com.teffy.setdefault.set;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

public class PhoneTypeUtils {
    /**
     * 返回桌面
     * @param context
     */
    public static void gobackHome(Context context) {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addCategory(Intent.CATEGORY_HOME);
        try {
            RunningTaskInfo runningHome = getRunningHome(context);
            i.setComponent(runningHome.topActivity);
            context.startActivity(i);
        } catch (Exception e) {
            context.startActivity(i);
        }
    }
    /**
     * 获取正在运行的 桌面程序 信息
     * @param context
     * @return
     */
    public static RunningTaskInfo getRunningHome(Context context) {
        List<String> homes = getAllExistedHomes(context);
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(100);
        RunningTaskInfo home = null;
        for (RunningTaskInfo r : rti) {
            if(homes.contains(r.topActivity.getPackageName())){
                home = r;
                break;
            }
        }
        return home;
    }
    /**
     * 获取系统中所有的桌面程序的信息
     * @param context
     * @return
     */
    private static List<String> getAllExistedHomes(Context context) {
        List<String> packages = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : resolveInfo) {
            packages.add(info.activityInfo.packageName);
        }
        return packages;
    }
}
