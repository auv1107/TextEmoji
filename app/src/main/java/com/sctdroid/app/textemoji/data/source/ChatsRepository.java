package com.sctdroid.app.textemoji.data.source;

import com.sctdroid.app.textemoji.data.bean.ChatItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixindong on 4/18/17.
 */

public class ChatsRepository implements ChatsDataSource {
    private static ChatsRepository INSTANCE;
    private ChatsRepository mRemoteDataSource;
    private ChatsDataSource mLocalDataSource;
    private List<ChatsRepositoryObserver> mObservers = new ArrayList<>();


    private ChatsRepository(ChatsDataSource localDataSource, ChatsRepository remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    public static ChatsRepository getInstance(ChatsDataSource localDataSource,
                                              ChatsRepository remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ChatsRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public List<ChatItem> getChats() {
        return mLocalDataSource.getChats();
    }

    @Override
    public void saveChats(List<ChatItem> items) {
        mLocalDataSource.saveChats(items);
        notifyContentObserver();
    }

    @Override
    public void appendChat(ChatItem item) {
        mLocalDataSource.appendChat(item);
        notifyContentObserver();
    }

    @Override
    public void removeChat(int position) {
        mLocalDataSource.removeChat(position);
        notifyContentObserver();
    }

    public void addContentObserver(ChatsRepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(ChatsRepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (ChatsRepositoryObserver observer : mObservers) {
            observer.onChatsChanged();
        }
    }

    public interface ChatsRepositoryObserver {

        void onChatsChanged();

    }

}
