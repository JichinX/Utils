package me.xujichang.util.simple;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xjc on 2017/6/14.
 */

public abstract class SimpleViewHolder<T> extends RecyclerView.ViewHolder {

    public SimpleViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void bindData(T data);
}
