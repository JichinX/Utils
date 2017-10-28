package me.xujichang.util.base;

/**
 * Created by xjc on 2017/5/31.
 */

public interface ViewBaseListener {
    void startLoading(String msg);

    void stopLoading();

    void showToast(String msg);

    void loadingError(String msg);

    void loadingComplete();

}
