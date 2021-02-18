package com.lock.computerlockscreen.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.lock.computerlockscreen.R;
import com.lock.computerlockscreen.utils.Constants;
import java.io.ByteArrayOutputStream;

@RequiresApi(api = 18)
public class NotificationService extends NotificationListenerService {
    Context context;
    Handler handler = new Handler();

    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    public void onNotificationPosted(final StatusBarNotification statusBarNotification) {
        this.handler.postDelayed(new Runnable() {
            public void run() {
                NotificationService.this.sendNotification(statusBarNotification);
            }
        }, 1000);
    }

    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        Log.i("Msg", "Notification Removed");
    }


    public void sendNotification(StatusBarNotification statusBarNotification) {
        String str;
        String str2;
        String str3;
        Parcelable parcelable;
        if (statusBarNotification.getNotification() != null) {
            String packageName = statusBarNotification.getPackageName();
            String charSequence = statusBarNotification.getNotification().tickerText != null ? statusBarNotification.getNotification().tickerText.toString() : null;
            if (statusBarNotification.getNotification().largeIcon != null) {
                Bitmap bitmap = statusBarNotification.getNotification().largeIcon;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
                byteArrayOutputStream.toByteArray();
            }
            if (VERSION.SDK_INT < 19 || statusBarNotification.getNotification().extras == null) {
                str2 = null;
                str = null;
            } else {
                Bundle bundle = statusBarNotification.getNotification().extras;
                str = bundle.getString(NotificationCompat.EXTRA_TITLE);
                str2 = bundle.getCharSequence(NotificationCompat.EXTRA_TEXT) != null ? bundle.getCharSequence(NotificationCompat.EXTRA_TEXT).toString() : null;
            }
            if (VERSION.SDK_INT >= 20) {
                str3 = String.valueOf(statusBarNotification.getKey().split("\\|")[4]);
            } else {
                str3 = String.valueOf(statusBarNotification.getId());
            }
            try {
                Drawable drawable = createPackageContext(packageName, 0).getResources().getDrawable(statusBarNotification.getNotification().icon);
                if (drawable != null) {
                    parcelable = Constants.drawableToBmp(null,drawable, 20);
                    Log.i("Package", packageName);
                    Intent intent = new Intent("Msg");
                    intent.putExtra("id", str3);
                    intent.putExtra("package", packageName);
                    intent.putExtra("ticker", charSequence);
                    intent.putExtra("title", str);
                    intent.putExtra("postTime", statusBarNotification.getPostTime());
                    intent.putExtra("text", str2);
                    if (parcelable != null) {
                        intent.putExtra("icon", Constants.drawableToBmp(null, ContextCompat.getDrawable(this.context, R.drawable.android_icon), 20));
                    } else {
                        intent.putExtra("icon", parcelable);
                    }
                    intent.putExtra(BaseGmsClient.KEY_PENDING_INTENT, statusBarNotification.getNotification().contentIntent);
                    LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e2) {
                e2.printStackTrace();
            }
            parcelable = null;
            Log.i("Package", packageName);
            Intent intent2 = new Intent("Msg");
            intent2.putExtra("id", str3);
            intent2.putExtra("package", packageName);
            intent2.putExtra("ticker", charSequence);
            intent2.putExtra("title", str);
            intent2.putExtra("postTime", statusBarNotification.getPostTime());
            intent2.putExtra("text", str2);
            if (parcelable != null) {
            }
            intent2.putExtra(BaseGmsClient.KEY_PENDING_INTENT, statusBarNotification.getNotification().contentIntent);
            LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent2);
        }
    }
}
