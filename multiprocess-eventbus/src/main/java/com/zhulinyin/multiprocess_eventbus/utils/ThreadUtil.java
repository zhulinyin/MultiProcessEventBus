package com.zhulinyin.multiprocess_eventbus.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 线程工具类
 *
 */
public class ThreadUtil {

    private static final String TAG = "ThreadUtil";

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    @SuppressLint("CI_NotAllowInvokeExecutorsMethods")
    private static final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * @return 当前线程是否是 UI 线程
     */
    public static boolean isUIThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 当 Runnable 不在主线程时，切换 Runnable 的执行到主线程
     *
     * @param runnable 需要在主线程执行的 Runnable
     */
    public static void runOnUIThread(@NonNull Runnable runnable) {
        runOnUIThread(runnable, true);
    }

    /**
     * 当 Runnable 不在主线程时，切换 Runnable 的执行到主线程
     *
     * @param runnable            需要在主线程执行的 Runnable
     * @param runDirectOnUIThread 当当前线程是 UI 线程时是否直接运行该 runnable
     */
    public static void runOnUIThread(@NonNull Runnable runnable, boolean runDirectOnUIThread) {
        if (isUIThread() && runDirectOnUIThread) {
            runnable.run();
        } else {
            mainHandler.post(runnable);
        }
    }

    /**
     * 延时指定时间在 UI 线程执行指定操作
     *
     * @param runnable    需要在主线程执行的 Runnable
     * @param delayMillis 延时时间，单位毫秒，小于等于 0 时会将功能转发给 {@link #runOnUIThread(Runnable)}
     *                    执行
     */
    public static void runOnUIThread(final Runnable runnable, long delayMillis) {
        if (delayMillis <= 0) {
            runOnUIThread(runnable);
            return;
        }
        mainHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * 取消在 UI 线程等待队列中等待的 Runnable 的执行
     *
     * @param runnable UI 线程等待队列等待的 Runnable
     */
    public static void cancelUIRunnable(@Nullable Runnable runnable) {
        if (runnable == null) {
            return;
        }
        mainHandler.removeCallbacks(runnable);
    }

    /**
     * 如果在主线程，强制切换到子线程
     */
    public static void runOnWorkThread(@NonNull Runnable runnable) {
        if (isUIThread()) {
            executor.execute(runnable);
        } else{
            runnable.run();
        }
    }

    /**
     * 在指定线程中执行
     * @param runnable
     * @param isOnUIThread 是否在 UI 线程
     */
    public static void runOnSpecifiedThread(@NonNull Runnable runnable, boolean isOnUIThread) {
        if (isOnUIThread) {
            runOnUIThread(runnable);
        } else {
            runOnWorkThread(runnable);
        }
    }
}
