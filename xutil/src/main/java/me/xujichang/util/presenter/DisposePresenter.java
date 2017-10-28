package me.xujichang.util.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 可管理 Rxjava的Observer
 * Created by xjc on 2017/6/13.
 */

public abstract class DisposePresenter {
    protected CompositeDisposable disposables;

    public DisposePresenter() {
        this.disposables = new CompositeDisposable();
    }

    protected void addObserver(Disposable disposable) {
        disposables.add(disposable);
    }

    protected void destroyObservers() {
        disposables.dispose();
    }

}
