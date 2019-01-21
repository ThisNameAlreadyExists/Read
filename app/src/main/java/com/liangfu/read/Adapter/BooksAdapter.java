package com.liangfu.read.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liangfu.read.R;
import com.liangfu.read.model.Book;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author: LF
 * @date: 2016/11/10 11:01
 */
public class BooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<Book> mData;
    private LayoutInflater inflater;

    private OnItemClickLitener mOnItemClickLitener;

    public BooksAdapter(Context context, List<Book> data) {
        this.mContext = context;
        this.mData = data;
        this.inflater = LayoutInflater.from(mContext);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onAuthorClick(View view, int position);

        void onNewChapterClick(View view, int position);
    }

    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickLitener listener){
        this.mOnItemClickLitener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.books_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.tvBookAuthor.setText(mData.get(position).getAuthor());
        myViewHolder.tvBookNewChapter.setText(mData.get(position).getNewChapter());
        myViewHolder.tvBookTitle.setText(mData.get(position).getTitle());
        myViewHolder.tvBookUpdTime.setText(mData.get(position).getUpdTime());

        if (mOnItemClickLitener != null) {
            myViewHolder.idBooksItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(view, position);
                }
            });

            myViewHolder.tvBookAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onAuthorClick(view, position);
                }
            });

            myViewHolder.tvBookNewChapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onNewChapterClick(view, position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (mData == null || mData.size() <= 0) {
            return 0;
        }
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_books_item)
        LinearLayout idBooksItem;
        @Bind(R.id.tv_book_title)
        TextView tvBookTitle;
        @Bind(R.id.tv_book_newChapter)
        TextView tvBookNewChapter;
        @Bind(R.id.tv_book_author)
        TextView tvBookAuthor;
        @Bind(R.id.tv_book_updTime)
        TextView tvBookUpdTime;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
