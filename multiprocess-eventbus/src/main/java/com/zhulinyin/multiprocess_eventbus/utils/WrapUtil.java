package com.zhulinyin.multiprocess_eventbus.utils;

import android.os.Bundle;
import android.os.Messenger;
import android.text.TextUtils;

import com.zhulinyin.multiprocess_eventbus.Constant;

import java.util.ArrayList;

public class WrapUtil {
    public static Bundle wrapEvent(Object event) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CallDataKey.CLASS_NAME, event.getClass().getName());
        bundle.putString(Constant.CallDataKey.EVENT_JSON, SerializeUtil.encode(event));
        bundle.putBoolean(Constant.CallDataKey.IS_MAIN_THREAD, ThreadUtil.isUIThread());
        return bundle;
    }

    public static Object unWrapEvent(Bundle bundle) {
        String className = bundle.getString(Constant.CallDataKey.CLASS_NAME);
        try {
            if (!TextUtils.isEmpty(className)) {
                Class<?> clazz = Class.forName(className);
                return SerializeUtil.decode(bundle.getString(Constant.CallDataKey.EVENT_JSON), clazz);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isEventOnMainThread(Bundle bundle) {
        return bundle.getBoolean(Constant.CallDataKey.IS_MAIN_THREAD);
    }

    public static Bundle wrapClients(ArrayList<Messenger> clients) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.CallDataKey.CLIENTS, clients);
        return bundle;
    }

    public static ArrayList<Messenger> unWrapClients(Bundle bundle) {
        return bundle.getParcelableArrayList(Constant.CallDataKey.CLIENTS);
    }
}
