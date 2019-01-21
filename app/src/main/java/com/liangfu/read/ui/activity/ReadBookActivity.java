package com.liangfu.read.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liangfu.read.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: LF
 * @date: 2016/11/11 11:36
 */
public class ReadBookActivity extends AppCompatActivity {

    @Bind(R.id.tv_read_chapter)
    TextView tvReadChapter;
    @Bind(R.id.tv_read_chapter_content)
    TextView tvReadChapterContent;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_read_lastChapter)
    Button btnReadLastChapter;
    @Bind(R.id.btn_read_back)
    Button btnReadBack;
    @Bind(R.id.btn_read_nextChapter)
    Button btnReadNextChapter;

    String chapterName;
    String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_book);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText("正文");
        Intent intent = this.getIntent();
        String chapterUrl = intent.getStringExtra("CHAPTER_URL");
        if (chapterUrl != null || !chapterUrl.contains("")) {
            getChapterContent(chapterUrl);
        }
    }

    private void getChapterContent(final String chapterUrl) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    tvReadChapter.setText(chapterName);
                    tvReadChapterContent.setText(content);
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                Message msg = new Message();
                try {
                    doc = Jsoup.connect(chapterUrl).timeout(30000).get();
                    chapterName = doc.select("div.text.t_c h1").first().text();
                    Element contentElement = doc.select("#content").first();
                    Elements scripts = contentElement.select("script");
                    if (scripts != null && scripts.size() > 6) {
                        for (int i = 0, size = scripts.size(); i < size; i++) {
                            String html = scripts.get(i).html();
                            if (html.indexOf("output") >= 0) {
                                String txtName = html.toString();
                                String url = "http://121.40.22.93/novel/192/945/" + txtName.substring(txtName.indexOf("\"") + 1, txtName.lastIndexOf("\""));
                                getContentDetail(url);
                            }
                        }
                    } else {
                        String contentStr = contentElement.text();
                        if (contentStr.contains("quanxiaoshuo.com（全小说无弹窗）")) {
                            content = contentStr.substring(contentStr.indexOf("请记住我们的网址。") + 10, contentStr.length()).replace(" 　　", "\r\n　　");
                        } else {
                            content = contentStr;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void getContentDetail(String path) {
        // 新建一个URL对象
        URL url = null;
        HttpURLConnection urlConn = null;
        try {
            url = new URL(path);
            // 打开一个HttpURLConnection连接
            urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String data = readStream(urlConn.getInputStream());
                String result = data.substring(data.indexOf("'") + 1, data.lastIndexOf("'"));
                content = result.replace("<br/>", "\r\n");
            } else {
                Toast.makeText(this, "请求获取数据失败，请稍候再试", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            if(urlConn != null){
                urlConn.disconnect();
            }

        }


    }

    private static String readStream(InputStream inputStream) throws Exception {
        BufferedReader reader_post = new BufferedReader(new InputStreamReader(inputStream, "GB2312"));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = reader_post.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        this.finish();
    }

    @OnClick({R.id.btn_read_lastChapter, R.id.btn_read_back, R.id.btn_read_nextChapter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_read_lastChapter:
                break;
            case R.id.btn_read_back:
                this.finish();
                break;
            case R.id.btn_read_nextChapter:
                break;
        }
    }
}
