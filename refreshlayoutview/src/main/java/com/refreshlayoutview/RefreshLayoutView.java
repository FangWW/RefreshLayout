/*
 * mail:1065680448@qq.com
 */
package com.refreshlayoutview;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.refreshlayoutview.imp.OnPullListener;
import com.refreshlayoutview.imp.OnPullListenered;

import java.util.List;


/**
 * 上拉更多  下拉刷新
 *
 * @Duthor FangJW
 * @Date 15/11/3
 */
public class RefreshLayoutView extends SwipeRefreshLayout implements OnPullListenered {
    private Context mContext;
    /**
     * 内容列表视图
     */
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    /**
     * 设置上下拉接口
     */
    private OnPullListener mOnPullListener;
    /**
     * 能否向上拉动
     */
    private boolean mIsCanUP = true;

    private boolean mEnabledUP = true;
    private boolean mEnabledDown = true;
    /**
     * 默认分页大小
     */
    public static final int PAGE_SIZE = 10;
    /**
     * 当前显示状态视图
     */
    private ScrollView mCurrStateView = null;
    /**
     * 错误状态视图
     */
    private View mErrorView = null;
    /**
     * 当前空视图
     */
    private View mEmptyView = null;

    public RefreshLayoutView(Context context) {
        super(context);
        init(context);
    }

    public RefreshLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    public boolean canChildScrollUp() {
        if (mCurrStateView.getVisibility() == VISIBLE) {//如果状态视图显示出来  就交给父类处理
            return super.canChildScrollUp();
        }
        //当显示我们的列表时候  就按照我们的列表滚动去处理
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return mRecyclerView.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(mRecyclerView, -1);
        }
    }

    private void init(Context context) {
        mContext = context;
        setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        LinearLayout rootLayout = new LinearLayout(mContext);
        RelativeLayout.LayoutParams rootLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        addView(rootLayout, rootLayoutParams);

        //初始化状态视图
        initEmptyView();
        initErrorView();

        mCurrStateView = new ScrollView(mContext);
        mCurrStateView.addView(mEmptyView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rootLayout.addView(mCurrStateView, layoutParams);

        //设置RecyclerView
        mRecyclerView = new RecyclerView(mContext);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        rootLayout.addView(mRecyclerView);

        showConetntView();

        /**
         * 自动底部加载更多
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mCurrentState = RecyclerView.SCROLL_STATE_IDLE;

            private int lastdy = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mCurrentState == RecyclerView.SCROLL_STATE_DRAGGING || mCurrentState == RecyclerView.SCROLL_STATE_SETTLING) {

                    if (!mEnabledUP || dy < 0 || !mIsCanUP) {//向下滑动或者不能上拉
                        //可以不处理，在SwipeRefreshLayout的onRefreshListener中实现下拉刷新
                    } else {//向上滑动
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        if (layoutManager instanceof LinearLayoutManager) {
                            int lastitem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                            if (recyclerView.getAdapter().getItemCount() == lastitem + 1) {
                                RefreshLayoutView.this.setRefreshing(true);
                                RefreshLayoutView.this.setEnabled(false && mEnabledDown);
                                if (mOnPullListener != null) {
                                    mOnPullListener.onPullUpToLoadMore(mOnPullListener.getPage());
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
                onRefreshing();
            }
        });
    }

    public void onRefreshing() {
        RefreshLayoutView.this.setRefreshing(true);
        RefreshLayoutView.this.setEnabled(false && mEnabledDown);
        if (mOnPullListener != null) {
            mOnPullListener.onPullDownToRefresh();
        }
    }

    public void onStopRefreshing() {
        RefreshLayoutView.this.setRefreshing(false);
        RefreshLayoutView.this.setEnabled(true && mEnabledDown);
    }

    /**
     * 供适配器回调
     */
    @Override
    public void onPullDownToRefreshed(List dataList) {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshLayoutView.this.setEnabled(true && mEnabledDown);
                RefreshLayoutView.this.setRefreshing(false);
                mIsCanUP = true;
                if (mOnPullListener != null) {
                    if (mOnPullListener.getDatas() == null || mOnPullListener.getDatas().isEmpty()) {
                        showEmptyView();
                    } else if (mOnPullListener.getDatas() != null && !mOnPullListener.getDatas().isEmpty()) {
                        showConetntView();
                    } else if (mOnPullListener.getDatas() == null || mOnPullListener.getDatas().isEmpty()) {
                        showErrorView();
                    }
                }
            }
        }, 500);
    }

    public boolean isEnabledUP() {
        return mEnabledUP;
    }

    /**
     * 能否上拉
     */
    public void setEnabledUP(boolean enabledUP) {
        mEnabledUP = enabledUP;
    }

    public boolean isEnabledDown() {
        return mEnabledDown;
    }

    /**
     * 能否下拉
     */
    public void setEnabledDown(boolean enabledDown) {
        mEnabledDown = enabledDown;
        RefreshLayoutView.this.setEnabled(enabledDown);
    }

    public void setDividerItemDecoration(DividerItemDecoration dividerItemDecoration) {
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onPullUpToLoadMoreed(final List dataList) {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshLayoutView.this.setEnabled(true && mEnabledDown);
                RefreshLayoutView.this.setRefreshing(false);
                if (mOnPullListener != null) {
                    mIsCanUP = dataList != null && dataList.size() == PAGE_SIZE;
                }
            }
        }, 500);

    }

    private void showErrorView() {
        mCurrStateView.removeAllViews();
        mCurrStateView.addView(mErrorView);
        mCurrStateView.setVisibility(VISIBLE);
        mRecyclerView.setVisibility(GONE);
    }

    private void showEmptyView() {
        mCurrStateView.removeAllViews();
        mCurrStateView.addView(mEmptyView);
        mCurrStateView.setVisibility(VISIBLE);
        mRecyclerView.setVisibility(GONE);
    }

    private void showConetntView() {
        mCurrStateView.setVisibility(GONE);
        mRecyclerView.setVisibility(VISIBLE);
    }

    private View initErrorView() {
        if (mErrorView == null) {
            mErrorView = LayoutInflater.from(mContext).inflate(R.layout.layout_state_view, null);
            ImageView mImgMsg = (ImageView) mErrorView.findViewById(R.id.img_msg);
            TextView mTvMsg = (TextView) mErrorView.findViewById(R.id.tv_msg);
            mTvMsg.setText("error");
        }
        return mErrorView;
    }

    private View initEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_state_view, null);
            ImageView mImgMsg = (ImageView) mEmptyView.findViewById(R.id.img_msg);
            TextView mTvMsg = (TextView) mEmptyView.findViewById(R.id.tv_msg);
            mTvMsg.setText("no data");
        }
        return mEmptyView;
    }

    /**
     * 设置空视图
     *
     * @param imgRes
     * @param msg
     */
    public void setEmptyView(@DrawableRes int imgRes, @StringRes int msg) {
//        mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_state_view, null);
        ImageView mImgMsg = null;
        try {
            mImgMsg = (ImageView) mEmptyView.findViewById(R.id.img_msg);
            TextView mTvMsg = (TextView) mEmptyView.findViewById(R.id.tv_msg);
            mTvMsg.setText(msg);
            mImgMsg.setBackgroundResource(imgRes);
        } catch (Exception e) {
            Log.e("ww", "你使用了自定义视图了~ 老夫找不到~");
        }
    }

    /**
     * 设置错误视图
     *
     * @param imgRes
     * @param msg
     */
    private void setErrorView(@DrawableRes int imgRes, @StringRes int msg) {
//        mErrorView = LayoutInflater.from(mContext).inflate(R.layout.layout_state_view, null);
        try {
            ImageView mImgMsg = (ImageView) mErrorView.findViewById(R.id.img_msg);
            TextView mTvMsg = (TextView) mErrorView.findViewById(R.id.tv_msg);
            mTvMsg.setText(msg);
            mImgMsg.setBackgroundResource(imgRes);
        } catch (Exception e) {
            Log.e("ww", "你使用了自定义视图了~ 老夫找不到~");
        }
    }

    /**
     * 设置没有数据时显示视图
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        mCurrStateView.removeAllViews();
        mCurrStateView.addView(mEmptyView);
    }

    /**
     * 设置错误状态时显示视图
     *
     * @param errorView
     */
    public void setErrorView(View errorView) {
        mErrorView = errorView;
        mCurrStateView.removeAllViews();
        mCurrStateView.addView(mErrorView);
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
