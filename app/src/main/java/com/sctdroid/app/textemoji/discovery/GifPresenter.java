package com.sctdroid.app.textemoji.discovery;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.sctdroid.app.textemoji.data.bean.GifResponse;
import com.sctdroid.app.textemoji.data.source.GifRepository;
import com.sctdroid.app.textemoji.data.source.GifsLoader;

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

    private int mCurrentPage = INITIAL_PAGE;
    private int mAllCount = 0;
    private int mPageCount = 1;

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
    public void start() {

    }

    @Override
    public void query(String keyword) {
        mGifLoader.setQueryFilter(new GifsLoader.QueryFilter(keyword, INITIAL_PAGE, DEFAULT_PAGE_SIZE));
        mGifLoader.forceLoad();
    }

    @Override
    public void queryNext(String keyword) {
        mGifLoader.setQueryFilter(new GifsLoader.QueryFilter(keyword, mCurrentPage + 1, DEFAULT_PAGE_SIZE));
        mGifLoader.forceLoad();
    }

    @Override
    public boolean hasMore() {
        return mCurrentPage < mPageCount;
    }

    @Override
    public Loader<GifResponse> onCreateLoader(int id, Bundle args) {
        return mGifLoader;
    }

    @Override
    public void onLoadFinished(Loader<GifResponse> loader, GifResponse data) {
        mCurrentPage = data.getPageNumber();
        mAllCount = data.getAllCount();
        mPageCount = data.getPageCount();

        if (data.getPageNumber() > 1) {
            if (data.getData().size() > 0) {
                mGifView.showMore(data.getData());
            } else {
                mGifView.showNoMore();
            }
        } else {
            if (data.getData().size() > 0) {
                mGifView.showGifs(data.getData());
            } else {
                mGifView.showNoData();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<GifResponse> loader) {

    }
}
