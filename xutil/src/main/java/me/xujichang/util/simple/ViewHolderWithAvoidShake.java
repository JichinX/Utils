package me.xujichang.util.simple;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.xujichang.util.simple.interfaces.XOnClickListener;

/**
 * des:
 *
 * @author xjc by 2017/11/17 14:26 .
 */

public class ViewHolderWithAvoidShake extends RecyclerView.ViewHolder {
    private SimpleAvoidShake mSimpleAvoidShake;

    public ViewHolderWithAvoidShake(View itemView) {
        super(itemView);
        mSimpleAvoidShake = new SimpleAvoidShake();
    }


    public <T extends View> void proxyClickListener(long seconds, T view, XOnClickListener<T> listener) {
        mSimpleAvoidShake.proxyClickListener(seconds, view, listener);
    }

    public <T extends View> void proxyClickListener(T view, XOnClickListener<T> listener) {
        mSimpleAvoidShake.proxyClickListener(view, listener);
    }
}
