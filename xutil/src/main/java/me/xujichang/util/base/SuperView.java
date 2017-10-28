package me.xujichang.util.base;

/**
 * Created by xjc on 2017/5/23.
 */

public interface SuperView<T> extends ViewBaseListener {
    void setPresenter(T presenter);

    void initView();
}
