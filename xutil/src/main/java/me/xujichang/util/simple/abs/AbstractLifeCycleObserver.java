package me.xujichang.util.simple.abs;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;

/**
 * des:
 *
 * @author xjc by 2017/11/17 14:33 .
 */

public abstract class AbstractLifeCycleObserver implements LifecycleObserver {
    /**
     * 对应Activity的生命周期{@link Activity#onStart()}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public abstract void onStart();

    /**
     * 对应Activity的生命周期{@link Activity#onStart()}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public abstract void onStop();

    /**
     * 对应Activity的生命周期{@link Activity#onResume()} ()}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public abstract void onResume();

    /**
     * 对应Activity的生命周期{@link Activity#onPause()} ()}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public abstract void onPause();

    /**
     * 对应Activity的生命周期{@link Activity#onCreate(Bundle)} ()}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public abstract void onCreate();

    /**
     * 对应Activity的生命周期{@link Activity#onDestroy()} ()}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public abstract void onDestroy();

    /**
     * 对应Activity的生命周期的每个变化都会响应
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public abstract void onChange();
}
