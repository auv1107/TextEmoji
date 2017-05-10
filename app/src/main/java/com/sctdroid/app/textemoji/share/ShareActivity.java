package com.sctdroid.app.textemoji.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.bean.TextPicItem;
import com.sctdroid.app.textemoji.utils.Constants;
import com.sctdroid.app.textemoji.utils.SharePreferencesUtils;
import com.sctdroid.app.textemoji.utils.TCAgentUtils;
import com.sctdroid.app.textemoji.utils.WeixinShareUtils;
import com.sctdroid.app.textemoji.utils.compact.Compact;
import com.sctdroid.app.textemoji.views.TextEmoji;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by lixindong on 4/25/17.
 */

public class ShareActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Compact.getInstance().init(this);
        ShareSDK.initSDK(this, Constants.SHARE_SDK_APPID);
        TCAgentUtils.onPageStart(this, ShareActivity.class.getSimpleName());

        Intent intent = getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction())
                && "text/plain".equals(intent.getType())) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);

            boolean withShadow = SharePreferencesUtils.withShadow(this, false);
            int textSize = SharePreferencesUtils.textSize(this, getResources().getInteger(R.integer.option_default_textSize));

            // prepare to generate data
            TextPicItem item = new TextPicItem.Builder()
                    .content(text)
                    .textSize(textSize)
                    .withShadow(withShadow)
                    .build();
            TextEmoji textEmoji = new TextEmoji(this);
            textEmoji.setText(item);

            // prepare data to share
            Bitmap bitmap = textEmoji.getBitmap(true);

            // save and share it
//            Uri uri = StorageHelper.saveBitmap(bitmap, filename, StorageHelper.DIR_TMP);
            TCAgentUtils.Share(this, Constants.LABEL_FROM_SHARE, item.content);
            WeixinShareUtils.shareImage(bitmap);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Compact.DestoryInstance();
        ShareSDK.stopSDK();
        TCAgentUtils.onPageEnd(this, ShareActivity.class.getSimpleName());
    }
}
