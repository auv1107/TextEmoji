package com.sctdroid.app.textemoji.data.request;

import com.sctdroid.app.textemoji.data.QueryFilter;
import com.sctdroid.app.textemoji.data.source.remote.SooGifRemoteDataSource;

/**
 * Created by lixindong on 6/2/17.
 */

public class SooGifQueryFilter extends QueryFilter {
    public SooGifQueryFilter(String keyword, int page, int pageSize) {
        super(SooGifRemoteDataSource.KEY_TAG, keyword,
                SooGifRemoteDataSource.KEY_PAGE_NUMBER, page + "",
                SooGifRemoteDataSource.KEY_PAGE_SIZE, pageSize + "");
    }
}
