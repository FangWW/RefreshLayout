package com.refreshlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.refreshlayout.bean.OnePictureDetail;
import com.refreshlayout.bean.OnePictureList;
import com.refreshlayout.util.IConfig;
import com.refreshlayoutview.RefreshLayoutAdapter;
import com.refreshlayoutview.RefreshLayoutView;
import com.refreshlayoutview.adapter.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.rf_layout)
    RefreshLayoutView mRefreshLayoutView;
    private OkHttpClient okHttpClient;
    private RefreshLayoutAdapter<OnePictureDetail.DataEntity> mRefreshLayoutAdapter;
    List<OnePictureDetail.DataEntity> mReadShareDatas = new ArrayList<>();
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ImgRequest.initImgRequest(this);
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        mRefreshLayoutAdapter = new RefreshLayoutAdapter<OnePictureDetail.DataEntity>(this, R.layout.layout_one) {
            @Override
            public void onPullDownToRefresh() {
                getOneListData("0", true);
            }

            /**
             * 如果不是十的倍数就是最后一页
             * @param page
             */
            @Override
            public void onPullUpToLoadMore(int page) {
                try {
                    getOneListData(mRefreshLayoutAdapter.getDatas().get(mRefreshLayoutAdapter.getDatas().size() - 1).getHpcontent_id(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onBindViewHolder(final ViewHolder holder, final OnePictureDetail.DataEntity item, int position) {
                final ImageView mIvImg = (ImageView) holder.get(R.id.iv_img);
                final TextView mTvTitle = (TextView) holder.get(R.id.tv_title);
                ImgRequest.inTo(mIvImg, item.getHp_img_url(), null);
                mTvTitle.setText(item.getHp_title());
                mIvImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OneDetailActivity.gotoHere(MainActivity.this, item, mIvImg, (TextView) holder.get(R.id.tv_bg));
                    }
                });
            }
        };
        mRefreshLayoutView.setAdatper(mRefreshLayoutAdapter);
        //自定义翻页大小 默认20
        mRefreshLayoutView.setPageSize(10);
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                //主动刷新
                mRefreshLayoutView.onRefreshing();
            }
        }, 1);
        mRefreshLayoutView.setEnabledUP(true);
        mRefreshLayoutView.setEnabledDown(true);
    }

    /**
     * 请求列表 id
     *
     * @param id
     * @param isRefresh
     */
    private void getOneListData(String id, final boolean isRefresh) {
        Request request = new Request.Builder()
                .url(String.format(IConfig.ONE_PICTURE_URL, id))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseText = response.body().string();
                    OnePictureList onePictureList = gson.fromJson(responseText, OnePictureList.class);
                    final List<String> data = onePictureList.getData();
                    if (data != null && !data.isEmpty()) {
                        getOneDetails(data, isRefresh);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /**
     * 得到列表id的详情
     *
     * @param datas
     * @param isRefresh
     */
    private void getOneDetails(final List<String> datas, final boolean isRefresh) {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(String.format(IConfig.ONE_PICTURE_DETAIL_URL, datas.get(0)))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //数据
                    String responseText = response.body().string();
                    //Gson解析
                    OnePictureDetail onePictureDetail = gson.fromJson(responseText, OnePictureDetail.class);
                    final OnePictureDetail.DataEntity one = onePictureDetail.getData();
                    if (one != null) {
                        mReadShareDatas.add(one);
                    }
                    if (datas.size() == 1) {
                        if (isRefresh) {
                            //刷新传递数据
                            mRefreshLayoutAdapter.postMsgDataRefreshed(new ArrayList<>(mReadShareDatas));
                        } else {
                            //加载传递数据
                            mRefreshLayoutAdapter.postMsgDataLoadedMore(new ArrayList<>(mReadShareDatas));
                        }
                        mReadShareDatas.clear();
                    } else {
                        datas.remove(0);
                        getOneDetails(datas, isRefresh);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            HeaderRefreshLayout.gotoHere(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
