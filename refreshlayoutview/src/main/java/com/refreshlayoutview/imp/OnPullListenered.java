/*
 * mail:1065680448@qq.com
 */
package com.refreshlayoutview.imp;

import java.util.List;

/**
 * 刷新完成
 *
 * @Author FangJW
 * @Date 15/11/3
 */
public interface OnPullListenered {
    void onPullDownToRefreshed(List dataList, boolean isAutoView);

    void onPullUpToLoadMoreed(List dataList);
}
