package com.zhulinyin.multiprocess_eventbus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zhulinyin.multiprocess_eventbus.manager.MessengerManager;

public class MainProcessService extends Service {

    public MainProcessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return MessengerManager.getInstance().getLocalMessenger().getBinder();
    }
}
