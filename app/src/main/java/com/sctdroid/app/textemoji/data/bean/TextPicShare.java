package com.sctdroid.app.textemoji.data.bean;

import android.graphics.Bitmap;

/**
 * Created by lixindong on 5/26/17.
 */

public class TextPicShare implements Shareable {
    private final TextPicItem mItem;
    private final Bitmap mBitmap;

    public TextPicShare(TextPicItem mItem, Bitmap bitmap) {
        this.mItem = mItem;
        this.mBitmap = bitmap;
    }

    @Override
    public boolean isBitmap() {
        return true;
    }

    @Override
    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String getTag() {
        return mItem.content;
    }

    @Override
    public String getPreview() {
        return null;
    }
}
