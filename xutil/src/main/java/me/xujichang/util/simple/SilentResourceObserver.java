package me.xujichang.util.simple;


import me.xujichang.util.base.ViewBaseListener;
import me.xujichang.util.tool.StringTool;

/**
 * @author xjc
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
