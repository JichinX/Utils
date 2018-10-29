package me.xujichang.util.system;

import java.lang.reflect.Method;

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
}
