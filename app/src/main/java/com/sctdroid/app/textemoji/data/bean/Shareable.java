package com.sctdroid.app.textemoji.data.bean;

import android.graphics.Bitmap;

/**
 * Created by lixindong on 5/26/17.
 */

public interface Shareable {
    boolean isBitmap();
    Bitmap getBitmap();
    String getUrl();
    String getTag();
    String getPreview();
}
