package com.qb.easy_bo.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qb.easy_bo.R;
import com.qb.easy_bo.bean.DownloadFinishedBean;
import com.qb.easy_bo.utils.common.CacheUtil;
import com.qb.easy_bo.utils.common.MediaUtil;

import java.util.List;

public class DownloadFinishedAdapter extends RecyclerView.Adapter<DownloadFinishedAdapter.ViewHolder> {
    private Context mContext;
    private List<DownloadFinishedBean> mList;
    private LayoutInflater mInflater;
    private onItemClickListener onItemClickListener;

    public DownloadFinishedAdapter(Context context, List<DownloadFinishedBean> mList) {
        this.mContext = context;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(DownloadFinishedAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<DownloadFinishedBean> mList) {
        this.mList = mList;
    }

    public interface onItemClickListener {
        void onItemClick(View v, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.download_finished_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final DownloadFinishedBean downloadFinishedBean = mList.get(position);
        if (downloadFinishedBean == null) {
            return;
        }
        holder.tvTitle.setText(downloadFinishedBean.getName());
        holder.tvSize.setText(CacheUtil.getFormatSize(downloadFinishedBean.getLength()));
        holder.ivImage.setImageBitmap(downloadFinishedBean.getBitmap());
        holder.tvTime.setText(downloadFinishedBean.getTime());
        holder.rlDownloaded.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemClickListener.onItemClick(view,position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvSize, tvTime;
        private RelativeLayout rlDownloaded;
        private ImageView ivImage;

        ViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSize = (TextView) itemView.findViewById(R.id.tvSize);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            rlDownloaded = (RelativeLayout) itemView.findViewById(R.id.rlDownloaded);

        }
    }
}
