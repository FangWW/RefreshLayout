package com.refreshlayout;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.refreshlayout.bean.OnePictureDetail;
import com.refreshlayout.util.IConfig;
import com.refreshlayout.util.ScreenUtil;


/**
 * @Author FangJW
 * @Date 8/10/17
 * ONE详情
 */
public class OneDetailActivity extends AppCompatActivity implements View.OnClickListener {


    private OnePictureDetail.DataEntity mOne;
    private ImageView mIvImg;
    /**
     * TextView
     */
    private TextView mTvTitle;
    /**
     * TextView
     */
    private TextView mTvContent;
    private Toolbar mToolbar;

    public static void gotoHere(Activity act, OnePictureDetail.DataEntity one, ImageView img, TextView textView) {
        Intent intent = new Intent(act, OneDetailActivity.class);
        intent.putExtra(IConfig.DATA, one);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> imagePair = Pair.create((View) img, "image");
            Pair<View, String> textPair = Pair.create((View) textView, "text");
            act.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(act, imagePair, textPair).toBundle());
        } else {
            act.startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_detail);
        initView();
        intData();
    }

    private void intData() {
        ScreenUtil.getScreenWH(this);
        mOne = (OnePictureDetail.DataEntity) getIntent().getSerializableExtra(IConfig.DATA);
        setToolbarTitle(mOne.getHp_title());
        ImgRequest.inTo(mIvImg, mOne.getHp_img_url(), null);
        mTvTitle.setText(mOne.getHp_author());
        mTvContent.setText(mOne.getHp_content());
        mIvImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePictrue(mOne);
                return false;
            }
        });
        mIvImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomizeDialog();
            }
        });
    }

    /**
     * 保存图片
     *
     * @param one
     */
    private void savePictrue(final OnePictureDetail.DataEntity one) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("保存图片");

        builder.setPositiveButton("保存(没实现)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (ContextCompat.checkSelfPermission(OneDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(OneDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
//                    ToastUtils.showToast(R.string.please_open_the_file_storage);
//                    return;
//                }
//                GlideImgManager.savePicture(OneDetailActivity.this, String.valueOf(one.getHp_author()).concat(".jpg"), one.getHp_img_url());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }


    private void showCustomizeDialog() {
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_one_detail, null);
        dialogView.setBackgroundColor(ActivityCompat.getColor(this, R.color.white));
        dialogView.findViewById(R.id.toolbar).setVisibility(View.GONE);
        final ImageView dialogImg = (ImageView) dialogView.findViewById(R.id.iv_img);
        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.tv_title);
        TextView dialogContent = (TextView) dialogView.findViewById(R.id.tv_content);
        dialogTitle.setTextColor(ActivityCompat.getColor(this, R.color.black88));
        dialogContent.setTextColor(ActivityCompat.getColor(this, R.color.black88));
        dialogImg.setMinimumHeight(ScreenUtil.getScreenH() / 5);
        dialogImg.setBackgroundColor(getResources().getColor(R.color.black));
        dialogImg.setAdjustViewBounds(true);
        dialogImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        dialogTitle.setText(mOne.getHp_author());
        dialogContent.setText("时间" + " " + mOne.getHp_makettime());
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("保存(没实现)",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        savePicOrShare(false);
                    }
                });
        customizeDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        final AlertDialog dialog = customizeDialog.show();

        ImgRequest.inTo(dialogImg, mOne.getHp_img_url(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    int padding = dialog.getWindow().getDecorView().getPaddingRight();
                    ScreenUtil.getScreenWH(OneDetailActivity.this);
                    float w = ScreenUtil.getScreenW() - padding * 2;
                    dialogImg.setMinimumHeight((int) (w / loadedImage.getWidth() * loadedImage.getHeight()));
                    dialogImg.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }

        mIvImg = (ImageView) findViewById(R.id.iv_img);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvContent = (TextView) findViewById(R.id.tv_content);
    }

    public void setToolbarTitle(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != mToolbar) {
            if (!TextUtils.isEmpty(title)) {
                mToolbar.setTitle(title);
            } else {
                mToolbar.setTitle(null);
            }
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

}