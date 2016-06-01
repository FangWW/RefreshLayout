/*
 * mail:1065680448@qq.com
 */
package com.refreshlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * 图片请求
 * <br/>
 * http://developer.qiniu.com/code/v6/api/kodo-api/image/imageview2.html
 * <br/>
 * http://developer.qiniu.com/code/v6/api/kodo-api/obsolete/imagemogr.html#imageMogr-tailor
 * <br/>
 * ?imageMogr2/thumbnail/!200x200>
 * <br/>
 * 七牛  图片裁剪api
 *
 * @Author FangJW
 * @Date 15/10/22
 */
public class ImgRequest {
    public static boolean IS_WIFI = true;
    private static final String[] IMAG_HOST = {"7xjsvq", ".youyulx.com"};
    private static final String IMAGEMOGR2 = "?imageMogr2/thumbnail/!";
    private static final String[] IMG_SIZE = {"120x120", "200x200", "500x500", "800x800"};
    private static final String[] IMG_BIG_SIZE = {"200x200", "300x300", "800x800", "1000x1000"};
//    private static final String[] IMG_SIZE = {"120x120", "200x200", "350x350", "500x500"};
    /**
     * 适用于 群组小图
     */
    public static final int IMG_SIZE_X = 0;
    /**
     * 适用于 item上面图片
     */
    public static final int IMG_SIZE_XX = 1;
    /**
     * 适用于 铺面4到5寸屏幕图片
     */
    public static final int IMG_SIZE_XXX = 2;
    /**
     * 适用于 查看大图
     */
    public static final int IMG_SIZE_XXXX = 3;

    /**
     * application 中初始化
     */
    public static void initImgRequest(Context context) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.ic_error_no_result)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .imageDownloader(new AuthImageDownloader(context))
                .threadPoolSize(5)
                .threadPriority(Thread.MIN_PRIORITY + 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .diskCacheSize(50 * 1024 * 1024).memoryCacheSize(5 * 1024 * 1024)
                .memoryCache(new LRULimitedMemoryCache(5 * 1024 * 1024)).tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(options)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);

    }

    public static String getImgCut(String url, int imgSize) {
        if (TextUtils.isEmpty(url) || imgSize >= IMG_SIZE.length || imgSize < 0) {
            return "";
        }
        for (String host : IMAG_HOST)
            if (url.contains(host)) {
                return url.concat(IMAGEMOGR2).concat(IS_WIFI ? IMG_BIG_SIZE[imgSize] : IMG_SIZE[imgSize]).concat(">");
            }
        return url;
    }

    /**
     * 默认缓存图片并设置默认图片
     *
     * @param img
     * @param url
     */
//    public static void inTo(ImageView img, String url) {
//        inTo(img, url, null, null, null);
//    }
    public static void inToForSize(ImageView img, String url, int imgSize) {
        inTo(img, getImgCut(url, imgSize), null, null, null);
    }

    /**
     * @param img       图片
     * @param url       图片地址
     * @param defResImg 默认图片
     */
//    public static void inTo(ImageView img, String url, int defResImg) {
//        inTo(img, url, defResImg, true, null, null);
//    }
    public static void inToForSize(ImageView img, String url, int defResImg, int imgSize) {
        inTo(img, getImgCut(url, imgSize), defResImg, true, null, null);
    }

    public static void inTo(ImageView img, String url, int defResImg, boolean isCache) {
        inTo(img, url, defResImg, isCache, null, null);
    }

    public static void inTo(ImageView img, String url, ImageLoadingListener listener) {
        inTo(img, url, 0, true, listener, null);
    }

    /**
     * 请求图片
     * <p/>
     * 将  defResImg&isCache封装成DisplayImageOptions传给"最终"into()
     *
     * @param img
     * @param url
     * @param defResImg
     * @param isCache
     * @param listener
     * @param progressListener
     */
    public static void inTo(ImageView img, String url, int defResImg, boolean isCache, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defResImg)
                .showImageForEmptyUri(defResImg)
                .showImageOnFail(defResImg)
                .cacheInMemory(isCache)
                .cacheOnDisk(isCache)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        inTo(img, url, options, listener, progressListener);
    }

    /**
     * 最终
     *
     * @param img
     * @param url
     * @param options
     * @param listener
     * @param progressListener
     */
    public static void inTo(ImageView img, String url, DisplayImageOptions options, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
//        if (url.contains(IMAGEMOGR2)) {
        if (!url.equals(img.getTag())) {
            img.setTag(url);
            ImageLoader.getInstance().displayImage(url, img, options, listener, progressListener);
        }
//        } else {
//            img.setBackgroundResource(R.drawable.group_travel_default);
//        }
    }

    public static void cacheClear() {
        ImageLoader.getInstance().clearDiskCache();
    }
}
