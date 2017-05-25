package com.sctdroid.app.textemoji.data.source.remote;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.data.source.GifDataSource;
import com.sctdroid.app.textemoji.utils.network.HttpGetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 5/10/17.
 */

public class GifRemoteDataSource implements GifDataSource {
    private static final String REQUEST_URL = "https://api.tenor.co/v1/search?key=LIVDSRZULELA&limit=20&tag=";

    @Override
    public void refreshGifs() {

    }

    @Override
    public List<Gif> getGifs(String tag) {
        String result = request(tag);

        List<Gif> list = Collections.emptyList();
        if (!TextUtils.isEmpty(result)) {
            list = parse(result);
        }
        return list;
    }

    private List<Gif> parse(@NonNull String s) {
        List<Gif> result = Collections.emptyList();
        try {
            JSONObject object = new JSONObject(s);
            if (!JSONObject.NULL.equals(object) &&
                    object.has("results")) {
                result = new ArrayList<>();
                JSONArray array = object.optJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.optJSONObject(i);
                    Gif gif = parseItem(item);
                    result.add(gif);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private Gif parseItem(@NonNull JSONObject item) {
        Gif gif = Gif.NULL;
        if (item.has("media")) {
            JSONArray array = item.optJSONArray("media");
            if (array.length() > 0) {
                JSONObject object = array.optJSONObject(0);
                if (object.has("gif")) {
                    JSONObject gifObject = object.optJSONObject("gif");
                    String url = gifObject.optString("url");
                    String preview = gifObject.optString("preview");

                    JSONArray dim = gifObject.optJSONArray("dims");
                    int width = 0;
                    int height = 0;
                    if (dim.length() > 1) {
                        width = dim.optInt(0);
                        height = dim.optInt(1);
                    }

                    gif = Gif.Builder.newInstance()
                            .width(width)
                            .height(height)
                            .preview(preview)
                            .url(url)
                            .build();
                }
            }
        }
        return gif;
    }

    private String request(String tag) {
        return HttpGetData.requestGet(REQUEST_URL + tag);
    }
}
