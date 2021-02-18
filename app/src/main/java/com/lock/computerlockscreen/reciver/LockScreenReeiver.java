package com.lock.computerlockscreen.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import com.lock.computerlockscreen.activites.IOSLockScreen;
import com.lock.computerlockscreen.background.AppConst;
import com.lock.computerlockscreen.background.LauncherApp;
import com.lock.computerlockscreen.background.Utils;
import com.lock.computerlockscreen.background.Wallpaper;
import com.lock.computerlockscreen.utils.Constants;
import com.lock.computerlockscreen.utils.MySettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

public class LockScreenReeiver extends BroadcastReceiver {
    public static boolean wasScreenOn = true;

    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            wasScreenOn = false;
            try {
                Intent intent2 = new Intent(context, IOSLockScreen.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
            } catch (Exception unused) {
            }
        } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
            if (Constants.getAutoWallpaperEnable(context)) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        LockScreenReeiver.this.setWallpaper(context);
                    }
                }, 1000);
            }
            wasScreenOn = true;
            Intent intent3 = new Intent(context, IOSLockScreen.class);
            intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent3);
        } else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && MySettings.getLockscreen(context)) {
            Intent intent4 = new Intent(context, IOSLockScreen.class);
            intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent4);
        }
        try {
            String stringExtra = intent.getStringExtra("state");
            if (stringExtra != null) {
                if (stringExtra.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    MySettings.getLockscreen(context);
                }
                if (stringExtra.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) && MySettings.getLockscreen(context) && MySettings.getDemo(context)) {
                    Intent intent5 = new Intent(context, IOSLockScreen.class);
                    intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent5.putExtra("state", stringExtra);
                    context.startActivity(intent5);
                }
                if (stringExtra.equals(TelephonyManager.EXTRA_STATE_IDLE) && MySettings.getLockscreen(context) && MySettings.getDemo(context)) {
                    Intent intent6 = new Intent(context, IOSLockScreen.class);
                    intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent6.putExtra("state", stringExtra);
                    context.startActivity(intent6);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void setWallpaper(Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long time = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime())).getTime() - simpleDateFormat.parse(MySettings.getBackgroundImageTime(context)).getTime();
            Log.i("Duration", String.valueOf(TimeUnit.MILLISECONDS.toMinutes(time)));
            if (TimeUnit.MILLISECONDS.toMinutes(time) > 30) {
//                getWallpaperList(context);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

//    private void getWallpaperList(final Context context) {
//        try {
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(0, "http://45.55.46.214/wallpaper_ahmed/api/albumimageurl.php?package_name=" + context.getApplicationContext().getPackageName() + "&album_id=18", (Response.Listener<JSONObject>) new Response.Listener<JSONObject>() {
//                public void onResponse(JSONObject jSONObject) {
//                    LockScreenReeiver.this.getAllImages(jSONObject, context);
//                }
//            }, (Response.ErrorListener) new Response.ErrorListener() {
//                public void onErrorResponse(VolleyError volleyError) {
//                }
//            });
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 2, 1.0f));
//            jsonObjectRequest.setShouldCache(false);
//            LauncherApp.getInstance().addToRequestQueue(jsonObjectRequest);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void getAllImages(JSONObject jSONObject, Context context) {
        try {
            Wallpaper wallpaper = (Wallpaper) new Gson().fromJson(jSONObject.toString(), Wallpaper.class);
            if (wallpaper.getStatus().equals("true")) {
                setSelectedWallpaper(wallpaper.getAlbum_images().get(new Random().nextInt(wallpaper.getAlbum_images().size())).image.trim(), context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelectedWallpaper(String str, final Context context) {
        Glide.with(context).load(AppConst.IMAGE_URL + str).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Utils.saveImage(((BitmapDrawable)resource).getBitmap());
                MySettings.setBackgroundImageTime(context, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));

            }
        });
    }
}
