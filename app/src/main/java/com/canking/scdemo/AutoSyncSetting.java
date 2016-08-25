package com.canking.scdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by changxing on 16-8-23.
 */
public class AutoSyncSetting extends SysSettingBase {
    public AutoSyncSetting(Context cxt) {
        mAppCtx = cxt.getApplicationContext();
    }

    @Override
    public boolean isEnabled() {
        return getSyncState();
    }


    private boolean getBackgroundDataState() {
        ConnectivityManager cm;
        try {
            cm = (ConnectivityManager) mAppCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
        } catch (Exception e) {
            return false;
        }
        return cm.getBackgroundDataSetting();
    }

    private boolean getSyncState() {
        boolean bool1 = getBackgroundDataState();
        boolean bool2 = ContentResolver.getMasterSyncAutomatically();
        return bool1 && bool2;
    }

    @Override
    public void setEnabled(boolean open) {
        boolean backgroundData = getBackgroundDataState();
        if (backgroundData) {
            ContentResolver.setMasterSyncAutomatically(open);
        } else {
            Toast.makeText(mAppCtx, "设置无效", Toast.LENGTH_LONG).show();
        }
    }
}
