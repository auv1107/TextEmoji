package com.sctdroid.app.textemoji.discovery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.utils.ToastUtils;
import com.sctdroid.app.textemoji.views.OnItemTouchListener;
import com.sctdroid.app.textemoji.views.ShareDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 5/25/17.
 */

public class GifFragment extends Fragment implements GifContract.View {
    private GifContract.Presenter mPresenter;
    private GifAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final static String KEYWORD = "KEYWORD";
    private String mKeyword;

    private ShareDialog mShareDialog;

    public static GifFragment newInstance(String keyword) {

        Bundle args = new Bundle();
        args.putString(KEYWORD, keyword);

        GifFragment fragment = new GifFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new GifAdapter();

        Bundle bundle = getArguments();
        mKeyword = bundle.getString(KEYWORD);

        mShareDialog = new ShareDialog(getContext());

        mPresenter.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gif, container, false);

        initRecyclerView(root);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.query(mKeyword);
    }

    private void initRecyclerView(View root) {
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.query(mKeyword);
            }
        });

        recyclerView.addOnItemTouchListener(new OnItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                if (vh instanceof GifViewHolder) {
                    GifViewHolder viewHolder = (GifViewHolder) vh;
                    mShareDialog.bind(viewHolder.getGif());
                    mShareDialog.show();
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                ToastUtils.show(getActivity(), "onItemLongClick", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(GifContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * implementations of Presenter
     */
    @Override
    public void showGifs(List<Gif> data) {
        mAdapter.updateData(data);
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Classes
     */

    static class GifViewHolder extends RecyclerView.ViewHolder {
        private final Context mContext;

        private final ImageView item_image;

        private Gif mGif;

        public GifViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            item_image = (ImageView) itemView.findViewById(R.id.item_gif);
        }
        public GifViewHolder(LayoutInflater inflater, ViewGroup parent) {
            this(inflater.inflate(R.layout.item_gif_grid, parent, false));
        }

        private Context getContext() {
            return mContext;
        }

        public void bind(@NonNull final Gif gif) {
            mGif = gif;
            Glide.with(getContext())
                    .load(gif.preview)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(item_image);
        }

        public Gif getGif() {
            return mGif;
        }
    }

    static class GifAdapter extends RecyclerView.Adapter<GifViewHolder> {
        private List<Gif> mData = new ArrayList<>();

        public GifAdapter() {
            super();
        }

        @Override
        public GifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GifViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(GifViewHolder holder, int position) {
            holder.bind(getItem(position));
        }

        private Gif getItem(int position) {
            return position < getItemCount() ? mData.get(position) : Gif.NULL;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void updateData(List<Gif> data) {
            mData.clear();
            if (!Collections.EMPTY_LIST.equals(data)) {
                mData.addAll(data);
            }
            notifyDataSetChanged();
        }
    }
}
