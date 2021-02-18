package com.lock.computerlockscreen.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import pub.devrel.easypermissions.EasyPermissions;

public class Constants {
    public static final String AUTO_WALLPAPER = "AUTO_WALLPAPER";
    public static final String BACKGROUND_TIME = "BACKGROUND_TIME";
    public static final String CREDENTIALS = "ali:uraan1234";
    public static final int DrawOverlay_REQUEST_CODE = 9;
    public static final String GETTING_LOCK = "http://centsolapps.com/api/AppLock/lockscreen.php";
    public static final String GETTING_THEME = "http://centsolapps.com/api/AppThemes/getAppsThemes.php";
    public static String HUAWEI_TEST_ID = "62488F7DC24E4F985C4A4569D0E026BF";
    public static final int LOCATION_ENABLE_REQUEST = 12;
    public static String[] LOCATION_PERMISSION = {"android.permission.ACCESS_FINE_LOCATION"};
    public static final int MAX_RETRY = 2;
    public static final int MY_SOCKET_TIMEOUT_MS = 10000;
    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String ONETIME = "ONETIME";
    public static final String PASSCODEENABLE = "PASSCODEENABLE";
    public static String[] PHONE_STATE_PERMISSION = {"android.permission.READ_PHONE_STATE", "android.permission.CAMERA"};
    public static final String RATING_DIALOUGE_ONETIME = "RATING_DIALOUGE_ONETIME";
    public static final int RC_LOCATION_PERMISSION_REQUEST = 10;
    public static final int RC_PHONE_STATE_PERMISSION_REQUEST = 15;
    public static final int RC_STORAGE_PERMISSION_REQUEST = 13;
    public static final int RC_STORAGE_PERMISSION_REQUEST_2 = 14;
    public static String[] STORAGE_PERMISSION = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final String TAG = "LOCKSCREEN";
    public static final String TESTING_G3_DEVICE = "97ED7F8DA9093934D8FC33E1AAEC8431";
    public static final String TESTING_MOTO_DEVICE = "D14837E3270A175216B4FD9862D1D9CF";
    public static final String TESTING_NEXUS_DEVICE = "2B41522941092B19AFA3080C3600F2F3";
    public static final String THEME_IMG_URL = "http://centsolapps.com/api/AppThemes/images_2/";
    public static final String USER_IMAGE = "USER_IMAGE";
    public static final String USER_NAME = "USER_NAME";
    public static final String WEATHERENABLE = "WEATHERENABLE";
    public static final String WEATHERRELOAD = "WEATHERRELOAD";
    public static final String WEATHER_API = "http://api.openweathermap.org/data/2.5/weather?lat=";
    public static final String WEATHER_API_KEY = "3fa2f2d599c66bcbee374657ddde80f5";
    public static final String WEATHER_ICON = "WEATHER_ICON";
    public static final String WEATHER_LAST_UPDATE = "WEATHER_LAST_UPDATE";
    public static final String WEATHER_LOCATION_NAME = "WEATHER_LOCATION_NAME";
    public static final String WEATHER_TEMP = "WEATHER_TEMP";
    public static final int WRITE_SETTINGS_REQUEST = 11;

    public static String getPathFromUri(Context context, Uri uri) {
        String[] strArr = {"_data"};
        try {
            Cursor query = context.getContentResolver().query(uri, strArr, (String) null, (String[]) null, (String) null);
            if (query == null) {
                return null;
            }
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex(strArr[0]));
            query.close();
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SharedPreferences setNotif(Context context, boolean z) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        defaultSharedPreferences.edit().putBoolean(NOTIFICATION, z).apply();
        return defaultSharedPreferences;
    }

    public static boolean getNotif(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NOTIFICATION, false);
    }

    public static String getUserImage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USER_IMAGE, "");
    }

    public static void setUserImage(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_IMAGE, str).apply();
    }

    public static String getUserName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USER_NAME, "");
    }

    public static void setUserName(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_NAME, str).apply();
    }

    public static Bitmap drawableToBmp(Activity activity, Drawable drawable) {
        Bitmap bitmap;
        int convertDpToPixel = (int) convertDpToPixel(20.0f, activity);
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(convertDpToPixel, convertDpToPixel, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static float convertDpToPixel(float f, Context context) {
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        return f * (((float) resources.getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static String getweathericon(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(WEATHER_ICON, "");
    }

    public static void setweathericon(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(WEATHER_ICON, str).apply();
    }

    public static String getWeatherLastUpdate(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(WEATHER_LAST_UPDATE, "");
    }

    public static void setWeatherLastUpdate(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(WEATHER_LAST_UPDATE, str).apply();
    }

    public static String getweatherlocation(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(WEATHER_LOCATION_NAME, "");
    }

    public static void setweatherlocation(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(WEATHER_LOCATION_NAME, str).apply();
    }

    public static String getweathertemp(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(WEATHER_TEMP, "");
    }

    public static void setweathertemp(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(WEATHER_TEMP, str).apply();
    }

    public static boolean getWeaterEnable(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(WEATHERENABLE, false);
    }

    public static void setWeaterEnable(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(WEATHERENABLE, z).apply();
    }

    public static boolean getEnable(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ONETIME, true);
    }

    public static void setEnable(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(ONETIME, z).apply();
    }

    public static boolean getAutoWallpaperEnable(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(AUTO_WALLPAPER, false);
    }

    public static void setAutoWallpaperEnable(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(AUTO_WALLPAPER, z).apply();
    }

    public static String getWeaterReload(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(WEATHERRELOAD, "");
    }

    public static void setWeaterReload(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(WEATHERRELOAD, str).apply();
    }

    public static boolean isAppInstalled(Context context, String str) {
        try {
            context.getPackageManager().getApplicationInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static boolean hasPermissions(Context context, String... strArr) {
        return EasyPermissions.hasPermissions(context, strArr);
    }

    public static Bitmap drawableToBmp(Activity activity, Drawable drawable, int i) {
        Bitmap bitmap;
        if (drawable == null) {
            return null;
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);
        } else {
            int convertDpToPixel = (int) convertDpToPixel((float) i, activity);
            bitmap = Bitmap.createBitmap(convertDpToPixel, convertDpToPixel, Bitmap.Config.ARGB_4444);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean getRatingDailoge(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(RATING_DIALOUGE_ONETIME, true);
    }

    public static void setRatingDailoge(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(RATING_DIALOUGE_ONETIME, z).apply();
    }
}
