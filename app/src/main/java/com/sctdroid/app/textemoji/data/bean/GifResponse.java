package com.sctdroid.app.textemoji.data.bean;

import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 5/27/17.
 */

public class GifResponse {
    public static final GifResponse NULL = new GifResponse(Collections.<Gif>emptyList(), 0, 0, 0, 0) {
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

    public GifResponse(List<Gif> data, int pageNumber, int pageSize, int pageCount, int allCount) {
        this.mImages = data;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.allCount = allCount;
    }

    public List<Gif> getData() {
        return mImages;
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
}
