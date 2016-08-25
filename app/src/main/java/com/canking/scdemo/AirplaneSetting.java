package com.canking.scdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by changxing on 16-8-18.
 */
public class AirplaneSetting extends SysSettingBase {

    public AirplaneSetting(Context context) {
        mAppCtx = context;
    }

    @Override
    public boolean isEnabled() {
        ContentResolver contentResolver = mAppCtx.getContentResolver();
        return Settings.System.getInt(contentResolver, Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    @Override
    public void setEnabled(boolean open) {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        mAppCtx.startActivity(intent);
    }

}
