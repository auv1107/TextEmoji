package com.sctdroid.app.textemoji.discovery;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.data.source.GifRepository;
import com.sctdroid.app.textemoji.data.source.GifsLoader;

import java.util.List;

/**
 * Created by lixindong on 5/25/17.
 */

public class GifPresenter implements GifContract.Presenter, LoaderManager.LoaderCallbacks<List<Gif>> {
    private final GifContract.View mGifView;
    private final GifRepository mGifRepository;
    private final LoaderManager mLoaderManager;
    private final GifsLoader mGifLoader;

    private final int QUERY_GIF = 7;

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
        mGifLoader.setQueryFilter(new GifsLoader.QueryFilter(keyword));
        mGifLoader.forceLoad();
    }

    @Override
    public Loader<List<Gif>> onCreateLoader(int id, Bundle args) {
        return mGifLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Gif>> loader, List<Gif> data) {
        mGifView.showGifs(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Gif>> loader) {

    }
}
