package com.lock.computerlockscreen.model;

import android.telephony.PhoneStateListener;
import android.widget.ImageView;

import com.lock.computerlockscreen.R;

public class MyPhoneStateListener extends PhoneStateListener {
    private ImageView iv_gsmSignalInfo;


    public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {

        super.onSignalStrengthsChanged(signalStrength);

            int level = signalStrength.getGsmSignalStrength();
            if (level > 4) level /= 7.75;
            else if (level < 1) {
                int strength = signalStrength.getCdmaDbm();
                if (strength < -100) level = 0;
                else if (strength < -95) level = 1;
                else if (strength < -85) level = 2;
                else if (strength < -75) level = 3;
                else if (strength != 0) level = 4;
                else {
                    strength = signalStrength.getEvdoDbm();
                    if (strength == 0 || strength < -100) level = 0;
                    else if (strength < -95) level = 1;
                    else if (strength < -85) level = 2;
                    else if (strength < -75) level = 3;
                    else level = 4;
                }
            }

        setGSMSignalLevel(level);

    }

    private void setGSMSignalLevel(int i) {
        if (this.iv_gsmSignalInfo != null) {
            switch (i) {
                case 0:
                    this.iv_gsmSignalInfo.setImageResource(R.drawable.signal_0);
                    return;
                case 1:
                    this.iv_gsmSignalInfo.setImageResource(R.drawable.signal_1);
                    return;
                case 2:
                    this.iv_gsmSignalInfo.setImageResource(R.drawable.signal_2);
                    return;
                case 3:
                    this.iv_gsmSignalInfo.setImageResource(R.drawable.signal_3);
                    return;
                case 4:
                    this.iv_gsmSignalInfo.setImageResource(R.drawable.signal_4);
                    return;
                default:
                    this.iv_gsmSignalInfo.setImageResource(R.drawable.signal_full);
                    return;
            }
        }
    }

    public void setImageView(ImageView imageView) {
        this.iv_gsmSignalInfo = imageView;
    }
}
