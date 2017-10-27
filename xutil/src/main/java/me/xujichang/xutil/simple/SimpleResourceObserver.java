package me.xujichang.xutil.simple;


import io.reactivex.observers.ResourceObserver;
import me.xujichang.xutil.base.ViewBaseListener;
import me.xujichang.xutil.tool.StringTool;

/**
 * Created by xjc on 2017/6/13.
 */

public abstract class SimpleResourceObserver<T> extends ResourceObserver<T> {

    protected ViewBaseListener listener;

    public SimpleResourceObserver(ViewBaseListener listener) {
        this.listener = listener;
    }

    @Override
    public void onError(Throwable e) {
        if (null != listener) {
            listener.loadingError(StringTool.getErrorMsg(e));
            onComplete();
        }
    }

    @Override
    public void onComplete() {
        if (null != listener) {
            listener.loadingComplete();
        }
    }
}
