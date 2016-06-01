/*
 * mail:1065680448@qq.com
 */
package com.refreshlayoutview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.refreshlayoutview.adapter.RVHolder;
import com.refreshlayoutview.adapter.ViewHolder;
import com.refreshlayoutview.imp.OnPullListener;
import com.refreshlayoutview.imp.OnPullListenered;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author FangJW
 * @Date 15/11/4
 */
public abstract class RefreshLayoutAdapter<T> extends RecyclerView.Adapter<RVHolder> implements OnPullListener {

    private int viewType;
    private View mViewHeader;
    private Context context;

    public final int MSG_DATAREFRESHED = 1;
    public final int MSG_DATALOADEDMORE = 2;

    private boolean isAutoView = true;

    /**
     * 加载item类型
     */
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    /**
     * 当前页数
     */
    private int mPage = 1;

    private OnPullListenered mOnPullListenered;

    private List<T> mDatas = new ArrayList<T>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int what = msg.what;
            final List<T> temp = (List<T>) msg.obj;
            final List<T> datas = temp == null ? null : new ArrayList<>(temp);
            switch (what) {
                case MSG_DATAREFRESHED:
                    mPage = 2;
                    mDatas.clear();
                    if (datas != null) {
                        mDatas.addAll(datas);
                    }
                    if (mOnPullListenered != null) {
                        mOnPullListenered.onPullDownToRefreshed(datas, isAutoView);
                    }
                    notifyDataSetChanged();
                    break;
                case MSG_DATALOADEDMORE:
                    mPage++;
                    if (datas != null) {
                        mDatas.addAll(datas);
                    }
                    if (mOnPullListenered != null) {
                        mOnPullListenered.onPullUpToLoadMoreed(datas);
                    }
                    notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    public void setAutoView(boolean autoView) {
        isAutoView = autoView;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void add(T t) {
        this.mDatas.add(t);
    }

    public int getPage() {
        return mPage;
    }

    public RefreshLayoutAdapter(Context context, int viewType) {
        this.context = context;
        this.viewType = viewType;
    }

    public RefreshLayoutAdapter(Context context, int viewType, View viewHeader) {
        this.context = context;
        this.viewType = viewType;
        this.mViewHeader = viewHeader;
    }

    public void add(T t, int position) {
        this.mDatas.add(position, t);
    }

    public void remove(int position) {
        this.mDatas.remove(position);
    }

    public void removeAll() {
        this.mDatas.clear();
    }

    @Override
    public int getItemCount() {
        int count = mDatas == null ? 0 : mDatas.size();
        if (haveHeader()) {
            count = count + 1;
        }
        return count;
    }

    @Override
    public abstract void onPullDownToRefresh();

    @Override
    public abstract void onPullUpToLoadMore(int pager);

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
        if (viewType == ITEM_VIEW_TYPE_HEADER && haveHeader()) {
            return new RVHolder(mViewHeader);
        }
        View view = LayoutInflater.from(context).inflate(this.viewType, parent, false);
        return new RVHolder(view);
    }

    /**
     * 是否有头部视图
     *
     * @return
     */
    private boolean haveHeader() {
        return mViewHeader != null;
    }


    @Override
    public void onViewRecycled(final RVHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, int position) {
        if (isHeader(position) && haveHeader()) {
            return;
        }
        if (haveHeader()) {
            position = position - 1;
        }
        onBindViewHolder(holder.getViewHolder(), mDatas.get(position), position);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                }
            });
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onItemLongClick(null, v, holder.getPosition(), holder.getItemId());
                }
            });
        }

    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    public abstract void onBindViewHolder(ViewHolder holder, T item, int position);

    private AdapterView.OnItemClickListener onItemClickListener;

    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


}