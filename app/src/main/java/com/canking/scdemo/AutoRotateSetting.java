package com.canking.scdemo;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by changxing on 16-8-19.
 */
public class AutoRotateSetting extends SysSettingBase {
    public AutoRotateSetting(Context cxt) {
        mAppCtx = cxt;
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        boolean state = false;
        try {
            state = Settings.System.getInt(mAppCtx.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION) == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return state;
    }

    @Override
    public void setEnabled(boolean open) {
        Settings.System.putInt(mAppCtx.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, open ? 1 : 0);
    }
}
