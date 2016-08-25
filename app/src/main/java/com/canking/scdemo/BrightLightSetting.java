package com.canking.scdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by changxing on 16-8-25.
 */
public class BrightLightSetting extends SysSettingBase {
    public static final String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
    public static final int SCREEN_BRIGHTNESS_MODE_MANUAL = 0;
    //@see Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC (API 8)
    public static final int SCREEN_BRIGHTNESS_MODE_AUTOMATIC = 1;

    public static final int BRIGHTNESS_CODE_AUTO = 3;
    public static final int BRIGHTNESS_CODE_MAX = 1;
    public static final int BRIGHTNESS_CODE_MID = 0;
    public static final int BRIGHTNESS_CODE_MIN = 2;

    private static final int BRIGHTNESS_VALUE_INVALID = -1;
    private static final int BRIGHTNESS_VALUE_MIN = 50;
    private static final int BRIGHTNESS_VALUE_MID = 128;
    private static final int BRIGHTNESS_VALUE_MAX = 255;
    private static final int BRIGHTNESS_SEP_MIN_MID = 70;
    private static final int BRIGHTNESS_SEP_MID_MAX = 220;
    private ContentResolver mContentResolver;

    public BrightLightSetting(Context context) {
        mAppCtx = context;
        mContentResolver = mAppCtx.getContentResolver();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BRIGHTNESS_CODE_AUTO,
            BRIGHTNESS_CODE_MAX,
            BRIGHTNESS_CODE_MID,
            BRIGHTNESS_CODE_MIN
    })
    @interface BrightnessCode {
    }

    @BrightnessCode
    @Override
    public int getState() {
        return getBrightnessCode();
    }

    @Override
    public void setState(@BrightnessCode int state) {
        if (state == BRIGHTNESS_CODE_AUTO) {
            // to auto mode
            enableAutoMode(true);
            Toast.makeText(mAppCtx, "brightness_auto", Toast.LENGTH_LONG).show();

        } else if (state == BRIGHTNESS_CODE_MIN) {
            // to 1/4 brightness
            enableAutoMode(false);
            setBrightness(BRIGHTNESS_VALUE_MIN);
            Toast.makeText(mAppCtx, "brightness_min", Toast.LENGTH_LONG).show();

        } else if (state == BRIGHTNESS_CODE_MID) {
            // to 1/2 brightness
            setBrightness(BRIGHTNESS_VALUE_MID);
            Toast.makeText(mAppCtx, "brightness_mid", Toast.LENGTH_LONG).show();

        } else if (state == BRIGHTNESS_CODE_MAX) {
            // to max brightness
            setBrightness(BRIGHTNESS_VALUE_MAX);
            Toast.makeText(mAppCtx, "brightness_max.", Toast.LENGTH_LONG).show();
        }
    }

    private int getBrightnessCode() {
        if (isAutoModeEnabled()) {
            return BRIGHTNESS_CODE_AUTO;
        }

        int curBrightnessValue = Settings.System.getInt(mContentResolver,
                Settings.System.SCREEN_BRIGHTNESS, 0);
        if (curBrightnessValue < BRIGHTNESS_SEP_MIN_MID) {
            return BRIGHTNESS_CODE_MIN;
        } else if (curBrightnessValue < BRIGHTNESS_SEP_MID_MAX) {
            return BRIGHTNESS_CODE_MID;
        } else {
            return BRIGHTNESS_CODE_MAX;
        }
    }

    public void toggleState() {
        int brightnessCode = getBrightnessCode();
        if (brightnessCode == BRIGHTNESS_CODE_AUTO) {
            setState(BRIGHTNESS_CODE_MIN);
        } else if (brightnessCode == BRIGHTNESS_CODE_MIN) {
            setState(BRIGHTNESS_CODE_MID);
        } else if (brightnessCode == BRIGHTNESS_CODE_MID) {
            // to max brightness
            setState(BRIGHTNESS_CODE_MAX);
        } else if (brightnessCode == BRIGHTNESS_CODE_MAX) {
            // to auto mode
            setState(BRIGHTNESS_CODE_AUTO);
        }
    }

    private void setBrightness(int value) {
        Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, value);
        IBinder binder = SMCompat.getService(Context.POWER_SERVICE);
        if (binder != null) {
            PMCompat.setBacklightBrightness(
                    PMCompat.asInterface(binder), value);
        }
    }

    private boolean isAutoModeEnabled() {
        int mode = Settings.System.getInt(mContentResolver, SCREEN_BRIGHTNESS_MODE,
                SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        return (mode == SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    private void enableAutoMode(boolean enable) {
        int mode = (enable ? SCREEN_BRIGHTNESS_MODE_AUTOMATIC : SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(mContentResolver, SCREEN_BRIGHTNESS_MODE, mode);
    }
}
