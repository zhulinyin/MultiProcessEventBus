package com.zhulinyin.multiprocess_eventbus.manager;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.zhulinyin.multiprocess_eventbus.Constant;
import com.zhulinyin.multiprocess_eventbus.utils.ThreadUtil;
import com.zhulinyin.multiprocess_eventbus.utils.WrapUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MessengerManager {

    private static final String TAG = "MessengerManager";

    private static volatile MessengerManager sInstance;

    private List<Messenger> mMessengers = new ArrayList<>();

    private Messenger mLocalMessenger;

    public static MessengerManager getInstance() {
        if (sInstance == null) {
            synchronized (MessengerManager.class) {
                if (sInstance == null) {
                    sInstance = new MessengerManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Messenger localMessenger) {
        mLocalMessenger = localMessenger;
    }

    public Messenger getLocalMessenger() {
        return mLocalMessenger;
    }

    public void registerMessenger(Messenger messenger) {
        mMessengers.add(messenger);
    }

    public void registerMessengers(ArrayList<Messenger> messengers) {
        Log.d(TAG, "before_registerMessengers: " + mMessengers);
        messengers.remove(mLocalMessenger);
        mMessengers.removeAll(messengers);
        mMessengers.addAll(messengers);
        Log.d(TAG, "after_registerMessengers: " + mMessengers);
    }

    public void unRegisterMessenger(Messenger messenger) {
        mMessengers.remove(messenger);
    }

    public void postWithType(Object event, final int postType) {
        if (postType == Constant.MsgCode.MSG_POST) {
            EventBus.getDefault().post(event);
        } else if (postType == Constant.MsgCode.MSG_POST_STICKY) {
            EventBus.getDefault().postSticky(event);
        }
        final Bundle data = WrapUtil.wrapEvent(event);
        ThreadUtil.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                for (int i = mMessengers.size() - 1; i >= 0; i--) {
                    Log.d(TAG, "post to: " + mMessengers.get(i));
                    try {
                        Message message = Message.obtain(null, postType);
                        message.setData(data);
                        mMessengers.get(i).send(message);
                    } catch (RemoteException e) {
                        Log.e(TAG, "binder is dead, fail post to: " + mMessengers.get(i));
                        mMessengers.remove(i);
                    }
                }
            }
        });
    }

    public void registerClient(Messenger service) {
        Log.d(TAG, "registerClient: " + service);
        if (service != null) {
            try {
                registerMessenger(service);
                Message message = Message.obtain(null, Constant.MsgCode.MSG_REGISTER_CLIENT);
                message.replyTo = mLocalMessenger;
                service.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastClients() {
        ArrayList<Messenger> clients = new ArrayList<>(mMessengers);
        clients.add(mLocalMessenger);
        Log.d(TAG, "broadcastClients: " + clients);
        for (Messenger client : mMessengers) {
            Message message = Message.obtain(null, Constant.MsgCode.MSG_BROADCAST_CLIENTS);
            message.setData(WrapUtil.wrapClients(clients));
            try {
                client.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
