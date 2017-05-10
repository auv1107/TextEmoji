package com.sctdroid.app.textemoji.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lixindong on 5/10/17.
 */

public class Gif {
    public static final Gif NULL = new Gif("", "", 0, 0) {
        @Override public boolean equals(Object o) {
            return o == this || o == null; // API specifies this broken equals implementation
        }
        @Override public String toString() {
            return "null";
        }
    };

    public final String url;
    public final String preview;
    public final int width;
    public final int height;

    private Gif(String url, String preview, int width, int height) {
        super();
        this.url = url;
        this.preview = preview;
        this.width = width;
        this.height = height;
    }

    public static class Builder {
        private String url;
        private String preview;
        private int width;
        private int height;

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder preview(String preview) {
            this.preview = preview;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Gif build() {
            return new Gif(url, preview, width, height);
        }
    }

    public String toJson() {
        return toJsonObject().toString();
    }

    public JSONObject toJsonObject() {
        JSONObject result = new JSONObject();
        try {
            result.put("url", url);
            result.put("preview", preview);
            result.put("width", width);
            result.put("height", height);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Gif fromJson(String json) {
        try {
            return fromJsonObject(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Gif.NULL;
    }

    public static Gif fromJsonObject(JSONObject object) {
        String url = object.optString("url");
        String preview = object.optString("preview");
        int width = object.optInt("width");
        int height = object.optInt("height");
        return Builder.newInstance()
                .url(url)
                .preview(preview)
                .width(width)
                .height(height)
                .build();
    }
}
