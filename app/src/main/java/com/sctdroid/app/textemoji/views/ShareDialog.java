package com.sctdroid.app.textemoji.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.bean.Gif;

/**
 * Created by lixindong on 2017/5/26.
 */

public class ShareDialog extends Dialog implements View.OnClickListener {
    private ImageView mItemGif;
    private ImageView mItemPreview;
    private ImageView mItemPlaceHolder;
    private ImageView mShareWx;
    private ImageView mShareOther;
    private ImageView mShareQQ;
    private Gif mGif;

    public ShareDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);

        mItemGif = (ImageView) findViewById(R.id.item_gif);
        mItemPlaceHolder = (ImageView) findViewById(R.id.place_holder);
        mItemPreview = (ImageView) findViewById(R.id.item_preview);
        mShareWx = (ImageView) findViewById(R.id.share_wx);
        mShareQQ = (ImageView) findViewById(R.id.share_qq);
        mShareOther = (ImageView) findViewById(R.id.share_other);

        mShareWx.setOnClickListener(this);
        mShareQQ.setOnClickListener(this);
        mShareOther.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        if (!Gif.NULL.equals(mGif)) {
            Glide.with(getContext())
                    .load(mGif.preview)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(mItemPreview);
            Glide.with(getContext())
                    .load(mGif.url)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(mItemGif);
        }
    }

    public void bind(@NonNull Gif gif) {
        mGif = gif;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_wx:
                break;
            case R.id.share_qq:
                break;
            case R.id.share_other:
                break;
        }
    }
}
