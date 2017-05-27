package com.sctdroid.app.textemoji.data.source.remote;

import android.text.TextUtils;

import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.data.bean.GifResponse;
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
    private static final String SCOPE = "zimo";
    private static final String SALT = "5cb470314206c227b56091a399f871df";

    @Override
    public GifResponse getGifs(String tag) {
        return getGifs(tag, 0, 20);
    }

    @Override
    public GifResponse getGifs(String tag, int pageNumber, int pageSize) {
        String result = request(tag, pageNumber, pageSize);

        GifResponse response = GifResponse.NULL;
        if (!TextUtils.isEmpty(result)) {
            response = parse(result);
        }

        return response;
    }

    private GifResponse parse(String result) {
        GifResponse response = GifResponse.NULL;
        int pageNumber = 0;
        int pageSize = 0;
        int pageCount = 0;
        int allCount = 0;
        try {
            JSONObject object = new JSONObject(result);
            if (object.has("data")) {
                List<Gif> images = Collections.emptyList();
                JSONObject data = object.optJSONObject("data");
                if (!JSONObject.NULL.equals(data)) {
                    if (data.has("images")) {
                        images = parseData(data.optJSONArray("images"));
                    }
                    if (data.has("pagination")) {
                        JSONObject pagination = data.optJSONObject("pagination");
                        pageNumber = pagination.getInt("pageNumber");
                        pageSize = pagination.getInt("pageSize");
                        pageCount = pagination.getInt("pageCount");
                        allCount = pagination.getInt("allCount");
                    }

                    response = new GifResponse(images, pageNumber, pageSize, pageCount, allCount);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    private List<Gif> parseData(JSONArray images) {
        List<Gif> res = new ArrayList<>();
        for (int i = 0; i < images.length(); i++) {
            Gif gif = parseItem(images.optJSONObject(i));
            res.add(gif);
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
        return request(text, 0, 20);
    }

    private String request(String text, int pageNumber, int pageSize) {
        String timestamp = System.currentTimeMillis() + "";
        String sign = EncoderUtils.encodeMD5(text + SCOPE + timestamp + SALT);

        String url = REQUEST_URL
                + "?text=" + text
                + "&scope=" + SCOPE
                + "&timestamp=" + timestamp
                + "&sign=" + sign
                + "&pageNumber=" + pageNumber
                + "&pageSize=" + pageSize;
        return HttpGetData.requestGet(url);
    }
}
