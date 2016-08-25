package com.canking.scdemo;

import android.content.Context;

/**
 * Created by changxing on 16-8-18.
 */
public abstract class SysSettingBase {
    protected static final String SETTINGS_PACKAGE = "com.android.settings";
    protected static final String SETTINGSAPPWIDGERPROVIDER = "com.android.settings.widget.SettingsAppWidgetProvider";
    protected static final String ALTERNATIVE_CATEGORY = "android.intent.category.ALTERNATIVE";


    protected static final int STATE_DISABLED = 0;
    protected static final int STATE_ENABLED = 1;
    protected static final int STATE_TURNING_ON = 2;
    protected static final int STATE_TURNING_OFF = 3;
    protected static final int STATE_UNKNOWN = 4;


    protected Context mAppCtx;

    public boolean isEnabled() {
        return false;
    }


    public void setEnabled(boolean open) {
    }


    public int getState() {
        return 0;
    }


    public void setState(int state) {
    }

    public void toggleState() {

    }
}
