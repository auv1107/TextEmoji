package com.sctdroid.app.textemoji.emoji;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.bean.ChatItem;
import com.sctdroid.app.textemoji.data.bean.Emoji;
import com.sctdroid.app.textemoji.data.bean.EmojiCategory;
import com.sctdroid.app.textemoji.data.bean.Gif;
import com.sctdroid.app.textemoji.data.bean.GifChatItem;
import com.sctdroid.app.textemoji.data.bean.Me;
import com.sctdroid.app.textemoji.data.bean.Shareable;
import com.sctdroid.app.textemoji.data.bean.TextPicItem;
import com.sctdroid.app.textemoji.data.bean.TextPicShare;
import com.sctdroid.app.textemoji.data.source.GifDataSource;
import com.sctdroid.app.textemoji.developer.DeveloperActivity;
import com.sctdroid.app.textemoji.me.MeActivity;
import com.sctdroid.app.textemoji.utils.BitmapUtils;
import com.sctdroid.app.textemoji.utils.Constants;
import com.sctdroid.app.textemoji.utils.DisplayUtils;
import com.sctdroid.app.textemoji.utils.EmojiUtils;
import com.sctdroid.app.textemoji.utils.SharePreferencesUtils;
import com.sctdroid.app.textemoji.utils.SingleFileScanner;
import com.sctdroid.app.textemoji.utils.TCAgentUtils;
import com.sctdroid.app.textemoji.utils.compact.Compact;
import com.sctdroid.app.textemoji.views.EmojiCategoryView;
import com.sctdroid.app.textemoji.views.EmojiTager;
import com.sctdroid.app.textemoji.views.RelativeLayoutCompact;
import com.sctdroid.app.textemoji.views.ShareDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.sctdroid.app.textemoji.utils.Constants.KEY_GIF_SORUCE;

/**
 * Created by lixindong on 4/18/17.
 */

public class EmojiFragment extends Fragment implements EmojiContract.View, BaseEmojiViewHolder.EventDelegate {
    private ContentAdapter mAdapter;
    private EmojiContract.Presenter mPresenter;

    /**
     * Views
     */
    private TextInputEditText mTextInputEditText;
    private RecyclerView mRecyclerView;
    private CardView mOptions;
    private ImageView mEmojiButton;
    private EmojiTager mEmojiTager;
    private ShareDialog mShareDialog;
    private Spinner mGifSourceSpinner;

    private ImageView[] mGifs = new ImageView[3];

    private EmojiRadioAdapter mEmojiRadioAdapter;
    private EmojiPagerAdapter mEmojiPagerAdapter;

    private int mTextSize;

    private boolean mWithShadow;
    private SingleFileScanner mScanner;
    private int mMinTextSize;
    private int mDefaultTextSize;
    private int mSpanPerSegment;
    private int mSoftKeyboardDefaultHeight;

    /**
     * option type
     */
    private static final int OPTION_TYPE_NONE = -1;
    private static final int OPTION_TYPE_KEYBOARD = 0;
    private static final int OPTION_TYPE_OPTIONS = 1;
    private static final int OPTION_TYPE_EMOJI = 2;

    private int mType = OPTION_TYPE_NONE;


    @Override
    public void setPresenter(EmojiContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static EmojiFragment newInstance() {
        return new EmojiFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init adapter here
        mAdapter = new ContentAdapter(getActivity(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayoutCompact root = (RelativeLayoutCompact) inflater.inflate(R.layout.fragment_emoji, container, false);

        // do initial things here
        initValues();
        initViews(root);
        initHeadBar(root);
        initRecyclerView(root);
        initEvent(root);
        initOptions(root);
        initImmEvent(root);

        mPresenter.create();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void initImmEvent(RelativeLayoutCompact root) {

        root.setOnImmStatusChangedListener(new RelativeLayoutCompact.OnImmStatusChangedListener() {
            @Override
            public void show(int height) {
                SharePreferencesUtils.apply(getActivity(), Constants.SOFT_KEYBOARD_HEIGHT, height);
                mEmojiTager.getLayoutParams().height = height;
                mOptions.getLayoutParams().height = height;
                scrollChatToBottom();
            }

            @Override
            public void hidden() {

            }
        });
    }

    private void initValues() {
        mMinTextSize = getResources().getInteger(R.integer.min_textSize);
        mSpanPerSegment = getResources().getInteger(R.integer.span_per_segment);
        mDefaultTextSize = getResources().getInteger(R.integer.option_default_textSize);
        mSoftKeyboardDefaultHeight =
                getActivity().getResources().getDimensionPixelSize(R.dimen.soft_keyboard_default_size);
    }

    private void initOptions(View root) {
        mWithShadow = SharePreferencesUtils.withShadow(getActivity(), false);
        mTextSize = SharePreferencesUtils.textSize(getActivity(), mDefaultTextSize);

        SeekBar seekBar = (SeekBar) root.findViewById(R.id.text_size);
        seekBar.setProgress((mTextSize - mMinTextSize) / mSpanPerSegment);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.gif_source_array, android.R.layout.simple_spinner_dropdown_item);
        mGifSourceSpinner.setAdapter(adapter);

        int source = SharePreferencesUtils.getInt(getContext(), KEY_GIF_SORUCE);
        mGifSourceSpinner.setSelection(source);
    }

    private void initViews(View root) {
        mOptions = (CardView) root.findViewById(R.id.options);
        mEmojiTager = (EmojiTager) root.findViewById(R.id.emoji_tager);
        mEmojiRadioAdapter = new EmojiRadioAdapter(getActivity());
        mEmojiPagerAdapter = new EmojiPagerAdapter(getActivity());
        mEmojiTager.setRadioGroupAdapter(mEmojiRadioAdapter);
        mEmojiTager.setViewPagerAdapter(mEmojiPagerAdapter);

        int height = SharePreferencesUtils.getInt(getActivity(),
                Constants.SOFT_KEYBOARD_HEIGHT, mSoftKeyboardDefaultHeight);
        mEmojiTager.getLayoutParams().height = height;
        mOptions.getLayoutParams().height = height;

        mEmojiButton = (ImageView) root.findViewById(R.id.emoji_button);

        mGifs[0] = (ImageView) root.findViewById(R.id.gif3);
        mGifs[1] = (ImageView) root.findViewById(R.id.gif1);
        mGifs[2] = (ImageView) root.findViewById(R.id.gif2);

        mShareDialog = new ShareDialog(getContext());

        mGifSourceSpinner = (Spinner) root.findViewById(R.id.gif_source_spinner);
    }

    private void initEvent(final View root) {
        final TextView sendButton = (TextView) root.findViewById(R.id.send_button);
        final ImageView switchButton = (ImageView) root.findViewById(R.id.switch_button);

        mTextInputEditText = (TextInputEditText) root.findViewById(R.id.text_input);
        Compact.getInstance().disableShowSoftInput(mTextInputEditText);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = mTextInputEditText.getText().toString();
                mPresenter.processInput(inputText, mTextSize, mWithShadow);
                if (!TextUtils.isEmpty(inputText)) {
                    TCAgentUtils.TextInput(getActivity(), inputText);
                }
            }
        });
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType != OPTION_TYPE_OPTIONS) {
                    optionShowType(OPTION_TYPE_OPTIONS);
                } else {
                    optionShowType(OPTION_TYPE_KEYBOARD);
                }
            }
        });
        mEmojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType != OPTION_TYPE_EMOJI) {
                    optionShowType(OPTION_TYPE_EMOJI);
                } else {
                    optionShowType(OPTION_TYPE_KEYBOARD);
                }
            }
        });
        mTextInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mType == OPTION_TYPE_NONE ||
                            mType == OPTION_TYPE_OPTIONS) {
                        optionShowType(OPTION_TYPE_KEYBOARD);
                    }
                }
                scrollChatToBottom();
            }
        });
        mTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.getTrimmedLength(s) == 0) {
                    // hide send button, show switch button
                    sendButton.setVisibility(View.INVISIBLE);
                    switchButton.setVisibility(View.VISIBLE);
                } else {
                    // hide switch button, show send button
                    sendButton.setVisibility(View.VISIBLE);
                    switchButton.setVisibility(View.INVISIBLE);
                }
            }

            Timer mTimer = new Timer();
            private void searchIfInputPaused(final String text) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        mPresenter.instantGifSearch(text);
                    }
                };
                cancenTimerTask();
                mTimer = new Timer();
                mTimer.schedule(task, 1000);
            }

            private void cancenTimerTask() {
                mTimer.cancel();
                mTimer.purge();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.getTrimmedLength(s.toString()) != 0) {
                    searchIfInputPaused(s.toString());
                } else {
                    clearGifs();
                    cancenTimerTask();
                }
            }
        });
        mTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType == OPTION_TYPE_NONE || mType == OPTION_TYPE_OPTIONS) {
                    optionShowType(OPTION_TYPE_KEYBOARD);
                }
            }
        });
        mTextInputEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_UP && mType == OPTION_TYPE_KEYBOARD) {
                    mTextInputEditText.clearFocus();
                    mType = OPTION_TYPE_NONE;
                    clearGifs();
                }
                return false;
            }
        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mType != OPTION_TYPE_NONE) {
                    optionShowType(OPTION_TYPE_NONE);
                    return true;
                }
                return false;
            }
        });

        SeekBar seekBar = (SeekBar) root.findViewById(R.id.text_size);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextSize = progress * mSpanPerSegment + mMinTextSize;

                SharePreferencesUtils.applyTextSize(getActivity(), mTextSize);
                TCAgentUtils.UpdateTextSize(getActivity(), mTextSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // enter me
        ImageView rightOption = (ImageView) root.findViewById(R.id.right_option);
        rightOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DeveloperActivity.class));
            }
        });

        mEmojiPagerAdapter.setOnItemClickListener(new EmojiCategoryView.ContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, Emoji emoji) {
                int index = mTextInputEditText.getSelectionStart();
                Editable editable = mTextInputEditText.getText();
                editable.insert(index, emoji.emoji);
            }
        });

        mEmojiPagerAdapter.setOnDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = mTextInputEditText.getSelectionStart();
                if (index > 0) {
                    Editable editable = mTextInputEditText.getText();
                    int length = 1;
                    if (index > 1) {
                        String text = editable.toString().substring(index-2,index);
                        if (EmojiUtils.isEmoji(text)) {
                            length = 2;
                        }
                    }
                    editable.delete(index - length, index);
                }
            }
        });

        mGifSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.useGifSource(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerView(View root) {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initHeadBar(View root) {
        TextView title = (TextView) root.findViewById(R.id.title);
        ImageView left_option = (ImageView) root.findViewById(R.id.left_option);
        ImageView right_option = (ImageView) root.findViewById(R.id.right_option);
        title.setText(R.string.string_emoji);
        left_option.setVisibility(View.GONE);
    }

    private void scrollChatToBottom() {
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void optionShowType(int type) {
        mType = type;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (type) {
            case OPTION_TYPE_NONE:
                // hide all
                // do not focus
                imm.hideSoftInputFromWindow(mTextInputEditText.getWindowToken(), 0);
                mOptions.setVisibility(View.GONE);
                mEmojiTager.setVisibility(View.GONE);
                mEmojiButton.setImageResource(R.drawable.option_emoji);
                mTextInputEditText.clearFocus();
                break;
            case OPTION_TYPE_KEYBOARD:
                // show keyboard only
                // focus on edit text
                mTextInputEditText.requestFocus();
                imm.showSoftInput(mTextInputEditText, InputMethodManager.SHOW_FORCED);
                mOptions.setVisibility(View.GONE);
                mEmojiTager.setVisibility(View.GONE);
                mEmojiButton.setImageResource(R.drawable.option_emoji);
                TCAgentUtils.OptionClicked(getActivity(), Constants.LABEL_OPTION_HIDE);
                break;
            case OPTION_TYPE_OPTIONS:
                // show options only
                // do not focus on edit text
                imm.hideSoftInputFromWindow(mTextInputEditText.getWindowToken(), 0);
                mOptions.setVisibility(View.VISIBLE);
                mEmojiTager.setVisibility(View.GONE);
                mEmojiButton.setImageResource(R.drawable.option_emoji);
                mTextInputEditText.clearFocus();
                TCAgentUtils.OptionClicked(getActivity(), Constants.LABEL_OPTION_SHOW);
                break;
            case OPTION_TYPE_EMOJI:
                // show emoji only
                // focus on edit text
                mTextInputEditText.requestFocus();
                imm.hideSoftInputFromWindow(mTextInputEditText.getWindowToken(), 0);
                mOptions.setVisibility(View.GONE);
                mEmojiTager.setVisibility(View.VISIBLE);
                mEmojiButton.setImageResource(R.drawable.option_keyboard);
                break;
        }
        clearGifs();
    }

    /**
     *
     * @param view
     * @param data
     * @return
     */
    @Override
    public boolean onContentLongClicked(@NonNull View view,@NonNull Object data) {
        if (data instanceof TextPicItem) {
            Bitmap bitmap = BitmapUtils.convertViewToBitmap(view);

            mShareDialog.bind(new TextPicShare((TextPicItem) data, bitmap));
        }
        if (view instanceof ImageView
                && data instanceof GifChatItem) {
            mShareDialog.bind((Shareable) data);
        }
        mShareDialog.show();
        return true;
    }

    @Override
    public boolean onAvatarClicked(View view) {
        startActivity(new Intent(getActivity(), MeActivity.class));
        return true;
    }

    // This method is deprecated as it is not working on Android N
    private void notifyGalleryToUpdate(Uri uri) {
        Intent intent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        getActivity().sendBroadcast(intent);
    }

    public void scanPhoto(final String imageFileName) {
        if (mScanner == null) {
            mScanner = new SingleFileScanner(getActivity().getApplicationContext());
        }
        mScanner.scan(imageFileName);
    }


    /**
     * show Chats
     * @param data chat data
     */
    @Override
    public void showChats(List<ChatItem> data) {
        mAdapter.updateData(data);
        scrollChatToBottom();
    }

    @Override
    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void showEmptyText() {
        Toast.makeText(getActivity(), R.string.empty_text, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void clearEditText() {
        mTextInputEditText.getText().clear();
    }

    @Override
    public void updateMe(Me me) {
        mAdapter.updateProfile(me);
    }

    @Override
    public void initEmojiBoard(List<EmojiCategory> data) {
        mEmojiRadioAdapter.updateData(data);
        mEmojiPagerAdapter.updateData(data);
    }

    public boolean onBackPressed() {
        if (mType == OPTION_TYPE_EMOJI ||
                mType == OPTION_TYPE_OPTIONS) {
            optionShowType(OPTION_TYPE_NONE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void showGifs(@NonNull final List<Gif> gifs, final String tag) {
        for (int i = 0; i < gifs.size() && i < mGifs.length; i++) {
            final Gif gif = gifs.get(i);
            Glide.with(getActivity())
                    .load(gif.preview)
                    .asBitmap()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mGifs[i]);
            mGifs[i].setVisibility(View.VISIBLE);
            mGifs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.sendGif(gif, tag);
                    clearEditText();
                    clearGifs();
                }
            });
        }
    }

    @Override
    public void clearGifs() {
        mGifs[0].setVisibility(View.GONE);
        mGifs[1].setVisibility(View.GONE);
        mGifs[2].setVisibility(View.GONE);
    }

    /**
     * Classes for RecyclerView
     */
    private static class DefaultViewHolder extends BaseEmojiViewHolder<TextPicItem> {
        private final TextView item_content;
        private final ImageView item_avatar;
        private final TextView item_text;
        private final View item_text_container;

        public DefaultViewHolder(Context context, LayoutInflater inflater, ViewGroup parent) {
            super(context, inflater.inflate(R.layout.chat_item, parent, false));
            item_content = (TextView) itemView.findViewById(R.id.item_content);
            item_avatar = (ImageView) itemView.findViewById(R.id.item_avatar);
            item_text = (TextView) itemView.findViewById(R.id.text);
            item_text_container = itemView.findViewById(R.id.item_text_container);
        }

        @Override
        protected void bind(@NonNull final TextPicItem item) {
            if (TextPicItem.NULL.equals(item)) {
                return;
            }
            item_content.setText(item.content);
            if (item.textSize > 0) {
                item_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.textSize);
                item_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.textSize);
            }

            item_text.setText(item.content);

            item_text_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDelegate != null) {
                        mDelegate.onContentLongClicked(item_text_container, item);
                    } else {
                    }
                }
            });

            item_text_container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mDelegate != null) {
                        return mDelegate.onContentLongClicked(item_text_container, item);
                    } else {
                        return false;
                    }
                }
            });

            item_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDelegate != null) {
                        mDelegate.onAvatarClicked(v);
                    }
                }
            });
/*
            Glide.with(getContext())
                    .load(item.avatarResId)
                    .into(item_avatar);
*/
        }

        /**
         * tmp method to show profile
         */
        @Override
        public void bindProfile(Me mMe) {
//            item_avatar.setImageURI(Uri.fromFile(new File(mMe.getAvatar())));
        }

        @Override
        public void bindAvatar(Bitmap avatar) {
            item_avatar.setImageBitmap(avatar);
        }
    }

    private static class GifViewHolder extends BaseEmojiViewHolder<GifChatItem> {
        private ImageView item_gif;
        private ImageView item_avatar;

        public GifViewHolder(Context context, View itemView) {
            super(context, itemView);
            item_avatar = (ImageView) itemView.findViewById(R.id.item_avatar);
            item_gif = (ImageView) itemView.findViewById(R.id.item_raw);
            item_gif.setAdjustViewBounds(true);
        }

        public GifViewHolder(Context context, LayoutInflater inflater, ViewGroup parent) {
            this(context, inflater.inflate(R.layout.gif_chat_item, parent, false));
        }

        @Override
        protected void bind(@NonNull final GifChatItem item) {
            Glide.with(getContext())
                    .load(item.gif.url)
                    .placeholder(R.drawable.pic_loading_progress_bar)
                    .override(DisplayUtils.dp2px(getContext(), 160), Integer.MAX_VALUE)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(item_gif);

            item_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDelegate != null) {
                        mDelegate.onAvatarClicked(v);
                    }
                }
            });

            item_gif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDelegate != null) {
                        mDelegate.onContentLongClicked(v, item);
                    } else {
                    }
                }
            });

        }

        /**
         * tmp method to show profile
         */
        @Override
        public void bindProfile(Me mMe) {
//            item_avatar.setImageURI(Uri.fromFile(new File(mMe.getAvatar())));
        }
        @Override
        public void bindAvatar(Bitmap avatar) {
            item_avatar.setImageBitmap(avatar);
        }
    }

    static class ContentAdapter extends RecyclerView.Adapter<BaseEmojiViewHolder> {
        private final int VIEW_TYPE_TEXT_PIC = 0;
        private final int VIEW_TYPE_GIF = 1;

        private final Context mContext;
        private List<ChatItem> mData = new ArrayList<>();
        private final BaseEmojiViewHolder.EventDelegate mDelegate;
        private Me mMe;
        private Bitmap mAvatar;

        public ContentAdapter(Context context, BaseEmojiViewHolder.EventDelegate delegate) {
            super();
            mContext = context;
            mDelegate = delegate;
        }
        public Context getContext() {
            return mContext;
        }

        @Override
        public BaseEmojiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_GIF:
                    return new GifViewHolder(getContext(), LayoutInflater.from(getContext()), parent);
            }
            return new DefaultViewHolder(getContext(), LayoutInflater.from(getContext()), parent);
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position) instanceof TextPicItem) {
                return VIEW_TYPE_TEXT_PIC;
            }
            if (getItem(position) instanceof GifChatItem) {
                return VIEW_TYPE_GIF;
            }
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(BaseEmojiViewHolder holder, int position) {
            if (!Me.NULL.equals(mMe)) {
                holder.bindProfile(mMe);
                holder.bindAvatar(mAvatar);
            }
            holder.bind(getItem(position));
            holder.setEventDelegate(mDelegate);
        }

        private ChatItem getItem(int position) {
            return getItemCount() > position ? mData.get(position) : null;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void updateData(List<ChatItem> list) {
            mData.clear();
            mData.addAll(list);
            notifyDataSetChanged();
        }

        public void appendData(ChatItem item) {
            if (!TextPicItem.NULL.equals(item)) {
                mData.add(item);
                notifyDataSetChanged();
            }
        }

        public void appendData(List<ChatItem> list) {
            if (list != null && list.size() > 0) {
                mData.addAll(list);
                notifyDataSetChanged();
            }
        }

        /**
         * tmp methods to show Me profile
         * @param me
         */
        public void updateProfile(Me me) {
            mMe = me;
            mAvatar = BitmapFactory.decodeFile(me.getAvatar());
            notifyDataSetChanged();
        }
    }
}
