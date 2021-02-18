package com.lock.computerlockscreen.activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.lock.computerlockscreen.R;
import com.lock.computerlockscreen.background.Utils;

import java.io.File;
import java.io.IOException;

public class Fullscreen extends AppCompatActivity {
    private ImageView imageview;
    private String string;
    private  Integer integer;
    Uri uri;
    private ProgressDialog mProgressDialog;
    public static final int READ_WRITE_STORAGE = 52;
    private LinearLayout mLlSetWallpaper;
    private LinearLayout mLlShare;
    private LinearLayout mLlDownloadWallpaper;
    public Utils utils;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
getWindow().setFlags(1024,1024);
        final Bundle intent = getIntent().getExtras();
        integer = intent.getInt("img");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        this.utils = new Utils(this);

        imageview = findViewById(R.id.imgFullscreen);
        imageview.setImageResource(integer);
        mLlSetWallpaper = findViewById(R.id.llSetWallpaper);
        mLlShare = findViewById(R.id.llShare);
        mLlDownloadWallpaper = findViewById(R.id.llDownloadWallpaper);
        mLlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                integer = intent.getInt("img");
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, integer);
                startActivity(Intent.createChooser(shareIntent, "Share Via"));
            }
        });
        mLlDownloadWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                saveImage();
            }
        });
        mLlSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallpaper();
            }
        });
    }

    private void wallpaper() {
        if (Build.VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
            setWallpaper();
            return;
        } else {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
            return;
        }
    }

    private void setWallpaper() {
        final Bitmap bitmap = ((BitmapDrawable) this.imageview.getDrawable()).getBitmap();
        final ProgressDialog progressDialog = new ProgressDialog(this, 2);
        progressDialog.setTitle("Setting Wallpaper");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            public void run() {
                Utils.saveImage(bitmap);
                Fullscreen.this.runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }


    private void saveImage()
    {
        if (Build.VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
             final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), integer);
//            final Bitmap bitmap = ((BitmapDrawable) this.imageview.getDrawable()).getBitmap();
            final ProgressDialog progressDialog = new ProgressDialog(this, 2);
            progressDialog.setTitle("Downloading Wallpaper");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    Fullscreen.this.utils.saveImageToSDCard(bitmap);
                    Fullscreen.this.runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                }
            }).start();
            return;
        } else {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
            return;
        }
    }



}
