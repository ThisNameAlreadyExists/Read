package com.liangfu.read.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.liangfu.read.Adapter.BooksAdapter;
import com.liangfu.read.R;
import com.liangfu.read.model.Book;
import com.liangfu.read.ui.activity.BookChapterActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author: LF
 * @date: 2016/11/9 9:26
 */
public class BooksFragment extends Fragment implements BooksAdapter.OnItemClickLitener {

    @Bind(R.id.id_recyclerview_book)
    RecyclerView idRecyclerviewBook;
    @Bind(R.id.pf_refresh)
    PtrClassicFrameLayout mPtrFrame;
    @Bind(R.id.et_search_book)
    EditText etSearchBook;

    private BooksAdapter adapter;

    private Book mBook;

    private List<Book> mBookList;

    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF recyclerAdapter;

    private int pageIndex = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_fragment, container, false);
        ButterKnife.bind(this, view);
        mBookList = new ArrayList<>();
        getBookData("http://quanxiaoshuo.com/top/", 1);
        initView();
        return view;
    }

    private void getBookData(String href, Integer page) {
        mBookList.clear();
        pageIndex = 1;
        final String url;
        if(page == null){
            url = href;
        }else {
            url = href + page;
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                Message msg = new Message();
                try {
                    doc = Jsoup.connect(url).timeout(30000).get();
                    Elements mastheads = doc.select("ul.list_content");
                    for (int i = 0, size = mastheads.size(); i < size; i++) {
                        mBook = new Book();
                        Element titleElement = mastheads.get(i).select("li.cc2 a").first();
                        Element newChatperElement = mastheads.get(i).select("li.cc3 a").first();
                        Element authorElement = mastheads.get(i).select("li.cc4 a").first();
                        Element updTimeElement = mastheads.get(i).select("li.cc5").first();
                        mBook.setTitle(titleElement.text());
                        mBook.setAuthor(authorElement.text());
                        mBook.setAuthorUrl(authorElement.attr("href"));
                        mBook.setBookUrl("http://quanxiaoshuo.com" + titleElement.attr("href"));
                        mBook.setNewChapter(newChatperElement.text());
                        mBook.setNewChapterUrl("http://quanxiaoshuo.com" + newChatperElement.attr("href"));
                        mBook.setUpdTime(updTimeElement.text());
                        mBookList.add(mBook);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.what = 1;
                msg.obj = mBookList;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void initView() {
        idRecyclerviewBook.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BooksAdapter(getActivity(), mBookList);
        recyclerAdapter = new RecyclerAdapterWithHF(adapter);
        idRecyclerviewBook.setAdapter(recyclerAdapter);
        adapter.setOnItemClickListener(this);
        initRefresh();
        etSearchBook.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    if(textView.getText().toString().trim().equals("")){
                        return false;
                    }
                    getBookData("http://quanxiaoshuo.com/s_" + textView.getText().toString(), null);
                    return true;
                }
                return false;
            }
        });
    }

    private void initRefresh(){
        //下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this); //下拉刷新一些设置 详情参考文档
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        //下拉刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新返回第一页
                getBookData("http://quanxiaoshuo.com/top/", 1);
                //延时一秒后显示完成
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mPtrFrame.refreshComplete();
                        mPtrFrame.setLoadMoreEnable(true);
                    }
                }, 2000);
            }
        });
        //上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                pageIndex++;
                loadBooks("http://quanxiaoshuo.com/top/", pageIndex);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mPtrFrame.loadMoreComplete(true);
                    }
                }, 2000);
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static BooksFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        BooksFragment fragment = new BooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        String bookUrl = mBookList.get(position).getBookUrl();
        if (bookUrl.contains("http://quanxiaoshuo.com//")) {
            return;
        }
        Intent intent = new Intent(getActivity(), BookChapterActivity.class);
        intent.putExtra("BOOK_URL", bookUrl);
        startActivity(intent);
    }

    @Override
    public void onAuthorClick(View view, int position) {
        String authorUrl = mBookList.get(position).getAuthorUrl();
    }

    @Override
    public void onNewChapterClick(View view, int position) {
        String url = mBookList.get(position).getNewChapterUrl();
    }

    //上拉加载
    private void loadBooks(String href, final int page) {

        final String url = href + page;

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                Message msg = new Message();
                try {
                    doc = Jsoup.connect(url).timeout(30000).get();
                    Elements mastheads = doc.select("ul.list_content");
                    for (int i = 0, size = mastheads.size(); i < size; i++) {
                        mBook = new Book();
                        Element titleElement = mastheads.get(i).select("li.cc2 a").first();
                        Element newChatperElement = mastheads.get(i).select("li.cc3 a").first();
                        Element authorElement = mastheads.get(i).select("li.cc4 a").first();
                        Element updTimeElement = mastheads.get(i).select("li.cc5").first();
                        mBook.setTitle(titleElement.text());
                        mBook.setAuthor(authorElement.text());
                        mBook.setAuthorUrl(authorElement.attr("href"));
                        mBook.setBookUrl("http://quanxiaoshuo.com" + titleElement.attr("href"));
                        mBook.setNewChapter(newChatperElement.text());
                        mBook.setNewChapterUrl("http://quanxiaoshuo.com" + newChatperElement.attr("href"));
                        mBook.setUpdTime(updTimeElement.text());
                        mBookList.add(mBook);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.what = 1;
                msg.obj = mBookList;
                handler.sendMessage(msg);
            }
        }).start();
    }

}
