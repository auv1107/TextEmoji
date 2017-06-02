package com.sctdroid.app.textemoji.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.sctdroid.app.textemoji.data.GifResponse;
import com.sctdroid.app.textemoji.data.QueryFilter;

/**
 * Created by lixindong on 5/10/17.
 */

public class GifsLoader extends AsyncTaskLoader<GifResponse> implements GifRepository.GifRepositoryObserver {
    private final GifRepository mRepository;

    public GifsLoader(Context context, @NonNull GifRepository repository) {
        super(context);
        mRepository = repository;
    }

    @Override
    public GifResponse loadInBackground() {
        return mRepository.getGifs(mQueryFilter);
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mRepository.removeContentObserver(this);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onStartLoading() {
        mRepository.addContentObserver(this);
    }

    @Override
    public void onGifChanged() {
        if (isStarted()) {
            forceLoad();
        }
    }

    private QueryFilter mQueryFilter;

    public void setQueryFilter(@NonNull QueryFilter filter) {
        mQueryFilter = filter;
    }
    public QueryFilter getQueryFilter() {
        return mQueryFilter;
    }
}
