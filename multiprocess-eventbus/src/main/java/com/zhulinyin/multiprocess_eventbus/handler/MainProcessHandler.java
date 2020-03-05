package com.zhulinyin.multiprocess_eventbus.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.zhulinyin.multiprocess_eventbus.Constant;
import com.zhulinyin.multiprocess_eventbus.manager.MessengerManager;
import com.zhulinyin.multiprocess_eventbus.utils.ThreadUtil;
import com.zhulinyin.multiprocess_eventbus.utils.WrapUtil;

import org.greenrobot.eventbus.EventBus;

public class MainProcessHandler extends Handler {

    private static final String TAG = "MainProcessHandler";

    public MainProcessHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case Constant.MsgCode.MSG_POST:
                final Object postEvent = WrapUtil.unWrapEvent(msg.getData());
                ThreadUtil.runOnSpecifiedThread(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(postEvent);
                    }
                }, WrapUtil.isEventOnMainThread(msg.getData()));
                break;
            case Constant.MsgCode.MSG_POST_STICKY:
                final Object postStickyEvent = WrapUtil.unWrapEvent(msg.getData());
                ThreadUtil.runOnSpecifiedThread(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().postSticky(postStickyEvent);
                    }
                }, WrapUtil.isEventOnMainThread(msg.getData()));
                break;
            case Constant.MsgCode.MSG_REGISTER_CLIENT:
                MessengerManager.getInstance().registerMessenger(msg.replyTo);
                MessengerManager.getInstance().broadcastClients();
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
