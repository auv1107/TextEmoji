package com.sctdroid.app.textemoji.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixindong on 6/5/17.
 */

public abstract class ObservableLoader<D> extends AsyncTaskLoader<D> {
    public ObservableLoader(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(D data) {
        super.deliverResult(data);
        notifyLoadCompleted(this, data);
    }

    @Override
    public void deliverCancellation() {
        super.deliverCancellation();
        notifyLoadCancelled(this);
    }

    public interface LoaderObserver<D> {
        void onLoadCompleted(Loader<D> loader, D data);
        void onLoadCancelled(Loader<D> loader);
    }

    private List<LoaderObserver<D>> mObservers = new ArrayList<>();

    public void addObserver(@NonNull LoaderObserver<D> observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeObserver(@NonNull LoaderObserver<D> observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    public void notifyLoadCompleted(Loader<D> loader, D data) {
        for (LoaderObserver<D> observer : mObservers) {
            observer.onLoadCompleted(loader, data);
        }
    }

    public void notifyLoadCancelled(Loader<D> loader) {
        for (LoaderObserver<D> observer : mObservers) {
            observer.onLoadCancelled(loader);
        }
    }
}
