package me.xujichang.util.base;

/**
 * @author xjc
 * Created by xjc on 2017/5/23.
 */

public interface SuperPresenter {
    /**
     * start时候的回调
     */
    void start();

    /**
     * destroy时的回调
     */
    void destroy();
}
