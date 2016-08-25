package com.canking.scdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.List;

/**
 * Created by changxing on 16-8-19.
 */
public class NetSetting extends SysSettingBase {
    public static final int OPER_CHINAMOBILE = 0;
    public static final int OPER_CHINAUNICOM = 1;
    public static final int OPER_CHINATELECOM = 2;
    public static final int OPER_OTHER = 3;
    public static final int OPER_NONE = -1;

    private boolean sHasMobile;


    public NetSetting(Context ctx) {
        mAppCtx = ctx;
    }

    @Override
    public boolean isEnabled() {
        boolean state;
        if (CMCompat.isSetMobileDataSupport()) {
            state = !getDataStateV9(mAppCtx);
        } else {
            state = !getIsApnEnable();
        }
        return state;
    }

    @Override
    public void setEnabled(final boolean open) {
        changeDataState(mAppCtx);
    }

    public void changeDataState(Context context) {
        boolean success = false;
        boolean state;
        if (CMCompat.isSetMobileDataSupport()) {
            state = !getDataStateV9(context);
            success = changeDataStateV9(context, state);
        } else {
            state = !getIsApnEnable();
        }
        if (!success) {
            success = switchApn(state);
        }
        if (!success) {
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent = new Intent();
                intent.setComponent(new ComponentName(
                        "com.android.phone",
                        "com.android.phone.settings.MobileNetworkSettings"));

                if (!isActivityAvailable(context, intent)) {
                    if (getOperator(context) == OPER_NONE) {
                        Toast.makeText(mAppCtx, "No SIM card...", Toast.LENGTH_LONG).show();
                        return;
                    }
                    intent.setComponent(new ComponentName(
                            "com.android.settings",
                            "com.android.settings.Settings$DataUsageSummaryActivity"));
                }
            } else {
                intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            }
            if (isActivityAvailable(context, intent)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Toast.makeText(mAppCtx, "No setting...", Toast.LENGTH_LONG).show();
            }
        } else {

            Toast.makeText(mAppCtx, !state ? "closed" : "open", Toast.LENGTH_LONG).show();

        }
    }

    private boolean switchApn(boolean b) {
        if (getOperator(mAppCtx) == OPER_NONE) {
            return false;
        }
        return false;
    }

    private boolean getDataStateV9(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return CMCompat.getMobileDataEnabled(cm);
    }

    private boolean changeDataStateV9(Context context, boolean state) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return CMCompat.setMobileDataEnabled(cm, state);
    }


    private boolean getCurrentApnStatus(Context context) {
        int i = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDataState();
        return (1 == i) || (2 == i);
    }

    private boolean getIsApnEnable() {
        boolean bool2 = getCurrentApnStatus(mAppCtx);
        return bool2;
    }

    private static int getOperator(Context ctx) {
        TelephonyManager telManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telManager.getSimState() != TelephonyManager.SIM_STATE_READY) {
            return OPER_NONE;
        }
        String operator = telManager.getSimOperator();
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                //  Mobile
                return OPER_CHINAMOBILE;
            } else if (operator.equals("46001") || operator.equals("46006")) {
                //  Unicom
                return OPER_CHINAUNICOM;
            } else if (operator.equals("46003") || operator.equals("46005")) {
                //  Telecom
                return OPER_CHINATELECOM;
            } else {
                return OPER_OTHER;
            }
        }
        return OPER_NONE;
    }

    public static boolean isActivityAvailable(Context cxt, Intent intent) {
        PackageManager pm = cxt.getPackageManager();
        if (pm == null) {
            return false;
        }
        List<ResolveInfo> list = pm.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list != null && list.size() > 0;
    }
}
