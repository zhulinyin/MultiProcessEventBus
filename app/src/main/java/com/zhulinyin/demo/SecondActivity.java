package com.zhulinyin.demo;

import android.content.Intent;
import android.view.View;

public class SecondActivity extends BaseActivity {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump_button:
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    String getActivityTitle() {
        return "子进程1";
    }
}
