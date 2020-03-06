package com.zhulinyin.multiprocess_eventbus.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.NonNull;

import com.zhulinyin.multiprocess_eventbus.Constant;
import com.zhulinyin.multiprocess_eventbus.manager.MessengerManager;
import com.zhulinyin.multiprocess_eventbus.utils.ThreadUtil;
import com.zhulinyin.multiprocess_eventbus.utils.WrapUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ChildProcessHandler extends Handler {

    private static final String TAG = "ChildProcessHandler";

    public ChildProcessHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case Constant.MsgCode.MSG_POST:
                Object postEvent = WrapUtil.unWrapEvent(msg.getData());
                EventBus.getDefault().post(postEvent);
                break;
            case Constant.MsgCode.MSG_POST_STICKY:
                Object postStickyEvent = WrapUtil.unWrapEvent(msg.getData());
                EventBus.getDefault().postSticky(postStickyEvent);
                break;
            case Constant.MsgCode.MSG_BROADCAST_CLIENTS:
                ArrayList<Messenger> clients = WrapUtil.unWrapClients(msg.getData());
                MessengerManager.getInstance().registerMessengers(clients);
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
