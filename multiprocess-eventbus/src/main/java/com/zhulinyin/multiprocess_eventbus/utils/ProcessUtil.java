package com.zhulinyin.multiprocess_eventbus.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

public class ProcessUtil {

    private static final String TAG = "ProcessUtil";

    private static String sCurProcessName = null;

    private static Boolean sIsMainProcess = null;

    /**
     * 是否是主进程
     *
     * @param context 应用上下文
     * @return 是否是主进程
     */
    public static boolean isMainProcess(Context context) {
        if (sIsMainProcess != null) {
            return sIsMainProcess;
        }
        if (context == null) {
            return false;
        }
        String processName = getCurProcessName(context);
        return processName != null && (sIsMainProcess = processName.equals(context.getPackageName()));
    }

    /**
     * 获取当前进程进程名
     *
     * @param context 应用上下文
     * @return 当前进程进程名
     */
    public static String getCurProcessName(Context context) {
        String procName = sCurProcessName;
        if (!TextUtils.isEmpty(procName)) {
            return procName;
        }
        try {
            int pid = Process.myPid();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
                    if (appProcess.pid == pid) {
                        Log.d(TAG, "processName = " + appProcess.processName);
                        sCurProcessName = appProcess.processName;
                        return sCurProcessName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sCurProcessName;
    }

    public static int getCurProcessId() {
        return Process.myPid();
    }
}
