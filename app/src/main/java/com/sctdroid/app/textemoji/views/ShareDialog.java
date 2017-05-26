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
import com.sctdroid.app.textemoji.businessUtils.ShareUtils;
import com.sctdroid.app.textemoji.data.bean.Shareable;

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
                    .into(mItemRaw);
        }
    }

    public void bind(@NonNull Shareable shareable) {
        mShareable = shareable;
    }

    @Override
    public void onClick(View v) {
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
        dismiss();
    }
}
