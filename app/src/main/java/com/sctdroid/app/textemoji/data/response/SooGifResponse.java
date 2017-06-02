package com.sctdroid.app.textemoji.data.response;

import com.sctdroid.app.textemoji.data.GifResponse;
import com.sctdroid.app.textemoji.data.QueryFilter;
import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.data.source.remote.SooGifRemoteDataSource;

import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 5/27/17.
 */

public class SooGifResponse implements GifResponse {
    public static final SooGifResponse NULL = new SooGifResponse(Collections.<Gif>emptyList(), 0, 0, 0, 0) {
        @Override public boolean equals(Object o) {
            return o == this || o == null; // API specifies this broken equals implementation
        }
        @Override public String toString() {
            return "null";
        }
    };
    private List<Gif> mImages;
    private int pageNumber;
    private int pageSize;
    private int pageCount;
    private int allCount;
    private QueryFilter mQueryFilter;

    public SooGifResponse(List<Gif> data, int pageNumber, int pageSize, int pageCount, int allCount) {
        this.mImages = data;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.allCount = allCount;
    }

    public List<Gif> getData() {
        return mImages;
    }

    @Override
    public boolean hasMore() {
        return pageNumber < pageCount;
    }

    public void setQueryFilter(QueryFilter filter) {
        mQueryFilter = filter;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getAllCount() {
        return allCount;
    }

    @Override
    public QueryFilter buildLoadMoreQueryFilter() {
        return new QueryFilter(SooGifRemoteDataSource.KEY_TAG, mQueryFilter.get(SooGifRemoteDataSource.KEY_TAG),
                SooGifRemoteDataSource.KEY_PAGE_NUMBER, "" + (pageNumber + 1),
                SooGifRemoteDataSource.KEY_PAGE_SIZE, mQueryFilter.get(SooGifRemoteDataSource.KEY_PAGE_SIZE));
    }

    @Override
    public boolean isNewest() {
        return pageNumber == 1;
    }
}
