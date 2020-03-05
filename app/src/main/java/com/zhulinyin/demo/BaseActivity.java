package com.zhulinyin.demo;

import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zhulinyin.multiprocess_eventbus.MultiProcessEventBus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BaseActivity";
    private static final int TIMES = 3000;
    private Button jumpButton;
    private Button sendButton;
    private Button sendManyTimesButton;
    private Button killButton;
    private TextView messageTextView;
    private long firstSendTimeStamp;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setTitle(getActivityTitle());
        MultiProcessEventBus.getDefault().register(BaseActivity.this);

        jumpButton = findViewById(R.id.jump_button);
        jumpButton.setOnClickListener(this);

        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MultiProcessEventBus.getDefault().post("收到来自" + getTitle() + "的消息");
                MultiProcessEventBus.getDefault().post(new OneTimeEvent("收到来自" + getTitle() + "的消息", System.currentTimeMillis()));
            }
        });

        sendManyTimesButton = findViewById(R.id.send_many_times_button);
        sendManyTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < TIMES; i++) {
                    boolean isFirst = false;
                    boolean isLast = false;
                    if (i == 0) {
                        isFirst = true;
                    }
                    if (i == TIMES - 1) {
                        isLast = true;
                    }
                    MultiProcessEventBus.getDefault().post(new ManyTimesEvent(isFirst, isLast,
                            "收到来自" + getTitle() + "的一堆消息", System.currentTimeMillis()));
                }
            }
        });

        killButton = findViewById(R.id.kill_button);
        killButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Process.killProcess(ProcessUtils.getMainProcessId(getApplicationContext()));
            }
        });

        messageTextView = findViewById(R.id.message_textview);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MultiProcessEventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(String message) {
        messageTextView.setText(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(OneTimeEvent oneTimeEvent) {
        String message = oneTimeEvent.getMessage();
        long sendTimeStamp = oneTimeEvent.getSendTimeStamp();
        long timeConsume = System.currentTimeMillis() - sendTimeStamp;
        String text = message + "\n" + "耗时：" + timeConsume;
        messageTextView.setText(text);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(ManyTimesEvent manyTimesEvent) {
        String message = manyTimesEvent.getMessage();
        long sendTimeStamp = manyTimesEvent.getSendTimeStamp();
        boolean isFirst = manyTimesEvent.isFirst();
        boolean isLast = manyTimesEvent.isLast();
        if (isFirst) {
            Log.d(TAG, "isFirst");
            count = 0;
            firstSendTimeStamp = sendTimeStamp;
        }
        count++;
        //Log.d(TAG, "第" + count + "条：" + (System.currentTimeMillis() - sendTimeStamp));
        messageTextView.setText(String.valueOf(count));
        if (isLast) {
            Log.d(TAG, "isLast");
            long timeConsume = System.currentTimeMillis() - firstSendTimeStamp;
            final String text = message + "\n"
                    + "消息总数：" + count + "\n"
                    + "耗时：" + timeConsume;
            messageTextView.setText(text);
        }
    }

    abstract String getActivityTitle();
}
