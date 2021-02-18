package com.lock.computerlockscreen.background;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String KEY_ALBUMS = "albums";
    private static final String KEY_GALLERY_NAME = "gallery_name";
    private static final String KEY_GOOGLE_USERNAME = "google_username";
    private static final String KEY_NO_OF_COLUMNS = "no_of_columns";
    private static final String PREF_NAME = "AwesomeWallpapers";
    private static final String TAG = "PrefManager";
    int PRIVATE_MODE = 0;
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public PrefManager(Context context) {
        this._context = context;
        this.pref = this._context.getSharedPreferences(PREF_NAME, this.PRIVATE_MODE);
    }

    public int getNoOfGridColumns() {
        return this.pref.getInt(KEY_NO_OF_COLUMNS, 2);
    }

    public String getGalleryName() {
        return this.pref.getString(KEY_GALLERY_NAME, AppConst.SDCARD_DIR_NAME);
    }
}
