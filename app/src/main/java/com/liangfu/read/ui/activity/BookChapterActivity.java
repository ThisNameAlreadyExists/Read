package com.liangfu.read.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.liangfu.read.Adapter.ChapterAdapter;
import com.liangfu.read.R;
import com.liangfu.read.base.Constants;
import com.liangfu.read.model.Chapter;
import com.sixth.adwoad.AdwoAdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: LF
 * @date: 2016/11/10 11:52
 */
public class BookChapterActivity extends AppCompatActivity implements ChapterAdapter.OnItemClickLitener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_chapter_title)
    TextView tvChapterTitle;
    @Bind(R.id.tv_chapter_bookType)
    TextView tvChapterBookType;
    @Bind(R.id.tv_chapter_author)
    TextView tvChapterAuthor;
    @Bind(R.id.tv_chapter_desc)
    TextView tvChapterDesc;
    @Bind(R.id.rc_chapter_list)
    RecyclerView rcChapterList;
    @Bind(R.id.btn_chapter_reverse)
    Button btnChapterReverse;
    @Bind(R.id.ll_ad_layout)
    LinearLayout llAdLayout;

    private String bookUrl;

    private ChapterAdapter adapter;

    private List<Chapter> chapters;

    private Chapter mChapter;

    /**
     * 是否为正序
     */
    private boolean isNormal = true;

    String title = null;
    String type = null;
    String author = null;
    String desc = null;

    private AdwoAdView adView = null;
    private LayoutParams params = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_chapter);
        ButterKnife.bind(this);
        getChapter();
        initView();
        initAD();
    }

    private void initAD() {
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        adView = new AdwoAdView(this, Constants.PUBLISHER_ID, true, 20);
//		adwo广告条的宽高默认20*50乘以屏幕密度，默认宽是不充满屏宽，如果您想设置设置广告条宽充满屏幕宽您可以在实例化广告对象之前调用AdwoAdView.setBannerMatchScreenWidth(true)
//		设置广告条充满屏幕宽
//		adView.setBannerMatchScreenWidth(true);
//		设置单次请求
//		adView.setRequestInterval(0);
        //如果你有合作渠道，请设置合作渠道id，具体id值请联系安沃工作人员 。可选择设置
//		adView.setMarketId((byte) 9);
        // 设置广告监听回调
//        adView.setListener(this);
        // 把广告条加入界面布局
        llAdLayout.addView(adView, params);
    }

    private void initView() {
        tvTitle.setText("目录");
        tvChapterDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
        Intent intent = this.getIntent();
        bookUrl = intent.getStringExtra("BOOK_URL");
        adapter = new ChapterAdapter(this, chapters);
        rcChapterList.setLayoutManager(new LinearLayoutManager(this));
        rcChapterList.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    public void getChapter() {
        chapters = new ArrayList<>();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    tvChapterTitle.setText(title);
                    tvChapterBookType.setText(type);
                    tvChapterAuthor.setText(author);
                    tvChapterDesc.setText(desc);
                    adapter.notifyDataSetChanged();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                Message msg = new Message();
                try {
                    doc = Jsoup.connect(bookUrl).timeout(30000).get();
                    title = doc.select("div.text.t_c h1 a").first().text();
                    type = doc.select("div.f_l.t_r.w3 font").first().text();
                    author = doc.select("div.f_l.t_c.w2 a").first().text();
                    if (doc.select("div.desc").first().text() == null) {
                        desc = doc.select("div.desc span").first().text();
                    } else {
                        desc = doc.select("div.desc").first().text();
                    }
                    Element chapterElement = doc.select("div.chapters").first();
                    Elements mastheads = chapterElement.select("div.chapter");
                    for (int i = 0, size = mastheads.size(); i < size; i++) {
                        mChapter = new Chapter();
                        Element chapterName = mastheads.get(i).select("a").first();
                        mChapter.setChapter(chapterName.text());
                        mChapter.setChapterUrl("http://quanxiaoshuo.com" + chapterName.attr("href"));
                        chapters.add(mChapter);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.what = 1;
                msg.obj = chapters;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onItemClick(View view, int position, String url) {
        Intent intent = new Intent(this, ReadBookActivity.class);
        intent.putExtra("CHAPTER_URL", url);
        startActivity(intent);
    }

    @OnClick({R.id.iv_back, R.id.btn_chapter_reverse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.btn_chapter_reverse:
                if (chapters != null && chapters.size() > 0) {
                    if (isNormal) {
                        btnChapterReverse.setText("正序");
                        isNormal = false;
                    } else {
                        btnChapterReverse.setText("倒序");
                        isNormal = true;
                    }
                    Collections.reverse(chapters);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
