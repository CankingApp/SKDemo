package com.canking.scdemo;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by changxing on 16-8-23.
 */
public class BluetoothSetting extends SysSettingBase {

    public static String BLUETOOTH_ACTION_STATE_CHANGED;
    public static String BLUETOOTH_EXTRA_STATE;

    // BT state abstraction
    public static int BLUETOOTH_STATE_UNKNOWN;
    public static int BLUETOOTH_STATE_OFF;
    public static int BLUETOOTH_STATE_TURNING_ON;
    public static int BLUETOOTH_STATE_ON;
    public static int BLUETOOTH_STATE_TURNING_OFF;


    @Override
    public boolean isEnabled() {
        int state = bluetoothStateToFiveState(getBluetoothState());
        return state == STATE_ENABLED || state == STATE_TURNING_ON;
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public void setEnabled(final boolean open) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... args) {
                setEnable(open);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Toast.makeText(mAppCtx, "Bluetooth:" + open, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private BluetoothControl mBluetoothControl;

    interface BluetoothControl {
        void setEnabled(boolean enabled);

        int getBluetoothState();
    }

    class BluetoothControl20 implements BluetoothControl {

        private BluetoothAdapter mAdapter;

        public BluetoothControl20() {

            mAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mAdapter == null) {
                return;
            }

            // initialize state
            BLUETOOTH_STATE_UNKNOWN = -1;
            BLUETOOTH_STATE_OFF = BluetoothAdapter.STATE_OFF;
            BLUETOOTH_STATE_TURNING_ON = BluetoothAdapter.STATE_TURNING_ON;
            BLUETOOTH_STATE_ON = BluetoothAdapter.STATE_ON;
            BLUETOOTH_STATE_TURNING_OFF = BluetoothAdapter.STATE_TURNING_OFF;

            BLUETOOTH_ACTION_STATE_CHANGED = BluetoothAdapter.ACTION_STATE_CHANGED;
            BLUETOOTH_EXTRA_STATE = BluetoothAdapter.EXTRA_STATE;
        }

        @Override
        public int getBluetoothState() {
            try {
                mAdapter = BluetoothAdapter.getDefaultAdapter();
            } catch (Exception e) {
            }
            try {
                return mAdapter.getState();
            } catch (Exception e) {
            }
            return 1;
        }

        @Override
        public void setEnabled(boolean enabled) {
            if (enabled) {
                try {
                    mAdapter.enable();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            } else {
                try {
                    mAdapter.disable();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }

    public BluetoothSetting(Context context) {
        mAppCtx = context;
        mBluetoothControl = new BluetoothControl20(); // 2.0+ adapter
    }

    private int getBluetoothState() {
        return mBluetoothControl.getBluetoothState();
    }

    private void setEnable(boolean enable) {
        mBluetoothControl.setEnabled(enable);
    }

    private static int bluetoothStateToFiveState(int bluetoothState) {
        switch (bluetoothState) {
            case BluetoothAdapter.STATE_OFF:
                return STATE_DISABLED;
            case BluetoothAdapter.STATE_ON:
                return STATE_ENABLED;
            case BluetoothAdapter.STATE_TURNING_ON:
                return STATE_TURNING_ON;
            case BluetoothAdapter.STATE_TURNING_OFF:
                return STATE_TURNING_OFF;
            default:
                return STATE_UNKNOWN;
        }
    }
}
