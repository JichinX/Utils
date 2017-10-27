package me.xujichang.xutil.adapter;

/**
 * Created by xjc on 2017/5/31.
 */

public interface AdapterCommonListener<T> {
    /**
     * Item的点击事件
     *
     * @param position
     * @param source
     */
    void onClick(int position, T source);

    /**
     * 加载更多
     *
     * @param position
     * @param hasMore
     */
    void loadMore(int position, boolean hasMore);
}
