package me.xujichang.util.system;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.google.common.base.Strings;

import java.lang.reflect.Method;

import me.xujichang.util.thirdparty.DeviceUtils;
import me.xujichang.util.tool.LogTool;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/12-下午3:30
 */
public class SystemInfo {

    public static String getAndroidOsSystemProperties(String key) {
        String ret = "";
        try {
            Class<?> kclass = Class.forName("android.os.SystemProperties");
            Method method = kclass.getMethod("get", String.class);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            ret = (String) method.invoke(kclass, key);
            ret = ret.split(" ")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == ret) {
            ret = "";
        }
        return ret;
    }

    public static String getSn(Context context) {
        //常规
        //ro.serialno
        //ro.boot.serialno
        //特有
        //gsm.serial
        String[] propertys = {"gsm.serial", "ro.boot.serialno", "ro.serialno",};

        String sn = null;
        for (int i = 0; i < propertys.length; i++) {
            sn = getAndroidOsSystemProperties(propertys[i]);
            if (!Strings.isNullOrEmpty(sn)) {
                break;
            }
        }
        if (Strings.isNullOrEmpty(sn)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    sn = getOldSn();
                } else {
                    sn = Build.getSerial();
                }
            } else {
                sn = Build.SERIAL;
            }
        }
        if (Strings.isNullOrEmpty(sn)) {
            sn = getOldSn();
        }
        LogTool.d("sn:" + sn);
        return sn;
    }

    public static String getOldSn() {
        LogTool.d("oldSn:" + DeviceUtils.getAndroidID());
        return DeviceUtils.getAndroidID();
    }
}
