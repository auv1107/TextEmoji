package com.sctdroid.app.textemoji.data.source;

import com.sctdroid.app.textemoji.data.bean.ChatItem;

import java.util.List;

/**
 * Created by lixindong on 4/18/17.
 */

public interface ChatsDataSource {
    List<ChatItem> getChats();
    void saveChats(List<ChatItem> items);
    void appendChat(ChatItem item);
    void removeChat(int position);
}
