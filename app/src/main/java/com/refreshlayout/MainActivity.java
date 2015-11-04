package com.refreshlayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.refreshlayout.adapter.RefreshLayoutAdapter;
import com.refreshlayout.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.recycleview)
    RecyclerView recycleview;
    @Bind(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @Bind(R.id.rf_layout)
    RefreshLayoutView mRefreshLayoutView;

    private RecyclerViewAdapter mAdapter = null;

    /**
     * 请求成功
     */
    public static final int REQUEST_SUCCESS = 100;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MainActivity.REQUEST_SUCCESS:
                    Toast.makeText(MainActivity.this, "请求数据成功！！", Toast.LENGTH_SHORT).show();
                    swiperefreshlayout.setRefreshing(false);
                    swiperefreshlayout.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRefreshLayoutView.setAdatper(new RefreshLayoutAdapter<List>(this, R.layout.recycleritem_layout) {
            @Override
            public void onPullDownToRefresh() {

                Log.e("ww", "刷新");
                List<List> a = new ArrayList();
                for (int i = 18 - 1; i >= 0; i--) {
                    a.add(new ArrayList());
                }
                postMsgDataRefreshed(a);
            }

            @Override
            public void onPullUpToLoadMore() {

                Log.e("ww", "加载");
                List<List> a = new ArrayList();
                for (int i = 18 - 1; i >= 0; i--) {
                    a.add(new ArrayList());
                }
                postMsgDataLoadedMore(a);
            }


            @Override
            public void onBindViewHolder(ViewHolder holder, List item, int position) {

                holder.getTextView(R.id.tv).setText("   RecyclerView   " + position + "===" + item);
            }
        });


//        mRefreshLayoutView.setAdatper(new RefreshLayoutAdapter2<List>() {
//            @Override
//            public void onPullDownToRefresh() {
//                Log.e("ww", "刷新");
//                List<List> a = new ArrayList();
//                for (int i = 18 - 1; i >= 0; i--) {
//                    a.add(new ArrayList());
//                }
//                postMsgDataRefreshed(a);
//            }
//
//            @Override
//            public void onPullUpToLoadMore() {
//                Log.e("ww", "加载");
//                List<List> a = new ArrayList();
//                for (int i = 18 - 1; i >= 0; i--) {
//                    a.add(new ArrayList());
//                }
//                postMsgDataLoadedMore(a);
//
//            }
//
//
//        });


        swiperefreshlayout.setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_green_dark,
                android.R.color.holo_blue_light, android.R.color.holo_orange_dark);

        //设置RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setHasFixedSize(true);
        recycleview.setLayoutManager(linearLayoutManager);
        mAdapter = new RecyclerViewAdapter(this);

        mAdapter.setmOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void click(int position) {
                Toast.makeText(MainActivity.this, "Click item Postion :  " + position, Toast.LENGTH_SHORT).show();
            }
        });

        recycleview.setAdapter(mAdapter);

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefreshlayout.setRefreshing(true);
                swiperefreshlayout.setEnabled(false);
                //模拟发请求 5秒延迟
                mHandler.sendEmptyMessageDelayed(REQUEST_SUCCESS, 5000);
            }
        });

        recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                swiperefreshlayout.setRefreshing(true);
                                swiperefreshlayout.setEnabled(false);
                                //模拟发请求 5秒延迟
                                mHandler.sendEmptyMessageDelayed(REQUEST_SUCCESS, 5000);
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
    }

}
