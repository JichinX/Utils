package me.xujichang.util.tool;

import android.util.Log;

/**
 * Created by huangjun on 2016/10/14.
 */

public class LogTool {

    private static final String TAG = "_LOG_";

    private static final String LOG_PREFIX = "=== ";

    private static final String LOG_SUFFIX = " ===";

    public static void i(String msg) {
        i(TAG, wrap(msg));
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void d(String msg) {
        d(TAG, wrap(msg));
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    private static final String wrap(String msg) {
        return LOG_PREFIX + msg + LOG_SUFFIX;
    }
}
