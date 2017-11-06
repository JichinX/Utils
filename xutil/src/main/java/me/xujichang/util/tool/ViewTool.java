package me.xujichang.util.tool;


import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;

import java.util.concurrent.TimeUnit;

import me.xujichang.util.simple.SimpleObserver;

/**
 * @author xjc
 *         Created by xjc on 2017/8/4。
 */

public class ViewTool {

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

    /**
     * 通过将View的点击事件进行转接 配合Rxjava实现View的防抖操作
     */
    public <T extends View> void proxyClickListener(final T view,
                                                    final XOnClickListener<T> listener,
                                                    long seconds) {
        XObservableOnSubscribe<T> subscribe = new XObservableOnSubscribe<T>(view) {
            @Override
            protected void subscribe(final ObservableEmitter<T> e, final T view) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        e.onNext(view);
                    }
                });
            }
        };
        SimpleObserver<T> observer = new SimpleObserver<T>() {
            @Override
            public void onNext(@NonNull T t) {
                listener.onClick(t);
            }
        };
        Observable
                .create(subscribe)
                .throttleFirst(seconds, TimeUnit.MILLISECONDS)
                .subscribe(observer);
    }

    public <T extends View> void proxyClickListener(final T view,
                                                    final XOnClickListener<T> listener) {
        proxyClickListener(view, listener, 500);
    }

    abstract static class XObservableOnSubscribe<T extends View>
            implements ObservableOnSubscribe<T> {

        private T view;

        XObservableOnSubscribe(T view) {
            this.view = view;
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
            subscribe(e, view);
        }

        /**
         * 订阅事件
         *
         * @param e
         * @param view
         */
        protected abstract void subscribe(ObservableEmitter<T> e, T view);

    }

    /**
     * @param <T>
     */
    public interface XOnClickListener<T extends View> {
        /**
         * 点击事件
         *
         * @param view
         */
        void onClick(T view);
    }

    public static class XSimpleLifeCycleObserver extends XLifeCycleObserver {

        @Override
        public void onStart() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public void onChange() {

        }
    }

    /**
     * 将Lifecycle的生命周期事件
     * 转换成方法  方便使用
     */
    public static abstract class XLifeCycleObserver implements LifecycleObserver {
        /**
         * 对应Activity的生命周期{@link Activity#onStart()}
         */
        @OnLifecycleEvent(Event.ON_START)
        public abstract void onStart();

        /**
         * 对应Activity的生命周期{@link Activity#onStart()}
         */
        @OnLifecycleEvent(Event.ON_STOP)
        public abstract void onStop();

        /**
         * 对应Activity的生命周期{@link Activity#onResume()} ()}
         */
        @OnLifecycleEvent(Event.ON_RESUME)
        public abstract void onResume();

        /**
         * 对应Activity的生命周期{@link Activity#onPause()} ()}
         */
        @OnLifecycleEvent(Event.ON_PAUSE)
        public abstract void onPause();

        /**
         * 对应Activity的生命周期{@link Activity#onCreate(Bundle)} ()}
         */
        @OnLifecycleEvent(Event.ON_CREATE)
        public abstract void onCreate();

        /**
         * 对应Activity的生命周期{@link Activity#onDestroy()} ()}
         */
        @OnLifecycleEvent(Event.ON_DESTROY)
        public abstract void onDestroy();

        /**
         * 对应Activity的生命周期的每个变化都会响应
         */
        @OnLifecycleEvent(Event.ON_ANY)
        public abstract void onChange();
    }
}
