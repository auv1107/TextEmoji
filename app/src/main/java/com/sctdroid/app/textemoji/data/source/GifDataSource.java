package com.sctdroid.app.textemoji.data.source;

import com.sctdroid.app.textemoji.data.GifResponse;
import com.sctdroid.app.textemoji.data.QueryFilter;

/**
 * Created by lixindong on 5/10/17.
 */

public interface GifDataSource {
    GifResponse getGifs(String tag);
    GifResponse getGifs(String tag, int pageNumber, int pageSize);
    GifResponse getGifs(QueryFilter filter);

    void refreshGifs();
}
