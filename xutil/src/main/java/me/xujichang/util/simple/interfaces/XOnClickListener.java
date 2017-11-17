package me.xujichang.util.simple.interfaces;

import android.view.View;

/**
 * des:
 *
 * @author xjc by 2017/11/17 14:30 .
 */

public interface XOnClickListener<T extends View> {
    void onClick(T view);
}
