package com.zhulinyin.demo;

public class OneTimeEvent {

    private String message;

    private long sendTimeStamp;

    public OneTimeEvent(String message, long sendTimeStamp) {
        this.message = message;
        this.sendTimeStamp = sendTimeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSendTimeStamp() {
        return sendTimeStamp;
    }

    public void setSendTimeStamp(long sendTimeStamp) {
        this.sendTimeStamp = sendTimeStamp;
    }
}
