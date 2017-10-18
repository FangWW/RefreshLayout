/*
 * mail:1065680448@qq.com
 */
package com.refreshlayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.refreshlayoutview.RefreshLayoutAdapter;
import com.refreshlayoutview.RefreshLayoutView;
import com.refreshlayoutview.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 带headerview的refreshlayout
 *
 * @Author FangJW
 * @Date 6/1/16
 */
public class HeaderRefreshLayout extends AppCompatActivity {
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
    private ConvenientBanner adCycleView;


    public static void gotoHere(Activity activity) {
        activity.startActivity(new Intent(activity, HeaderRefreshLayout.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutInflater inflater = LayoutInflater.from(this);

        ButterKnife.bind(this);
        ImgRequest.initImgRequest(this);

        swiperefreshlayout.setVisibility(View.GONE);

        LinearLayout headerView = (LinearLayout) inflater.inflate(R.layout.layout_head, null);
        initCycleView(headerView);
        mRefreshLayoutView.setPageSize(10);
        //添加 header
        mRefreshLayoutView.setAdatper(new RefreshLayoutAdapter<List>(this, R.layout.recycleritem_layout, headerView) {
            @Override
            public void onPullDownToRefresh() {

                Log.e("ww", "刷新");
                final List<List> a = new ArrayList();
                for (int i = 9; i >= 0; i--) {
                    a.add(new ArrayList());
                }

                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        postMsgDataRefreshed(a);
                    }
                }, 5500);
            }

            /**
             * 默认十页为分页   如果不是十的倍数就是最后一页
             * @param page
             */
            @Override
            public void onPullUpToLoadMore(int page) {

                Log.e("ww", "加载;page=" + page + ";总共数据" + getDatas().size());
                final List<List> a = new ArrayList();
                for (int i = 9; i >= 0; i--) {
                    a.add(new ArrayList());
                }
                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        postMsgDataLoadedMore(a);
                    }
                }, 5500);
            }


            @Override
            public void onBindViewHolder(ViewHolder holder, List item, int position) {

                holder.getTextView(R.id.tv).setText("   RecyclerView   " + position + "===" + item);
            }
        });

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayoutView.onRefreshing();
            }
        }, 1);
        mRefreshLayoutView.setEnabledUP(true);
        mRefreshLayoutView.setEnabledDown(true);


    }


    private void initCycleView(View view) {
        ArrayList<String> mImageUrl = null;
        String imageUrl1 = "http://ad.qyer.com/www/images/5ed4abfbadf17f57829d72a7f0b958fd.jpg";
        String imageUrl3 = "http://ad.qyer.com/www/images/8056fa630a8881c7f60c98552f1e45be.jpg";
        String imageUrl4 = "http://a0.att.hudong.com/15/08/300218769736132194086202411_950.jpg";
        String imageUrl5 = "http://dimg01.c-ctrip.com/images/fd/tg/g3/M05/30/F6/CggYGlaUpSKAI8vWAAD3kzfN-LU683_R_1024_10000.jpg";
        String imageUrl6 = "http://img4.tuniucdn.com/site/file/zt/laoyutuijian/balidao/images/header.jpg";
        mImageUrl = new ArrayList<>();
        mImageUrl.add(imageUrl1);
        mImageUrl.add(imageUrl3);
        mImageUrl.add(imageUrl4);
        mImageUrl.add(imageUrl5);
        mImageUrl.add(imageUrl6);


        adCycleView = (ConvenientBanner) view.findViewById(R.id.adCycleView);
        adCycleView.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, mImageUrl)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{android.R.drawable.btn_star_big_off, android.R.drawable.btn_star_big_on})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }

    @Override
    public void onStop() {
        super.onStop();
        adCycleView.stopTurning();
    }

    @Override
    public void onStart() {
        super.onStart();
        adCycleView.startTurning(5000);
    }

    public class LocalImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, final String data) {
            ImgRequest.inToForSize(imageView, data, ImgRequest.IMG_SIZE_XXXX);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_share) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
