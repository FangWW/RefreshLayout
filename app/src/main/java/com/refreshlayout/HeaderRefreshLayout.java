/*
 * mail:1065680448@qq.com
 */
package com.refreshlayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    @Bind(R.id.rf_layout)
    RefreshLayoutView mRefreshLayoutView;

    private ConvenientBanner adCycleView;

    private boolean isNoData = true;


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

        LinearLayout headerView = (LinearLayout) inflater.inflate(R.layout.layout_head, null);
        initCycleView(headerView);
        //自定义翻页大小 默认20
        mRefreshLayoutView.setPageSize(10);
        //添加 header
        mRefreshLayoutView.setAdatper(new RefreshLayoutAdapter<List>(this, R.layout.recycleritem_layout, headerView) {
            @Override
            public void onPullDownToRefresh() {
                Log.e("ww", "刷新");
                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isNoData) {//默认刷新没有数据会切换成空界面
                            postMsgDataRefreshed(null);
                            isNoData = false;
                        } else {
                            final List<List> a = getData();
                            //刷新传递数据
                            postMsgDataRefreshed(a);
                        }
                    }
                }, 1500);
            }

            /**
             * 如果不是十的倍数就是最后一页
             * @param page
             */
            @Override
            public void onPullUpToLoadMore(int page) {
                Log.e("ww", "加载;page=" + page + ";总共数据" + getDatas().size());
                final List<List> a = getData();
                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载传递数据
                        postMsgDataLoadedMore(a);
                    }
                }, 1500);
            }


            @Override
            public void onBindViewHolder(ViewHolder holder, List item, int position) {
                holder.getTextView(R.id.tv).setText("   RecyclerView   \n" + position + "===" + item);
            }
        });

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                //主动刷新
                mRefreshLayoutView.onRefreshing();
            }
        }, 1);
        mRefreshLayoutView.setEnabledUP(true);
        mRefreshLayoutView.setEnabledDown(true);

        //设置空视图
        mRefreshLayoutView.setEmptyView(R.drawable.frame_mode_translation_turn_day_16, R.string.no_data);
        //设置错误视图
        mRefreshLayoutView.setErrorView(R.drawable.frame_mode_translation_turn_night_18, R.string.error_data);

    }

    //模拟数据
    @NonNull
    private List<List> getData() {
        final List<List> a = new ArrayList();
        for (int i = 9; i >= 0; i--) {
            a.add(new ArrayList());
        }
        return a;
    }


    //初始化头部 banner
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_error://错误
                mRefreshLayoutView.showErrorView();
                break;
            case R.id.menu_null://没有数据
                mRefreshLayoutView.showEmptyView();
                break;
            case R.id.menu_data://正确
                mRefreshLayoutView.showConetntView();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
