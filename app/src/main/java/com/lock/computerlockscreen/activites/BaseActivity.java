package com.lock.computerlockscreen.activites;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import java.lang.Thread;

public class BaseActivity extends FragmentActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable th) {
               IOSLockScreen.mWindowManager.removeView(IOSLockScreen.view);
                BaseActivity.this.finish();
                if (defaultUncaughtExceptionHandler != null) {
                    defaultUncaughtExceptionHandler.uncaughtException(thread, th);
                } else {
                    System.exit(2);
                }
            }
        });
    }
}
