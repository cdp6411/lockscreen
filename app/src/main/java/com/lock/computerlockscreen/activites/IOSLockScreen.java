package com.lock.computerlockscreen.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.media.ExifInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;

import com.google.android.gms.common.internal.BaseGmsClient;
import com.lock.computerlockscreen.R;

import com.lock.computerlockscreen.adapter.CustomNotificationAdapter;
import com.lock.computerlockscreen.background.LauncherApp;
import com.lock.computerlockscreen.model.MyPhoneStateListener;
import com.lock.computerlockscreen.model.Notification;
import com.lock.computerlockscreen.utils.BlurBuilder;
import com.lock.computerlockscreen.utils.Constants;
import com.lock.computerlockscreen.utils.GPSTracker;
import com.lock.computerlockscreen.utils.MySettings;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class IOSLockScreen extends BaseActivity implements View.OnTouchListener, View.OnClickListener, SurfaceHolder.Callback {
    protected LocationManager locationManager;
    TextView showLocationTxt;
    protected Context context;
    private static  final int REQUEST_LOCATION=1;
    LocationManager locationManager1;
    private double latitude,longitude;

    TextView cityField, currentTemperatureField, updatedField, weatherIcon;
    Typeface weatherFont;
    public static WindowManager mWindowManager;
    public static RelativeLayout view;
    ImageView BatteryInfo;
    /* access modifiers changed from: private */
    public Intent RecIntent;
    /* access modifiers changed from: private */
    public Bitmap bitmap;
    int brightness = 3;
    Camera cam;
    Typeface clock;
    LinearLayout content;
    TextView date_time;
    IntentFilter filter;
    GPSTracker gps;
    ImageView imgPass1;
    ImageView imgPass2;
    ImageView imgPass3;
    ImageView imgPass4;
    boolean isFlashOn = false;
    Boolean isRotationOn = false;
    ImageView iv_simInfo1;
    ImageView iv_wifiInfo;
    RelativeLayout lnum0;
    RelativeLayout lnum1;
    RelativeLayout lnum2;
    RelativeLayout lnum3;
    RelativeLayout lnum4;
    RelativeLayout lnum5;
    RelativeLayout lnum6;
    RelativeLayout lnum7;
    RelativeLayout lnum8;
    RelativeLayout lnum9;
    SwipeMenuListView lv_notifications;
    Context mContext;


    public BroadcastReceiver mInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Intent unused = IOSLockScreen.this.RecIntent = intent;
            IOSLockScreen.this.isRotationOn();
            if (intent.getAction().matches("android.intent.action.BATTERY_CHANGED")) {
                IOSLockScreen.this.batteryInfo(intent);
            }
            if (intent.getAction().matches("android.net.wifi.RSSI_CHANGED")) {
                IOSLockScreen.this.wifiLevel();
            }
            if (intent.getAction().matches("android.net.wifi.STATE_CHANGE")) {
                IOSLockScreen.this.isWifiOn();
            }
            if (intent.getAction().matches("android.intent.action.AIRPLANE_MODE")) {
                IOSLockScreen.this.isAirplaneModeOn();
            }
            if (intent.getAction().matches("android.bluetooth.adapter.action.STATE_CHANGED")) {
                IOSLockScreen.this.isBluetoothOn();
            }
            if (intent.getAction().compareTo("android.intent.action.TIME_TICK") == 0) {
                IOSLockScreen.this.setTime();
            }
            if (intent.getAction().matches("android.intent.action.CONFIGURATION_CHANGED")) {
                IOSLockScreen.this.isRotationOn();
            }
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                IOSLockScreen.this.gpsstate();
            }
        }
    };

    public PatternLockView mPatternLockView;

    public PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        public void onProgress(List<PatternLockView.Dot> list) {
            String name = getClass().getName();
            Log.d(name, "Pattern progress: " + PatternLockUtils.patternToString(IOSLockScreen.this.mPatternLockView, list));
        }

        public void onComplete(List<PatternLockView.Dot> list) {
            if (MySettings.getPattern(IOSLockScreen.this.mContext).equals(PatternLockUtils.patternToString(IOSLockScreen.this.mPatternLockView, list))) {
                IOSLockScreen.this.clearAll();
                return;
            }
            IOSLockScreen.view.findViewById(R.id.draw_patrn).startAnimation(AnimationUtils.loadAnimation(IOSLockScreen.this.mContext, R.anim.shake));
            if (MySettings.getVibrate(IOSLockScreen.this.mContext)) {
                IOSLockScreen.this.vibrator.vibrate(100);
            }
            IOSLockScreen.this.mPatternLockView.clearPattern();
        }

        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    public MyPhoneStateListener mPhoneStatelistener;
    ImageView main_bg;
    CustomNotificationAdapter notificationAdapter;
    ArrayList<Notification> notificationList = new ArrayList<>();

    public BroadcastReceiver onNotice = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Calendar instance = Calendar.getInstance();
            String stringExtra = intent.getStringExtra("id");
            String stringExtra2 = intent.getStringExtra("package");
            String stringExtra3 = intent.getStringExtra("title");
            String stringExtra4 = intent.getStringExtra("text");
            long longExtra = intent.getLongExtra("postTime", instance.getTime().getTime());
            Bitmap bitmap = (Bitmap) intent.getParcelableExtra("icon");
            PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra(BaseGmsClient.KEY_PENDING_INTENT);
            if (stringExtra3 != null && !stringExtra3.contains("displaying over other")) {
                Notification notification = new Notification(stringExtra, bitmap, stringExtra3, stringExtra4, 1, stringExtra2, longExtra, pendingIntent);
                for (int i = 0; i < IOSLockScreen.this.notificationList.size(); i++) {
                    if (IOSLockScreen.this.notificationList.get(i).id.equals(stringExtra) && !IOSLockScreen.this.notificationList.get(i).pack.equals("android")) {
                        IOSLockScreen.this.notificationList.remove(i);
                    }
                }
                if (!(notification.pack == null || notification.tv_title == null || notification.tv_text == null)) {
                    for (int i2 = 0; i2 < IOSLockScreen.this.notificationList.size(); i2++) {
                        if (IOSLockScreen.this.notificationList.get(i2).pack != null && IOSLockScreen.this.notificationList.get(i2).tv_title != null && IOSLockScreen.this.notificationList.get(i2).tv_text != null && IOSLockScreen.this.notificationList.get(i2).pack.equals(notification.pack) && IOSLockScreen.this.notificationList.get(i2).tv_title.equals(notification.tv_title) && IOSLockScreen.this.notificationList.get(i2).tv_text.equals(notification.tv_text)) {
                            IOSLockScreen.this.notificationList.remove(i2);
                        }
                    }
                }
                IOSLockScreen.this.notificationList.add(notification);
                IOSLockScreen.this.notificationAdapter.notifyDataSetChanged();
            }
        }
    };
    boolean onlyOne = true;
    Camera.Parameters params;

    public String passcode = "";
    RelativeLayout pin_layout;
    CircleImageView profile_icon;
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    Animation t;
    private Target target;
    TextView temp_text;
    TextView tv_airplane;
    TextView tv_bluetooth;
    TextView tv_brightness;
    TextView tv_data;
    TextView tv_date;
    TextView tv_flash;
    TextView tv_location;
    TextView tv_rotate;
    TextView tv_time;
    TextView tv_wifi;
    TextView txt_cancel;
    RelativeLayout txt_delete;
    TextView txt_emergency;
    TextView user_name;

    public Vibrator vibrator;
    ImageView weather_icon;
    LinearLayout weather_layout;
    LinearLayout weather_layout1;
    TextView weather_locatin;
    private WindowManager.LayoutParams wmParams;
    private float x1;
    private float x2;
    private float y1;
    private float y2;

    public void surfaceChanged(SurfaceHolder surfaceHolder2, int i, int i2, int i3) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        getWindow().setFlags(1024,1024);

        view = (RelativeLayout) View.inflate(this, R.layout.activity_ioslock_screen, (ViewGroup) null);

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        showLocationTxt=view.findViewById(R.id.show_location);
        this.main_bg = (ImageView) IOSLockScreen.view.findViewById(R.id.main_bg);
        cityField = (TextView)IOSLockScreen.view.findViewById(R.id.city_field);
        updatedField = (TextView)IOSLockScreen.view.findViewById(R.id.updated_field);
        currentTemperatureField = (TextView)IOSLockScreen.view.findViewById(R.id.current_temperature_field);
        weatherFont = Typeface.createFromAsset(IOSLockScreen.this.getAssets(), "weathericons-regular-webfont.ttf");
        weatherIcon = (TextView)IOSLockScreen.view.findViewById(R.id.weather_icon1);
        weatherIcon.setTypeface(weatherFont);
        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                currentTemperatureField.setText(weather_temperature);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

            }
        });
        getLocation();
        asyncTask.execute(String.valueOf(latitude),String.valueOf(longitude));
        //asyncTask.execute("21.1594627","72.6822074");
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/complockwallpaper.jpg";


        this.target = new Target() {
            public void onPrepareLoad(Drawable drawable) {
            }

            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                if (IOSLockScreen.this.bitmap != null) {
                    IOSLockScreen.this.bitmap.recycle();
                    Bitmap unused = IOSLockScreen.this.bitmap = null;
                }
                Bitmap unused2 = IOSLockScreen.this.bitmap = bitmap;
                IOSLockScreen.this.main_bg.setImageBitmap(IOSLockScreen.this.bitmap);
            }

            public void onBitmapFailed(Exception exc, Drawable drawable) {
                if (IOSLockScreen.this.bitmap != null) {
                    IOSLockScreen.this.bitmap.recycle();
                    Bitmap unused = IOSLockScreen.this.bitmap = null;
                }
                Bitmap unused2 = IOSLockScreen.this.bitmap = BitmapFactory.decodeResource(IOSLockScreen.this.getResources(), R.drawable.xiaomi_bg);
                IOSLockScreen.this.main_bg.setImageBitmap(IOSLockScreen.this.bitmap);
            }
        };
        File file = new File(str);
        if (!file.exists() || !file.canRead()) {
            if (this.bitmap != null) {
                this.bitmap.recycle();
                this.bitmap = null;
            }
            this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
            this.main_bg.setImageBitmap(this.bitmap);
        } else {
            Picasso.get().load("file://" + str).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(this.target);
        }
        new bgTask().execute(new Object[0]);
        mWindowAddView();
    }

    private class bgTask extends AsyncTask {
        private bgTask() {
        }

        /* access modifiers changed from: protected */
        public Object doInBackground(Object[] objArr) {
            IOSLockScreen.this.task();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Object obj) {
            super.onPostExecute(obj);
        }
    }

    public void onClick(View view2) {
        switch (view2.getId()) {
            case R.id.lnum0 /*2131230883*/:
                this.passcode += "0";
                break;
            case R.id.lnum1 /*2131230884*/:
                this.passcode += "1";
                break;
            case R.id.lnum2 /*2131230885*/:
                this.passcode += ExifInterface.GPS_MEASUREMENT_2D;
                break;
            case R.id.lnum3 /*2131230886*/:
                this.passcode += ExifInterface.GPS_MEASUREMENT_3D;
                break;
            case R.id.lnum4 /*2131230887*/:
                this.passcode += "4";
                break;
            case R.id.lnum5 /*2131230888*/:
                this.passcode += "5";
                break;
            case R.id.lnum6 /*2131230889*/:
                this.passcode += "6";
                break;
            case R.id.lnum7 /*2131230890*/:
                this.passcode += "7";
                break;
            case R.id.lnum8 /*2131230891*/:
                this.passcode += "8";
                break;
            case R.id.lnum9 /*2131230892*/:
                this.passcode += "9";
                break;
        }
        if (this.passcode.length() <= 4) {
            FeelDot(this.passcode.trim());
        }
        if (MySettings.getVibrate(this.mContext)) {
            this.vibrator.vibrate(20);
        }
    }

    private void mWindowAddView() {
        mWindowManager = (WindowManager) getApplicationContext().getSystemService("window");
        int i = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        this.wmParams = new WindowManager.LayoutParams();
        this.wmParams.format = -3;
        this.wmParams.type = i;
        this.wmParams.flags = 1280;
        this.wmParams.width = -1;
        this.wmParams.height = -1;
        view.setSystemUiVisibility(3846);
        try {
            mWindowManager.addView(view, this.wmParams);
            Log.e(".............", "addView");
        } catch (Exception unused) {
            Toast.makeText(this.mContext, "Something went Wrong", 1).show();
        }
    }

    @SuppressLint("WrongConstant")
    public boolean onTouch(View view2, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.x1 = motionEvent.getX();
            this.y1 = motionEvent.getY();
            return true;
        } else if (action != 2) {
            return true;
        } else {
            this.x2 = motionEvent.getX();
            this.y2 = motionEvent.getY();
            if (this.x2 - this.x1 > 100.0f) {
                if (!this.onlyOne) {
                    return true;
                }
                if (MySettings.getPasscodeEnable(this.mContext) || MySettings.getPatternEnable(this.mContext)) {
                    downLayoutVisible();
                } else {
                    if (MySettings.getVibrate(this.mContext)) {
                        ((Vibrator) getSystemService("vibrator")).vibrate(50);
                    }
                    if (MySettings.getSound(this.mContext)) {
                        MediaPlayer create = MediaPlayer.create(this.mContext, R.raw.screen_sound);
                        create.start();
                        create.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mediaPlayer.release();
                            }
                        });
                    }
                    clearAll();
                }
                this.onlyOne = false;
                return true;
            } else if (this.y1 - this.y2 > 20.0f) {
                this.content.setVisibility(8);
                this.content.setAnimation(inFromTopAnimation());
                mobilecheack();
                return true;
            } else if (this.y2 - this.y1 <= 20.0f) {
                return true;
            } else {
                this.content.setVisibility(0);
                this.content.setAnimation(outToTopAnimation());
                mobilecheack();
                return true;
            }
        }
    }

    public void clearAll() {
        this.t = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fad_out);
        if (view.findViewById(R.id.topView).getVisibility() == View.VISIBLE) {
            view.findViewById(R.id.topView).startAnimation(this.t);
        }
        if (view.findViewById(R.id.downView).getVisibility() == View.VISIBLE) {
            view.findViewById(R.id.downView).startAnimation(this.t);
        }
        MySettings.setDemo(this.mContext, false);
        mWindowManager.removeView(view);
        finish();
    }

    public void downLayoutVisible() {
        view.findViewById(R.id.downView).setVisibility(View.VISIBLE);
        view.findViewById(R.id.downView).startAnimation(this.t);
        int i = Build.VERSION.SDK_INT;
        try {
            this.main_bg.setImageBitmap(BlurBuilder.blur(this.mContext, this.bitmap.copy(Bitmap.Config.ARGB_8888, false)));
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public void setDragListeners() {
        view.findViewById(R.id.topView).setOnTouchListener(this);
        view.findViewById(R.id.content).setOnTouchListener(this);
    }

    /* access modifiers changed from: private */
    public void task() {
        runOnUiThread(new Runnable() {
            public void run() {
                MySettings.setDemo(IOSLockScreen.this.mContext, true);
                IOSLockScreen.this.t = AnimationUtils.loadAnimation(IOSLockScreen.this.getBaseContext(), R.anim.icon_fade_in);
                IOSLockScreen.this.content = (LinearLayout) IOSLockScreen.view.findViewById(R.id.content);
                PatternLockView unused = IOSLockScreen.this.mPatternLockView = (PatternLockView) IOSLockScreen.view.findViewById(R.id.patter_lock_view);
                IOSLockScreen.this.pin_layout = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.pin_layout);
                IOSLockScreen.this.tv_brightness = (TextView) IOSLockScreen.view.findViewById(R.id.tv_brightness);
                IOSLockScreen.this.lnum1 = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.lnum1);
                IOSLockScreen.this.lnum2 = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.lnum2);
                IOSLockScreen.this.lnum3 = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.lnum3);
                IOSLockScreen.this.lnum4 = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.lnum4);
                IOSLockScreen.this.lnum5 = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.lnum5);
                IOSLockScreen.this.lnum6 = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.lnum6);
                IOSLockScreen.this.lnum7 = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.lnum7);
                IOSLockScreen.this.lnum8 = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.lnum8);
                IOSLockScreen.this.lnum9 = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.lnum9);
                IOSLockScreen.this.lnum0 = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.lnum0);
                IOSLockScreen.this.imgPass1 = (ImageView) IOSLockScreen.view.findViewById(R.id.imgpass1);
                IOSLockScreen.this.imgPass2 = (ImageView) IOSLockScreen.view.findViewById(R.id.imgpass2);
                IOSLockScreen.this.imgPass3 = (ImageView) IOSLockScreen.view.findViewById(R.id.imgpass3);
                IOSLockScreen.this.imgPass4 = (ImageView) IOSLockScreen.view.findViewById(R.id.imgpass4);
                IOSLockScreen.this.tv_time = (TextView) IOSLockScreen.view.findViewById(R.id.tv_time);
                IOSLockScreen.this.tv_date = (TextView) IOSLockScreen.view.findViewById(R.id.tv_date);
                IOSLockScreen.this.txt_emergency = (TextView) IOSLockScreen.view.findViewById(R.id.txt_emergency);
                IOSLockScreen.this.txt_delete = (RelativeLayout) IOSLockScreen.view.findViewById(R.id.txt_delete);
                IOSLockScreen.this.txt_cancel = (TextView) IOSLockScreen.view.findViewById(R.id.txt_cancel);
                IOSLockScreen.this.profile_icon = (CircleImageView) IOSLockScreen.view.findViewById(R.id.profile_icon);
                IOSLockScreen.this.tv_airplane = (TextView) IOSLockScreen.view.findViewById(R.id.tv_airplane);
                IOSLockScreen.this.tv_wifi = (TextView) IOSLockScreen.view.findViewById(R.id.tv_wifi);
                IOSLockScreen.this.iv_wifiInfo = (ImageView) IOSLockScreen.view.findViewById(R.id.iv_wifiInfo);
                IOSLockScreen.this.tv_bluetooth = (TextView) IOSLockScreen.view.findViewById(R.id.tv_bluetooth);
                IOSLockScreen.this.tv_flash = (TextView) IOSLockScreen.view.findViewById(R.id.tv_flash);
                IOSLockScreen.this.surfaceView = (SurfaceView) IOSLockScreen.view.findViewById(R.id.surfaceView);
                IOSLockScreen.this.surfaceHolder = IOSLockScreen.this.surfaceView.getHolder();
                IOSLockScreen.this.surfaceHolder.addCallback(IOSLockScreen.this);
                IOSLockScreen.this.tv_data = (TextView) IOSLockScreen.view.findViewById(R.id.tv_data);
                IOSLockScreen.this.tv_rotate = (TextView) IOSLockScreen.view.findViewById(R.id.tv_rotate);
                try {
                    if (Build.VERSION.SDK_INT >= 23 && Settings.System.canWrite(IOSLockScreen.this.mContext)) {
                        if (Settings.System.getInt(IOSLockScreen.this.getContentResolver(), "accelerometer_rotation") != 1) {
                            IOSLockScreen.this.tv_rotate.setBackgroundResource(R.color.transparentWhite);
                        } else {
                            IOSLockScreen.this.tv_rotate.setBackgroundResource(R.color.press);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                IOSLockScreen.this.tv_location = (TextView) IOSLockScreen.view.findViewById(R.id.tv_location);
                IOSLockScreen.this.weather_layout1=(LinearLayout)IOSLockScreen.view.findViewById(R.id.weather_layout1);
                if (Constants.getWeaterEnable(IOSLockScreen.this.mContext)) {
                    IOSLockScreen.this.weather_layout1.setVisibility(View.VISIBLE);

                }
                if (Constants.getWeaterEnable(IOSLockScreen.this.mContext) && Constants.getEnable(IOSLockScreen.this.mContext)) {
                    IOSLockScreen.this.weather_layout1.setVisibility(View.VISIBLE);
                    Constants.setEnable(IOSLockScreen.this.mContext, false);
                }
                IOSLockScreen.this.BatteryInfo = (ImageView) IOSLockScreen.view.findViewById(R.id.iv_batteryInfo);
                IOSLockScreen.this.iv_simInfo1 = (ImageView) IOSLockScreen.view.findViewById(R.id.iv_simInfo1);
                MyPhoneStateListener unused2 = IOSLockScreen.this.mPhoneStatelistener = new MyPhoneStateListener();
                IOSLockScreen.this.mPhoneStatelistener.setImageView(IOSLockScreen.this.iv_simInfo1);
                TelephonyManager telephonyManager = (TelephonyManager) IOSLockScreen.this.getSystemService("phone");
                if (telephonyManager != null) {
                    telephonyManager.listen(IOSLockScreen.this.mPhoneStatelistener, 256);
                    try {
                        telephonyManager.getNetworkOperatorName();
                    } catch (SecurityException e3) {
                        e3.printStackTrace();
                    }
                }
                Typeface createFromAsset = Typeface.createFromAsset(IOSLockScreen.this.getAssets(), "clock_3.ttf");
                IOSLockScreen.this.clock = Typeface.createFromAsset(IOSLockScreen.this.getAssets(), "lightfont.otf");
                IOSLockScreen.this.tv_time.setTypeface(createFromAsset);
                IOSLockScreen.this.tv_date.setTypeface(createFromAsset);
                if (Constants.getUserImage(IOSLockScreen.this.mContext) == null) {
                    IOSLockScreen.this.profile_icon.setImageResource(R.drawable.profile_icon);
                } else if (new File(Constants.getUserImage(IOSLockScreen.this.mContext)).exists()) {
                    Glide.with(IOSLockScreen.this.mContext).load(Constants.getUserImage(IOSLockScreen.this.mContext)).into(IOSLockScreen.this.profile_icon);
                } else {
                    IOSLockScreen.this.profile_icon.setImageResource(R.drawable.profile_icon);
                }
                IOSLockScreen.this.user_name = (TextView) IOSLockScreen.view.findViewById(R.id.user_name);
                if (!Constants.getUserName(IOSLockScreen.this.mContext).equals("")) {
                    IOSLockScreen.this.user_name.setText(Constants.getUserName(IOSLockScreen.this.mContext));
                }
                if (MySettings.getPatternEnable(IOSLockScreen.this.mContext)) {
                    IOSLockScreen.view.findViewById(R.id.pattern_view).setVisibility(View.VISIBLE);
                } else if (MySettings.getPasscodeEnable(IOSLockScreen.this.mContext)) {
                    IOSLockScreen.this.pin_layout.setVisibility(View.VISIBLE);
                }
                IOSLockScreen.this.mPatternLockView.setDotCount(3);
                IOSLockScreen.this.mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(IOSLockScreen.this.mContext, R.dimen.pattern_lock_dot_size));
                IOSLockScreen.this.mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(IOSLockScreen.this.mContext, R.dimen.pattern_lock_dot_selected_size));
                IOSLockScreen.this.mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(IOSLockScreen.this.mContext, R.dimen.pattern_lock_path_width));
                IOSLockScreen.this.mPatternLockView.setAspectRatioEnabled(true);
                IOSLockScreen.this.mPatternLockView.setAspectRatio(2);
                IOSLockScreen.this.mPatternLockView.setViewMode(0);
                IOSLockScreen.this.mPatternLockView.setDotAnimationDuration(150);
                IOSLockScreen.this.mPatternLockView.setPathEndAnimationDuration(100);
                IOSLockScreen.this.mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(IOSLockScreen.this.mContext, R.color.white));
                IOSLockScreen.this.mPatternLockView.setInStealthMode(false);
                IOSLockScreen.this.mPatternLockView.setTactileFeedbackEnabled(true);
                IOSLockScreen.this.mPatternLockView.setInputEnabled(true);
                IOSLockScreen.this.mPatternLockView.addPatternLockListener(IOSLockScreen.this.mPatternLockViewListener);
                IOSLockScreen.this.lnum1.setOnClickListener(IOSLockScreen.this);
                IOSLockScreen.this.lnum2.setOnClickListener(IOSLockScreen.this);
                IOSLockScreen.this.lnum3.setOnClickListener(IOSLockScreen.this);
                IOSLockScreen.this.lnum4.setOnClickListener(IOSLockScreen.this);
                IOSLockScreen.this.lnum5.setOnClickListener(IOSLockScreen.this);
                IOSLockScreen.this.lnum6.setOnClickListener(IOSLockScreen.this);
                IOSLockScreen.this.lnum7.setOnClickListener(IOSLockScreen.this);
                IOSLockScreen.this.lnum8.setOnClickListener(IOSLockScreen.this);
                IOSLockScreen.this.lnum9.setOnClickListener(IOSLockScreen.this);
                IOSLockScreen.this.lnum0.setOnClickListener(IOSLockScreen.this);
                IOSLockScreen.this.tv_brightness.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT < 23) {
                            IOSLockScreen.this.setBrightness();
                            IOSLockScreen.this.displayBrightness();
                        } else if (Settings.System.canWrite(IOSLockScreen.this.mContext)) {
                            IOSLockScreen.this.setBrightness();
                            IOSLockScreen.this.displayBrightness();
                        }
                    }
                });
                IOSLockScreen.this.tv_airplane.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        IOSLockScreen.this.modifyAirplanemode();
                        IOSLockScreen.this.isAirplaneModeOn();
                    }
                });
                IOSLockScreen.this.tv_wifi.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        WifiManager wifiManager = (WifiManager) IOSLockScreen.this.getApplicationContext().getSystemService("wifi");
                        if (wifiManager.isWifiEnabled()) {
                            wifiManager.setWifiEnabled(false);
                        } else {
                            wifiManager.setWifiEnabled(true);
                        }
                    }
                });
                IOSLockScreen.this.tv_bluetooth.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (defaultAdapter.isEnabled()) {
                            defaultAdapter.disable();
                        } else {
                            defaultAdapter.enable();
                        }
                    }
                });
                IOSLockScreen.this.tv_flash.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        IOSLockScreen.this.turnFlashOnOff();
                    }
                });
                IOSLockScreen.this.tv_data.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (MySettings.getPasscodeEnable(IOSLockScreen.this.mContext) || MySettings.getPatternEnable(IOSLockScreen.this.mContext)) {
                            IOSLockScreen.this.downLayoutVisible();
                        } else {
                            IOSLockScreen.this.clearAll();
                        }
                        Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                        intent.addFlags(268435456);
                        IOSLockScreen.this.startActivity(intent);
                    }
                });
                IOSLockScreen.this.tv_rotate.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= 23 && Settings.System.canWrite(IOSLockScreen.this.mContext)) {
                            if (Settings.System.getInt(IOSLockScreen.this.getContentResolver(), "accelerometer_rotation", 0) == 1) {
                                IOSLockScreen.this.isRotationOn = true;
                            } else {
                                IOSLockScreen.this.isRotationOn = false;
                            }
                            if (IOSLockScreen.this.isRotationOn.booleanValue()) {
                                IOSLockScreen.setAutoOrientationEnabled(IOSLockScreen.this.mContext, false);
                                IOSLockScreen.this.tv_rotate.setBackgroundResource(R.color.transparentWhite);
                                return;
                            }
                            IOSLockScreen.setAutoOrientationEnabled(IOSLockScreen.this.mContext, true);
                            IOSLockScreen.this.tv_rotate.setBackgroundResource(R.color.press);
                        }
                    }
                });
                IOSLockScreen.this.tv_location.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (MySettings.getPasscodeEnable(IOSLockScreen.this.mContext) || MySettings.getPatternEnable(IOSLockScreen.this.mContext)) {
                            IOSLockScreen.this.downLayoutVisible();
                        } else {
                            IOSLockScreen.this.clearAll();
                        }
                        Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                        intent.setFlags(268435456);
                        IOSLockScreen.this.startActivity(intent);
                    }
                });
                IOSLockScreen.this.txt_delete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        String unused = IOSLockScreen.this.passcode = "";
                        IOSLockScreen.this.FeelDot(IOSLockScreen.this.passcode);
                        if (MySettings.getVibrate(IOSLockScreen.this.mContext)) {
                            IOSLockScreen.this.vibrator.vibrate(50);
                        }
                    }
                });
                IOSLockScreen.this.txt_cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        IOSLockScreen.this.main_bg.setImageBitmap(IOSLockScreen.this.bitmap);
                        String unused = IOSLockScreen.this.passcode = "";
                        IOSLockScreen.this.FeelDot(IOSLockScreen.this.passcode);
                        IOSLockScreen.view.findViewById(R.id.topView).setVisibility(0);
                        IOSLockScreen.view.findViewById(R.id.downView).setVisibility(8);
                        IOSLockScreen.this.onlyOne = true;
                    }
                });
                IOSLockScreen.this.BatteryInfo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (MySettings.getPasscodeEnable(IOSLockScreen.this.mContext) || MySettings.getPatternEnable(IOSLockScreen.this.mContext)) {
                            IOSLockScreen.this.downLayoutVisible();
                        } else {
                            IOSLockScreen.this.clearAll();
                        }
                        try {
                            IOSLockScreen.this.startActivity(new Intent("android.intent.action.POWER_USAGE_SUMMARY"));
                        } catch (Exception unused) {
                        }
                    }
                });
                Vibrator unused3 = IOSLockScreen.this.vibrator = (Vibrator) IOSLockScreen.this.getApplicationContext().getSystemService("vibrator");
                IOSLockScreen.this.filter = new IntentFilter();
                IOSLockScreen.this.filter.addAction("android.intent.action.BATTERY_CHANGED");
                IOSLockScreen.this.filter.addAction("android.net.wifi.RSSI_CHANGED");
                IOSLockScreen.this.filter.addAction("android.intent.action.AIRPLANE_MODE");
                IOSLockScreen.this.filter.addAction("android.net.wifi.STATE_CHANGE");
                IOSLockScreen.this.filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
                IOSLockScreen.this.filter.addAction("android.media.RINGER_MODE_CHANGED");
                IOSLockScreen.this.filter.addAction("android.intent.action.TIME_TICK");
                IOSLockScreen.this.filter.addAction("android.location.PROVIDERS_CHANGED");
                IOSLockScreen.this.filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
                IOSLockScreen.this.registerReceiver(IOSLockScreen.this.mInfoReceiver, IOSLockScreen.this.filter);
                IOSLockScreen.this.setDragListeners();
                IOSLockScreen.this.displayBrightness();
                IOSLockScreen.this.setTime();
                IOSLockScreen.this.isAirplaneModeOn();
                IOSLockScreen.this.wifiLevel();
                IOSLockScreen.this.isBluetoothOn();
                IOSLockScreen.this.mobilecheack();
                IOSLockScreen.this.isRotationOn();
                IOSLockScreen.this.gpsstate();
                IOSLockScreen.this.lv_notifications = (SwipeMenuListView) IOSLockScreen.view.findViewById(R.id.lv_notifications);
                if (Constants.getNotif(IOSLockScreen.this.mContext) && Build.VERSION.SDK_INT >= 19) {
                    LocalBroadcastManager.getInstance(IOSLockScreen.this.mContext).registerReceiver(IOSLockScreen.this.onNotice, new IntentFilter("Msg"));
                }
                IOSLockScreen.this.notificationAdapter = new CustomNotificationAdapter(IOSLockScreen.this.mContext, IOSLockScreen.this.notificationList);
                IOSLockScreen.this.lv_notifications.setAdapter((ListAdapter) IOSLockScreen.this.notificationAdapter);
                IOSLockScreen.this.lv_notifications.setSwipeDirection(-1);
                IOSLockScreen.this.lv_notifications.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
                    public void onSwipeStart(int i) {
                    }

                    public void onSwipeEnd(int i) {
                        if (i >= 0 && i < IOSLockScreen.this.notificationList.size()) {
                            IOSLockScreen.this.notificationList.remove(i);
                            IOSLockScreen.this.notificationAdapter.notifyDataSetChanged();
                        }
                    }
                });
                IOSLockScreen.this.lv_notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        if (MySettings.getPasscodeEnable(IOSLockScreen.this.mContext) || MySettings.getPatternEnable(IOSLockScreen.this.mContext)) {
                            IOSLockScreen.this.downLayoutVisible();
                        } else {
                            IOSLockScreen.this.clearAll();
                        }
                        try {
                            if (IOSLockScreen.this.notificationList.get(i).pendingIntent != null) {
                                IOSLockScreen.this.notificationList.get(i).pendingIntent.send();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        String string;
        super.onNewIntent(intent);
        if (intent.getExtras() != null && (string = intent.getExtras().getString("state")) != null) {
            if (string.equals(TelephonyManager.EXTRA_STATE_RINGING) || string.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                mWindowManager.removeView(view);
                finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.cam != null) {
            this.cam.stopPreview();
            this.cam.setPreviewCallback((Camera.PreviewCallback) null);
            this.cam.release();
            this.cam = null;
        }
        if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
        try {
            if (this.mInfoReceiver != null) {
                unregisterReceiver(this.mInfoReceiver);
            }
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.onNotice);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void setBrightness() {
        if (this.brightness >= 0 && this.brightness <= 3) {
            this.brightness = 3;
            this.brightness += 63;
        } else if (this.brightness >= 4 && this.brightness <= 66) {
            this.brightness = 66;
            this.brightness += 63;
        } else if (this.brightness >= 67 && this.brightness <= 129) {
            this.brightness = 129;
            this.brightness += 63;
        } else if (this.brightness >= 130 && this.brightness <= 192) {
            this.brightness = 192;
            this.brightness += 63;
        } else if (this.brightness >= 193 && this.brightness <= 255) {
            this.brightness = 3;
        }
        if (getBrightMode() == 1) {
            Settings.System.putInt(getContentResolver(), "screen_brightness_mode", 0);
        }
        Settings.System.putInt(getContentResolver(), "screen_brightness", this.brightness);
    }

    /* access modifiers changed from: protected */
    public int getBrightMode() {
        try {
            return Settings.System.getInt(getContentResolver(), "screen_brightness_mode");
        } catch (Exception e) {
            Log.d("tag", e.toString());
            return 0;
        }
    }

    /* access modifiers changed from: private */
    public void displayBrightness() {
        if (getBrightMode() != 0) {
            this.tv_brightness.setText("Auto");
        } else if (this.brightness >= 0 && this.brightness <= 3) {
            this.tv_brightness.setText("0%");
        } else if (this.brightness >= 4 && this.brightness <= 66) {
            this.tv_brightness.setText("25%");
        } else if (this.brightness >= 67 && this.brightness <= 129) {
            this.tv_brightness.setText("50%");
        } else if (this.brightness >= 130 && this.brightness <= 192) {
            this.tv_brightness.setText("75%");
        } else if (this.brightness >= 193 && this.brightness <= 255) {
            this.tv_brightness.setText("100%");
        }
    }

    public void gpsstate() {
        if (((LocationManager) getSystemService("location")).isProviderEnabled("gps")) {
            this.tv_location.setBackgroundResource(R.color.press);
        } else {
            this.tv_location.setBackgroundResource(R.color.transparentWhite);
        }
    }

    /* access modifiers changed from: private */
    public void FeelDot(String str) {
        Bitmap bitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.feelpass)).getBitmap();
        Bitmap bitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.openpass)).getBitmap();
        if (str.length() == 0) {
            this.imgPass1.setImageBitmap(bitmap3);
            this.imgPass2.setImageBitmap(bitmap3);
            this.imgPass3.setImageBitmap(bitmap3);
            this.imgPass4.setImageBitmap(bitmap3);
            return;
        }
        if (str.length() == 1) {
            this.imgPass1.setImageBitmap(bitmap2);
        }
        if (str.length() == 2) {
            this.imgPass2.setImageBitmap(bitmap2);
        }
        if (str.length() == 3) {
            this.imgPass3.setImageBitmap(bitmap2);
        }
        if (str.length() == 4) {
            this.imgPass4.setImageBitmap(bitmap2);
            if (MySettings.getPassword(this.mContext).equals(str)) {
                if (MySettings.getSound(this.mContext)) {
                    MediaPlayer create = MediaPlayer.create(this.mContext, R.raw.screen_sound);
                    create.start();
                    create.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                        }
                    });
                }
                clearAll();
                return;
            }
            try {
                view.findViewById(R.id.shakelayout).startAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.shake));
                if (MySettings.getVibrate(this.mContext)) {
                    this.vibrator.vibrate(200);
                }
                this.passcode = "";
                FeelDot(this.passcode);
            } catch (Exception unused) {
            }
        }
    }

    public void modifyAirplanemode() {
        if (MySettings.getPasscodeEnable(this.mContext) || MySettings.getPatternEnable(this.mContext)) {
            downLayoutVisible();
        } else {
            clearAll();
        }
        if (Build.VERSION.SDK_INT < 17) {
            try {
                Intent intent = new Intent("android.settings.AIRPLANE_MODE_SETTINGS");
                intent.setFlags(268435456);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.e("exception", e + "");
            }
        } else {
            try {
                Intent intent2 = new Intent("android.settings.WIRELESS_SETTINGS");
                intent2.setFlags(268435456);
                startActivity(intent2);
            } catch (ActivityNotFoundException e2) {
                Log.e("exception", e2 + "");
            }
        }
    }

    public void isAirplaneModeOn() {
        int i = 1;
        if (Build.VERSION.SDK_INT < 17 ? Settings.System.getInt(getContentResolver(), "airplane_mode_on", 0) == 0 : Settings.Global.getInt(getContentResolver(), "airplane_mode_on", 0) == 0) {
            i = 0;
        }
        if (i != 0) {
            this.tv_airplane.setBackgroundResource(R.color.press);
        } else {
            this.tv_airplane.setBackgroundResource(R.color.transparentWhite);
        }
    }

    private Animation outToTopAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, -1.0f, 2, 0.0f);
        translateAnimation.setDuration(500);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        return translateAnimation;
    }

    private Animation inFromTopAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, 0.0f, 2, -1.0f);
        translateAnimation.setDuration(500);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        return translateAnimation;
    }

    /* access modifiers changed from: private */
    public void setTime() {
        this.tv_time.setText(new SimpleDateFormat("h:mm").format(new Date()));
        int i = Calendar.getInstance().get(5);
        String format = new SimpleDateFormat("MMM").format(Calendar.getInstance().getTime());
        String format2 = new SimpleDateFormat("EEEE").format(new Date());
        TextView textView = this.tv_date;
        textView.setText(format2 + ", " + format + " " + String.valueOf(i));
    }

    public void wifiLevel() {
        int calculateSignalLevel = WifiManager.calculateSignalLevel(((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getRssi(), 4);
        Log.i("Level", String.valueOf(calculateSignalLevel));
        if (calculateSignalLevel == 0) {
            this.iv_wifiInfo.setImageResource(R.drawable.wifi_level_one);
        } else if (calculateSignalLevel == 1) {
            this.iv_wifiInfo.setImageResource(R.drawable.wifi_level_two);
        } else if (calculateSignalLevel == 2) {
            this.iv_wifiInfo.setImageResource(R.drawable.wifi_level_three);
        } else if (calculateSignalLevel == 3) {
            this.iv_wifiInfo.setImageResource(R.drawable.wifi_level_full);
        }
    }

    /* access modifiers changed from: private */
    public void isWifiOn() {
        if (((NetworkInfo) this.RecIntent.getParcelableExtra("networkInfo")).isConnected()) {
            this.iv_wifiInfo.setVisibility(0);
            this.tv_wifi.setBackgroundResource(R.color.press);
            return;
        }
        this.iv_wifiInfo.setVisibility(8);
        this.tv_wifi.setBackgroundResource(R.color.transparentWhite);
    }

    /* access modifiers changed from: private */
    public void isBluetoothOn() {
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            this.tv_bluetooth.setBackgroundResource(R.color.press);
        } else {
            this.tv_bluetooth.setBackgroundResource(R.color.transparentWhite);
        }
    }

    /* access modifiers changed from: private */
    public void turnFlashOnOff() {
        if (!this.isFlashOn) {
            acquireCamera(this.surfaceHolder);
            try {
                this.params.setFlashMode("torch");
                this.cam.setParameters(this.params);
                this.cam.startPreview();
                this.tv_flash.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.press));
                this.isFlashOn = true;
            } catch (Exception unused) {
                this.tv_flash.setBackgroundColor(ContextCompat.getColor(this, R.color.transparentWhite));
                this.isFlashOn = false;
                releaseCamera();
            }
        } else if (this.cam != null) {
            try {
                this.params = this.cam.getParameters();
                this.params.setFlashMode("off");
                this.cam.setParameters(this.params);
                this.cam.stopPreview();
                this.tv_flash.setBackgroundColor(ContextCompat.getColor(this, R.color.transparentWhite));
                this.isFlashOn = false;
                releaseCamera();
            } catch (Exception unused2) {
                releaseCamera();
                this.tv_flash.setBackgroundColor(ContextCompat.getColor(this, R.color.transparentWhite));
                this.isFlashOn = false;
            }
        } else {
            this.tv_flash.setBackgroundColor(ContextCompat.getColor(this, R.color.transparentWhite));
            this.isFlashOn = false;
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder2) {
        this.surfaceHolder = surfaceHolder2;
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder2) {
        releaseCamera();
    }

    private void acquireCamera(SurfaceHolder surfaceHolder2) {
        if (this.cam == null) {
            try {
                this.cam = Camera.open();
                this.params = this.cam.getParameters();
                this.cam.setPreviewDisplay(surfaceHolder2);
            } catch (Exception unused) {
                if (this.cam == null) {
                    Toast.makeText(this.mContext, "Phone restart required for this feature.!", 0).show();
                    return;
                }
                this.cam.release();
                this.cam = null;
            }
        }
    }

    private void releaseCamera() {
        if (this.cam != null) {
            this.cam.stopPreview();
            this.cam.setPreviewCallback((Camera.PreviewCallback) null);
            this.cam.release();
            this.cam = null;
        }
    }

    /* access modifiers changed from: private */
    public void mobilecheack() {
        if (isMobileDataEnable()) {
            this.tv_data.setBackgroundResource(R.color.press);
        } else {
            this.tv_data.setBackgroundResource(R.color.transparentWhite);
        }
    }

    public boolean isMobileDataEnable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
        try {
            Method declaredMethod = Class.forName(connectivityManager.getClass().getName()).getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            declaredMethod.setAccessible(true);
            return ((Boolean) declaredMethod.invoke(connectivityManager, new Object[0])).booleanValue();
        } catch (Exception unused) {
            return false;
        }
    }

    public static void setAutoOrientationEnabled(Context context, boolean z) {
        Settings.System.putInt(context.getContentResolver(), "accelerometer_rotation", z ? 1 : 0);
    }

    public void isRotationOn() {
        if (Settings.System.getInt(getContentResolver(), "accelerometer_rotation", 0) == 1) {
            this.tv_rotate.setBackgroundResource(R.color.press);
        } else {
            this.tv_rotate.setBackgroundResource(R.color.transparentWhite);
        }
    }





    public void batteryInfo(Intent intent) {
        int intExtra = intent.getIntExtra(NotificationCompat.CATEGORY_STATUS, 0);
        if (intExtra == 2 || intExtra == 5) {
            this.BatteryInfo.setImageResource(R.drawable.charging);
            return;
        }
        int size = this.notificationList.size();
        for (int i = 0; i < size; i++) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.notificationList.size()) {
                    break;
                } else if (this.notificationList.get(i2).pack.equals("android") || this.notificationList.get(i2).pack.equals("com.android.systemui")) {
                    this.notificationList.remove(i2);
                } else {
                    i2++;
                }
            }
            this.notificationList.remove(i2);
        }
        if (this.notificationAdapter != null) {
            this.notificationAdapter.notifyDataSetChanged();
        }
        levels();
    }

    public void levels() {
        int intExtra = this.RecIntent.getIntExtra("level", 0);
        if (intExtra < 20) {
            this.BatteryInfo.setImageResource(R.drawable.battery_level_one);
        } else if (intExtra >= 20 && intExtra < 45) {
            this.BatteryInfo.setImageResource(R.drawable.battery_level_two);
        } else if (intExtra >= 45 && intExtra < 65) {
            this.BatteryInfo.setImageResource(R.drawable.battery_level_three);
        } else if (intExtra >= 65 && intExtra < 90) {
            this.BatteryInfo.setImageResource(R.drawable.battery_level_four);
        } else if (intExtra >= 90 && intExtra <= 100) {
            this.BatteryInfo.setImageResource(R.drawable.battery_full);
        }
    }
    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(IOSLockScreen.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(IOSLockScreen.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=lat;
                longitude=longi;

                //showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=lat;
                longitude=longi;

                //showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=lat;
                longitude=longi;

                //showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else
            {
//                Toast.makeText(this, "Can't Get Your Location Check your Internet connection", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }

    }
}
