package com.lock.computerlockscreen.activites;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Handler;

import android.os.Bundle;
import android.util.Log;

import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.lock.computerlockscreen.R;

public class StartActivity extends Activity {
    Context mContxt;


    private InterstitialAd mInterstitialAd;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        getWindow().setFlags(1024,1024);
        this.mContxt = this;
//        Window window = getWindow();

//        if (Build.VERSION.SDK_INT >= 19) {
//            window.setFlags(512, 512);
//        }
//        try {
//            if (Build.VERSION.SDK_INT >= 21) {
//                window.addFlags(Integer.MIN_VALUE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            if (Build.VERSION.SDK_INT >= 19) {
//                window.clearFlags(67108864);
//            }
//        } catch (Resources.NotFoundException e2) {
//            e2.printStackTrace();
//        }
//        try {
//            if (Build.VERSION.SDK_INT >= 21) {
//                window.setStatusBarColor(getResources().getColor(R.color.transparentWhite));
//            }
//        } catch (Resources.NotFoundException e3) {
//            e3.printStackTrace();
//        }
//        getWindow().getDecorView().setSystemUiVisibility(1280);
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setStatusBarCoor(0);
//        }
        ((ImageView) findViewById(R.id.imageView)).startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoon_in));
        new Handler().postDelayed(new Runnable() {
            public void run() {
                StartActivity.this.startActivity(new Intent(StartActivity.this.mContxt, HomeActivity.class));
                StartActivity.this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
                StartActivity.this.finish();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        }, 3000);
    }
}

