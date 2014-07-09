
package com.teffy.setdefault.set;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class TouchHandler extends Handler {
    public static final String TAG = "TouchHandler";
    public static final boolean DEBUG = true;
    private static void logMsg(String msg){
        if(DEBUG){
            Log.i(TAG, msg);
        }
    }

    public static final int MSG_WHAT_COMMENT = 19910107;
    public static final int MSG_WHAT_COMMENT_FAILED = MSG_WHAT_COMMENT+12;

    private TouchEventHandlerThread paramd;

    TouchHandler(TouchEventHandlerThread paramd, Looper paramLooper) {
        super(paramLooper);
        this.paramd = paramd;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_WHAT_COMMENT:
                if (paramd == null){
                    logMsg("TouchEventHandlerThread is null");
                    return;
                }
                paramd.killTopAndExcuteMine();
                break;
            case MSG_WHAT_COMMENT_FAILED:
                logMsg("FAILED");
                break;
            default:
                break;
        }
    }
}
