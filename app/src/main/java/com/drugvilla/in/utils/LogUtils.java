package com.drugvilla.in.utils;

import android.util.Log;

import com.drugvilla.in.BuildConfig;


/**
 * this class is used to manage logs all over the project.
 */
public class LogUtils {

    private static boolean DEBUG = !BuildConfig.DEBUG;

    public static void errorLog(String TAG, String message) {
        if (DEBUG) {
            LogUtils.errorLog(TAG, message);
        }
    }

    public static void debugLog(String TAG, String message) {
        if (DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static void warnLog(String TAG, String message) {
        if (DEBUG) {
            Log.w(TAG, message);
        }
    }

    public static void verboseLog(String TAG, String message) {
        if (DEBUG) {
            Log.v(TAG, message);
        }
    }

    public static void infoLog(String TAG, String message) {
        if (DEBUG) {
            Log.i(TAG, message);
        }
    }
}
