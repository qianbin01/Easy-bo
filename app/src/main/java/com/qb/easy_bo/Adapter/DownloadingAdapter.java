package com.qb.easy_bo.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qb.easy_bo.R;
import com.qb.easy_bo.bean.DownloadingBean;


import java.util.List;

public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.ViewHolder> {
    private Context mContext;
    private List<DownloadingBean> mList;
    private LayoutInflater mInflater;
    private onPlayOrPauseClickListener playOrPauseClickListener;
    private onStopClickListner onStopClickListner;

    public onPlayOrPauseClickListener getPlayOrPauseClickListener() {
        return playOrPauseClickListener;
    }

    public void setPlayOrPauseClickListener(onPlayOrPauseClickListener playOrPauseClickListener) {
        this.playOrPauseClickListener = playOrPauseClickListener;
    }

    public DownloadingAdapter.onStopClickListner getOnStopClickListner() {
        return onStopClickListner;
    }

    public void setOnStopClickListner(DownloadingAdapter.onStopClickListner onStopClickListner) {
        this.onStopClickListner = onStopClickListner;
    }

    public DownloadingAdapter(Context context, List<DownloadingBean> mList) {
        this.mContext = context;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public interface onPlayOrPauseClickListener {
        void onItemClick(int position);
    }

    public interface onStopClickListner {
        void onItemClick(int position);
    }

    public void setList(List<DownloadingBean> mList) {
        this.mList = mList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.downloading_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final DownloadingBean bean = mList.get(position);
        if (bean == null) {
            return;
        }
        holder.tvTitle.setText(bean.getName());
        holder.tvNow.setText(bean.getNowLength() + "");
        holder.tvLength.setText("/" + bean.getLength());
        holder.mSeekBar.setMax(mList.get(position).getLength());
        holder.mSeekBar.setProgress(mList.get(position).getNowLength());
        holder.tvSpeed.setText(String.format("%dKB/s", mList.get(position).getSpeed()));
        if (bean.isPlayFlag()) {
            holder.btnPause.setText(R.string.pause);
        } else {
            holder.btnPause.setText(R.string.start);
        }
        holder.btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOrPauseClickListener.onItemClick(position);
            }
        });
        holder.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStopClickListner.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvLength, tvNow, tvSpeed;
        private SeekBar mSeekBar;
        private Button btnPause, btnStop;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvLength = (TextView) itemView.findViewById(R.id.tvLength);
            tvNow = (TextView) itemView.findViewById(R.id.tvNow);
            tvSpeed = (TextView) itemView.findViewById(R.id.tvSpeed);
            mSeekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
            btnPause = (Button) itemView.findViewById(R.id.btnPause);
            btnStop = (Button) itemView.findViewById(R.id.btnStop);
        }
    }
}
