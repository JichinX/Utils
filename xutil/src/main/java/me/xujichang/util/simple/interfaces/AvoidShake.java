package me.xujichang.util.simple.interfaces;

import android.view.View;

/**
 * des:
 *
 * @author xjc by 2017/11/17 14:22 .
 */

public interface AvoidShake {
    <T extends View> void proxyClickListener(long seconds, T view, XOnClickListener<T> listener);
}
