package com.sctdroid.app.textemoji.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by lixindong on 5/27/17.
 */

public class SwipeLayout extends SwipeRefreshLayout implements GestureDetector.OnGestureListener{
    private GestureDetector mGestureDetector;
    private OnLoadListener mOnLoadListener;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!mIsLoading && distanceY > 0 && mSwipeContent.isBottom() && mSwipeContent.canLoad()) {
            mIsLoading = true;
            if (mOnLoadListener != null) {
                mOnLoadListener.onLoad();
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public interface OnLoadListener {
        void onLoad();
    }

    public void setOnLoadListener(OnLoadListener listener) {
        mOnLoadListener = listener;
    }

    private SwipeContent mSwipeContent;
    private boolean mIsLoading = false;

    public interface SwipeContent {
        boolean canLoad();
        boolean isBottom();
    }

    public void setSwipeContent(SwipeContent swipeContent) {
        mSwipeContent = swipeContent;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean loading) {
        mIsLoading = loading;
    }
}
