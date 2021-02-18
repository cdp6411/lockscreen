package com.lock.computerlockscreen.services;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.lock.computerlockscreen.reciver.LockScreenReeiver;
import com.lock.computerlockscreen.utils.MySettings;


public class MyService extends Service {
    BroadcastReceiver mReceiver;
    Intent myIntent;

    public IBinder onBind(Intent intent) {
        this.myIntent = intent;
        return null;
    }

    public void onCreate() {
        if (MySettings.getLockscreen(this)) {
            try {
                ((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).newKeyguardLock("IN").disableKeyguard();
            } catch (Exception e) {
                e.printStackTrace();
            }
            IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.mReceiver = new LockScreenReeiver();
            registerReceiver(this.mReceiver, intentFilter);
        }
        super.onCreate();
    }

    public void onStart(Intent intent, int i) {
        super.onStart(intent, i);
    }

    public void onDestroy() {
        if (this.mReceiver != null) {
            unregisterReceiver(this.mReceiver);
            try {
                ((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).newKeyguardLock("IN").reenableKeyguard();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
