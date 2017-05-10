package com.sctdroid.app.textemoji.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sctdroid.app.textemoji.data.bean.ChatItem;
import com.sctdroid.app.textemoji.data.bean.Me;

/**
 * Created by lixindong on 4/20/17.
 */

public abstract class BaseEmojiViewHolder<T extends ChatItem> extends RecyclerView.ViewHolder {
    private Context mContext;
    protected EventDelegate mDelegate;

    public BaseEmojiViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
    }

    protected abstract void bind(@NonNull T chat);
    /**
     * tmp method to show profile
     */
    public abstract void bindProfile(Me mMe);
    public abstract void bindAvatar(Bitmap avatar);

    protected Context getContext() {
        return mContext;
    }

    public interface EventDelegate {
        boolean onContentLongClicked(View view, Object data);
        boolean onAvatarClicked(View view);
    }

    public void setEventDelegate(EventDelegate delegate) {
        mDelegate = delegate;
    }
}
