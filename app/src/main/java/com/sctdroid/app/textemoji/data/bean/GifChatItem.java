package com.sctdroid.app.textemoji.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lixindong on 5/11/17.
 */

public class GifChatItem implements ChatItem {
    public static final GifChatItem NULL = new GifChatItem(Gif.NULL, "") {
        @Override public boolean equals(Object o) {
            return o == this || o == null; // API specifies this broken equals implementation
        }
        @Override public String toString() {
            return "null";
        }
    };


    public final Gif gif;
    public final String tag;

    private GifChatItem(Gif gif, String tag) {
        this.gif = gif;
        this.tag = tag;
    }

    @Override
    public String toJson() {
        return toJsonObject().toString();
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject object = gif.toJsonObject();
        try {
            object.put("tag", tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public boolean isNull() {
        return NULL.equals(this);
    }

    public static ChatItem fromJsonObject(JSONObject object) {
        Gif gif = Gif.fromJsonObject(object);
        String tag = object.optString("tag");
        return Builder.newInstance()
                .gif(gif)
                .tag(tag)
                .build();
    }

    public static class Builder {
        private Gif gif;
        private String tag;

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder gif(Gif gif) {
            this.gif = gif;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public GifChatItem build() {
            return new GifChatItem(gif, tag);
        }
    }
}
