package me.xujichang.util.adapter;

import android.view.View;

import me.xujichang.util.simple.SimpleViewHolder;


/**
 * Created by xjc on 2017/6/2.
 */

public abstract class MultipleItemTypeViewHolder<T> extends SimpleViewHolder<T> {

    public MultipleItemTypeViewHolder(View itemView) {
        super(itemView);
    }
}
