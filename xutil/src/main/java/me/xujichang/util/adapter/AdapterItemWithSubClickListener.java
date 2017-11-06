package me.xujichang.util.adapter;

/**
 * 包含子选项的监听事件
 * @author xjc
 * Created by xjc on 2017/5/31.
 */

public interface AdapterItemWithSubClickListener<T, V> extends AdapterCommonListener<T> {
    /**
     * 子选项打开的位置
     *
     * @param position
     */
    void onSubOpen(int position);

    /**
     * 子选项的点击事件
     *
     * @param subSource
     */
    void onSubClick(V subSource);
}
