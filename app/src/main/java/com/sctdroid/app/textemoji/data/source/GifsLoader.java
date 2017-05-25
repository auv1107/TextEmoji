package com.sctdroid.app.textemoji.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.sctdroid.app.textemoji.data.bean.Gif;

import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 5/10/17.
 */

public class GifsLoader extends AsyncTaskLoader<List<Gif>> implements GifRepository.GifRepositoryObserver {
    private final GifRepository mRepository;

    public GifsLoader(Context context, @NonNull GifRepository repository) {
        super(context);
        mRepository = repository;
    }

    @Override
    public List<Gif> loadInBackground() {
        if (mQueryFilter != null && !TextUtils.isEmpty(mQueryFilter.tag)) {
            return mRepository.getGifs(mQueryFilter.tag);
        } else {
            return Collections.emptyList();
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

        public QueryFilter(String tag) {
            this.tag = tag;
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
