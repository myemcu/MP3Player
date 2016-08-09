package com.myemcu.app_14mp3player;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
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

        AssetManager am = getAssets(); // 获取资源管(资源管理器)

        // 在意图过滤器中注册其广播
        ServiceReceiver serviceReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter(MainActivity.CTRL);
        registerReceiver(serviceReceiver,filter);

        MediaPlayer mPlayer = new MediaPlayer();

        super.onCreate();
    }

    private class ServiceReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
