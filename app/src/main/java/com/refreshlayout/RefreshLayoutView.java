/*
 * mail:1065680448@qq.com
 */
package com.refreshlayout;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.refreshlayout.imp.OnPullListener;
import com.refreshlayout.imp.OnPullListenered;

import java.util.List;


/**
 * 上拉更多  下拉刷新
 *
 * @Duthor FangJW
 * @Date 15/11/3
 */
public class RefreshLayoutView extends SwipeRefreshLayout implements OnPullListenered {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private OnPullListener mOnPullListener;

    public RefreshLayoutView(Context context) {
        super(context);
        init(context);
    }

    public RefreshLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_green_dark,
                android.R.color.holo_blue_light, android.R.color.holo_orange_dark);

        mRecyclerView = new RecyclerView(mContext);

        //设置RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        addView(mRecyclerView);
        /**
         * 自动底部加载更多
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mCurrentState = RecyclerView.SCROLL_STATE_IDLE;

            private int lastdy = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mCurrentState == RecyclerView.SCROLL_STATE_DRAGGING || mCurrentState == RecyclerView.SCROLL_STATE_SETTLING) {

                    if (dy < 0) {//向下滑动
                        //可以不处理，在SwipeRefreshLayout的onRefreshListener中实现下拉刷新
                    } else {//向上滑动
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        if (layoutManager instanceof LinearLayoutManager) {
                            int lastitem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                            if (recyclerView.getAdapter().getItemCount() == lastitem + 1) {
                                RefreshLayoutView.this.setRefreshing(true);
                                RefreshLayoutView.this.setEnabled(false);
                                if (mOnPullListener != null) {
                                    mOnPullListener.onPullUpToLoadMore();
                                }
                            }
                        }
                    }

                    lastdy = dy;
                }
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mCurrentState = newState;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        /**
         * 下拉刷新
         */
        RefreshLayoutView.this.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshLayoutView.this.setRefreshing(true);
                RefreshLayoutView.this.setEnabled(false);
                if (mOnPullListener != null) {
                    mOnPullListener.onPullDownToRefresh();
                }
            }
        });
    }

    /**
     * 供适配器回调
     */
    @Override
    public void onPullDownToRefreshed(List dataList) {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshLayoutView.this.setEnabled(true);
                RefreshLayoutView.this.setRefreshing(false);
            }
        }, 500);
    }

    @Override
    public void onPullUpToLoadMoreed(List dataList) {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshLayoutView.this.setEnabled(true);
                RefreshLayoutView.this.setRefreshing(false);
            }
        }, 500);

    }

    /**
     * 设置刷新监听
     *
     * @param onPullListener
     */
    public void setOnPullListener(OnPullListener onPullListener) {
        mOnPullListener = onPullListener;
    }

    public void setAdatper(RecyclerView.Adapter adapter) {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(adapter);
            if (adapter instanceof OnPullListener) {
                OnPullListener onPullListener = (OnPullListener) adapter;
                setOnPullListener(onPullListener);
                onPullListener.setOnPullListenered(this);
            }
        }
    }


}
