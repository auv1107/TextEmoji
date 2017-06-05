package com.sctdroid.app.textemoji.discovery;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.sctdroid.app.textemoji.data.GifResponse;
import com.sctdroid.app.textemoji.data.request.SooGifQueryFilter;
import com.sctdroid.app.textemoji.data.request.TenorGifQueryFilter;
import com.sctdroid.app.textemoji.data.source.GifRepository;
import com.sctdroid.app.textemoji.data.source.GifsLoader;
import com.sctdroid.app.textemoji.utils.Constants;

/**
 * Created by lixindong on 5/25/17.
 */

public class GifPresenter implements GifContract.Presenter, LoaderManager.LoaderCallbacks<GifResponse> {
    private final GifContract.View mGifView;
    private final GifRepository mGifRepository;
    private final LoaderManager mLoaderManager;
    private final GifsLoader mGifLoader;

    private final int QUERY_GIF = 7;

    private final int DEFAULT_PAGE_SIZE = 20;
    private final int INITIAL_PAGE = 0;

    private GifResponse mGifResponse;

    public GifPresenter(GifContract.View view, LoaderManager loaderManager, GifsLoader loader, GifRepository repository) {
        mGifView = view;
        mLoaderManager = loaderManager;
        mGifLoader = loader;
        mGifRepository = repository;

        mGifView.setPresenter(this);
    }

    @Override
    public void create() {
        mLoaderManager.initLoader(QUERY_GIF, null, this);
    }

    @Override
    public void afterViewInited() {
        mGifView.updateGifSource(mGifSource);
    }

    @Override
    public void start() {

    }

    @Override
    public void query(String keyword) {
        if (mGifSource == Constants.GIF_SORUCE_SOOGIF) {
            mGifLoader.setQueryFilter(new SooGifQueryFilter(keyword, INITIAL_PAGE, DEFAULT_PAGE_SIZE));
        } else {
            mGifLoader.setQueryFilter(new TenorGifQueryFilter(keyword, "", "" + DEFAULT_PAGE_SIZE));
        }
        mGifLoader.forceLoad();
    }

    @Override
    public void queryNext(String keyword) {
        if (mGifResponse != null) {
            mGifLoader.setQueryFilter(mGifResponse.buildLoadMoreQueryFilter());
            mGifLoader.forceLoad();
        }
    }

    @Override
    public boolean hasMore() {
        return mGifResponse != null && mGifResponse.hasMore();
    }

    @Override
    public Loader<GifResponse> onCreateLoader(int id, Bundle args) {
        return mGifLoader;
    }

    @Override
    public void onLoadFinished(Loader<GifResponse> loader, GifResponse response) {
        mGifResponse = response;

        if (!response.isNewest()) {
            if (response.getData().size() > 0) {
                mGifView.showMore(response.getData());
            } else {
                mGifView.showNoMore();
            }
        } else {
            if (response.getData().size() > 0) {
                mGifView.showGifs(response.getData());
            } else {
                mGifView.showNoData();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<GifResponse> loader) {

    }

    private int mGifSource = 0;
    public void setGifSource(int source) {
        mGifSource = source;
    }
}
