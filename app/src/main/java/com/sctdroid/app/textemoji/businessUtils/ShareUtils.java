package com.sctdroid.app.textemoji.businessUtils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.bean.Shareable;
import com.sctdroid.app.textemoji.emoji.StorageHelper;
import com.sctdroid.app.textemoji.utils.Constants;
import com.sctdroid.app.textemoji.utils.EncoderUtils;
import com.sctdroid.app.textemoji.utils.TCAgentUtils;
import com.sctdroid.app.textemoji.utils.ToastUtils;
import com.sctdroid.app.textemoji.utils.WeixinShareUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by lixindong on 5/26/17.
 */

public class ShareUtils {
    public static void saveAndShare(final Context context, final Shareable shareable, final SharePlatform platform) {
        if (shareable.isBitmap()) {
            String name = EncoderUtils.encodeSHA1(System.currentTimeMillis() + "") + ".png";
            StorageHelper.saveBitmap(shareable.getBitmap(), name, StorageHelper.DIR_TMP);
            share(context, StorageHelper.DIR_TMP + name, false, platform);
            TCAgentUtils.ShareToChannel(context, platform, true, shareable.getTag());
        } else {
            Glide.with(context)
                    .load(shareable.getUrl())
                    .downloadOnly(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                            String dir = StorageHelper.DIR_TMP;
                            String absolutePath = dir + EncoderUtils.encodeSHA1(System.currentTimeMillis() + "") + ".gif";
                            StorageHelper.checkAndMkdir(dir);
                            File f = new File(absolutePath);
                            try {
                                StorageHelper.copy(resource, f);
                                share(context, absolutePath, false, platform);
                                TCAgentUtils.ShareGif(context, Constants.LABEL_FROM_EMOJI, shareable.getTag());
                                TCAgentUtils.ShareToChannel(context, platform, false, shareable.getTag());
                            } catch (IOException e) {
                                e.printStackTrace();
                                ToastUtils.show(context, R.string.some_things_wrong, Toast.LENGTH_SHORT);
                            }
                        }
                    });
        }
    }

    private static void share(Context context, String absolutePath, boolean isBitmap, SharePlatform platform) {
        switch (platform) {
            case WECHAT:
                WeixinShareUtils.shareImageToWechat(absolutePath, isBitmap);
                break;
            case QQ:
                WeixinShareUtils.shareImageToQQ(context, absolutePath);
                break;
            case OTHERS:
            default:
                WeixinShareUtils.shareImageToOthers(context, absolutePath);
                break;
        }
    }

    public enum SharePlatform {
        WECHAT,
        QQ,
        OTHERS
    }
}
