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
import com.qb.easy_bo.bean.LocalVideo;
import com.qb.easy_bo.utils.common.CacheUtil;
import com.qb.easy_bo.utils.common.MediaUtil;

import java.util.List;

public class LocalVideoAdapter extends RecyclerView.Adapter<LocalVideoAdapter.ViewHolder> {
    private Context mContext;
    private List<LocalVideo> mList;
    private LayoutInflater mInflater;
    private onItemClickListener onItemClickListener;

    public LocalVideoAdapter(Context context, List<LocalVideo> mList) {
        this.mContext = context;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(LocalVideoAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<LocalVideo> mList) {
        this.mList = mList;
    }

    public interface onItemClickListener {
        void onItemClick(View v, int position, LocalVideo video);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.local_video_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final LocalVideo video = mList.get(position);
        if (video == null) {
            return;
        }
        holder.tvTitle.setText(video.getVideoName());
        holder.tvSize.setText(CacheUtil.getFormatSize(video.getSize()));
        holder.ivImage.setImageBitmap(video.getImage());
        holder.rlMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position,video);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvSize;
        private RelativeLayout rlMusic;
        private ImageView ivImage;
        ViewHolder(View itemView) {
            super(itemView);
            ivImage= (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSize = (TextView) itemView.findViewById(R.id.tvSize);
            rlMusic = (RelativeLayout) itemView.findViewById(R.id.rlMusic);

        }
    }
}
