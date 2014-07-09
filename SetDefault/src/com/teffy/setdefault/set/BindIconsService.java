package com.teffy.setdefault.set;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
/**
 * Service用户绑定View
 * @author lumeng
 *
 */
public class BindIconsService extends Service {
    public static final String BIND_TAG = "unbind";
    private ScreenStateReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new ScreenStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver, filter );// 屏幕点亮的广播，必须在code中注册才有效
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            unregisterReceiver(receiver);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sp = getSharedPreferences(BindActions.BIND_SERVICE_STATE,Context.MODE_PRIVATE);
        boolean b = sp.getBoolean(BIND_TAG, false);// 获取是否需要绑定
        if (b) {
            AlertView.removeAlertWindow(this);//不需要绑定，删除AlerView，并停止service
            stopSelf();
        } else {
            createView();
        }
        return START_STICKY;
    }
    /**
     * 添加透明窗口
     */
    private void createView() {
        SharedPreferences sp = getSharedPreferences(BindActions.SCREEN_BIND_ACTIONS, Context.MODE_PRIVATE);
        boolean _call = sp.getBoolean(BindActions.BIND_ICON_ACTION_+"Call", true);
        boolean _contacts = sp.getBoolean(BindActions.BIND_ICON_ACTION_+"Contacts", true);
        boolean _mms = sp.getBoolean(BindActions.BIND_ICON_ACTION_+"Mms", true);
        AlertView.addAlertWindow(this, _call, _contacts, _mms);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
