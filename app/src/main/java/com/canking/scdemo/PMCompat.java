/*
 * Copyright (C) 2012 Tapas Mobile Ltd.  All Rights Reserved.
 */

package com.canking.scdemo;

import android.os.IBinder;

import java.lang.reflect.Method;

public class PMCompat {
    private static final String CLASSNAME_IPOWERMANAGER = "android.os.IPowerManager";
    private static final String CLASSNAME_IPOWERMANAGER_STUB = "android.os.IPowerManager$Stub";

    private static Class<?> sIPowerManagerClass;
    private static Method sAsInterfaceMethod;
    private static Method sSetBacklightBrightnessMethod;
    private static Method sRebootMethod;
    private static Object sPowerManager;

    static {
        try {
            Class<?> clazz = Class.forName(CLASSNAME_IPOWERMANAGER_STUB);
            sIPowerManagerClass = Class.forName(CLASSNAME_IPOWERMANAGER);
            sAsInterfaceMethod = clazz.getMethod("asInterface", IBinder.class);
            sSetBacklightBrightnessMethod = sIPowerManagerClass.getMethod("setBacklightBrightness",
                    int.class);
            sRebootMethod = sIPowerManagerClass.getMethod("reboot",
                    String.class);
        } catch (Exception e) {
            sAsInterfaceMethod = null;
            sIPowerManagerClass = null;
            sSetBacklightBrightnessMethod = null;
            sRebootMethod = null;
        }
    }

    public static void reboot(Object obj, String reason) {
        if (obj != null && sRebootMethod != null) {
            try {
                Method localMethod = sRebootMethod;
                Object[] arrayOfObject = new Object[]{reason};
                localMethod.invoke(obj, arrayOfObject);
            } catch (Exception e) {
                e.printStackTrace();
                // ignore this, will to the final
            }
        }
        // if anything wrong, will be here
    }

    public static boolean setBacklightBrightness(Object obj, int brightness) {
        if (obj != null && sSetBacklightBrightnessMethod != null) {
            try {
                Method localMethod = sSetBacklightBrightnessMethod;
                Object[] arrayOfObject = new Object[]{brightness};
                localMethod.invoke(obj, arrayOfObject);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // if anything wrong, will be here
        return false;
    }

    public static Object asInterface(IBinder binder) {
        if (sPowerManager != null) return sPowerManager;
        if (sAsInterfaceMethod != null) {
            try {
                Method localMethod = sAsInterfaceMethod;
                Object[] arrayOfObject = new Object[]{binder};
                sPowerManager = localMethod.invoke(null, arrayOfObject);
                return sPowerManager;
            } catch (IllegalAccessException localIllegalAccessException) {
                // ignore this, will to the final
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}