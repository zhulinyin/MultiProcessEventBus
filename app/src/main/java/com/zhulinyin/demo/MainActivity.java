package com.zhulinyin.demo;

import android.content.Intent;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump_button:
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    String getActivityTitle() {
        return "主进程";
    }
}
