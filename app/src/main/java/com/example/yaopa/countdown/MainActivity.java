package com.example.yaopa.countdown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yaopa.countdown.view.CountDownView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private CountDownView countDownView;
    private long lastTime;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setClickable(false);
                countDownView.start();
            }
        });
        countDownView = (CountDownView) findViewById(R.id.countdown_view);
        countDownView.setTime(5f);
        countDownView.setCountListener(new CountDownView.CountDownListner() {
            private long aLong;

            @Override
            public void onStartCount() {
                show("开始计时");
                aLong = System.currentTimeMillis();
            }

            @Override
            public void onFinishCount() {
                show("计时结束");
                button.setClickable(true);
                Log.e(TAG, "onFinishCount: " + (System.currentTimeMillis() - aLong));
            }
        });
    }

    @Override
    public void onBackPressed() {
        long millis = System.currentTimeMillis();
        if ((millis - lastTime) < 2 * 1000) {
            super.onBackPressed();
        }
        else {
            lastTime = millis;
            show("再次点击退出");
        }
    }

    public void show(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
        else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
