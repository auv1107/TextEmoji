package com.sctdroid.app.textemoji.data.source;

import com.sctdroid.app.textemoji.data.GifResponse;
import com.sctdroid.app.textemoji.data.QueryFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixindong on 5/10/17.
 */

public class GifRepository implements GifDataSource {
    @Override
    public GifResponse getGifs(String tag) {
        return mRemoteDataSource.getGifs(tag);
    }

    @Override
    public GifResponse getGifs(String tag, int pageNumber, int pageSize) {
        return mRemoteDataSource.getGifs(tag, pageNumber, pageSize);
    }

    @Override
    public GifResponse getGifs(QueryFilter filter) {
        return mRemoteDataSource.getGifs(filter);
    }

    private static GifRepository INSTANCE;
    private GifDataSource mLocalDataSource;
    private GifDataSource mRemoteDataSource;

    private List<GifRepository.GifRepositoryObserver> mObservers = new ArrayList<>();

    public static synchronized GifRepository getInstance(GifDataSource localDataSource,
                                                           GifDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new GifRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private GifRepository(GifDataSource localDataSource,
                            GifDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    public void updateRemoteDataSource(GifDataSource dataSource) {
        mRemoteDataSource = dataSource;
    }

    public void addContentObserver(GifRepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(GifRepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (GifRepositoryObserver observer : mObservers) {
            observer.onGifChanged();
        }
    }

    @Override
    public void refreshGifs() {
        notifyContentObserver();
    }

    public interface GifRepositoryObserver {

        void onGifChanged();

    }

}
