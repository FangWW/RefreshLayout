/*
 * mail:1065680448@qq.com
 */
package com.refreshlayout.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.refreshlayout.imp.OnPullListener;
import com.refreshlayout.imp.OnPullListenered;

import java.util.List;

/**
 * @Duthor FangJW
 * @Date 15/11/4
 */
public abstract class RefreshLayoutAdapter<T> extends RecyclerView.Adapter<RVHolder> implements OnPullListener {

    private final int viewType;
    private Context context;

    public final int MSG_DATAREFRESHED = 1;
    public final int MSG_DATALOADEDMORE = 2;

    private OnPullListenered mOnPullListenered;

    private List<T> mDatas;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int what = msg.what;
            final List<T> datas = (List<T>) msg.obj;
            switch (what) {
                case MSG_DATAREFRESHED:
                    mDatas = datas;
                    if (mOnPullListenered != null) {
                        mOnPullListenered.onPullDownToRefreshed(mDatas);
                    }
                    notifyDataSetChanged();
                    break;
                case MSG_DATALOADEDMORE:
                    mDatas = datas;
                    if (mOnPullListenered != null) {
                        mOnPullListenered.onPullUpToLoadMoreed(mDatas);
                    }
                    notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };


    public RefreshLayoutAdapter(Context context, int viewType) {
        this.context = context;
        this.viewType = viewType;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public abstract void onPullDownToRefresh();

    @Override
    public abstract void onPullUpToLoadMore();

    @Override
    public void setOnPullListenered(OnPullListenered onPullListenered) {
        mOnPullListenered = onPullListenered;
    }

    public void postMsgDataRefreshed(List<T> datas) {
        mHandler.obtainMessage(MSG_DATAREFRESHED, datas).sendToTarget();
    }

    public void postMsgDataLoadedMore(List<T> datas) {
        mHandler.obtainMessage(MSG_DATALOADEDMORE, datas).sendToTarget();
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(this.viewType, parent, false);

        return new RVHolder(view);
    }


    @Override
    public void onViewRecycled(final RVHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, final int position) {

        onBindViewHolder(holder.getViewHolder(), mDatas.get(position), position);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                }
            });
        }

    }

    public abstract void onBindViewHolder(ViewHolder holder, T item, int position);


    private AdapterView.OnItemClickListener onItemClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}