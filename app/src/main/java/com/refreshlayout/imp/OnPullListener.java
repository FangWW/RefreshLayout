/*
 * mail:1065680448@qq.com
 */
package com.refreshlayout.imp;

/**
 * 刷新接口
 *
 * @Duthor FangJW
 * @Date 15/11/3
 */
public interface OnPullListener {
    void onPullDownToRefresh();

    void onPullUpToLoadMore();

    void setOnPullListenered(OnPullListenered onPullListenered);
}
