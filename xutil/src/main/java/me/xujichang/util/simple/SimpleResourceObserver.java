package me.xujichang.util.simple;


import io.reactivex.observers.ResourceObserver;
import me.xujichang.util.base.ViewBaseListener;
import me.xujichang.util.tool.StringTool;

/**
 * @author xjc
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
