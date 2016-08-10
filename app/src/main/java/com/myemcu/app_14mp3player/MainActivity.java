/*
* 程序异常：
*           1. Activity与Service的广播通信间的标识不一致导致通信不上；
*           2. Activity中的IntentFilter filter = new IntentFilter(广播标识);中必须写上广播标识。
* */

package com.myemcu.app_14mp3player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.transform.Source;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mStop;
    private ImageButton mPlay;
    private TextView mTitle;
    private TextView mAuthor;
    private Button mPrev;
    private Button mNext;

    public static final String CTRL = "com.myemcu.app_14mp3player.ctrl"; // 播放，暂停控制
    public static final String UPTE = "com.myemcu.app_14mp3player.upte"; // 更新，界面显示

    int status = 0x11;  // 播放状态(0x11:未播放,0x12:正播放,0x13:暂停播放)

    String[] titleStr  = new String[] {"老男孩","春天里","在路上"};
    String[] authorStr = new String[] {"筷子兄弟","汪峰","刘欢"};

    private ActivityReceiver mActivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 主框架搭建

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews(); // 获取界面控件

        // 集中处理Button点击事件
        mStop.setOnClickListener(this); // Alt+Enter(在MainActivity上实现接口)
        mPlay.setOnClickListener(this);
        mPrev.setOnClickListener(this);
        mNext.setOnClickListener(this);

        // 在意图过滤器中注册其广播
        // Alt+Enter(内部类)
        mActivityReceiver = new ActivityReceiver();
        IntentFilter filter = new IntentFilter(UPTE); // 记得里面有参数
        registerReceiver(mActivityReceiver,filter);

        // 启动后台Service(先在工程中建立该Service)
        Intent intent = new Intent(this,MusicService.class);
        startService(intent);
    }

    private void findViews() {
        mStop = (ImageButton) findViewById(R.id.stop);
        mPlay = (ImageButton) findViewById(R.id.play);
        mTitle = (TextView) findViewById(R.id.title);
        mAuthor = (TextView) findViewById(R.id.author);
        mPrev = (Button) findViewById(R.id.prev);
        mNext = (Button) findViewById(R.id.next);
    }

    @Override
    public void onClick(View v) { // Button事件的集中处理

        Intent intent = new Intent(CTRL);

        switch (v.getId()) {
            case R.id.play: intent.putExtra("ctl",1); break;
            case R.id.stop: intent.putExtra("ctl",2); break;
            case R.id.prev: intent.putExtra("ctl",3); break;
            case R.id.next: intent.putExtra("ctl",4); break;
        }

        sendBroadcast(intent);  // 发送广播并携带相应数据
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mActivityReceiver);
    }

    //Activity的广播接收器主要实现界面更新
    private class ActivityReceiver extends BroadcastReceiver{ // Alt+Enter实现方法
        @Override
        public void onReceive(Context context, Intent intent) {

            int current = intent.getIntExtra("current",-1);
            int update  = intent.getIntExtra("update",-1);

            if (current>=0) {
                mTitle.setText(titleStr[current]);
                mAuthor.setText(authorStr[current]);
            }

            switch (update) {
                case 0x11: mPlay.setImageResource(R.drawable.play);  status=0x11; break;
                case 0x12: mPlay.setImageResource(R.drawable.pause); status=0x12; break;
                case 0x13: mPlay.setImageResource(R.drawable.play);  status=0x13; break;
            }

        }
    }
}
