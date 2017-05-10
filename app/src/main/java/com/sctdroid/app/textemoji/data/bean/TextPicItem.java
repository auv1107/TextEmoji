package com.sctdroid.app.textemoji.data.bean;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lixindong on 4/18/17.
 */

public class TextPicItem implements ChatItem {
    public final String content;
    public final int avatarResId;
    public final int textSize;
    public final boolean withShadow;

    public static final TextPicItem NULL = new TextPicItem("", -1, 0, false) {
        @Override public boolean equals(Object o) {
            return o == this || o == null; // API specifies this broken equals implementation
        }
        @Override public String toString() {
            return "null";
        }
    };

    private TextPicItem(String content, int avatarResId, int textSize, boolean withShadow) {
        this.content = content;
        this.avatarResId = avatarResId;
        this.textSize = textSize;
        this.withShadow = withShadow;
    }

    public static class Builder {
        private int avatarResId = -1;
        private String content;
        private int textSize = 0;
        private boolean withShadow = false;

        public Builder avatarResId(int avatarResId) {
            this.avatarResId = avatarResId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder withShadow(boolean withShadow) {
            this.withShadow = withShadow;
            return this;
        }

        public TextPicItem build() {
            return new TextPicItem(content, avatarResId, textSize, withShadow);
        }
    }

    @Override
    public String toJson() {
        JSONObject object = toJsonObject();
        return object.toString();
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("content", content);
            object.put("avatarResId", avatarResId);
            object.put("textSize", textSize);
            object.put("withShadow", withShadow);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public boolean isNull() {
        return NULL.equals(this);
    }

    public static TextPicItem fromJsonObject(@NonNull JSONObject object) {
        String content = object.optString("content");
        int avatarResId = object.optInt("avatarResId");
        int textSize = object.optInt("textSize");
        boolean withShadow = object.optBoolean("withShadow");
        return new Builder()
                .avatarResId(avatarResId)
                .content(content)
                .textSize(textSize)
                .withShadow(withShadow)
                .build();
    }
}
