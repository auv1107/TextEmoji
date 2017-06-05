package com.sctdroid.app.textemoji.data.source.remote;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.sctdroid.app.textemoji.data.GifResponse;
import com.sctdroid.app.textemoji.data.QueryFilter;
import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.data.response.TenorGifResponse;
import com.sctdroid.app.textemoji.data.source.GifDataSource;
import com.sctdroid.app.textemoji.utils.MetaUtils;
import com.sctdroid.app.textemoji.utils.network.HttpGetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 5/10/17.
 */

public class TenorGifRemoteDataSource implements GifDataSource {
    private static final String REQUEST_URL = "https://api.tenor.co/v1/search";
    private static String KEY = "";
    private static final String LOCALE = "zh_CN";

    public static final String KEY_POS = "pos";
    public static final String KEY_LIMIT = "limit";
    public static final String KEY_TEXT = "tag";

    public TenorGifRemoteDataSource(Context context) {
        super();
        KEY = MetaUtils.getMetaData(context, "TENOR_API_KEY");
    }

    @Override
    public void refreshGifs() {

    }

    @Override
    public TenorGifResponse getGifs(String tag) {
        return getGifs(tag, 0, 20);
    }

    @Override
    public TenorGifResponse getGifs(String tag, int pos, int limit) {
        return getGifs(tag, "" + pos, "" + limit);
    }

    private TenorGifResponse getGifs(String tag, String pos, String limit) {
        String result = request(tag, pos, limit);

        TenorGifResponse response = TenorGifResponse.NULL;
        if (!TextUtils.isEmpty(result)) {
            response = parse(result);
        }

        return response;
    }

    @Override
    public GifResponse getGifs(QueryFilter filter) {
        if (filter == null) {
            return null;
        }
        String text = filter.get(KEY_TEXT);
        String pos = filter.get(KEY_POS);
        String limit = filter.get(KEY_LIMIT);

        TenorGifResponse response = getGifs(text, pos, limit);
        response.setQueryFilter(filter);

        return response;
    }

    private TenorGifResponse parse(@NonNull String s) {
        List<Gif> result = Collections.emptyList();
        String next = "";
        try {
            JSONObject object = new JSONObject(s);
            if (!JSONObject.NULL.equals(object)) {
                if (object.has("results")) {
                    result = new ArrayList<>();
                    JSONArray array = object.optJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.optJSONObject(i);
                        Gif gif = parseItem(item);
                        result.add(gif);
                    }
                }
                if (object.has("next")) {
                    next = object.optString("next");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new TenorGifResponse(result, next);
    }

    private Gif parseItem(@NonNull JSONObject item) {
        Gif.Builder builder = Gif.Builder.newInstance();
        if (item.has("media")) {
            JSONArray array = item.optJSONArray("media");
            if (array.length() > 0) {
                JSONObject object = array.optJSONObject(0);
                if (object.has("tinygif")) {
                    JSONObject gifObject = object.optJSONObject("tinygif");
                    String url = gifObject.optString("url");
                    String preview = gifObject.optString("preview");

                    JSONArray dim = gifObject.optJSONArray("dims");
                    int width = 0;
                    int height = 0;
                    if (dim.length() > 1) {
                        width = dim.optInt(0);
                        height = dim.optInt(1);
                    }

                    builder.width(width)
                            .height(height)
                            .preview(preview)
                            .url(url);
                }
            }
        }
        if (item.has("title")) {
            String title = item.optString("title");
            builder.title(title);
        }
        return builder.build();
    }

    private String request(String tag, String pos, String limit) {
        if (TextUtils.isEmpty(tag)) {
            return "";
        }
        String url = REQUEST_URL
                + "?tag=" + URLEncoder.encode(tag)
                + "&key=" + KEY
                + "&locale=" + LOCALE;

        if (!TextUtils.isEmpty(pos)) {
            url += "&pos=" + pos;
        }
        if (!TextUtils.isEmpty(limit)) {
            url += "&limit=" + limit;
        }
        Log.i("tenorgif", "url " + url);

        return HttpGetData.requestGet(url);
    }
}
