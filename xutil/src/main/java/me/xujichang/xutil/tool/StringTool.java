package me.xujichang.xutil.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by xjc on 2017/5/24.
 */

public class StringTool {

    /**
     * 处理可能为空的字符串
     */
    public static String dealIfNull(String str) {
        return null == str ? "" : str;
    }

    public static void checkNull(String str, String name) {
        if (TextUtils.isEmpty(str)) {
            Throwable throwable = new Throwable("属性：（" + name + "）不能为 Empty");
            throw new RuntimeException(throwable);
        }
    }

    public static String parseUrl(String dir, String s) {
        checkNothing(dir);
        checkNothing(s);
        return new StringBuilder(dir).append(s).toString();
    }

    private static void checkNotNull(Object o) {
        if (null == o) {
            Throwable throwable = new Throwable("对象" + o.getClass().getSimpleName() + "不能为null");
            throw new RuntimeException(throwable);
        }
    }

    private static void checkNothing(String o) {
        checkNotNull(o);

        if (TextUtils.isEmpty(o)) {
            Throwable throwable = new Throwable("此处 字符串不能为 \"\"");
            throw new RuntimeException(throwable);
        }
    }

    public static String getErrorMsg(Throwable e) {
        if (null == e) {
            return "";
        }
        String msg = e.getMessage();
        if (TextUtils.isEmpty(msg) && null != e.getCause()) {
            msg = e.getCause().getMessage();
        }
        if (TextUtils.isEmpty(msg)) {
            msg = e.getClass().getSimpleName();
        }
        return msg;
    }

    /**
     * @param query
     */
    public static Map<String, String> parseQuery2Map(String query) {
        Map<String, String> queryMap = new HashMap<>();
        String[] strings = query.split("&");
        for (String string : strings) {
            String[] sub = string.split("=");
            if (sub.length > 1) {
                queryMap.put(sub[0], sub[1]);
            }
        }
        return queryMap;
    }

    public static String getMD5(String val) throws NoSuchAlgorithmException {
        // MessageDigest专门用于加密的类
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] result = messageDigest.digest(val.getBytes()); // 得到加密后的字符组数

            StringBuffer sb = new StringBuffer();

            for (byte b : result) {
                int num = b & 0xff; // 这里的是为了将原本是byte型的数向上提升为int型，从而使得原本的负数转为了正数
                String hex = Integer.toHexString(num); //这里将int型的数直接转换成16进制表示
                //16进制可能是为1的长度，这种情况下，需要在前面补0，
                if (hex.length() == 1) {
                    sb.append(0);
                }
                sb.append(hex);
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String deleteString(String ssdz, String s) {
        if (TextUtils.isEmpty(ssdz)) {
            return "";
        }
        return ssdz.replaceAll(s, "");
    }

    public static String getGender(int xb) {
        String XB = "未知";
        switch (xb) {
            case 1:
                XB = "男";
                break;
            case 2:
                XB = "女";
                break;
            default:
                XB = "未知性别";
        }
        return XB;
    }

    public static String convertArray2String(ArrayList<Integer> houseIds) {
        int size = houseIds.size();
        if (null == houseIds || houseIds.size() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(houseIds.get(0)));
        if (size > 1) {
            for (int i = 1; i < size; i++) {
                stringBuilder.append(",").append(houseIds.get(i));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * bitmap转为base64
     */
    public static String bitmapToBase64(Bitmap bitmap, int quality) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        return bitmapToBase64(bitmap, 80);
    }

    /**
     * base64转为bitmap
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        if (null == base64Data) {
            return null;
        }
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static String formatTime(long time) {

        return formatTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatTime(long time, String pattern) {
        if (time == 0L) {
            return "未知";
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        String date = sDateFormat.format(new java.util.Date(time));
        return date;
    }

}
