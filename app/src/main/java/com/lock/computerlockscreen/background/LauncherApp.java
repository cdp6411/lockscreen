package com.lock.computerlockscreen.background;


import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class LauncherApp extends Application {
    public static final String TAG = "LauncherApp";
    private static LauncherApp mInstance;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;
    private RequestQueue mRequestQueue;
    private PrefManager pref;

    public static synchronized LauncherApp getInstance() {
        LauncherApp launcherApp;
        synchronized (LauncherApp.class) {
            launcherApp = mInstance;
        }
        return launcherApp;
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.pref = new PrefManager(this);
    }

    public PrefManager getPrefManger() {
        if (this.pref == null) {
            this.pref = new PrefManager(this);
        }
        return this.pref;
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (this.mImageLoader == null) {
            getLruBitmapCache();
            this.mImageLoader = new ImageLoader(this.mRequestQueue, this.mLruBitmapCache);
        }
        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (this.mLruBitmapCache == null) {
            this.mLruBitmapCache = new LruBitmapCache();
        }
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> request, String str) {
        if (TextUtils.isEmpty(str)) {
            str = TAG;
        }
        request.setTag(str);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object obj) {
        if (this.mRequestQueue != null) {
            this.mRequestQueue.cancelAll(obj);
        }
    }
}
