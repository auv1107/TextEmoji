package com.sctdroid.app.textemoji.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.sctdroid.app.textemoji.data.bean.ChatItem;
import com.sctdroid.app.textemoji.utils.ObservableLoader;

import java.util.List;

/**
 * Created by lixindong on 4/14/17.
 */

public class ChatsLoader extends ObservableLoader<List<ChatItem>> implements ChatsRepository.ChatsRepositoryObserver {
    private final ChatsRepository mRepository;

    public ChatsLoader(Context context, @NonNull ChatsRepository repository) {
        super(context);
        mRepository = repository;
    }

    @Override
    public List<ChatItem> loadInBackground() {
        return mRepository.getChats();
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
    public void onChatsChanged() {
        if (isStarted()) {
            forceLoad();
        }
    }
}
