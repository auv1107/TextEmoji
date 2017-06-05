package com.sctdroid.app.textemoji.emoji;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.sctdroid.app.textemoji.data.bean.Emoji;
import com.sctdroid.app.textemoji.data.bean.EmojiCategory;
import com.sctdroid.app.textemoji.views.EmojiCategoryView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixindong on 4/28/17.
 */

public class EmojiPagerAdapter extends PagerAdapter {
    private final Context mContext;

    private List<EmojiCategory> mData = new ArrayList<>();
    private Map<String, EmojiCategoryView> mCachedView = new HashMap<>();
    private EmojiCategoryView.ContentAdapter.OnItemClickListener mOnItemClickListener;
    private EmojiCategoryView.ContentAdapter.OnItemLongClickListener mOnItemLongClickListener;
    private View.OnClickListener mOnDeleteClickListener;

    public EmojiPagerAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public EmojiCategory getItem(int position) {
        return position < getCount() ? mData.get(position) : EmojiCategory.NULL;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        EmojiCategory category = getItem(position);
        EmojiCategoryView categoryView;
        if (mCachedView.containsKey(category.name)) {
            categoryView = mCachedView.get(category.name);
        } else {
            categoryView = new EmojiCategoryView(mContext);
        }
        categoryView.setOnItemClickListener(mOnItemClickListener);
        categoryView.setOnItemLongClickListener(mOnItemLongClickListener);
        categoryView.setOnDeleteClickListener(mOnDeleteClickListener);
        categoryView.bind(category);
        container.addView(categoryView);
        return categoryView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (((View) object).getParent() == container) {
            container.removeView((View) object);
            EmojiCategory category = getItem(position);
            if (category != null && mCachedView.containsKey(category.name)) {
                mCachedView.remove(category.name);
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void updateData(List<EmojiCategory> data) {
        mData.clear();
        if (!Collections.EMPTY_LIST.equals(data)) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(EmojiCategoryView.ContentAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(EmojiCategoryView.ContentAdapter.OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public void setOnDeleteClickListener(View.OnClickListener listener) {
        mOnDeleteClickListener = listener;
    }
}
