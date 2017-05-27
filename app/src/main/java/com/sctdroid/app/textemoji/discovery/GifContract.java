package com.sctdroid.app.textemoji.discovery;

import com.sctdroid.app.textemoji.BasePresenter;
import com.sctdroid.app.textemoji.BaseView;
import com.sctdroid.app.textemoji.data.bean.Gif;

import java.util.List;

/**
 * Created by lixindong on 5/25/17.
 */

public class GifContract {
    interface Presenter
            extends BasePresenter {

        void query(String keyword);

        void queryNext(String mKeyword);

        boolean hasMore();
    }

    interface View extends BaseView<Presenter> {

        void showGifs(List<Gif> data);

        void showMore(List<Gif> data);

        void showNoMore();

        void showNoData();
    }
}
