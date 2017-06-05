package com.sctdroid.app.textemoji.developer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.utils.ActivityUtils;
import com.sctdroid.app.textemoji.utils.TCAgentUtils;

/**
 * Created by lixindong on 4/21/17.
 */

public class DeveloperActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_developer);

        DeveloperFragment fragment = (DeveloperFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = DeveloperFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgentUtils.onPageEnd(this, DeveloperActivity.class.getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgentUtils.onPageStart(this, DeveloperActivity.class.getSimpleName());
    }
}
