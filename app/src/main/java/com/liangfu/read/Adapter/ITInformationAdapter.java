package com.liangfu.read.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liangfu.read.R;
import com.liangfu.read.model.ITArticle;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author: LF
 * @date: 2016/11/9 16:04
 */
public class ITInformationAdapter extends RecyclerView.Adapter<ITInformationAdapter.MyViewHolder> {

    private Context mContext;
    private List<ITArticle> mDatas;
    private LayoutInflater inflater;

    private OnItemClickLitener mOnItemClickLitener;

    public ITInformationAdapter(Context context, List<ITArticle> datas) {
        this.mContext = context;
        this.mDatas = datas;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.homepage_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvArticleTitle.setText(mDatas.get(position).getTitle());
        Picasso.with(mContext).load(mDatas.get(position).getImageUrl()).into(holder.ivArticleImg);
        holder.tvArticleSummary.setText(mDatas.get(position).getSummary());
        holder.tvArticlePosttime.setText(mDatas.get(position).getPostTime());
        if (mOnItemClickLitener != null) {
            holder.itinformationItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() <= 0) {
            return 0;
        }
        return mDatas.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_article_title)
        TextView tvArticleTitle;
        @Bind(R.id.iv_article_img)
        ImageView ivArticleImg;
        @Bind(R.id.tv_article_summary)
        TextView tvArticleSummary;
        @Bind(R.id.tv_article_posttime)
        TextView tvArticlePosttime;
        @Bind(R.id.itinformation_item)
        RelativeLayout itinformationItem;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
