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
import com.qb.easy_bo.bean.NetWorkVideo;

import java.util.List;

public class NetWorkVideoAdapter extends RecyclerView.Adapter<NetWorkVideoAdapter.ViewHolder> {
    private Context mContext;
    private List<NetWorkVideo> mList;
    private LayoutInflater mInflater;
    private onItemClickListener onItemClickListener;

    public NetWorkVideoAdapter(Context context, List<NetWorkVideo> mList) {
        this.mContext = context;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(NetWorkVideoAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<NetWorkVideo> mList) {
        this.mList = mList;
    }

    public interface onItemClickListener {
        void onItemClick(View v, int position, NetWorkVideo netWorkVideo);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.network_video_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final NetWorkVideo netWorkVideo = mList.get(position);
        if (netWorkVideo == null) {
            return;
        }
        holder.tvTitle.setText(netWorkVideo.getName());
        holder.ivImage.setImageResource(netWorkVideo.getResId());
        holder.rlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position, netWorkVideo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivImage;
        private RelativeLayout rlVideo;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            rlVideo = (RelativeLayout) itemView.findViewById(R.id.rlVideo);
        }
    }
}
