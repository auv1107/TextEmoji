package com.sctdroid.app.textemoji.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.utils.Constants;
import com.sctdroid.app.textemoji.utils.SharePreferencesUtils;
import com.sctdroid.app.textemoji.utils.TCAgentUtils;
import com.sctdroid.app.textemoji.utils.WeixinShareUtils;
import com.sctdroid.app.textemoji.utils.compact.Compact;

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

            int textSize = SharePreferencesUtils.textSize(this, getResources().getInteger(R.integer.option_default_textSize));

            Bitmap bitmap = getShareBitmap(text, textSize);
            TCAgentUtils.Share(this, Constants.LABEL_FROM_SHARE, text);
            WeixinShareUtils.shareImage(bitmap);
            finish();
        }
    }

    private Bitmap getShareBitmap(String text, int textSize) {
        View view = getLayoutInflater().inflate(R.layout.layout_text_card, (ViewGroup) findViewById(android.R.id.content), false);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);
        textView.setTextSize(textSize);

        view.setDrawingCacheEnabled(true);

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false); // clear drawing cache

        return b;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Compact.DestoryInstance();
        ShareSDK.stopSDK();
        TCAgentUtils.onPageEnd(this, ShareActivity.class.getSimpleName());
    }
}
