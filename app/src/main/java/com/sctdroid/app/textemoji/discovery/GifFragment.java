package com.sctdroid.app.textemoji.discovery;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.utils.ToastUtils;
import com.sctdroid.app.textemoji.views.OnItemTouchListener;
import com.sctdroid.app.textemoji.views.ShareDialog;
import com.sctdroid.app.textemoji.views.SwipeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lixindong on 5/25/17.
 */

public class GifFragment extends Fragment implements GifContract.View {
    private GifContract.Presenter mPresenter;
    private GifAdapter mAdapter;
    private SwipeLayout mSwipeLayout;

    private final static String KEYWORD = "KEYWORD";
    private String mKeyword;

    private ShareDialog mShareDialog;
    private View mStateContainer;
    private TextView mState;
    private TextView mItemGifSupport;

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
        initViews(root);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showState(State.STATE_LOADING);
        mSwipeLayout.setRefreshing(true);
        mPresenter.afterViewInited();
        mPresenter.query(mKeyword);
    }

    private void showState(State state) {
        switch (state) {
            case STATE_SHOW_DATA:
                mSwipeLayout.setVisibility(View.VISIBLE);
                mStateContainer.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
                mSwipeLayout.setVisibility(View.GONE);
                mStateContainer.setVisibility(View.VISIBLE);
                mState.setText(R.string.state_loading);
                break;
            case STATE_NO_DATA:
                mSwipeLayout.setVisibility(View.GONE);
                mStateContainer.setVisibility(View.VISIBLE);
                mState.setText(R.string.state_no_data);
                break;
            case STATE_NETWORK_ERROR:
                mSwipeLayout.setVisibility(View.GONE);
                mStateContainer.setVisibility(View.VISIBLE);
                mState.setText(R.string.state_network_error);
                break;
        }
    }

    private void initViews(View root) {
        mStateContainer = root.findViewById(R.id.item_state_container);
        mState = (TextView) root.findViewById(R.id.item_state);
        mItemGifSupport = (TextView) root.findViewById(R.id.item_gif_support);
    }

    private void initRecyclerView(View root) {
        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mSwipeLayout = (SwipeLayout) root.findViewById(R.id.swipe_refresh_layout);
        mSwipeLayout.setOnRefreshListener(new SwipeLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.query(mKeyword);
            }
        });

        mSwipeLayout.setSwipeContent(new SwipeLayout.SwipeContent() {
            @Override
            public boolean canLoad() {
                return mPresenter.hasMore();
            }

            @Override
            public boolean isBottom() {
                int lastPosition = 0;
                if(layoutManager instanceof GridLayoutManager){
                    //通过LayoutManager找到当前显示的最后的item的position
                    lastPosition = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                }else if(layoutManager instanceof LinearLayoutManager){
                    lastPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                }else if(layoutManager instanceof StaggeredGridLayoutManager) {
                    //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                    //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                    int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(lastPositions);
                    lastPosition = findMax(lastPositions);
                }

                return lastPosition == layoutManager.getItemCount() - 1;
            }

            private int findMax(int[] set) {
                int max = set[0];
                for (int value : set) {
                    if (value > max) {
                        max = value;
                    }
                }
                return max;
            }
        });
        mSwipeLayout.setOnLoadListener(new SwipeLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mPresenter.queryNext(mKeyword);
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
                ToastUtils.show(getActivity(), R.string.no_long_click_action, Toast.LENGTH_SHORT);
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
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
        if (Collections.EMPTY_LIST.equals(data)) {
            showState(State.STATE_NO_DATA);
        } else {
            showState(State.STATE_SHOW_DATA);
        }
    }

    @Override
    public void showMore(List<Gif> data) {
        mAdapter.appendData(data);
        if (mSwipeLayout.isLoading()) {
            mSwipeLayout.setLoading(false);
        }
    }

    @Override
    public void showNoMore() {
        ToastUtils.show(getContext(), R.string.no_more_data, Toast.LENGTH_SHORT);
        if (mSwipeLayout.isLoading()) {
            mSwipeLayout.setLoading(false);
        }
    }

    @Override
    public void showNoData() {
        showState(State.STATE_NO_DATA);
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void updateGifSource(int source) {
        int source_id = source == 0 ? R.string.source_soogif : R.string.source_tenor;
        String str = getString(R.string.gif_support, getString(source_id));
        if (mItemGifSupport != null) {
            mItemGifSupport.setText(str);
        }
    }

    /**
     * Classes
     */

    static class GifViewHolder extends RecyclerView.ViewHolder {
        private final Context mContext;

        private final ImageView item_image;
        private final ImageView placeholder;

        private Gif mGif;

        public GifViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            item_image = (ImageView) itemView.findViewById(R.id.item_raw);
            placeholder = (ImageView) itemView.findViewById(R.id.place_holder);
        }
        public GifViewHolder(LayoutInflater inflater, ViewGroup parent) {
            this(inflater.inflate(R.layout.item_gif_grid, parent, false));
        }

        private Context getContext() {
            return mContext;
        }

        public void bind(@NonNull final Gif gif) {
            mGif = gif;
            placeholder.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(gif.preview)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            placeholder.setVisibility(View.GONE);
                            return false;
                        }
                    })
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
            appendData(data);
        }

        public void appendData(List<Gif> data) {
            if (!Collections.EMPTY_LIST.equals(data)) {
                mData.addAll(data);
            }
            notifyDataSetChanged();
        }
    }

    private enum State {
        STATE_SHOW_DATA,
        STATE_LOADING,
        STATE_NO_DATA,
        STATE_NETWORK_ERROR
    }
}
