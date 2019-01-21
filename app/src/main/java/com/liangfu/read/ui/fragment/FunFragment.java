package com.liangfu.read.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liangfu.read.Adapter.ITInformationAdapter;
import com.liangfu.read.R;
import com.liangfu.read.model.ITArticle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: LF
 * @date: 2016/11/9 9:26
 */
public class FunFragment extends Fragment {

    private RecyclerView idRecyclerview;

    private ITInformationAdapter adapter;

    private ITArticle ITArticle;

    private List<ITArticle> ITArticleList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        initData();
        initView(view);

        return view;
    }

    private void initData() {
        ITArticleList = new ArrayList<>();

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
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
                    doc = Jsoup.connect("http://jcodecraeer.com/plus/list.php?tid=4").timeout(10000).get();
                    Elements mastheads = doc.select("li.archive-item");
                    for(int i = 0, size = mastheads.size();i < size;i++){
                        ITArticle = new ITArticle();
                        Element titleElement = mastheads.get(i).select("li.archive-item a").first();
                        Element imgUrlElement = mastheads.get(i).select("li.archive-item a div.covercon img").first();
                        Element summaryElement = mastheads.get(i).select("div.archive-detail p").first();
                        Element postTimeElement = mastheads.get(i).select("div.archive-detail div.archive-data span.glyphicon-class").first();
                        ITArticle.setSummary(summaryElement.text());
                        ITArticle.setUrl("http://www.jcodecraeer.com/" + titleElement.attr("href"));
                        if(imgUrlElement != null){
                            ITArticle.setImageUrl("http://www.jcodecraeer.com/" + imgUrlElement.attr("src"));
                        }
                        ITArticle.setPostTime(postTimeElement.text());
                        ITArticle.setTitle(titleElement.attr("title"));
                        ITArticleList.add(ITArticle);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.what = 1;
                msg.obj = ITArticleList;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void initView(View view) {
        idRecyclerview = (RecyclerView) view.findViewById(R.id.id_recyclerview);
        adapter = new ITInformationAdapter(getActivity(), ITArticleList);
        idRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        idRecyclerview.setAdapter(adapter);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static FunFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        FunFragment fragment = new FunFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.id_recyclerview)
    public void onClick() {
    }
}
