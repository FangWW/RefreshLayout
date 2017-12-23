package com.refreshlayout.util;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @Author FangJW
 * @Date 5/15/17
 */
public class ScreenUtil {
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    static int[] ScreenWH;

    public static int[] getScreenWH(Activity context) {
        if (ScreenWH == null) {
            ScreenWH = new int[2];
            DisplayMetrics metric = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;   // 屏幕高度（像素）
            ScreenWH[0] = width;
            ScreenWH[1] = height;
        }
        return ScreenWH;
    }


    public static int getScreenW() {
        return ScreenWH[0];
    }

    public static int getScreenH() {
        return ScreenWH[1];
    }

}
