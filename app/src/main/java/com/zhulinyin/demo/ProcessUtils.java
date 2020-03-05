package com.zhulinyin.demo;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

public class ProcessUtils {

    public static void killMainProcess(Context context) {
        Process.killProcess(getMainProcessId(context));
    }

    public static int getMainProcessId(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : activityManager.getRunningAppProcesses()) {
            if (appProcessInfo.processName.equals(context.getPackageName())) {
                return appProcessInfo.pid;
            }
        }
        return 0;
    }
}
