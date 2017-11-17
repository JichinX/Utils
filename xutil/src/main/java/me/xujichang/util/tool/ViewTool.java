package me.xujichang.util.tool;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import io.reactivex.disposables.CompositeDisposable;
import me.xujichang.util.simple.SimpleAvoidShake;

/**
 * @author xjc
 *         Created by xjc on 2017/8/4。
 */

public class ViewTool extends SimpleAvoidShake {

    private static ViewTool instance;
    private Lifecycle mLifecycle;
    private CompositeDisposable mDisposable;

    private ViewTool() {

    }

    protected ViewTool(String pS) {

    }

    private static class ClassHolder {

        public static ViewTool mViewTool = new ViewTool();
    }

    public static ViewTool getInstance(Lifecycle lifecycle) {
        if (null == instance) {
            instance = ClassHolder.mViewTool;
        }
        instance.init(lifecycle);
        return instance;
    }

    public static ViewTool getInstance(LifecycleOwner lifecycleOwner) {
        return getInstance(lifecycleOwner.getLifecycle());
    }

    private void init(Lifecycle lifecycle) {
        clear();
        mDisposable = new CompositeDisposable();
        mLifecycle = lifecycle;
        LogTool.d("init");
        mLifecycle.addObserver(new XSimpleLifeCycleObserver() {
            @Override
            public void onDestroy() {
                mDisposable.dispose();
                LogTool.d("dispose");
            }
        });
    }

    private void clear() {
        mLifecycle = null;
        mDisposable = null;
    }

}
