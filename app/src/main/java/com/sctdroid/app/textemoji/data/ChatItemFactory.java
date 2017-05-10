package com.sctdroid.app.textemoji.data;

import com.sctdroid.app.textemoji.data.bean.ChatItem;
import com.sctdroid.app.textemoji.data.bean.GifChatItem;
import com.sctdroid.app.textemoji.data.bean.TextPicItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lixindong on 5/11/17.
 */

public class ChatItemFactory {
    private static final int TYPE_TEXT_PIC = 0;
    private static final int TYPE_GIF = 1;

    public static ChatItem fromJsonObject(JSONObject object) {
        if (!object.has("type")) {
            // version 1
            return TextPicItem.fromJsonObject(object);
        } else if (object.has("data")){
            // version 2
            JSONObject data = object.optJSONObject("data");
            int type = object.optInt("type");
            if (type == TYPE_GIF) {
                return GifChatItem.fromJsonObject(data);
            }
            if (type == TYPE_TEXT_PIC) {
                return TextPicItem.fromJsonObject(data);
            }
        }
        return null;
    }

    public static JSONObject toJsonObject(ChatItem chatItem) {
        JSONObject object = new JSONObject();
        try {
            if (chatItem instanceof TextPicItem) {
                object.put("type", TYPE_TEXT_PIC);
            } else if (chatItem instanceof GifChatItem) {
                object.put("type", TYPE_GIF);
            }
            object.put("data", chatItem.toJsonObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
