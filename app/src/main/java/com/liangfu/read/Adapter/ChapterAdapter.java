package com.liangfu.read.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liangfu.read.R;
import com.liangfu.read.model.Chapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author: LF
 * @date: 2016/11/10 14:56
 */
public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {

    private Context mContext;
    private List<Chapter> mData;
    private LayoutInflater inflater;

    private OnItemClickLitener onItemClickLitener;

    public ChapterAdapter(Context context, List<Chapter> data) {
        this.mContext = context;
        this.mData = data;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chapter_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvChapterItemName.setText(mData.get(position).getChapter());
        if(onItemClickLitener != null){
            holder.idChapterItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickLitener.onItemClick(view, position, mData.get(position).getChapterUrl());
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

        @Bind(R.id.id_chapter_item)
        LinearLayout idChapterItem;
        @Bind(R.id.tv_chapter_item_name)
        TextView tvChapterItemName;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, String url);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickLitener listener) {
        this.onItemClickLitener = listener;
    }

}
