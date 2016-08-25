package com.canking.scdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AutoRotateSetting autoSetting;
    WifiSetting wifiSetting;
    NetSetting netSetting;
    GpsSetting gpsSetting;
    AutoSyncSetting autoSyncSetting;
    BluetoothSetting bluetoothSetting;
    BrightLightSetting brightLightSetting;
    RingtoneSetting ringtoneSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoSetting = new AutoRotateSetting(this);
        wifiSetting = new WifiSetting(this);
        netSetting = new NetSetting(this);
        gpsSetting = new GpsSetting(this);
        autoSyncSetting = new AutoSyncSetting(this);
        bluetoothSetting = new BluetoothSetting(this);
        brightLightSetting = new BrightLightSetting(this);
        ringtoneSetting = new RingtoneSetting(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        boolean airplane = isAirplaneModeOn(this);
        Toast.makeText(this, "Blue:" + bluetoothSetting.isEnabled() + "Sync:" + autoSyncSetting.isEnabled() +
                " Airplane:" + airplane + " auto:" + autoSetting.isEnabled() + " netSetting:" + netSetting.isEnabled() +
                " GPS:" + gpsSetting.isEnabled() + " bright:" + brightLightSetting.getState() + " ringtone:" + ringtoneSetting.getState(), Toast.LENGTH_LONG).show();
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.airplane_btn) {
            setAirplaneModeOn(this);
        } else if (id == R.id.auto_btn) {
            autoSetting.setEnabled(true);
        } else if (id == R.id.wifi_btn) {
            wifiSetting.setEnabled(true);
        } else if (id == R.id.net_btn) {
            netSetting.setEnabled(true);
        } else if (id == R.id.gps_btn) {
            gpsSetting.setEnabled(true);
        } else if (id == R.id.scyn_btn) {
            autoSyncSetting.setEnabled(true);
        } else if (id == R.id.blue_btn) {
            bluetoothSetting.setEnabled(true);
        } else if (id == R.id.light_btn) {
            brightLightSetting.toggleState();
        } else if (id == R.id.ringtone_btn) {
            ringtoneSetting.toggleState();
        } else if (id == R.id.flashlight_btn) {
            // TODO: no need now
        }
    }

    public static void setAirplaneModeOn(Context context) {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        context.startActivity(intent);
    }

    static boolean isAirplaneModeOn(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.System.getInt(contentResolver, Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }
}
