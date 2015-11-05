/*
 * mail:1065680448@qq.com
 */
package com.refreshlayout;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.refreshlayoutview.imp.OnPullListener;
import com.refreshlayoutview.imp.OnPullListenered;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * base 刷新适配器
 *
 * @Duthor FangJW
 * @Date 15/11/3
 */
public abstract class RefreshLayoutAdapter2<T> extends RecyclerView.Adapter implements OnPullListener {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate方法的第二个参数一定要写viewGroup，不然item的宽度是固定的。并不等于屏幕宽度
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((MyViewHolder) holder).tv.setText("   RecyclerView   " + position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv)
        TextView tv;

        @Bind(R.id.rootView)
        LinearLayout rootView;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
