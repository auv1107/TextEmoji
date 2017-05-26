package com.sctdroid.app.textemoji.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by lixindong on 5/26/17.
 */

public class BitmapUtils {
    public static Bitmap convertViewToBitmap(View v){
        v.setDrawingCacheEnabled(true);
        Bitmap vBitmap = v.getDrawingCache();

        if (vBitmap == null) {
            v.setDrawingCacheEnabled(false);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
            v.draw(c);
            return b;
        } else {
            Bitmap bitmap = Bitmap.createBitmap(vBitmap);
            v.setDrawingCacheEnabled(false);
            return bitmap;
        }
    }
}
