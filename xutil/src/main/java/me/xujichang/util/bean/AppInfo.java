package me.xujichang.util.bean;

import android.content.SharedPreferences;

/**
 * Created by xjc on 2017/6/10.
 */

public class AppInfo {

    private String appName;
    private String verCode;
    private String packageName;
    private String content;
    private String verName;
    private String appFileName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getAppFileName() {
        return appFileName;
    }

    public void setAppFileName(String appFileName) {
        this.appFileName = appFileName;
    }

    /**
     * 插入到Sp
     */
    public void saveToSp(SharedPreferences.Editor editor) {
        editor.putString("appName", appName);
        editor.putString("verCode", verCode);
        editor.putString("packageName", packageName);
        editor.putString("content", content);
        editor.putString("verName", verName);
        editor.putString("appFileName", appFileName);
    }

    /**
     * 从SP取
     */
    public void initFromSp(SharedPreferences preferences) {
        appName = preferences.getString("appName", "");
        verCode = preferences.getString("verCode", "");
        packageName = preferences.getString("packageName", "");
        content = preferences.getString("content", "");
        verName = preferences.getString("verName", "");
        appFileName = preferences.getString("appFileName", "");
    }
}
