package me.xujichang.xutil.simple;


import me.xujichang.xutil.base.ViewBaseListener;
import me.xujichang.xutil.tool.StringTool;

/**
 * Created by xjc on 2017/6/14.
 */

public abstract class SilentResourceObserver<T> extends SimpleResourceObserver<T> {

    public SilentResourceObserver(ViewBaseListener listener) {
        super(listener);
    }

    @Override
    public void onError(Throwable e) {
        if (null != listener) {
            listener.loadingError(StringTool.getErrorMsg(e));
        }
    }

    @Override
    public void onComplete() {
    }
}
