package com.sctdroid.app.textemoji.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.sctdroid.app.textemoji.utils.compact.Compact;

import java.io.File;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by lixindong on 4/25/17.
 */

public class WeixinShareUtils {
    public static void shareImage(Context context, Uri uri) {
        Intent sendIntent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        sendIntent.setComponent(comp);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("image/*");
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(sendIntent);
    }

    public static void shareImage(Bitmap bitmap) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_EMOJI);
        shareParams.setImageData(bitmap);

        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.share(shareParams);
    }

    public static void shareImageUrl(String url) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_EMOJI);
        shareParams.setImageUrl(url);

        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.share(shareParams);
    }

    public static void shareImageImageToWechat(String path) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_EMOJI);
        shareParams.setImagePath(path);

        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.share(shareParams);
    }

    public static void shareImageToQQ(Context context, String path) {
        File file = new File(path);
        if (file.exists()) {
            Uri uri = Compact.getInstance().fromFile(new File(path));

            Intent sendIntent = new Intent();
            ComponentName comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
            sendIntent.setComponent(comp);
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("image/*");
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(sendIntent);
        }
    }

    public static void shareImageToOthers(Context context, String path) {
        File file = new File(path);
        if (file.exists()) {
            Uri uri = Compact.getInstance().fromFile(new File(path));
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("image/*");
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(sendIntent);
        }
    }
}
