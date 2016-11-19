package com.qb.easy_bo.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qb.easy_bo.R;
import com.qb.easy_bo.bean.LocalMusic;
import com.qb.easy_bo.utils.common.CacheUtil;
import com.qb.easy_bo.utils.common.MediaUtil;

import java.util.List;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.ViewHolder> {
    private Context mContext;
    private List<LocalMusic> mList;
    private LayoutInflater mInflater;
    private onItemClickListener onItemClickListener;

    public LocalMusicAdapter(Context context, List<LocalMusic> mList) {
        this.mContext = context;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(LocalMusicAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<LocalMusic> mList) {
        this.mList = mList;
    }

    public interface onItemClickListener {
        void onItemClick(View v, int position,LocalMusic music);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.local_music_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final LocalMusic music = mList.get(position);
        if (music == null) {
            return;
        }
        holder.tvTitle.setText(music.getTitle());
        holder.tvArtist.setText(music.getArtist());
        holder.tvTime.setText(MediaUtil.getMinuteTime(music.getLength()));
        holder.tvSize.setText(CacheUtil.getFormatSize(music.getSize()));
        holder.rlMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position,music);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvArtist, tvTime, tvSize;
        private RelativeLayout rlMusic;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvArtist = (TextView) itemView.findViewById(R.id.tvArtist);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvSize = (TextView) itemView.findViewById(R.id.tvSize);
            rlMusic = (RelativeLayout) itemView.findViewById(R.id.rlMusic);

        }
    }
}
