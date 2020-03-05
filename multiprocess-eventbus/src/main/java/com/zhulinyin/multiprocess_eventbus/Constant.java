package com.zhulinyin.multiprocess_eventbus;

public interface Constant {

    interface MsgCode {

        int MSG_REGISTER_CLIENT = 1;

        int MSG_BROADCAST_CLIENTS = 2;

        int MSG_POST = 3;

        int MSG_POST_STICKY = 4;
    }

    interface CallDataKey {

        String CLASS_NAME = "className";

        String EVENT_JSON = "eventJson";

        String CLIENTS = "clients";

        String IS_MAIN_THREAD = "isMainThread";
    }
}
