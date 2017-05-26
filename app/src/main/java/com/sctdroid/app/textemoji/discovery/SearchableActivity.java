package com.sctdroid.app.textemoji.discovery;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.source.GifRepository;
import com.sctdroid.app.textemoji.data.source.GifsLoader;
import com.sctdroid.app.textemoji.data.source.remote.SooGifRemoteDataSource;
import com.sctdroid.app.textemoji.utils.ActivityUtils;

public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.title_activity_search_result);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Get the intent, verify the action and get the query
        String query = "";

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }

        GifFragment fragment = (GifFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = GifFragment.newInstance(query);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        GifRepository repository = GifRepository.getInstance(null, new SooGifRemoteDataSource());
        GifsLoader loader = new GifsLoader(this, repository);
        GifPresenter presenter = new GifPresenter(fragment, getSupportLoaderManager(), loader, repository);
    }
}
