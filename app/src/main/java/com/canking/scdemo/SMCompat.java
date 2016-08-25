package com.canking.scdemo;

import android.os.IBinder;

import java.lang.reflect.Method;

public class SMCompat {
    private static final String CLASSNAME_SERVICE_MANAGER = "android.os.ServiceManager";

    private static Class<?> sServiceManagerClass;
    private static Method sGetServiceMethod;
    private static Method sCheckServiceMethod;
    private static Method sAddServiceMethod;
    private static Method sListServicesMethod;

    static {
        try {
            sServiceManagerClass = Class.forName(CLASSNAME_SERVICE_MANAGER, false, Thread.currentThread().getContextClassLoader());
            sGetServiceMethod = sServiceManagerClass.getMethod("getService", String.class);
            sCheckServiceMethod = sServiceManagerClass.getMethod("checkService", String.class);
            sAddServiceMethod = sServiceManagerClass.getMethod("addService", String.class, IBinder.class);
            sListServicesMethod = sServiceManagerClass.getMethod("listServices");
        } catch (ClassNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static IBinder getService(Object name) {
        if (sGetServiceMethod != null) {
            try {
                Method localMethod = sGetServiceMethod;
                Object[] arrayOfObject = new Object[]{name};
                Object ret = localMethod.invoke(null, arrayOfObject);
                return (IBinder) ret;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static IBinder checkService(Object name) {
        if (sCheckServiceMethod != null) {
            try {
                Method localMethod = sCheckServiceMethod;
                Object[] arrayOfObject = new Object[]{name};
                Object ret = localMethod.invoke(null, arrayOfObject);
                return (IBinder) ret;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void addService(String name, IBinder service) {
        if (sAddServiceMethod != null) {
            try {
                Method localMethod = sAddServiceMethod;
                Object[] arrayOfObject = new Object[]{name, service};
                localMethod.invoke(null, arrayOfObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String[] listServices() {
        if (sListServicesMethod != null) {
            try {
                Method localMethod = sListServicesMethod;
                return (String[]) localMethod.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
