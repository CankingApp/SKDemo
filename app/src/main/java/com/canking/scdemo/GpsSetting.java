package com.canking.scdemo;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

/**
 * Created by changxing on 16-8-19.
 */
public class GpsSetting extends SysSettingBase {
    public static final String LOCATION_SOURCE_ACTION = Settings.ACTION_LOCATION_SOURCE_SETTINGS;


    public GpsSetting(Context context) {
        mAppCtx = context;
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        LocationManager locationMgr = (LocationManager) mAppCtx.getSystemService(Context.LOCATION_SERVICE);
        boolean on = false;
        try {
            on = locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return on;
    }

    @Override
    public void setEnabled(boolean open) {
        PackageManager pManager = mAppCtx.getPackageManager();
        // pManager
        Intent intent1 = new Intent();
        intent1.setClassName(SETTINGS_PACKAGE, SETTINGSAPPWIDGERPROVIDER);
        intent1.addCategory(ALTERNATIVE_CATEGORY);

        List<ResolveInfo> rList = null;
        if (pManager != null) {
            rList = pManager.queryBroadcastReceivers(intent1,
                    PackageManager.GET_RESOLVED_FILTER);
        }
        boolean canOpen = false;
        for (int count = 0; rList != null && count < rList.size(); count++) {
            ActivityInfo info = rList.get(count).activityInfo;
            canOpen = info.exported && TextUtils.isEmpty(info.permission);
            if (canOpen) {
                break;
            }
        }
        if (canOpen) {
            Intent intent = new Intent();
            intent.setClassName(SETTINGS_PACKAGE, SETTINGSAPPWIDGERPROVIDER);
            intent.addCategory(ALTERNATIVE_CATEGORY);
            Uri localUri = Uri.parse("custom:3");
            intent.setData(localUri);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mAppCtx, 0, intent, 0);
            try {
                pendingIntent.send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(mAppCtx, open ? "GPS:开" : "GPS:关", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent2 = new Intent();
            intent2.setAction(LOCATION_SOURCE_ACTION);
            intent2.addCategory(Intent.CATEGORY_DEFAULT);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mAppCtx.startActivity(intent2);
        }
    }
}
