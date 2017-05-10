package com.sctdroid.app.textemoji.emoji;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.source.ChatsLoader;
import com.sctdroid.app.textemoji.data.source.ChatsRepository;
import com.sctdroid.app.textemoji.data.source.EmojiLoader;
import com.sctdroid.app.textemoji.data.source.EmojiRepository;
import com.sctdroid.app.textemoji.data.source.GifRepository;
import com.sctdroid.app.textemoji.data.source.GifsLoader;
import com.sctdroid.app.textemoji.data.source.MeLoader;
import com.sctdroid.app.textemoji.data.source.MeRepository;
import com.sctdroid.app.textemoji.data.source.local.ChatsLocalDataSource;
import com.sctdroid.app.textemoji.data.source.local.EmojiLocalDataSource;
import com.sctdroid.app.textemoji.data.source.local.MeLocalDataSource;
import com.sctdroid.app.textemoji.data.source.remote.GifRemoteDataSource;
import com.sctdroid.app.textemoji.slide.SlidePagerActivity;
import com.sctdroid.app.textemoji.utils.ActivityUtils;
import com.sctdroid.app.textemoji.utils.Constants;
import com.sctdroid.app.textemoji.utils.SharePreferencesUtils;
import com.sctdroid.app.textemoji.utils.TCAgentUtils;
import com.sctdroid.app.textemoji.utils.ToastUtils;
import com.sctdroid.app.textemoji.utils.compact.Compact;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by lixindong on 4/18/17.
 */

public class EmojiActivity extends AppCompatActivity {
    private EmojiFragment mEmojiFragment;
    private EmojiPresenter mEmojiPresenter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Compact.DestoryInstance();
        ToastUtils.DestoryInstance();
        ShareSDK.stopSDK();
        TCAgentUtils.onPageEnd(this, EmojiActivity.class.getSimpleName());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_emoji);

        Compact.getInstance().init(this);
        ShareSDK.initSDK(this, Constants.SHARE_SDK_APPID);
        TCAgentUtils.onPageStart(this, EmojiActivity.class.getSimpleName());

        boolean isFirstTimeStart = SharePreferencesUtils.isFirstTimeStart(this);
        SharePreferencesUtils.applyFirstTimeStart(this, false);

        mEmojiFragment = (EmojiFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mEmojiFragment == null) {
            mEmojiFragment = EmojiFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mEmojiFragment, R.id.contentFrame);
        }

        ChatsRepository repository = ChatsRepository.getInstance(new ChatsLocalDataSource(this), null);
        ChatsLoader chatsLoader = new ChatsLoader(this, repository);
        MeRepository meRepository = MeRepository.getInstance(new MeLocalDataSource(this), null);
        MeLoader meLoader = new MeLoader(this, meRepository);
        EmojiRepository emojiRepository = EmojiRepository.getInstance(new EmojiLocalDataSource(this), null);
        EmojiLoader emojiLoader = new EmojiLoader(this, emojiRepository);
        GifRepository gifRepository = GifRepository.getInstance(null, new GifRemoteDataSource());
        GifsLoader gifsLoader = new GifsLoader(this, gifRepository);
        mEmojiPresenter = new EmojiPresenter(emojiLoader, meLoader, chatsLoader, gifsLoader, getSupportLoaderManager(), repository, gifRepository, mEmojiFragment);
        mEmojiPresenter.isFirstTime(isFirstTimeStart);

        if (isFirstTimeStart) {
            viewPictures();
        }
    }

    private static final ArrayList<Integer> IMAGES = new ArrayList<>();
    static {
            IMAGES.add(R.drawable.indicator_step_1);
            IMAGES.add(R.drawable.indicator_step_2);
            IMAGES.add(R.drawable.indicator_step_3);
            IMAGES.add(R.drawable.indicator_step_4);
            IMAGES.add(R.drawable.indicator_step_5);
    }

    public void viewPictures() {
        Intent intent = new Intent(this, SlidePagerActivity.class);
        intent.putExtra(SlidePagerActivity.EXTRA_TITLE, "");
        intent.putIntegerArrayListExtra(SlidePagerActivity.EXTRA_PICTURES, IMAGES);
        startActivity(intent);
    }


    private long lastPressTime = 0;

    @Override
    public void onBackPressed() {
        if (mEmojiFragment.onBackPressed()) {
            return;
        }
        if (isFirstTimePress()) {
            return;
        }
        super.onBackPressed();
    }

    private boolean isFirstTimePress() {
        long current = System.currentTimeMillis();
        long span = current - lastPressTime;
        if (span >= Constants.EXIT_TIME_SPAN) {
            lastPressTime = current;
            ToastUtils.show(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT);
            return true;
        } else {
            return false;
        }
    }
}
