package com.sctdroid.app.textemoji.data;

import java.util.HashMap;

/**
 * Created by lixindong on 6/2/17.
 */

public class QueryFilter extends HashMap<String, String> {
    public QueryFilter(String... params) {
        super();
        for (int i = 0; i + 1 < params.length; i++) {
            put(params[i], params[i + 1]);
        }
    }
}
