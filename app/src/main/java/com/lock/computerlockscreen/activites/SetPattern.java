package com.lock.computerlockscreen.activites;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.bumptech.glide.Glide;
import com.lock.computerlockscreen.R;
import com.lock.computerlockscreen.utils.Constants;
import com.lock.computerlockscreen.utils.MySettings;

import de.hdodenhof.circleimageview.CircleImageView;
import java.io.File;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SetPattern extends Activity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    boolean again = false;
    Button cancel_button;
    Button confrm_button;
    Dialog dialog;
    String first = "";
    TextView help_txt;
    Context mContxt;
    /* access modifiers changed from: private */
    public PatternLockView mPatternLockView;
    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        public void onProgress(List<PatternLockView.Dot> list) {
            String name = getClass().getName();
            Log.d(name, "Pattern progress: " + PatternLockUtils.patternToString(SetPattern.this.mPatternLockView, list));
            SetPattern.this.help_txt.setText("Release Finger When Done");
        }

        public void onComplete(List<PatternLockView.Dot> list) {
            String name = getClass().getName();
            Log.d(name, "Pattern complete: " + PatternLockUtils.patternToString(SetPattern.this.mPatternLockView, list));
            if (!SetPattern.this.again) {
                SetPattern.this.s = PatternLockUtils.patternToString(SetPattern.this.mPatternLockView, list);
                SetPattern.this.help_txt.setText("Pattern Recorded");
            } else if (SetPattern.this.first.equals("") || !SetPattern.this.first.equals(PatternLockUtils.patternToString(SetPattern.this.mPatternLockView, list))) {
                SetPattern.this.help_txt.setText("Wrong pattern, please retry");
                Toast.makeText(SetPattern.this.mContxt, "Wrong pattern, please retry", 1).show();
                ((Vibrator) SetPattern.this.getApplicationContext().getSystemService("vibrator")).vibrate(100);
                SetPattern.this.mPatternLockView.clearPattern();
            } else {
                SetPattern.this.help_txt.setText("Your new unlock pattern");
                MySettings.setPattern(PatternLockUtils.patternToString(SetPattern.this.mPatternLockView, list), SetPattern.this.mContxt);
                SetPattern.this.ok_button.setVisibility(8);
                SetPattern.this.confrm_button.setVisibility(0);
            }
        }

        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };
    Button ok_button;
    CircleImageView profile_icon;
    String s = "";
    TextView user_name;

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContxt = this;
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_set_pattern);
        this.help_txt = (TextView) findViewById(R.id.help_txt);
        this.profile_icon = (CircleImageView) findViewById(R.id.profile_icon);
        this.user_name = (TextView) findViewById(R.id.user_name);
        this.cancel_button = (Button) findViewById(R.id.cancel_button);
        this.cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SetPattern.this.mPatternLockView.clearPattern();
                SetPattern.this.help_txt.setText("Draw an unlock pattern");
                SetPattern.this.s = "";
                SetPattern.this.again = false;
                SetPattern.this.ok_button.setVisibility(0);
                SetPattern.this.confrm_button.setVisibility(8);
            }
        });
        if (!Constants.getUserName(this.mContxt).equals("")) {
            this.user_name.setText(Constants.getUserName(this.mContxt));
        }
        if (Constants.getUserImage(this.mContxt) == null) {
            this.profile_icon.setImageResource(R.drawable.profile_icon_add);
        } else if (new File(Constants.getUserImage(this.mContxt)).exists()) {
            Glide.with(this.mContxt).load(Constants.getUserImage(this.mContxt)).into(this.profile_icon);
        } else {
            this.profile_icon.setImageResource(R.drawable.profile_icon_add);
        }
        this.profile_icon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SetPattern.this.accessStoragePermision();
            }
        });
        this.ok_button = (Button) findViewById(R.id.ok_button);
        this.ok_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SetPattern.this.mPatternLockView.clearPattern();
                SetPattern.this.help_txt.setText("Draw the Pattern again to confirm");
                SetPattern.this.first = SetPattern.this.s;
                SetPattern.this.again = true;
            }
        });
        this.confrm_button = (Button) findViewById(R.id.confrm_button);
        this.confrm_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SetPattern.this.again = false;
                MySettings.setPatternEnable(true, SetPattern.this.mContxt);
                SetPattern.this.setResult(4444);
                SetPattern.this.finish();
            }
        });
        this.user_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SetPattern.this.showUserNameDialoge();
            }
        });
        this.mPatternLockView = (PatternLockView) findViewById(R.id.patter_lock_view);
        this.mPatternLockView.setDotCount(3);
        this.mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        this.mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        this.mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        this.mPatternLockView.setAspectRatioEnabled(true);
        this.mPatternLockView.setAspectRatio(2);
        this.mPatternLockView.setViewMode(0);
        this.mPatternLockView.setDotAnimationDuration(150);
        this.mPatternLockView.setPathEndAnimationDuration(100);
        this.mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.white));
        this.mPatternLockView.setWrongStateColor(ResourceUtils.getColor(this, R.color.pomegranate));
        this.mPatternLockView.setInStealthMode(false);
        this.mPatternLockView.setTactileFeedbackEnabled(true);
        this.mPatternLockView.setInputEnabled(true);
        this.mPatternLockView.addPatternLockListener(this.mPatternLockViewListener);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        Uri data;
        super.onActivityResult(i, i2, intent);
        if (i == 1122 && intent != null && (data = intent.getData()) != null) {
            String pathFromUri = Constants.getPathFromUri(this.mContxt, data);
            Constants.setUserImage(this.mContxt, pathFromUri);
            if (pathFromUri == null) {
                this.profile_icon.setImageResource(R.drawable.profile_icon_add);
            } else if (new File(pathFromUri).exists()) {
                Glide.with(this.mContxt).load(pathFromUri).into(this.profile_icon);
            } else {
                this.profile_icon.setImageResource(R.drawable.profile_icon_add);
            }
        }
    }

    @AfterPermissionGranted(13)
    public void accessStoragePermision() {
        if (Constants.hasPermissions(this.mContxt, Constants.STORAGE_PERMISSION)) {
            try {
                startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1122);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this.mContxt, e.toString(), 0).show();
            }
        } else {
            EasyPermissions.requestPermissions((Activity) this, getString(R.string.permission_txt), 13, Constants.STORAGE_PERMISSION);
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    public void onPermissionsGranted(int i, @NonNull List<String> list) {
        if (i == 13 && Constants.hasPermissions(this.mContxt, Constants.STORAGE_PERMISSION)) {
            try {
                startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1122);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this.mContxt, e.toString(), 0).show();
            }
        }
    }

    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        } else {
            Toast.makeText(this.mContxt, "Required permission is not granted.!", 1).show();
        }
    }

    public void showUserNameDialoge() {
        this.dialog = new Dialog(this.mContxt);
        this.dialog.setContentView(R.layout.user_name_dialoge);
        final EditText editText = (EditText) this.dialog.findViewById(R.id.user_name_et);
        ((TextView) this.dialog.findViewById(R.id.cancel_username)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SetPattern.this.dialog.dismiss();
            }
        });
        ((TextView) this.dialog.findViewById(R.id.done_username)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!editText.getText().toString().trim().equals("")) {
                    Constants.setUserName(SetPattern.this.mContxt, editText.getText().toString().trim());
                    SetPattern.this.user_name.setText(editText.getText().toString().trim());
                    SetPattern.this.dialog.dismiss();
                    return;
                }
                Toast.makeText(SetPattern.this.mContxt, "Enter Name First", 1).show();
            }
        });
        this.dialog.show();
    }
}
