package com.phychan.mylibrary.base;

import android.app.Application;
import android.content.Context;

import com.phychan.mylibrary.BuildConfig;

import java.lang.reflect.Field;


public abstract class BaseApplication extends Application {
    /**
     * 测试标识，正式打包设为false
     */
    public static boolean DEBUG = BuildConfig.DEBUG;

    private static BaseApplication instance;

    private static int version;
    private static String versionName;

    public static int statusHeaigh;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        try {
            DEBUG = (boolean) getBuildConfigValue(this, "DEBUG");
        } catch (Exception e) {
            e.printStackTrace();
        }

        version = 1;
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
        }
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
        }

    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public static int getVersion() {
        return version;
    }

    public static String getVersionName() {
        return versionName;
    }

    public static int getId(String name, String type) {
        return instance.getResources().getIdentifier(name, type, instance.getPackageName());
    }

    public static Object getBuildConfigValue(Context context, String fieldName) {
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void logout();
}
