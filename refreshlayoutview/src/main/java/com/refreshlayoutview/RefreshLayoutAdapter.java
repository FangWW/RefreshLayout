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
 * @Duthor FangJW
 * @Date 15/11/4
 */
public abstract class RefreshLayoutAdapter<T> extends RecyclerView.Adapter<RVHolder> implements OnPullListener {

    private int viewType;
    private View mViewHeader;
    private Context context;

    public final int MSG_DATAREFRESHED = 1;
    public final int MSG_DATALOADEDMORE = 2;

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
            final List<T> datas = (List<T>) msg.obj;
            switch (what) {
                case MSG_DATAREFRESHED:
                    mPage = 2;
                    mDatas.clear();
                    mDatas.addAll(datas);
                    if (mOnPullListenered != null) {
                        mOnPullListenered.onPullDownToRefreshed(datas);
                    }
                    notifyDataSetChanged();
                    break;
                case MSG_DATALOADEDMORE:
                    mPage++;
                    mDatas.addAll(datas);
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

    public List<T> getDatas() {
        return mDatas;
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

    @Override
    public int getItemCount() {
        int count = mDatas == null ? 0 : mDatas.size();
        if (haveHeader()) {
            count = count + 1;
        }
        return count;
    }

    /**
     * 刷新
     */
    @Override
    public abstract void onPullDownToRefresh();

    /**
     * 下一次的页数
     * 默认十页为分页   如果不是十的倍数就是最后一页
     *
     * @param pager 当前第几页
     */
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

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}