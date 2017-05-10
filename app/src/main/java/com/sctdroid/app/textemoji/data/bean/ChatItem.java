package com.sctdroid.app.textemoji.data.bean;

import org.json.JSONObject;

/**
 * Created by lixindong on 5/11/17.
 */

public interface ChatItem {
    String toJson();

    JSONObject toJsonObject();

    boolean isNull();
}
