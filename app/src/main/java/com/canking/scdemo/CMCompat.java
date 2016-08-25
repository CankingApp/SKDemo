package com.canking.scdemo;

import android.net.ConnectivityManager;
import android.util.Log;

import java.lang.reflect.Method;

public class CMCompat {
    private static final boolean DEBUG = false;
    private static final String TAG = "ConnectivityMgrCompat";

    private static Method sGetMobileDataEnabledMethod;
    private static Method sSetMobileDataEnabledMethod;

    static {
        try {
            Class<?>[] arrayOfClass = new Class[] { boolean.class };
            sGetMobileDataEnabledMethod = ConnectivityManager.class.getMethod("getMobileDataEnabled"
            );
            sSetMobileDataEnabledMethod = ConnectivityManager.class.getMethod("setMobileDataEnabled",
                    arrayOfClass);
        } catch (NoSuchMethodException e) {
            if (DEBUG) e.printStackTrace();
            sGetMobileDataEnabledMethod = null;
            sSetMobileDataEnabledMethod = null;
        }
    }

    public static boolean isGetMobileDataSupport() {
        return sGetMobileDataEnabledMethod != null;
    }

    public static boolean isSetMobileDataSupport() {
        return sSetMobileDataEnabledMethod != null;
    }

    public static boolean getMobileDataEnabled(ConnectivityManager obj) {
        if (sGetMobileDataEnabledMethod != null) {
            try {
                Method localMethod = sGetMobileDataEnabledMethod;
                Object[] arrayOfObject = new Object[0];
                Object ret = localMethod.invoke(obj, arrayOfObject);
                return (Boolean) ret;
            } catch (IllegalAccessException localIllegalAccessException) {
                // ignore this, will to the final
            } catch (Exception e) {
                e.printStackTrace();
                // ignore this, will to the final
            }
        }
        // if anything wrong, will be here
        if (DEBUG) Log.e(TAG, "getMobileDataEnabled failure");
        return false;
    }

    public static boolean setMobileDataEnabled(ConnectivityManager obj, boolean state) {
        if (sSetMobileDataEnabledMethod != null) {
            try {
                Method localMethod = sSetMobileDataEnabledMethod;
                Object[] arrayOfObject = new Object[] { state };
                localMethod.invoke(obj, arrayOfObject);
                return true;
            } catch (IllegalAccessException localIllegalAccessException) {
                // ignore this, will to the final
            } catch (Exception e) {
                e.printStackTrace();
                // ignore this, will to the final
            }
        }
        // if anything wrong, will be here
        if (DEBUG) Log.e(TAG, "setMobileDataEnabled failure");
        return false;
    }
}