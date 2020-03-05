package com.zhulinyin.multiprocess_eventbus;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Messenger;

import com.zhulinyin.multiprocess_eventbus.handler.ChildProcessHandler;
import com.zhulinyin.multiprocess_eventbus.handler.MainProcessHandler;
import com.zhulinyin.multiprocess_eventbus.manager.MessengerManager;
import com.zhulinyin.multiprocess_eventbus.manager.ServiceBindManager;
import com.zhulinyin.multiprocess_eventbus.utils.ProcessUtil;

import org.greenrobot.eventbus.EventBus;

public class MultiProcessEventBus {

    private static final String TAG = "MultiProcessEventBus";

    private static volatile MultiProcessEventBus sInstance;

    public static MultiProcessEventBus getDefault() {
        if (sInstance == null) {
            synchronized (MultiProcessEventBus.class) {
                if (sInstance == null) {
                    sInstance = new MultiProcessEventBus();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        HandlerThread handlerThread = new HandlerThread("MessengerThread");
        handlerThread.start();
        if (ProcessUtil.isMainProcess(context)) {
            MessengerManager.getInstance().init(new Messenger(new MainProcessHandler(handlerThread.getLooper())));
        } else {
            MessengerManager.getInstance().init(new Messenger(new ChildProcessHandler(handlerThread.getLooper())));
            ServiceBindManager.getInstance().bindHostService(context);
        }
    }

    public void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public boolean isRegistered(Object subscriber) {
        return EventBus.getDefault().isRegistered(subscriber);
    }

    public void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public void post(Object event) {
        MessengerManager.getInstance().postWithType(event, Constant.MsgCode.MSG_POST);
    }

    public void postSticky(Object event) {
        MessengerManager.getInstance().postWithType(event, Constant.MsgCode.MSG_POST_STICKY);
    }
}
