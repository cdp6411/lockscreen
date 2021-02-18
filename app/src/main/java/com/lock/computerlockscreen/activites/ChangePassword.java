package com.lock.computerlockscreen.activites;

import android.app.Activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lock.computerlockscreen.R;
import com.lock.computerlockscreen.utils.Constants;
import com.lock.computerlockscreen.utils.MySettings;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ChangePassword extends Activity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    Typeface clock1;
    Context context;
    Dialog dialog;
    EditText email_passcode;
    ImageView imgPass1;
    ImageView imgPass2;
    ImageView imgPass3;
    ImageView imgPass4;
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
    RelativeLayout lout_lock_bg;
    Button mail_confr;
    Button not_confrm;
    /* access modifiers changed from: private */
    public String passcode = "";
    String password = "";
    CircleImageView profile_icon;
    String reTypePassword = "";
    RelativeLayout txt_delete;
    TextView txt_emergency;
    TextView txt_enter_password;
    TextView user_name;
    /* access modifiers changed from: private */
    public Vibrator vibrator;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_change_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.context=this;
        this.profile_icon = (CircleImageView) findViewById(R.id.profile_icon);
        this.lnum1 = (RelativeLayout) findViewById(R.id.lnum1);
        this.lnum2 = (RelativeLayout) findViewById(R.id.lnum2);
        this.lnum3 = (RelativeLayout) findViewById(R.id.lnum3);
        this.lnum4 = (RelativeLayout) findViewById(R.id.lnum4);
        this.lnum5 = (RelativeLayout) findViewById(R.id.lnum5);
        this.lnum6 = (RelativeLayout) findViewById(R.id.lnum6);
        this.lnum7 = (RelativeLayout) findViewById(R.id.lnum7);
        this.lnum8 = (RelativeLayout) findViewById(R.id.lnum8);
        this.lnum9 = (RelativeLayout) findViewById(R.id.lnum9);
        this.lnum0 = (RelativeLayout) findViewById(R.id.lnum0);
        this.imgPass1 = (ImageView) findViewById(R.id.imgpass1);
        this.imgPass2 = (ImageView) findViewById(R.id.imgpass2);
        this.imgPass3 = (ImageView) findViewById(R.id.imgpass3);
        this.imgPass4 = (ImageView) findViewById(R.id.imgpass4);
        this.txt_enter_password = (TextView) findViewById(R.id.txt_enter_password);
        this.txt_emergency = (TextView) findViewById(R.id.txt_emergency);
        this.txt_delete = (RelativeLayout) findViewById(R.id.txt_delete);
        this.user_name = (TextView) findViewById(R.id.user_name);
        this.clock1 = Typeface.createFromAsset(getAssets(), "bold_font.ttf");
        this.txt_enter_password.setTypeface(this.clock1);
        this.txt_emergency.setTypeface(this.clock1);
        this.vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);

        if (!Constants.getUserName(this.context).equals("")) {
            this.user_name.setText(Constants.getUserName(this.context));
        }
        if (Constants.getUserImage(this.context) == null) {
            this.profile_icon.setImageResource(R.drawable.profile_icon_add);
        } else if (new File(Constants.getUserImage(this.context)).exists()) {
            Glide.with(this.context).load(Constants.getUserImage(this.context)).into(this.profile_icon);
        } else {
            this.profile_icon.setImageResource(R.drawable.profile_icon_add);
        }
        this.profile_icon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ChangePassword.this.accessStoragePermision();
            }
        });
        this.txt_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String unused = ChangePassword.this.passcode = "";
                ChangePassword.this.FeelDot(ChangePassword.this.passcode);
                if (MySettings.getVibrate(ChangePassword.this.context)) {
                    ChangePassword.this.vibrator.vibrate(20);
                }
            }
        });
        this.user_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ChangePassword.this.showUserNameDialoge();
            }
        });
        this.lnum1.setOnClickListener(this);
        this.lnum2.setOnClickListener(this);
        this.lnum3.setOnClickListener(this);
        this.lnum4.setOnClickListener(this);
        this.lnum5.setOnClickListener(this);
        this.lnum6.setOnClickListener(this);
        this.lnum7.setOnClickListener(this);
        this.lnum8.setOnClickListener(this);
        this.lnum9.setOnClickListener(this);
        this.lnum0.setOnClickListener(this);

    }

    public void onActivityResult(int i, int i2, Intent intent) {
        Uri data;
        super.onActivityResult(i, i2, intent);
        if (i == 1122 && intent != null && (data = intent.getData()) != null) {
            String pathFromUri = Constants.getPathFromUri(this.context, data);
            Constants.setUserImage(this.context, pathFromUri);
            if (pathFromUri == null) {
                this.profile_icon.setImageResource(R.drawable.profile_icon_add);
            } else if (new File(pathFromUri).exists()) {
                Glide.with(this.context).load(pathFromUri).into(this.profile_icon);
            } else {
                this.profile_icon.setImageResource(R.drawable.profile_icon_add);
            }
        }
    }

    //permissions
    @AfterPermissionGranted(13)
    public void accessStoragePermision() {
        if (Constants.hasPermissions(this.context, Constants.STORAGE_PERMISSION)) {
            try {
                startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1122);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_txt), 13, Constants.STORAGE_PERMISSION);
        }
    }

    //this is for feel the dot in password
    public void FeelDot(String str) {
        if (str.length() == 0) {
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.openpass)).getBitmap();
            this.imgPass1.setImageBitmap(bitmap);
            this.imgPass2.setImageBitmap(bitmap);
            this.imgPass3.setImageBitmap(bitmap);
            this.imgPass4.setImageBitmap(bitmap);
            return;
        }
        if (str.length() == 1) {
            this.imgPass1.setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.feelpass)).getBitmap());
        }
        if (str.length() == 2) {
            this.imgPass2.setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.feelpass)).getBitmap());
        }
        if (str.length() == 3) {
            this.imgPass3.setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.feelpass)).getBitmap());
        }
        if (str.length() == 4) {
            this.imgPass4.setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.feelpass)).getBitmap());
            if (this.password.equals("")) {
                this.password = str;
                this.txt_enter_password.setText("Re-type Password");
                Bitmap bitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.openpass)).getBitmap();
                this.imgPass1.setImageBitmap(bitmap2);
                this.imgPass2.setImageBitmap(bitmap2);
                this.imgPass3.setImageBitmap(bitmap2);
                this.imgPass4.setImageBitmap(bitmap2);
                this.passcode = "";
            } else if (this.password.equals(str)) {
                MySettings.setPassword(str, this.context);
                MySettings.setPasscodeEnable(true, this.context);
                setResult(3333);
                finish();
            } else {
                try {
                    findViewById(R.id.shakelayout).startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.shake));
                    if (MySettings.getVibrate(this.context)) {
                        this.vibrator.vibrate(100);
                    }
                    this.passcode = "";
                    FeelDot(this.passcode);
                } catch (Exception unused) {
                }
            }
        }
    }

    //user enter name for dialouge
    public void showUserNameDialoge() {
        this.dialog = new Dialog(this.context);
        this.dialog.setContentView(R.layout.user_name_dialoge);
        final EditText editText = (EditText) this.dialog.findViewById(R.id.user_name_et);
        ((TextView) this.dialog.findViewById(R.id.cancel_username)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ChangePassword.this.dialog.dismiss();
            }
        });
        ((TextView) this.dialog.findViewById(R.id.done_username)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!editText.getText().toString().trim().equals("")) {
                    Constants.setUserName(ChangePassword.this.context, editText.getText().toString().trim());
                    ChangePassword.this.user_name.setText(editText.getText().toString().trim());
                    ChangePassword.this.dialog.dismiss();
                    return;
                }
                Toast.makeText(ChangePassword.this.context, "Enter Name First", Toast.LENGTH_LONG).show();
            }
        });
        this.dialog.show();
    }

    public void onClick(View view) {
        switch (view.getId()) {
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
        if (MySettings.getVibrate(this.context)) {
            this.vibrator.vibrate(20);
        }
    }
    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> list) {
        if (requestCode == 13 && Constants.hasPermissions(this.context, Constants.STORAGE_PERMISSION)) {
            try {
                startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1122);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        } else {
            Toast.makeText(this.context, "Required permission is not granted.!", Toast.LENGTH_LONG).show();
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    private Bitmap createBitmapBlur(Bitmap bitmap, float f) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            try {
                RenderScript create = RenderScript.create(this.context);
                Allocation createFromBitmap = Allocation.createFromBitmap(create, bitmap);
                Allocation createFromBitmap2 = Allocation.createFromBitmap(create, createBitmap);
                if (Build.VERSION.SDK_INT >= 17) {
                    try {
                        ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, Element.U8_4(create));
                        create2.setInput(createFromBitmap);
                        create2.setRadius(f);
                        create2.forEach(createFromBitmap2);
                    } catch (NoClassDefFoundError e) {
                        e.printStackTrace();
                        return bitmap;
                    }
                }
                createFromBitmap2.copyTo(bitmap);
                create.destroy();
            } catch (NoClassDefFoundError e2) {
                e2.printStackTrace();
                return bitmap;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return bitmap;
    }
}
