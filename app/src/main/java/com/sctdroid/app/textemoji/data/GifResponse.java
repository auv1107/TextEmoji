package com.sctdroid.app.textemoji.data;

import com.sctdroid.app.textemoji.data.bean.Gif;

import java.util.List;

/**
 * Created by lixindong on 6/2/17.
 */

public interface GifResponse {
    List<Gif> getData();
    boolean hasMore();
    QueryFilter buildLoadMoreQueryFilter();
    boolean isNewest();
}
