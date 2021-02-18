package com.lock.computerlockscreen.activites;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.lock.computerlockscreen.R;

public class Exit extends AppCompatActivity{
TextView mTextview,mRateusUsername,mDoneUsername,mCancelUsername;
LinearLayout mL1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        getWindow().setFlags(1024,1024);

        mTextview = findViewById(R.id.textview);
        mL1 = findViewById(R.id.l1);
        mRateusUsername = findViewById(R.id.rateus_username);
        mDoneUsername = findViewById(R.id.done_username);
        mCancelUsername = findViewById(R.id.cancel_username);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdLoader.Builder builder = new AdLoader.Builder(
                this, "ca-app-pub-3940256099942544/2247696110");

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView template = findViewById(R.id.my_template);
                template.setNativeAd(unifiedNativeAd);
            }
        });

        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());


    }


    public void no(View view) {
        Intent intent =new Intent(Exit.this,HomeActivity.class);
        startActivity(intent);
        finish();

    }

    public void yes(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void rateus(View view) {
        Uri uri1 = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
        Intent inrate = new Intent(Intent.ACTION_VIEW, uri1);

        try {
            startActivity(inrate);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Exit.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
