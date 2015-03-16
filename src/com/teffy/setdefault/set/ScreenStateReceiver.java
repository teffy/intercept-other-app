package com.teffy.setdefault.set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
/**
 * Receiver，用于接收屏幕点亮，变暗，解锁屏幕，和系统启动
 * @author lumeng
 *
 */
public class ScreenStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String intent_action = intent.getAction();
        if(BindActions.BIND_ACTION.equals(intent_action)){
            handleBindEvent(context, intent);
        } else if(Intent.ACTION_USER_PRESENT.equals(intent_action)){
            //Log.i("LM", "ACTION_USER_PRESENT");
            SystemClock.sleep(200);
            handleBindEvent(context, intent);
        } else if(Intent.ACTION_SCREEN_ON.equals(intent_action)){
            //Log.i("LM", "ACTION_SCREEN_ON");
            handleBindEvent(context, intent);
        } else if(Intent.ACTION_SCREEN_OFF.equals(intent_action)){
            //Log.i("LM", "ACTION_SCREEN_OFF");
        }else if(Intent.ACTION_BOOT_COMPLETED.equals(intent_action)){
            //Log.i("LM", "ACTION_BOOT_COMPLETED");
            handleBindEvent(context, intent);
        }
    }
    /**
     * 处理事件
     * @param context
     * @param intent
     */
    private void handleBindEvent(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences(BindActions.SCREEN_BIND_ACTIONS,
                Context.MODE_PRIVATE);
        boolean _call = sp.getBoolean(BindActions.BIND_ICON_ACTION_ + "Call", true);
        boolean _contacts = sp.getBoolean(BindActions.BIND_ICON_ACTION_ + "Contacts", true);
        boolean _mms = sp.getBoolean(BindActions.BIND_ICON_ACTION_ + "Mms", true);

        if (!_call && !_contacts && !_mms) {// 没有需要绑定的图标
            sp = context.getSharedPreferences(BindActions.BIND_SERVICE_STATE, Context.MODE_PRIVATE);
            sp.edit().putBoolean(BindIconsService.BIND_TAG, true).commit();
            Intent i = new Intent(context, BindIconsService.class);
            context.startService(i);
        } else {
            sp = context.getSharedPreferences(BindActions.BIND_SERVICE_STATE, Context.MODE_PRIVATE);
            sp.edit().putBoolean(BindIconsService.BIND_TAG, false).commit();
            context.startService(new Intent(context, BindIconsService.class));
        }
    }
}
