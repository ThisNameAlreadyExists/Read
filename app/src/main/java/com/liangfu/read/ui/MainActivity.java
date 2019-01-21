package com.liangfu.read.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.liangfu.read.R;
import com.liangfu.read.ui.fragment.AndroidFragment;
import com.liangfu.read.ui.fragment.BooksFragment;
import com.liangfu.read.ui.fragment.FunFragment;
import com.liangfu.read.ui.fragment.HomeFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    @Bind(R.id.layFrame)
    FrameLayout layFrame;
    @Bind(R.id.bottom_bar)
    BottomNavigationBar bottomBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_tv_title)
    TextView mainTvTitle;

    private HomeFragment homeFragment;
    private AndroidFragment androidFragment;
    private BooksFragment booksFragment;
    private FunFragment funFragment;

    private String[] titles = {"首页", "android开发", "小说", "轻松一刻"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBottomNavigationBar();
    }

    private void initBottomNavigationBar() {
        bottomBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomBar.addItem(new BottomNavigationItem(R.mipmap.ic_home_white_24dp, titles[0]).setActiveColorResource(R.color.bottomNavHomePage))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, titles[1]).setActiveColorResource(R.color.bottomNavNews))
                .addItem(new BottomNavigationItem(R.mipmap.ic_book_white_24dp, titles[2]).setActiveColorResource(R.color.bottomNavBook))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, titles[3]).setActiveColorResource(R.color.bottomNavReleax))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomBar.setTabSelectedListener(this);
        setDefaultFragment();
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        mainTvTitle.setText(titles[0]);
        addFrag(homeFragment);
        getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
    }

    /*添加Frag*/
    private void addFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (frag != null && !frag.isAdded()) {
            ft.add(R.id.layFrame, frag);
        }
        ft.commit();
    }

    /*隐藏所有fragment*/
    private void hideAllFrag() {
        hideFrag(homeFragment);
        hideFrag(androidFragment);
        hideFrag(booksFragment);
        hideFrag(funFragment);
    }

    /*隐藏frag*/
    private void hideFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && frag.isAdded()) {
            ft.hide(frag);
        }
        ft.commit();
    }

    @Override
    public void onTabSelected(int position) {
        hideAllFrag();//先隐藏所有frag
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                mainTvTitle.setText(titles[0]);
                addFrag(homeFragment);
                getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
                break;
            case 1:
                if (androidFragment == null) {
                    androidFragment = new AndroidFragment();
                }
                mainTvTitle.setText(titles[1]);
                addFrag(androidFragment);
                getSupportFragmentManager().beginTransaction().show(androidFragment).commit();
                break;
            case 2:
                if (booksFragment == null) {
                    booksFragment = new BooksFragment();
                }
                mainTvTitle.setText(titles[2]);
                addFrag(booksFragment);
                getSupportFragmentManager().beginTransaction().show(booksFragment).commit();
                break;
            case 3:
                if (funFragment == null) {
                    funFragment = new FunFragment();
                }
                mainTvTitle.setText(titles[3]);
                addFrag(funFragment);
                getSupportFragmentManager().beginTransaction().show(funFragment).commit();
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
