package com.sctdroid.app.textemoji.emoji;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.sctdroid.app.textemoji.data.GifResponse;
import com.sctdroid.app.textemoji.data.bean.ChatItem;
import com.sctdroid.app.textemoji.data.bean.EmojiCategory;
import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.data.bean.GifChatItem;
import com.sctdroid.app.textemoji.data.bean.Me;
import com.sctdroid.app.textemoji.data.bean.TextPicItem;
import com.sctdroid.app.textemoji.data.request.SooGifQueryFilter;
import com.sctdroid.app.textemoji.data.request.TenorGifQueryFilter;
import com.sctdroid.app.textemoji.data.source.ChatsLoader;
import com.sctdroid.app.textemoji.data.source.ChatsRepository;
import com.sctdroid.app.textemoji.data.source.EmojiLoader;
import com.sctdroid.app.textemoji.data.source.GifRepository;
import com.sctdroid.app.textemoji.data.source.GifsLoader;
import com.sctdroid.app.textemoji.data.source.MeLoader;

import java.util.List;

/**
 * Created by lixindong on 4/18/17.
 */

public class EmojiPresenter implements EmojiContract.Presenter, LoaderManager.LoaderCallbacks<List<ChatItem>> {
    private final EmojiContract.View mEmojiView;
    private final ChatsLoader mChatLoader;
    private final LoaderManager mLoaderManager;
    private final int CHATS_QUERY = 2;
    private final int ME_QUERY = 3;
    private final int EMOJI_QUERY = 4;
    private final int Gif_QUERY = 5;
    private final ChatsRepository mRepository;
    private final GifRepository mGifRepository;
    private final EmojiContract.ContractManager mContractManager;
    private final MeLoader mMeLoader;
    private final EmojiLoader mEmojiLoader;
    private final GifsLoader mGifsLoader;
    private boolean mIsFirstTimeStart = false;

    public EmojiPresenter(EmojiLoader emojiLoader, MeLoader meLoader, ChatsLoader chatsLoader, GifsLoader gifsLoader, LoaderManager loaderManager, ChatsRepository repository, GifRepository gifRepository, @NonNull EmojiContract.View emojiView, EmojiContract.ContractManager manager) {
        mEmojiLoader = emojiLoader;
        mMeLoader = meLoader;
        mChatLoader = chatsLoader;
        mGifsLoader = gifsLoader;
        mLoaderManager = loaderManager;
        mRepository = repository;
        mGifRepository = gifRepository;
        mContractManager = manager;
        mEmojiView = emojiView;
        mEmojiView.setPresenter(this);
    }

    @Override
    public void create() {
        mLoaderManager.initLoader(EMOJI_QUERY, null, new LoaderManager.LoaderCallbacks<List<EmojiCategory>>() {
            @Override
            public Loader<List<EmojiCategory>> onCreateLoader(int id, Bundle args) {
                return mEmojiLoader;
            }

            @Override
            public void onLoadFinished(Loader<List<EmojiCategory>> loader, List<EmojiCategory> data) {
                mEmojiView.initEmojiBoard(data);
            }

            @Override
            public void onLoaderReset(Loader<List<EmojiCategory>> loader) {

            }
        }).forceLoad();
        mLoaderManager.initLoader(Gif_QUERY, null, new LoaderManager.LoaderCallbacks<GifResponse>() {
            @Override
            public Loader<GifResponse> onCreateLoader(int id, Bundle args) {
                return mGifsLoader;
            }

            @Override
            public void onLoadFinished(Loader<GifResponse> loader, GifResponse data) {
                if (data.getData().size() > 0) {
                    mEmojiView.showGifs(data.getData(), "");
                } else {
                    mEmojiView.clearGifs();
                }
            }

            @Override
            public void onLoaderReset(Loader<GifResponse> loader) {

            }
        });
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(CHATS_QUERY, null, this).forceLoad();
        mLoaderManager.initLoader(ME_QUERY, null, new LoaderManager.LoaderCallbacks<Me>() {
            @Override
            public Loader<Me> onCreateLoader(int id, Bundle args) {
                return mMeLoader;
            }

            @Override
            public void onLoadFinished(Loader<Me> loader, Me data) {
                mEmojiView.updateMe(data);
            }

            @Override
            public void onLoaderReset(Loader<Me> loader) {

            }
        }).forceLoad();
    }

    @Override
    public Loader<List<ChatItem>> onCreateLoader(int id, Bundle args) {
        return mChatLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<ChatItem>> loader, List<ChatItem> data) {
        mEmojiView.showChats(data);
        if (mIsFirstTimeStart) {
            mEmojiView.scrollToTop();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ChatItem>> loader) {

    }

    @Override
    public void processInput(String inputText, int textSize, boolean withShadow) {
        if (TextUtils.isEmpty(inputText)) {
            mEmojiView.showEmptyText();
        } else {
            mRepository.appendChat(new TextPicItem.Builder()
                    .content(inputText)
                    .avatarResId(-1)
                    .textSize(textSize)
                    .withShadow(withShadow)
                    .build());
            mEmojiView.clearEditText();
        }

    }

    @Override
    public Uri saveBitmap(Bitmap bitmap, String filename, String dirPath) {
        return StorageHelper.saveBitmap(bitmap, filename, dirPath);
    }

    public void isFirstTime(boolean isFirstTimeStart) {
        mIsFirstTimeStart = isFirstTimeStart;
    }

    @Override
    public void instantGifSearch(String keyword) {
        if (mGifsLoader.isStarted()) {
            mGifsLoader.cancelLoad();
        }
        if (mGifSource == 0) {
            mGifsLoader.setQueryFilter(new SooGifQueryFilter(keyword, 0, 3));
        } else {
            mGifsLoader.setQueryFilter(new TenorGifQueryFilter(keyword, "", "3"));
        }
        mGifsLoader.forceLoad();
    }

    @Override
    public void sendGif(Gif gif, String tag) {
        mRepository.appendChat(
                GifChatItem.Builder
                .newInstance()
                        .gif(gif)
                        .tag(tag)
                        .build());
    }

    private int mGifSource = 0;

    @Override
    public void useGifSource(int source) {
        mContractManager.useGifDataSource(source);
        mGifSource = source;
    }
}
