package com.lock.computerlockscreen.activites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.behavior.HideBottomViewOnScrollBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.lock.computerlockscreen.R;
import com.lock.computerlockscreen.background.Utils;
import com.lock.computerlockscreen.services.MyService;
import com.lock.computerlockscreen.utils.Constants;
import com.lock.computerlockscreen.utils.MySettings;
import com.suke.widget.SwitchButton;


import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    LocationManager locationManager;
    private LinearLayout linnnnn;
    private TextView mTvPreview;
    private LinearLayout mPreviewLl;
    private TextView mMoreOptionsEnable;
    private LinearLayout mMoreOptionsRl;
    private TextView mTextViewBackgroundGallery;
    private LinearLayout mBackgroungRl;
    private ImageView mEnableIv;
    private TextView mTextViewLockscreen;
    private SwitchButton mToggleEnable;
    private RelativeLayout mEnable;
    private ImageView mPasscodeIv;
    private TextView mTextViewPasscode;
    private RelativeLayout mLayoutPasscode;
    private ImageView mChangePasscodeIv;
    private TextView mChangePasswordTv;
    private RelativeLayout mChangePasswordRl;
    private ImageView mSetPatternIv;
    private TextView mTextViewPattern;
    private SwitchButton mTogglePattern;
    private RelativeLayout mLayoutPattern;
    private ImageView mChangePatternIv;
    private TextView mChangePatternTv;
    private RelativeLayout mChangePatternRl;
    private ImageView mAutoWallpaperIv;
    private TextView mTextViewautoWallpaper;
    private SwitchButton mToggleAutoWallpaper;
    private RelativeLayout mAutoWallpaper;
    private ImageView mSoundIv;
    private TextView mTextViewSound;
    private SwitchButton mToggleSound;
    private RelativeLayout mSound;
    private ImageView mVibrateIcon;
    private TextView mTextViewVibration;
    private SwitchButton mToggleVibrate;
    private RelativeLayout mVibrate;
    private ImageView mWeatherIv;
    private TextView mWeatherLayoutTxt;
    private SwitchButton mToggleWeather;
    private ImageView mNotificationIv;
    private TextView mTextViewNotification;
    private SwitchButton mToggleNotificationAccess;
    private RelativeLayout mNotificationAcess;
    private ImageView mRateusIv;
    private TextView mTvRateus;
    private RelativeLayout mRateusRl;
    private ImageView mShareIv;
    private TextView mTvShare;
    private RelativeLayout mShareRl;
    private ImageView mMoreIv;
    private TextView mTvMore;
    private RelativeLayout mMoreRl;
    private LinearLayout mMainLayout;
    RelativeLayout background1;
    private SwitchButton  mTogglePasscode;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setFlags(1024, 1024);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        linnnnn=findViewById(R.id.linnnnn);

        mTogglePasscode=findViewById(R.id.toggle_passcode);
        mTvPreview = findViewById(R.id.tv_preview);
        mPreviewLl = findViewById(R.id.preview_ll);
        mMoreOptionsEnable = findViewById(R.id.more_options_enable);
        mMoreOptionsRl = findViewById(R.id.moreOptions_rl);
        mTextViewBackgroundGallery = findViewById(R.id.textViewBackgroundGallery);
        mBackgroungRl = findViewById(R.id.backgroung_rl);
        mEnableIv = findViewById(R.id.enable_iv);
        mTextViewLockscreen = findViewById(R.id.textViewLockscreen);
        mToggleEnable = findViewById(R.id.toggle_enable);
        mEnable = findViewById(R.id.enable);
        mPasscodeIv = findViewById(R.id.passcode_iv);
        mTextViewPasscode = findViewById(R.id.textViewPasscode);
//        mTogglePasscode = findViewById(R.id.toggle_passcode);
        mLayoutPasscode = findViewById(R.id.layoutPasscode);
        mChangePasscodeIv = findViewById(R.id.changePasscode_iv);
        mChangePasswordTv = findViewById(R.id.changePassword_tv);
        mChangePasswordRl = findViewById(R.id.changePassword_rl);
        mSetPatternIv = findViewById(R.id.setPattern_iv);
        mTextViewPattern = findViewById(R.id.textViewPattern);
        mTogglePattern = findViewById(R.id.toggle_pattern);
        mLayoutPattern = findViewById(R.id.layoutPattern);
        mChangePatternIv = findViewById(R.id.changePattern_iv);
        mChangePatternTv = findViewById(R.id.changePattern_tv);
        mChangePatternRl = findViewById(R.id.changePattern_rl);
        mAutoWallpaperIv = findViewById(R.id.auto_wallpaper_iv);
        mTextViewautoWallpaper = findViewById(R.id.textViewautoWallpaper);
        mToggleAutoWallpaper = findViewById(R.id.toggle_auto_wallpaper);
        mAutoWallpaper = findViewById(R.id.autoWallpaper);
        mSoundIv = findViewById(R.id.sound_iv);
        mTextViewSound = findViewById(R.id.textViewSound);
        mToggleSound = findViewById(R.id.toggle_sound);
        mSound = findViewById(R.id.sound);
        mVibrateIcon = findViewById(R.id.vibrate_icon);
        mTextViewVibration = findViewById(R.id.textViewVibration);
        mToggleVibrate = findViewById(R.id.toggle_vibrate);
        mVibrate = findViewById(R.id.vibrate);
        mWeatherIv = findViewById(R.id.weather_iv);
        mWeatherLayoutTxt = findViewById(R.id.weather_layout_txt);
        mToggleWeather = findViewById(R.id.toggle_weather);
        mNotificationIv = findViewById(R.id.notification_iv);
        mTextViewNotification = findViewById(R.id.textViewNotification);
        mToggleNotificationAccess = findViewById(R.id.toggle_notification_access);
        mNotificationAcess = findViewById(R.id.notification_acess);
        mRateusIv = findViewById(R.id.rateus_iv);
        mTvRateus = findViewById(R.id.tv_rateus);
        mRateusRl = findViewById(R.id.rateus_rl);
        mShareIv = findViewById(R.id.share_iv);
        mTvShare = findViewById(R.id.tv_share);
        mShareRl = findViewById(R.id.share_rl);
        mMoreIv = findViewById(R.id.more_iv);
        mTvMore = findViewById(R.id.tv_more);
        mMoreRl = findViewById(R.id.more_rl);
        this.locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        mMainLayout = findViewById(R.id.main_layout);
        if (Build.VERSION.SDK_INT >= 19) {
            mNotificationAcess.setVisibility(View.VISIBLE);
        }
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        background1=findViewById(R.id.backgorund1);
        background1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomeActivity.this, background.class);
                startActivity(intent);
            }
        });
        //onclick listner
        mPreviewLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySettings.getLockscreen(HomeActivity.this)) {
                    startActivity(new Intent(HomeActivity.this, IOSLockScreen.class));
                } else {
                    Toast.makeText(HomeActivity.this, "Enable Lock First", Toast.LENGTH_LONG).show();
                }
            }
        });
        mMoreOptionsRl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(HomeActivity.this)) {
                    Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
                    intent.setData(Uri.parse("package:" + HomeActivity.this.getPackageName()));
                    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    HomeActivity.this.startActivityForResult(intent, 11);
                }
            }
        });
        mBackgroungRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.accessStoragePermision();
            }
        });

        if (MySettings.getLockscreen(this)) {
            this.mToggleEnable.setChecked(true);
            enableLockToggle();
            linnnnn.setVisibility(View.VISIBLE);
        } else {
            this.mToggleEnable.setChecked(false);
            disableLockToggle();
        }
        mToggleEnable.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    HomeActivity.this.accessPhoneStatePermision();
                    linnnnn.setVisibility(View.VISIBLE);
                }
                else
                {
                    HomeActivity.this.disableLockToggle();
                    linnnnn.setVisibility(View.GONE);

                }
            }
        });

        mTogglePasscode.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    if (!MySettings.getPassword(HomeActivity.this).equals("")) {
                        MySettings.setPasscodeEnable(true, HomeActivity.this);
                    } else {
                        HomeActivity.this.startActivityForResult(new Intent(HomeActivity.this, ChangePassword.class), 2222);
                    }
                    HomeActivity.this.mTogglePattern.setChecked(false);
                    HomeActivity.this. mChangePasswordRl.setVisibility(View.VISIBLE);
                    return;
                }
                HomeActivity.this. mChangePasswordRl.setVisibility(View.GONE);
                MySettings.setPasscodeEnable(false, HomeActivity.this);
            }
        });
        if (MySettings.getPasscodeEnable(HomeActivity.this)) {
            this.mTogglePasscode.setChecked(true);
            this. mChangePasswordRl.setVisibility(View.VISIBLE);
        }

        mTogglePattern.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    if (MySettings.getPattern(HomeActivity.this).equals("")) {
                        HomeActivity.this.startActivityForResult(new Intent(HomeActivity.this, SetPattern.class), 444);
                    } else {
                        MySettings.setPatternEnable(true, HomeActivity.this);
                    }
                    HomeActivity.this.mTogglePasscode.setChecked(false);
                    HomeActivity.this. mChangePatternRl.setVisibility(View.VISIBLE);
                    return;
                }
                HomeActivity.this. mChangePatternRl.setVisibility(View.GONE);
                MySettings.setPatternEnable(isChecked, HomeActivity.this);
            }
        });
        if (MySettings.getPatternEnable(HomeActivity.this)) {
            this.mTogglePattern.setChecked(true);
            this. mChangePatternRl.setVisibility(View.VISIBLE);
        }
        mChangePasswordRl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, ChangePassword.class));
            }
        });
        mChangePatternRl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, SetPattern.class));
            }
        });
        if (Constants.getAutoWallpaperEnable(HomeActivity.this)) {
            mToggleAutoWallpaper.setChecked(true);
        } else {
            mToggleAutoWallpaper.setChecked(false);
        }


        mToggleAutoWallpaper.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    HomeActivity.this.accesStoragePermision();
                } else {
                    Constants.setAutoWallpaperEnable(HomeActivity.this, false);
                }
            }
        });
        if (MySettings.getSound(this))
        {
            mToggleSound.setChecked(true);
        } else {
            mToggleSound.setChecked(false);
        }

        mToggleSound.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    MySettings.setSound(isChecked, HomeActivity.this);
                } else {
                    MySettings.setSound(isChecked, HomeActivity.this);
                }
            }
        });
        if (MySettings.getVibrate(this)) {
            mToggleVibrate.setChecked(true);
        } else {
            mToggleVibrate.setChecked(false);
        }

        mToggleVibrate.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    MySettings.setVibrate(isChecked, HomeActivity.this);
                } else {
                    MySettings.setVibrate(isChecked, HomeActivity.this);
                }
            }
        });
        if (Constants.getWeaterEnable(this)) {
            mToggleWeather.setChecked(true);
        } else {
            mToggleWeather.setChecked(false);
        }

        mToggleWeather.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (!isChecked) {
                    Constants.setWeaterEnable(HomeActivity.this, isChecked);
                } else {
                    HomeActivity.this.accessPermision();
//                    HomeActivity.this.OnGPS();
                }
            }
        });
        if (Constants.getNotif(this)) {
            mToggleNotificationAccess.setChecked(true);
        } else {
            mToggleNotificationAccess.setChecked(false);
        }

        mToggleNotificationAccess.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= 19 && !HomeActivity.checkNotificationEnabled(HomeActivity.this)) {
                        HomeActivity.this.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    }
                    Constants.setNotif(HomeActivity.this, isChecked);
                    return;
                }
                Constants.setNotif(HomeActivity.this, isChecked);
            }
        });
        mRateusRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri1 = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                Intent inrate = new Intent(Intent.ACTION_VIEW, uri1);

                try {
                    startActivity(inrate);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(HomeActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
            }
        });
        mShareRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("android.intent.action.SEND");
                i.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=com.vadev");
                i.setType("text/plain");
                i.putExtra("android.intent.extra.SUBJECT", "Wallpaper Changer");
                startActivity(Intent.createChooser(i, getResources().getString(R.string.title_share)));
            }
        });
        mMoreRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri1 = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                Intent inrate = new Intent(Intent.ACTION_VIEW, uri1);

                try {
                    startActivity(inrate);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(HomeActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
                }
        });

    }
//permissions
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }


    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }
    @AfterPermissionGranted(13)
    public void accessStoragePermision() {
        if (Constants.hasPermissions(this, Constants.STORAGE_PERMISSION)) {
            try {
                startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1133);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            EasyPermissions.requestPermissions((HomeActivity) this, getString(R.string.permission_txt), 13, Constants.STORAGE_PERMISSION);
        }
    }
    @AfterPermissionGranted(15)
    public void accessPhoneStatePermision() {
        if (Constants.hasPermissions(HomeActivity.this, Constants.PHONE_STATE_PERMISSION)) {
            enableLock();
        } else {
            EasyPermissions.requestPermissions((HomeActivity) this, getString(R.string.permission_txt), 15, Constants.PHONE_STATE_PERMISSION);
        }
    }
    @AfterPermissionGranted(14)
    public void accesStoragePermision() {
        if (Constants.hasPermissions(HomeActivity.this, Constants.STORAGE_PERMISSION)) {
            Constants.setAutoWallpaperEnable(HomeActivity.this, true);
        } else {
            EasyPermissions.requestPermissions( (HomeActivity)this, getString(R.string.permission_txt), 14, Constants.STORAGE_PERMISSION);
        }
    }
    @AfterPermissionGranted(10)
    public void accessPermision() {
        if (!Constants.hasPermissions(this, Constants.LOCATION_PERMISSION)) {
            EasyPermissions.requestPermissions((HomeActivity) this, getString(R.string.permission_txt), 10, Constants.LOCATION_PERMISSION);
        } else if (Utils.isLocationEnabled(this)) {
            Constants.setWeaterEnable(this, true);
        } else {
            startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 12);
        }
    }
    private void enableLock() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkDrawOverlayPermission();
        } else {
            enableLockToggle();
        }
    }
    public void checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            try {
                startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + this.getPackageName())), 9);
            } catch (Exception unused) {
                Toast.makeText(this, "Activity not Found", Toast.LENGTH_LONG).show();
            }
        } else {
            enableLockToggle();
        }
    }

    public static boolean checkNotificationEnabled(Context context2) {
        try {
            return Settings.Secure.getString(context2.getContentResolver(), "enabled_notification_listeners").contains(context2.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void disableLockToggle() {
        this.mChangePasswordRl.setVisibility(View.GONE);
        this.mChangePatternRl.setVisibility(View.GONE);
        this.mTogglePattern.setChecked(false);
        this.mTogglePasscode.setChecked(false);
        MySettings.setLockscreen(false, this);
        stopService(new Intent(this, MyService.class));
    }

    private void enableLockToggle() {
        MySettings.setLockscreen(true, this);
       startService(new Intent(this, MyService.class));
    }
    public void onResume() {
        super.onResume();
        if (!MySettings.getPatternEnable(this)) {
            this.mTogglePattern.setChecked(false);
            this.mChangePatternRl.setVisibility(View.GONE);
        }
        if (!MySettings.getPasscodeEnable(this)) {
            this.mTogglePasscode.setChecked(false);
            this.mChangePasswordRl.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        if (!checkNotificationEnabled(this)) {
            this.mToggleNotificationAccess.setChecked(false);
        } else {
            this.mToggleNotificationAccess.setChecked(true);
        }
    }
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }
    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((HomeActivity) this, list)) {
            new AppSettingsDialog.Builder((HomeActivity) this).build().show();
            return;
        }
        Toast.makeText(this, "Required permission is not granted.!", Toast.LENGTH_LONG).show();
        if (i == 10) {
            this.mToggleWeather.setChecked(false);
        } else if (i == 15) {
            this.mToggleEnable.setChecked(false);
            enableLockToggle();
        }
    }
    public void onActivityResult(int i, int i2, Intent intent) {
        Uri data;
        String pathFromUri;
        if (i == 1133) {

            if (intent != null && (data = intent.getData()) != null && (pathFromUri = Constants.getPathFromUri(this, data)) != null && new File(pathFromUri).exists()) {
                Glide.with(HomeActivity.this).load(pathFromUri).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                            Utils.saveImage(((BitmapDrawable)resource).getBitmap());
                    }
                });
           }
        } else if (i == 2222 && i2 == 3333) {
            if (MySettings.getPasscodeEnable(this)) {
                this.mTogglePasscode.setChecked(true);
            } else {
                this.mTogglePasscode.setChecked(false);
            }
        } else if (i == 444 && i2 == 4444) {
            if (MySettings.getPatternEnable(this)) {
                this.mTogglePattern.setChecked(true);
            } else {
                this.mTogglePattern.setChecked(false);
            }
        } else if (i == 9) {
            if (Build.VERSION.SDK_INT < 23) {
                return;
            }
            if (Settings.canDrawOverlays(this)) {
                this.mToggleEnable.setChecked(true);
                enableLockToggle();
                return;
            }
            this.mToggleEnable.setChecked(false);
            disableLockToggle();
        } else if (i == 12 && this.locationManager.isProviderEnabled("gps")) {
            Constants.setWeaterEnable(this, true);
            this.mToggleWeather.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(HomeActivity.this,Exit.class);
        startActivity(intent);
    }
    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
