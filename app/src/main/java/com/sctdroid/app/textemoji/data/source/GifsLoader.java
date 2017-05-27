package com.sctdroid.app.textemoji.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.sctdroid.app.textemoji.data.bean.GifResponse;

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
        if (mQueryFilter != null && !TextUtils.isEmpty(mQueryFilter.tag)) {
            return mRepository.getGifs(mQueryFilter.tag, mQueryFilter.pageNumber, mQueryFilter.pageSize);
        } else {
            return GifResponse.NULL;
        }
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

    public static class QueryFilter {
        public final String tag;
        public final int pageNumber;
        public final int pageSize;

        public QueryFilter(String tag, int pageNumber, int pageSize) {
            this.tag = tag;
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
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
