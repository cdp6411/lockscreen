package com.lock.computerlockscreen.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySettings {
    public static final String PATTERN = "PATTERN";
    public static final String USING = "USING";

    public static boolean getSound(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Sound", true);
    }

    public static void setSound(boolean z, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean("Sound", z);
        edit.commit();
    }

    public static boolean getVibrate(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Vibrate", true);
    }

    public static void setVibrate(boolean z, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean("Vibrate", z);
        edit.commit();
    }

    public static boolean getLockscreen(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("Lockscreen", false);
    }

    public static void setLockscreen(boolean z, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean("Lockscreen", z);
        edit.commit();
    }

    public static boolean getPasscodeEnable(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.PASSCODEENABLE, false);
    }

    public static void setPasscodeEnable(boolean z, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(Constants.PASSCODEENABLE, z).apply();
    }

    public static boolean getPatternEnable(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("PatternEnable", false);
    }

    public static void setPatternEnable(boolean z, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean("PatternEnable", z);
        edit.commit();
    }

    public static SharedPreferences setDemo(Context context, boolean z) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        defaultSharedPreferences.edit().putBoolean(USING, z).apply();
        return defaultSharedPreferences;
    }

    public static boolean getDemo(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(USING, false);
    }

    public static String getPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("Password", "");
    }

    public static void setPassword(String str, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString("Password", str);
        edit.commit();
    }

    public static String getPattern(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PATTERN, "");
    }

    public static void setPattern(String str, Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(PATTERN, str);
        edit.commit();
    }

    public static String getBackgroundImageTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.BACKGROUND_TIME, "");
    }

    public static void setBackgroundImageTime(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Constants.BACKGROUND_TIME, str).apply();
    }
}
