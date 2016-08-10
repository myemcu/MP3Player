package com.myemcu.app_14mp3player;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

public class MusicService extends Service {

    int cnt=0; // 当前曲号

    String[] music = new String[] {"oldboy.mp3","spring.mp3","way.mp3"};

    private AssetManager mAssets;
    private MediaPlayer mPlayer;

    int status = 0x11;
    private ServiceReceiver mServiceReceiver;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {

        // 获取资源管(资源管理器)
        mAssets = getAssets();

        // 在意图过滤器中注册其广播
        mServiceReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter(MainActivity.CTRL);
        registerReceiver(mServiceReceiver,filter);

        // 创建媒体播放器
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {  //开机顺序播放(默认)

                cnt++;
                if (cnt>=3) cnt=0;

                Intent intent = new Intent(MainActivity.UPTE);
                intent.putExtra("current",cnt);
                sendBroadcast(intent); // 发送广播到MainActivity

                preparePlay(music[cnt]); // 音乐播放
            }
        });

        super.onCreate();
    }

    // 音乐播放(方法)
    private void preparePlay(String s) {
        try {
                AssetFileDescriptor afd = mAssets.openFd(s); // 打开指定的音乐文件
                mPlayer.reset();
                mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                mPlayer.prepare();
                mPlayer.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServiceReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(final Context context, Intent intent) {

            int control = intent.getIntExtra("ctl",-1);

            switch (control) {

                case 1: if (status==0x11) {
                            preparePlay(music[cnt]);
                            status=0x12;
                        }
                        else if (status==0x12) {
                            mPlayer.pause();
                            status=0x13;
                        }
                        else if (status==0x13) {
                            mPlayer.start();
                            status=0x12;
                        }
                        break;

                case 2: if (status==0x12 || status==0x13) {
                            mPlayer.stop();
                             status=0x11;
                        }

                        break;

                case 3: if (status==0x12) {
                            cnt--;
                            if (cnt<0)
                            cnt=2;
                            preparePlay(music[cnt]);
                        }
                        break;

                case 4: if (status==0x12) {
                            cnt++;
                            if (cnt>=3)
                            cnt=0;
                            preparePlay(music[cnt]);
                        }
                        break;
            }

            //preparePlay(music[cnt]); // 放在此处歌就停不下来

            /* 发送广播通知Activity更改图标、文本框 */
            Intent sendIntent = new Intent(MainActivity.UPTE);
            sendIntent.putExtra("update",status);
            sendIntent.putExtra("current",cnt);
            sendBroadcast(sendIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mServiceReceiver);
    }
}
