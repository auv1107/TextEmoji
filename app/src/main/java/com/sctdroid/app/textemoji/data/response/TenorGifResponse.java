package com.sctdroid.app.textemoji.data.response;

import android.text.TextUtils;

import com.sctdroid.app.textemoji.data.GifResponse;
import com.sctdroid.app.textemoji.data.QueryFilter;
import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.data.source.remote.TenorGifRemoteDataSource;

import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 6/2/17.
 */

public class TenorGifResponse implements GifResponse {
    public static final TenorGifResponse NULL = new TenorGifResponse(Collections.<Gif>emptyList(), "") {
        @Override public boolean equals(Object o) {
            return o == this || o == null; // API specifies this broken equals implementation
        }
        @Override public String toString() {
            return "null";
        }
    };

    private final String mNextPos;
    private final List<Gif> mData;
    private QueryFilter mQueryFilter;

    public TenorGifResponse(List<Gif> data, String nextPos) {
        mData = data;
        mNextPos = nextPos;
    }

    public void setQueryFilter(QueryFilter filter) {
        mQueryFilter = filter;
    }

    @Override
    public List<Gif> getData() {
        return mData;
    }

    @Override
    public boolean hasMore() {
        return !TextUtils.isEmpty(mNextPos) && !"0".equals(mNextPos);
    }

    @Override
    public QueryFilter buildLoadMoreQueryFilter() {
        return new QueryFilter(TenorGifRemoteDataSource.KEY_TEXT, mQueryFilter.get(TenorGifRemoteDataSource.KEY_TEXT),
                TenorGifRemoteDataSource.KEY_POS, mNextPos,
                TenorGifRemoteDataSource.KEY_LIMIT, mQueryFilter.get(TenorGifRemoteDataSource.KEY_LIMIT));
    }

    @Override
    public boolean isNewest() {
        return TextUtils.isEmpty(mQueryFilter.get(TenorGifRemoteDataSource.KEY_POS));
    }
}
