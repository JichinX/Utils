package me.xujichang.util.base;

/**
 * @author xjc
 * Created by xjc on 2017/5/31.
 */

public interface ViewBaseListener {
    /**
     * 开启Loading
     *
     * @param msg loading时提示
     */
    void startLoading(String msg);

    /**
     * 停止Loading
     */
    void stopLoading();

    /**
     * 显示Toast提示
     *
     * @param msg 提示信息
     */
    void showToast(String msg);

    /**
     * 加载错误
     *
     * @param msg 错误提示信息
     */
    void loadingError(String msg);

    /**
     * 加载完成
     */
    void loadingComplete();

}
