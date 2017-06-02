package com.sctdroid.app.textemoji.data.request;

import com.sctdroid.app.textemoji.data.QueryFilter;
import com.sctdroid.app.textemoji.data.source.remote.TenorGifRemoteDataSource;

/**
 * Created by lixindong on 6/2/17.
 */

public class TenorGifQueryFilter extends QueryFilter {
    public TenorGifQueryFilter(String tag, String pos, String limit) {
        super(TenorGifRemoteDataSource.KEY_TEXT, tag,
                TenorGifRemoteDataSource.KEY_POS, pos,
                TenorGifRemoteDataSource.KEY_LIMIT, limit);
    }
}
