package com.zhulinyin.multiprocess_eventbus.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

import com.zhulinyin.multiprocess_eventbus.MainProcessService;

/**
 * 主进程不要调用该类
 */
public class ServiceBindManager {

    private static final String TAG = "ServiceBindManager";

    /**
     * 是否正在请求绑定主进程
     */
    private boolean mPendingBindHostService = false;

    private static volatile ServiceBindManager sInstance;

    public static ServiceBindManager getInstance() {
        if (sInstance == null) {
            synchronized (ServiceBindManager.class) {
                if (sInstance == null) {
                    sInstance = new ServiceBindManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 绑定主进程service
     *
     * @param context
     */
    public void bindHostService(Context context) {
        if (mPendingBindHostService) {
            return;
        }
        mPendingBindHostService = true;
        Log.d(TAG, "start bind host service");
        context.bindService(new Intent(context, MainProcessService.class), mHostServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection mHostServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mPendingBindHostService = false;
            Messenger messenger = new Messenger(service);
            MessengerManager.getInstance().registerClient(messenger);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };
}
