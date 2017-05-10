package com.sctdroid.app.textemoji.data.source;

import com.sctdroid.app.textemoji.data.bean.Gif;

import java.util.List;

/**
 * Created by lixindong on 5/10/17.
 */

public interface GifDataSource {
    List<Gif> getGifs(String tag);

    void refreshGifs();
}
