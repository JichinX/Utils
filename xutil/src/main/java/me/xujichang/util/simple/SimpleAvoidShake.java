package me.xujichang.util.simple;

import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import me.xujichang.util.simple.abs.AbstractLifeCycleObserver;
import me.xujichang.util.simple.interfaces.AvoidShake;

/**
 * des:
 *
 * @author xjc by 2017/11/17 14:40 .
 */

public class SimpleAvoidShake implements AvoidShake {
    @Deprecated
    public <T extends View> void proxyClickListener(final T view, final XOnClickListener<T> listener, long seconds) {
        proxyClickListener(seconds, view, listener);
    }

    public <T extends View> void proxyClickListener(final T view,
                                                    final XOnClickListener<T> listener) {
        proxyClickListener(view, listener, 500);
    }

    /**
     * 通过将View的点击事件进行转接 配合Rxjava实现View的防抖操作
     */
    @Override
    public <T extends View> void proxyClickListener(long seconds, T view, final me.xujichang.util.simple.interfaces.XOnClickListener<T> listener) {
        XObservableOnSubscribe<T> subscribe = new XObservableOnSubscribe<T>(view) {
            @Override
            protected void subscribe(final ObservableEmitter<T> e, final T view) {
                view.setOnClickListener(new View.OnClickListener() {
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
    @Deprecated
    public interface XOnClickListener<T extends View> extends me.xujichang.util.simple.interfaces.XOnClickListener<T> {
    }

    @Deprecated
    public static class XSimpleLifeCycleObserver extends SimpleLifeCycleObserver {
    }

    /**
     * 将Lifecycle的生命周期事件
     * 转换成方法  方便使用
     */
    @Deprecated
    public static abstract class XLifeCycleObserver extends AbstractLifeCycleObserver {

    }
}
