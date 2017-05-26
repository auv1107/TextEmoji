package com.sctdroid.app.textemoji.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.businessUtils.ShareUtils;
import com.sctdroid.app.textemoji.data.bean.Shareable;
import com.sctdroid.app.textemoji.utils.ToastUtils;

/**
 * Created by lixindong on 2017/5/26.
 */

public class ShareDialog extends Dialog implements View.OnClickListener {
    private ImageView mItemRaw;
    private ImageView mItemPreview;
    private ImageView mItemPlaceHolder;
    private ImageView mShareWx;
    private ImageView mShareOther;
    private ImageView mShareQQ;
    private Shareable mShareable;

    private boolean mGifReady = false;

    public ShareDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);

        mItemRaw = (ImageView) findViewById(R.id.item_raw);
        mItemPlaceHolder = (ImageView) findViewById(R.id.place_holder);
        mItemPreview = (ImageView) findViewById(R.id.item_preview);
        mShareWx = (ImageView) findViewById(R.id.share_wx);
        mShareQQ = (ImageView) findViewById(R.id.share_qq);
        mShareOther = (ImageView) findViewById(R.id.share_other);

        mShareWx.setOnClickListener(this);
        mShareQQ.setOnClickListener(this);
        mShareOther.setOnClickListener(this);

        mGifReady = false;
    }

    @Override
    public void show() {
        super.show();
        if (mShareable.isBitmap()) {
            mItemRaw.setImageBitmap(mShareable.getBitmap());
            mItemPlaceHolder.setVisibility(View.GONE);
            mItemPreview.setImageResource(0);
        } else {
            mItemPlaceHolder.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(mShareable.getPreview())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(mItemPreview);
            Glide.with(getContext())
                    .load(mShareable.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mGifReady = true;
                            return false;
                        }
                    }).into(mItemRaw);
        }
    }

    public void bind(@NonNull Shareable shareable) {
        mShareable = shareable;
    }

    @Override
    public void onClick(View v) {
        if (!mShareable.isBitmap() && !mGifReady) {
            ToastUtils.show(getContext(), R.string.wait_for_gif_loading, Toast.LENGTH_SHORT);
            return;
        }
        switch (v.getId()) {
            case R.id.share_wx:
                ShareUtils.saveAndShare(getContext(), mShareable, ShareUtils.SharePlatform.WECHAT);
                break;
            case R.id.share_qq:
                ShareUtils.saveAndShare(getContext(), mShareable, ShareUtils.SharePlatform.QQ);
                break;
            case R.id.share_other:
                ShareUtils.saveAndShare(getContext(), mShareable, ShareUtils.SharePlatform.OTHERS);
                break;
        }
    }
}
