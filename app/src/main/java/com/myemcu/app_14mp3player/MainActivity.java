package com.myemcu.app_14mp3player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mStop;
    private ImageButton mPlay;
    private TextView mTitle;
    private TextView mAuthor;

    public static final String CTRL = "com.myemcu.app_14mp3player.ctrl"; // 播放，暂停控制
    public static final String UPTE = "com.myemcu.app_14mp3player.upte"; // 更新，界面显示

    int status = 0x11;  // 播放状态(0x11:未播放,0x12:正播放,0x13:暂停播放)

    String[] titleStr  = new String[] {"老男孩","春天里","在路上"};
    String[] authorStr = new String[] {"筷子兄弟","汪峰","刘欢"};

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 主框架搭建
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews(); // 获取界面控件

        // 集中处理Button点击事件
        mStop.setOnClickListener(this); // Alt+Enter(在MainActivity上实现接口)
        mPlay.setOnClickListener(this);

        // 创建自定义广播接收对象并在意图过滤器中注册
        ActivityReceiver activityReceiver = new ActivityReceiver(); // Alt+Enter(内部类)
        IntentFilter filter = new IntentFilter();
        registerReceiver(activityReceiver,filter);

        // 启动后台Service(先在工程中建立该Service)
        Intent intent = new Intent(this,MusicService.class);
        startService(intent);
    }

    private void findViews() {
        mStop = (ImageButton) findViewById(R.id.stop);
        mPlay = (ImageButton) findViewById(R.id.play);
        mTitle = (TextView) findViewById(R.id.title);
        mAuthor = (TextView) findViewById(R.id.author);
    }

    @Override
    public void onClick(View v) { // Button事件的集中处理

    }

    private class ActivityReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

        } // Alt+Enter实现方法
    }
}
