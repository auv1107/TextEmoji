package com.sctdroid.app.textemoji;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sctdroid.app.textemoji.data.source.JokesLoader;
import com.sctdroid.app.textemoji.data.source.JokesRepository;
import com.sctdroid.app.textemoji.data.source.local.JokesLocalDataSource;
import com.sctdroid.app.textemoji.data.source.remote.JokesRemoteDataSource;
import com.sctdroid.app.textemoji.jokes.JokeFragment;
import com.sctdroid.app.textemoji.jokes.JokePresenter;
import com.sctdroid.app.textemoji.utils.TCAgentUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<String> mTabList = new ArrayList<>();
    private TabLayoutFragmentAdapter mAdapter;
    private int[] mTabImgs = new int[]{R.drawable.home, R.drawable.collect, R.drawable.collect, R.drawable.collect};
    private List<Fragment> mFragments = new ArrayList<>();
    private JokePresenter mJokePresenter;
    private JokesRepository mJokesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        initTabList();
        mAdapter = new TabLayoutFragmentAdapter(getSupportFragmentManager(), mTabList, this, mFragments, mTabImgs);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(mAdapter.getTabView(i));
        }
        mTabLayout.addOnTabSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgentUtils.onPageStart(this, MainActivity.class.getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgentUtils.onPageEnd(this, MainActivity.class.getSimpleName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        initFragmentList();
    }

    /**
     * add Fragment
     */
    public void initFragmentList() {
        mFragments.clear();
        JokeFragment jokeFragment = JokeFragment.newInstance(getString(R.string.item_home));
        // Create the presenter
        mJokesRepository = JokesRepository.getInstance(new JokesRemoteDataSource(), new JokesLocalDataSource());
        JokesLoader tasksLoader = new JokesLoader(this, mJokesRepository);
        mJokePresenter = new JokePresenter(
                tasksLoader,
                getSupportLoaderManager(),
                mJokesRepository,
                jokeFragment
        );

        mFragments.add(jokeFragment);
        mFragments.add(CollectFragment.newInstance(getString(R.string.item_collect)));
        mFragments.add(CollectFragment.newInstance(getString(R.string.item_collect)));
        mFragments.add(CollectFragment.newInstance(getString(R.string.item_collect)));

    }

    /**
     * init the tab list.
     */
    private void initTabList() {
        mTabList.clear();
        mTabList.add(getString(R.string.item_home));
        mTabList.add(getString(R.string.item_collect));
        mTabList.add(getString(R.string.item_collect));
        mTabList.add(getString(R.string.item_collect));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        setTabSelectedState(tab);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        setTabUnSelectedState(tab);
    }


    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setTabSelectedState(TabLayout.Tab tab) {
        View customView = tab.getCustomView();
        TextView tabText = (TextView) customView.findViewById(R.id.tv_tab_text);
        ImageView tabIcon = (ImageView) customView.findViewById(R.id.iv_tab_icon);
        tabText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        String s = tabText.getText().toString();
        if (getString(R.string.item_home).equals(s)) {
            tabIcon.setImageResource(R.drawable.home_fill);
        } else if (getString(R.string.item_collect).equals(s)) {
            tabIcon.setImageResource(R.drawable.collect_fill);
        } else if (getString(R.string.item_collect).equals(s)) {
            tabIcon.setImageResource(R.drawable.collect_fill);
        } else if (getString(R.string.item_collect).equals(s)) {
            tabIcon.setImageResource(R.drawable.collect_fill);
        }
    }

    private void setTabUnSelectedState(TabLayout.Tab tab) {
        View customView = tab.getCustomView();
        TextView tabText = (TextView) customView.findViewById(R.id.tv_tab_text);
        ImageView tabIcon = (ImageView) customView.findViewById(R.id.iv_tab_icon);
        tabText.setTextColor(ContextCompat.getColor(this, R.color.black_1));
        String s = tabText.getText().toString();
        if (getString(R.string.item_home).equals(s)) {
            tabIcon.setImageResource(R.drawable.home);
        } else if (getString(R.string.item_collect).equals(s)) {
            tabIcon.setImageResource(R.drawable.collect);
        } else if (getString(R.string.item_collect).equals(s)) {
            tabIcon.setImageResource(R.drawable.collect);
        } else if (getString(R.string.item_collect).equals(s)) {
            tabIcon.setImageResource(R.drawable.collect);
        }
    }
}
