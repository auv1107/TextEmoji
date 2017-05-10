package com.sctdroid.app.textemoji.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sctdroid.app.textemoji.data.ChatItemFactory;
import com.sctdroid.app.textemoji.data.bean.ChatItem;
import com.sctdroid.app.textemoji.data.source.ChatsDataSource;
import com.sctdroid.app.textemoji.utils.AssetUtils;
import com.sctdroid.app.textemoji.utils.FileAccessUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 4/18/17.
 */

public class ChatsLocalDataSource implements ChatsDataSource {
    private static final String FILENAME = "chat_history";
    private static final String DEFAULT_CHAT_HISTORY_FILENAME = "default_chat_history.json";
    private final Context mContext;

    public ChatsLocalDataSource(Context context) {
        super();
        mContext = context;
    }

    @Override
    public List<ChatItem> getChats() {
        // get data
        String data = FileAccessUtils.read(getPath());
        if (TextUtils.isEmpty(data)) {
            data = AssetUtils.readAssertResource(mContext, DEFAULT_CHAT_HISTORY_FILENAME);
        }
        // to array
        // parse
        try {
            JSONArray array = new JSONArray(data);
            List<ChatItem> result = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                ChatItem item = ChatItemFactory.fromJsonObject(array.optJSONObject(i));
                result.add(item);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private String getPath() {
        return mContext.getFilesDir().getPath() + "/" + FILENAME;
    }

    @Override
    public void saveChats(@NonNull List<ChatItem> items) {
        // to JsonArray
        JSONArray array = new JSONArray();
        for (int i = 0; i < items.size(); i++) {
            JSONObject object = ChatItemFactory.toJsonObject(items.get(i));
            array.put(object);
        }
        // to String
        String data = array.toString();
        // save
        FileAccessUtils.write(getPath(), data);
    }

    @Override
    public void appendChat(ChatItem item) {
        if (item == null || item.isNull()) {
            return;
        }
        // getChats()
        List<ChatItem> items = getChats();
        // add item
        if (items == null || items.isEmpty()) {
            items = new ArrayList<>();
        }
        items.add(item);
        // save items
        saveChats(items);
    }
}
