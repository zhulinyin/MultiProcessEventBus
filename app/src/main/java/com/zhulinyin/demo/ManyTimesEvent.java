package com.zhulinyin.demo;

public class ManyTimesEvent {

    private boolean isFirst;

    private boolean isLast;

    private String message;

    private long sendTimeStamp;

    public ManyTimesEvent(boolean isFirst, boolean isLast, String message, long sendTimeStamp) {
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.message = message;
        this.sendTimeStamp = sendTimeStamp;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
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
