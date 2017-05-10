package com.sctdroid.app.textemoji.data.source;

import com.sctdroid.app.textemoji.data.bean.Gif;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixindong on 5/10/17.
 */

public class GifRepository implements GifDataSource {
    @Override
    public List<Gif> getGifs(String tag) {
        return mRemoteDataSource.getGifs(tag);
    }

    private static GifRepository INSTANCE;
    private final GifDataSource mLocalDataSource;
    private final GifDataSource mRemoteDataSource;

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
