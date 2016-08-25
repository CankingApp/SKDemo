package com.canking.scdemo;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by changxing on 16-8-19.
 */
public class WifiSetting extends SysSettingBase {
    private WifiManager mWifiMgr;
    private Context mAppCtx;

    public WifiSetting(Context context) {
        mAppCtx = context.getApplicationContext();
        mWifiMgr = (WifiManager) mAppCtx.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        int state = wifiStateToFiveState(mWifiMgr.getWifiState());
        if (state == STATE_ENABLED || state == STATE_TURNING_ON) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setEnabled(final boolean open) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... args) {
                mWifiMgr.setWifiEnabled(open);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Toast.makeText(mAppCtx, "Wifi:" + open, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    private static int wifiStateToFiveState(int wifiState) {
        switch (wifiState) {
            case WifiManager.WIFI_STATE_DISABLED:
                return STATE_DISABLED;
            case WifiManager.WIFI_STATE_ENABLED:
                return STATE_ENABLED;
            case WifiManager.WIFI_STATE_DISABLING:
                return STATE_TURNING_OFF;
            case WifiManager.WIFI_STATE_ENABLING:
                return STATE_TURNING_ON;
            default:
                return STATE_UNKNOWN;
        }
    }
}
