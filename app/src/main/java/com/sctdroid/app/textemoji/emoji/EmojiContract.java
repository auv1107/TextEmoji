package com.sctdroid.app.textemoji.emoji;

import android.graphics.Bitmap;
import android.net.Uri;

import com.sctdroid.app.textemoji.BasePresenter;
import com.sctdroid.app.textemoji.BaseView;
import com.sctdroid.app.textemoji.data.bean.ChatItem;
import com.sctdroid.app.textemoji.data.bean.TextPicItem;
import com.sctdroid.app.textemoji.data.bean.EmojiCategory;
import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.data.bean.Me;

import java.util.List;

/**
 * Created by lixindong on 4/18/17.
 */

public class EmojiContract {
    interface Presenter extends BasePresenter {

        void processInput(String inputText, int textSize, boolean withShadow);

        Uri saveBitmap(Bitmap bitmap, String filename, String dirPath);

        void instantGifSearch(String keyword);

        void sendGif(Gif gif, String tag);

        void useGifSource(int position);

        void removeChat(int position);

        void startSearch(String text);

        int getGifSourceId();

    }
    interface View extends BaseView<Presenter> {

        void showChats(List<ChatItem> data);

        void showEmptyText();

        void clearEditText();

        void updateMe(Me me);

        void scrollToTop();

        void scrollToBottom();

        void initEmojiBoard(List<EmojiCategory> data);

        void showGifs(List<Gif> gifs, String tag);

        void clearGifs();

        void showUseTenorSourceDialog();

        void hideBottom();
    }
    interface ContractManager {

        void useGifDataSource(int position);

        void startSearch(String text);

    }
}
