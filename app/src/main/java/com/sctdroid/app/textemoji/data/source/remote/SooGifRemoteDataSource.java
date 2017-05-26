package com.sctdroid.app.textemoji.data.source.remote;

import android.text.TextUtils;

import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.data.source.GifDataSource;
import com.sctdroid.app.textemoji.utils.EncoderUtils;
import com.sctdroid.app.textemoji.utils.network.HttpGetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 5/16/17.
 */

public class SooGifRemoteDataSource implements GifDataSource {
    private static final String REQUEST_URL = "http://napi.soogif.com/oapi/backend/image/search";

    @Override
    public List<Gif> getGifs(String tag) {
        String result = request(tag);

        List<Gif> list = Collections.emptyList();
        if (!TextUtils.isEmpty(result)) {
            list = parse(result);
        }
        return list;
    }

    private List<Gif> parse(String result) {
        List<Gif> res = Collections.emptyList();
        try {
            JSONObject object = new JSONObject(result);
            if (object.has("data")) {
                JSONObject data = object.optJSONObject("data");
                if (!JSONObject.NULL.equals(data) &&
                        data.has("images")) {
                    JSONArray images = data.optJSONArray("images");
                    res = new ArrayList<>();
                    for (int i = 0; i < images.length(); i++) {
                        Gif gif = parseItem(images.getJSONObject(i));
                        res.add(gif);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    private Gif parseItem(JSONObject object) {
        String url = object.optString("url");
        String preview = object.optString("fixedUrl");
        int width = object.optInt("width");
        int height = object.optInt("height");
        String title = object.optString("title");

        return Gif.Builder.newInstance()
                .height(height)
                .width(width)
                .preview(preview)
                .url(url)
                .title(title)
                .build();
    }

    @Override
    public void refreshGifs() {

    }

    private String request(String text) {
        String timestamp = System.currentTimeMillis() + "";
        String scope = "zimo";
        String SALT = "5cb470314206c227b56091a399f871df";
        String sign = EncoderUtils.encodeMD5(text + scope + timestamp + SALT);
        int pageSize = 30;

        String url = REQUEST_URL
                + "?text=" + text
                + "&scope=" + scope
                + "&timestamp=" + timestamp
                + "&sign=" + sign
                + "&pageSize=" + pageSize;
        return HttpGetData.requestGet(url);
    }
}
