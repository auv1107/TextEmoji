package com.sctdroid.app.textemoji.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.sctdroid.app.textemoji.data.bean.TextPicItem;
import com.sctdroid.app.textemoji.utils.DisplayUtils;

/**
 * Created by lixindong on 4/19/17.
 */

public class TextEmoji extends View {
    public static final int WIDTH = 120;
    public static final int HEIGHT = 120;
    public static final int DEFAULT_TEXT_SIZE = 20;
    private TextPicItem mItem;
    private TextPaint mPaint;
    private StaticLayout mStaticLayout;

    public TextEmoji(Context context) {
        this(context, null);
    }

    public TextEmoji(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new TextPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec(DisplayUtils.dp2px(getContext(), WIDTH), MeasureSpec.EXACTLY);
        int height = MeasureSpec.makeMeasureSpec(DisplayUtils.dp2px(getContext(), HEIGHT), MeasureSpec.EXACTLY);
        super.onMeasure(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStaticLayout != null) {
            drawLinesText(canvas);
        }
    }

    public void setText(TextPicItem item) {
        mItem = item;
        int textSize = DEFAULT_TEXT_SIZE;
        if (mItem.textSize > 0) {
            textSize = mItem.textSize;
        }
        mPaint.setTextSize(DisplayUtils.dp2px(getContext(), textSize));
        mPaint.setFakeBoldText(true);
        mPaint.setTextAlign(Paint.Align.CENTER);

        if (mItem.withShadow) {
            mPaint.setShadowLayer(textSize/3, 0, 0, Color.parseColor("#ffffff"));
        }
        mStaticLayout = new StaticLayout(mItem.content, mPaint, DisplayUtils.dp2px(getContext(), WIDTH), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F,true);
        postInvalidate();
    }

    /**
     *得到文字的高度
     */
    private float getFontHeight(TextPaint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();// 得到系统默认字体属性
        return fm.bottom - fm.top;
    }

    private void drawLinesText(Canvas canvas) {
        Rect rect = canvas.getClipBounds();

        canvas.save();
        canvas.translate(rect.left + rect.width()/2, rect.top+ (rect.height()  - getFontHeight(mPaint) * mStaticLayout.getLineCount())/2);
        mStaticLayout.draw(canvas);
        canvas.restore();
    }

    public Bitmap getBitmap(boolean isAlpha) {
        Bitmap bitmap = Bitmap.createBitmap(DisplayUtils.dp2px(getContext(), WIDTH),
                DisplayUtils.dp2px(getContext(), HEIGHT),
                Bitmap.Config.ARGB_8888);
        bitmap.setHasAlpha(isAlpha);
        Canvas canvas = new Canvas(bitmap);
        if (!isAlpha) {
            canvas.drawColor(Color.WHITE);
        }
        draw(canvas);
        return bitmap;
    }
}
