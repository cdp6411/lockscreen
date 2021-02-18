package com.lock.computerlockscreen.model;

import android.app.PendingIntent;
import android.graphics.Bitmap;

public class Notification {
    public int count;
    public Bitmap icon;
    public String id;
    public String pack;
    public PendingIntent pendingIntent;
    public long postTime;
    public String tv_text;
    public String tv_title;

    public Notification(String str, Bitmap bitmap, String str2, String str3, int i, String str4, long j, PendingIntent pendingIntent2) {
        this.id = str;
        this.icon = bitmap;
        this.tv_title = str2;
        this.tv_text = str3;
        this.count = i;
        this.pack = str4;
        this.postTime = j;
        this.pendingIntent = pendingIntent2;
    }
}
