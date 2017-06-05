package com.sctdroid.app.textemoji.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.source.MeLoader;
import com.sctdroid.app.textemoji.data.source.MeRepository;
import com.sctdroid.app.textemoji.data.source.local.MeLocalDataSource;
import com.sctdroid.app.textemoji.utils.ActivityUtils;
import com.sctdroid.app.textemoji.utils.TCAgentUtils;

/**
 * Created by lixindong on 4/20/17.
 */

public class MeActivity extends AppCompatActivity {
    @Override
    protected void onPause() {
        super.onPause();
        TCAgentUtils.onPageEnd(this, MeActivity.class.getSimpleName());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_me);

        MeFragment fragment = (MeFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = MeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        MeRepository repository = MeRepository.getInstance(new MeLocalDataSource(this), null);
        MeLoader loader = new MeLoader(this, repository);
        MeContract.Presenter presenter = new MePresenter(loader, getSupportLoaderManager(), repository, fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgentUtils.onPageStart(this, MeActivity.class.getSimpleName());
    }
}
