
package com.teffy.setdefault;

import android.app.Application;
import android.view.WindowManager;

public class MApplication extends Application {

    private WindowManager.LayoutParams windowParams ;

    public WindowManager.LayoutParams getWindowParams() {
        if(windowParams == null){
            windowParams = new WindowManager.LayoutParams();
            windowParams.width = 1;
            windowParams.height = 1;
            windowParams.gravity = 51;
            windowParams.x = 0;
            windowParams.y = 0;
            windowParams.format = 1;
            windowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            windowParams.flags = 262152;
        }
        return windowParams;
    }

}
