package com.teffy.setdefault.set;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
/**
 * 透明窗口View，用于获取用户的touch时间
 * @author lumeng
 */
public class AlertView extends View {

    public static final String TAG = "AlertView";
    public static final boolean DEBUG = true;
    private static void logMsg(String msg){
        if(DEBUG){
            Log.i(TAG, msg);
        }
    }

    private static AlertView singleAlertView = null;
    private TouchEventHandlerThread eventHandler;

    private boolean is_bind_call;
    private boolean is_bind_contacts;
    private boolean is_bind_mms;

    public boolean isIs_bind_call() {
        return is_bind_call;
    }

    public boolean isIs_bind_contacts() {
        return is_bind_contacts;
    }

    public boolean isIs_bind_mms() {
        return is_bind_mms;
    }

    private AlertView(Context context,boolean is_bind_call,boolean is_bind_contacts,boolean is_bind_mms) {
        super(context);
        this.is_bind_call = is_bind_call;
        this.is_bind_contacts = is_bind_contacts;
        this.is_bind_mms = is_bind_mms;
    }
    /**
     * 停止接受信息
     */
    private void stopHandleTouch(){
        if(this.eventHandler == null){
            return;
        }
        this.eventHandler.quit();
        this.eventHandler = null;
    }
    /**
     * 删除透明窗口
     * @param context
     */
    public static void removeAlertWindow(Context context) {
        if (singleAlertView == null)
            return;
        if(singleAlertView != null){
            singleAlertView.stopHandleTouch();
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeView(singleAlertView);
        singleAlertView = null;
    }
    /**
     * 添加透明窗口
     * @param paramContext
     * @param is_bind_call
     * @param is_bind_contacts
     * @param is_bind_mms
     */
    public static void addAlertWindow(Context paramContext,boolean is_bind_call,boolean is_bind_contacts,boolean is_bind_mms) {
        if(singleAlertView != null){
            removeAlertWindow(paramContext);
        }
        singleAlertView = new AlertView(paramContext, is_bind_call, is_bind_contacts, is_bind_mms);
        addAlertView(paramContext);
    }
    /**
     * 开始接受屏幕touch事件的信息
     * @param paramContext
     */
    private void startHandleTouch(Context paramContext) {
        this.eventHandler = new TouchEventHandlerThread(this, paramContext);
        this.eventHandler.start();
        this.eventHandler.checkTop();
    }

    private static int exception_count = 0;
    /**
     * 添加窗口
     * @param paramContext
     */
    private static void addAlertView(Context paramContext) {
        if (singleAlertView == null)
            return;
        WindowManager localWindowManager = (WindowManager) paramContext.getSystemService(Context.WINDOW_SERVICE);
        singleAlertView.setBackgroundColor(0);
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.width = 1;
        localLayoutParams.height = 1;
        localLayoutParams.gravity = 51;
        localLayoutParams.x = 0;
        localLayoutParams.y = 0;
        localLayoutParams.format = 1;
        localLayoutParams.type = 2010;
        localLayoutParams.flags = 262152;
        try {
            localWindowManager.addView(singleAlertView, localLayoutParams);
            singleAlertView.startHandleTouch(paramContext);
            return;
        } catch (Exception localException1) {
            if(DEBUG){
                logMsg("Exception->"+localException1.getMessage());
            }
            removeAlertWindow(paramContext);
            if(exception_count > 5){
                if(DEBUG){
                    logMsg("can not add alertview ,exception:-->"+localException1.getMessage());
                }
                return;
            }
            addAlertView(paramContext);
            exception_count++;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((this.eventHandler != null) && (this.eventHandler.isAlive()))
            this.eventHandler.checkTop();
        logMsg("onTouchEvent---");
        return super.onTouchEvent(event);
    }
}
