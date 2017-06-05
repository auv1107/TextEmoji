package com.sctdroid.app.textemoji.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.bean.Emoji;
import com.sctdroid.app.textemoji.data.bean.EmojiCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 4/28/17.
 */

public class EmojiCategoryView extends RelativeLayout {
    public EmojiCategoryView(Context context) {
        this(context, null);
    }

    public EmojiCategoryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiCategoryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private ContentAdapter mAdapter;
    private ImageView mDeleteButton;

    void init() {
        inflate(getContext(), R.layout.recycler_view, this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new ContentAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        mDeleteButton = (ImageView) findViewById(R.id.delete_button);
    }

    public void bind(EmojiCategory category) {
        mAdapter.updateData(category.data);
    }

    public void setOnItemClickListener(ContentAdapter.OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    public void setOnDeleteClickListener(final OnClickListener listener) {
        mDeleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteButton.setOnClickListener(listener);
            }
        });
    }

    public void setOnItemLongClickListener(ContentAdapter.OnItemLongClickListener listener) {
        mAdapter.setOnItemLongClickListener(listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context mContext;
        private final TextView item_emoji;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            item_emoji = (TextView) itemView.findViewById(R.id.item_emoji);
        }
        public ViewHolder(Context context, LayoutInflater inflater, ViewGroup parent) {
            this(context, inflater.inflate(R.layout.emoji_item, parent, false));
        }

        public void bind(Emoji item) {
            item_emoji.setText(item.emoji);
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final Context mContext;
        private List<Emoji> mData = new ArrayList<>();

        private OnItemClickListener mOnItemClickListener;
        private OnItemLongClickListener mOnItemLongClickListener;

        public ContentAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mContext, LayoutInflater.from(mContext), parent);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Emoji emoji = getItem(position);
            holder.bind(emoji);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClicked(v, emoji);
                    }
                });
            }
            if (mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mOnItemLongClickListener.onItemLongClicked(v, emoji);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public Emoji getItem(int position) {
            return position < mData.size() ? mData.get(position) : Emoji.NULL;
        }

        public void updateData(List<Emoji> data) {
            mData.clear();
            if (!Collections.EMPTY_LIST.equals(data)) {
                mData.addAll(data);
                if (mData.size() >= 7) {
                    mData.add(6, Emoji.NULL);
                }
            }
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
        }

        public void setOnItemLongClickListener(OnItemLongClickListener listener) {
            mOnItemLongClickListener = listener;
        }

        public interface OnItemClickListener {
            void onItemClicked(View view, Emoji emoji);
        }
        public interface OnItemLongClickListener {
            boolean onItemLongClicked(View view, Emoji emoji);
        }
    }
}
