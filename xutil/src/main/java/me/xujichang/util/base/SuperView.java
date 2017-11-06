package me.xujichang.util.base;

/**
 * @author xjc
 * Created by xjc on 2017/5/23.
 */

public interface SuperView<T> extends ViewBaseListener {
    /**
     * 绑定Presenter
     *
     * @param presenter Presenter对象
     */
    void setPresenter(T presenter);

    /**
     * 初始化布局，建议在{@link SuperPresenter#start()}方法中执行
     */
    void initView();
}
