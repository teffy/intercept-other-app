
package com.teffy.setdefault.set;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.teffy.setdefault.TwoActivity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class TouchEventHandlerThread extends HandlerThread {
    public static final String TAG = "TouchEventHandlerThread";
    public static final boolean DEBUG = true;
    private static void logMsg(String msg){
        if(DEBUG){
            Log.i(TAG, msg);
        }
    }
    public static final int LANCH_TYPE_NONE = -1;
    public static final int LANCH_TYPE_CALL = 1;
    public static final int LANCH_TYPE_CONTACTS = 2;
    public static final int LANCH_TYPE_MMS = 3;

    private HashSet<String> bind_set_call;
    private HashSet<String> bind_set_contacts;
    private HashSet<String> bind_set_mms;

    private Context mContext;
    private TouchHandler mHandler = null;
    private AlertView alertView;
    private ActivityManager mActivityManager;

    public TouchEventHandlerThread(AlertView alertView, Context paramContext) {
        super(TAG);
        this.mContext = paramContext;
        this.alertView = alertView;
        this.bind_set_call = new HashSet<String>();
        this.bind_set_contacts = new HashSet<String>();
        this.bind_set_mms = new HashSet<String>();
        mActivityManager = (ActivityManager)paramContext.getSystemService(Context.ACTIVITY_SERVICE);
        initFilterActions();
    }
    /**
     * 判断是否有需要绑定
     * @return
     */
    private boolean hasBind() {
        if (alertView == null) {
            return false;
        } else {
            return alertView.isIs_bind_call() || alertView.isIs_bind_contacts()
                    || alertView.isIs_bind_mms();
        }
    }
    /**
     * 添加需要过滤的类名信息
     */
    public void initFilterActions() {
        Intent i_call = new Intent();
        i_call.setAction("android.intent.action.CALL_BUTTON");
        addFilterString(i_call, bind_set_call);
        Intent i_dial = new Intent();
        i_dial.setAction("android.intent.action.DIAL");
        addFilterString(i_dial, bind_set_call);
        Intent localIntent5 = new Intent();
        localIntent5.setAction("android.intent.action.CALL");
        addFilterString(localIntent5, bind_set_call);
        // s6
        bind_set_call.add("com.yulong.android.contacts.dial.DialActivity");
        // suning
        bind_set_call.add("com.android.contacts.activities.DialtactsActivity");

        Intent i_contents = new Intent();
        i_contents.setAction("android.intent.action.VIEW");
        i_contents.setType("vnd.android.cursor.dir/contact");
        addFilterString(i_contents, bind_set_contacts);
        Intent i_person = new Intent("android.intent.action.VIEW");
        i_person.setType("vnd.android.cursor.dir/person");
        addFilterString(i_person, bind_set_contacts);
        // meizu 
        bind_set_contacts.add("com.meizu.mzsnssyncservice.ui.SnsTabActivity");
        bind_set_contacts.add("com.sec.android.app.contacts.PhoneBookTopMenuActivity");
        // xiaomi
        bind_set_contacts.add("com.android.contacts.activities.TwelveKeyDialer");
        bind_set_contacts.add("com.android.mms.ui.MmsTabActivity");
        bind_set_contacts.add("com.android.contacts.activities.PeopleActivity");
        // s6
        bind_set_contacts.add("com.yulong.android.contacts.ui.main.ContactMainActivity");
        Intent i_mms = new Intent();
        i_mms.setAction("android.intent.action.MAIN");
        i_mms.setType("vnd.android.cursor.dir/mms");
        addFilterString(i_mms, bind_set_mms);
        Intent i_mms_2 = new Intent("android.intent.action.VIEW");
        i_mms_2.setType("vnd.android-dir/mms-sms");
        addFilterString(i_mms_2, bind_set_mms);

        bind_set_mms.add("com.android.mms.ui.ConversationList");
        bind_set_mms.add("com.android.mms");
        // huawei
        bind_set_mms.add("com.huawei.message");
        // sony
        bind_set_mms.add("com.sonyericsson.conversations");
        // motorola
        bind_set_mms.add("com.motorola.blur.conversations");
        bind_set_mms.add("com.android.mms.ui.SingleRecipientConversationActivity");
        bind_set_mms.add("com.android.mms.ui.NewMessagePopupActivity");
        // s6
        bind_set_mms.add("com.yulong.android.mms.ui.MmsMainListFormActivity");
        // 360
        bind_set_call.add("com.qihoo360.contacts.contacts");
        bind_set_contacts.add("com.qihoo360.contacts.safecontacts");
        bind_set_mms.add("com.qihoo360.contacts.mms");

        bind_set_mms.add("com.google.android.talk.SigningInActivity");
        bind_set_mms.add("com.google.android.apps.babel.fragments.SmsOobActivity");
    }
    /**
     * 根据Intent去系统中查询可以启动该类intent的程序信息
     * @param paramIntent
     * @param bind_set
     */
    private void addFilterString(Intent paramIntent, HashSet<String> bind_set) {
        try {
            ContextWrapper contextWrapper = (ContextWrapper) mContext;
            PackageManager packageManager = contextWrapper.getPackageManager();
            List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(paramIntent,PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : queryIntentActivities) {
                String packageName = resolveInfo.activityInfo.packageName;
                String name = resolveInfo.activityInfo.name;
                String targetActivity = resolveInfo.activityInfo.targetActivity;
                if (!TextUtils.isEmpty(name)) {
                    bind_set.add(name);
                }
                if (!TextUtils.isEmpty(targetActivity)) {
                    bind_set.add(targetActivity);
                }
            }
        } catch (Exception e) {
            logMsg("----------"+e.getMessage());
        }
    }

    boolean isSuccess = false;
    private int temp = 10;
    /**
     * 检查栈顶
     */
    public void checkTop() {
        isSuccess = false;
        if (this.mHandler == null)
            return;
        this.mHandler.removeMessages(TouchHandler.MSG_WHAT_COMMENT);
        if(!hasBind()){
            return;
        }
        long i = 400L;
        while (true) {
            if(isSuccess){
                break;
            }
            if (i >= 600) {
                this.mHandler.sendEmptyMessageDelayed(TouchHandler.MSG_WHAT_COMMENT_FAILED, i);
                return;
            }
            this.mHandler.sendEmptyMessageDelayed(TouchHandler.MSG_WHAT_COMMENT, i);
            i += temp;
        }
    }
    /**
     * 检查栈顶的Acitivity的类型（是否需要绑定）
     * @return
     */
    private int checkTopType() {
        SystemClock.sleep(200);
        int lanch_type = LANCH_TYPE_NONE;
        List<RunningTaskInfo> localObject = mActivityManager.getRunningTasks(1);
        if (localObject != null && localObject.size() > 0) {
            RunningTaskInfo runningTaskInfo = localObject.get(0);
            ComponentName topActivity = runningTaskInfo.topActivity;
            String topActivityClassName = topActivity.getClassName();
            if(mContext == null){
                return lanch_type;
            }
            if (mContext.getPackageName().equals(topActivity.getPackageName())) {
                isSuccess = true;
            }
            logMsg("topActivityClassName->"+topActivityClassName);
            if (bind_set_call.contains(topActivityClassName)) {
                if (alertView.isIs_bind_call()) {
                    lanch_type = LANCH_TYPE_CALL;
                }
            }else if (bind_set_contacts.contains(topActivityClassName)) {
                if (alertView.isIs_bind_contacts()) {
                    lanch_type = LANCH_TYPE_CONTACTS;
                }
            }else if (bind_set_mms.contains(topActivityClassName)) {
                if (alertView.isIs_bind_mms()) {
                    lanch_type = LANCH_TYPE_MMS;
                }
            }
        }
        return lanch_type;
    }
    /**
     * 干掉其他程序，并启动我们的app
     */
    public void killTopAndExcuteMine() {
        if (isSuccess) {
            return;
        }
        int type = checkTopType();
        switch (type) {
            case LANCH_TYPE_CALL:
                startOurApp(TwoActivity.class);
                break;
            case LANCH_TYPE_CONTACTS:
                startOurApp(TwoActivity.class);
                break;
            case LANCH_TYPE_MMS:
                startOurApp(TwoActivity.class);
                break;
            case LANCH_TYPE_NONE:
//                mHandler.removeMessages(TouchHandler.MSG_WHAT_COMMENT);
//                isSuccess = true;
                return;
        }
    }
    /**
     * 启动我们的app
     * @param targetClass
     */
    private void startOurApp(Class<?> targetClass){
        List<RunningTaskInfo> topRunningTask = mActivityManager.getRunningTasks(1);
        if (topRunningTask != null && topRunningTask.size() > 0) {
            RunningTaskInfo topRunningTaskInfo = topRunningTask.get(0);
            String packagename = topRunningTaskInfo.topActivity.getPackageName();
            if (mContext.getPackageName().equals(packagename)) {
                return;
            }
            mActivityManager.killBackgroundProcesses(packagename);
        }
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_DEFAULT);
//        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setClassName(mContext, targetClass.getName());
        mContext.startActivity(i);
        isSuccess = true;
    }
    @Override
    protected void onLooperPrepared() {
        Looper localLooper = getLooper();
        this.mHandler = new TouchHandler(this, localLooper);
    }

    @Override
    public boolean quit() {
        this.mHandler = null;
        this.mContext = null;
        return super.quit();
    }
}
